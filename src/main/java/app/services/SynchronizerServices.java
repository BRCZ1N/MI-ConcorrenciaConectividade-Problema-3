package app.services;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import com.google.gson.Gson;

import org.apache.http.HttpVersion;
import org.springframework.stereotype.Component;
import app.exceptions.ServerConnectionException;
import app.model.OperationsModel;
import app.model.ReplySynchronObject;
import app.model.RequestSynchronObject;
import app.utilities.BanksEnum;
import app.utilities.Http;
import app.utilities.HttpMethods;
import app.utilities.RequestHttp;
import app.utilities.ResponseHttp;


@Component
public class SynchronizerServices {

	private static LinkedList<RequestSynchronObject> crOperationsBank;
	private static LinkedList<RequestSynchronObject> logList;

	private static long timeStamp = 0;

	public SynchronizerServices() {

		SynchronizerServices.crOperationsBank = new LinkedList<RequestSynchronObject>();
		SynchronizerServices.logList = new LinkedList<RequestSynchronObject>();

	}

	public static LinkedList<RequestSynchronObject> getCrOperationsBank() {
		return crOperationsBank;
	}

	public static void setCrOperationsBank(LinkedList<RequestSynchronObject> crOperationsBank) {
		SynchronizerServices.crOperationsBank = crOperationsBank;
	}

	public static long getTimeStamp() {
		return timeStamp;
	}

	public static void setTimeStamp(long timeStamp) {
		SynchronizerServices.timeStamp = timeStamp;
	}

	public void addRequestBank(RequestSynchronObject synch) {

		if (crOperationsBank.isEmpty()) {

			crOperationsBank.add(synch);

		} else {

			int range = 0;

			while (range < crOperationsBank.size()
					&& crOperationsBank.get(range).getTimeStamp() > synch.getTimeStamp()) {

				range++;

			}

			crOperationsBank.add(synch);

		}

	}

	public void exitCriticalRegion(OperationsModel operation) {
		
		RequestSynchronObject request = findByOperation(operation).get();
		crOperationsBank.remove(request);
		logList.add(request);
		
	}

	public Optional<RequestSynchronObject> findByOperation(OperationsModel operation) {

		for (RequestSynchronObject request : crOperationsBank) {

			if (request.getOperation().equals(operation)) {

				return Optional.of(request);

			}

		}
		
		return Optional.empty();

	}

	public ArrayList<ResponseHttp> enterCriticalRegion(OperationsModel operation) throws ServerConnectionException {

		ArrayList<ResponseHttp> responses = new ArrayList<ResponseHttp>();

		RequestHttp request;
		RequestSynchronObject synchObject;
		Gson gson = new Gson();

		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		timeStamp++;
		synchObject = new RequestSynchronObject(timeStamp, operation);
		addRequestBank(synchObject);
		request = new RequestHttp(HttpMethods.GET.getMethod(), "/account/reply", HttpVersion.HTTP_1_1.toString(),header, synchObject.toJSON());

		for (BanksEnum bank : BanksEnum.values()) {

			new Thread(() -> {

				try {

					ResponseHttp response = Http.sendHTTPRequestAndGetHttpResponse(request, bank.getBank().getIp());
					ReplySynchronObject resp = gson.fromJson(request.getBody(), ReplySynchronObject.class);
					timeStamp = Math.max(timeStamp, resp.getCurrentTimeStamp()) +1;
					responses.add(response);

				} catch (IOException e) {

					e.printStackTrace();

				}

			}).start();

		}

		while (responses.toArray().length != BanksEnum.values().length);

		return responses;

	}

	public ReplySynchronObject replyMessage(RequestSynchronObject synch) throws UnknownHostException {

		boolean replyDeferred = true;
		ReplySynchronObject message = null;

		while (replyDeferred) {

			if ((!synch.getOperation().getAccountOrigin().equals(crOperationsBank.getFirst().getOperation().getAccountOrigin()))|| (synch.getOperation().getAccountOrigin().equals(crOperationsBank.getFirst().getOperation().getAccountOrigin())&& crOperationsBank.getFirst().getTimeStamp() > synch.getTimeStamp())) {

				message = new ReplySynchronObject(timeStamp);
				replyDeferred = false;

			}

		}

		return message;

	}

}
