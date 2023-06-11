package app.services;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

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
import io.netty.handler.codec.http.HttpVersion;

@Component
public class SynchronizerServices {

	private static LinkedList<RequestSynchronObject> crOperationsOtherBanks;
	private static LinkedList<RequestSynchronObject> crOperationsBank;
	private RequestSynchronObject currentRequest;

	private static long timeStamp = 0;

	public SynchronizerServices() {

		SynchronizerServices.crOperationsOtherBanks = new LinkedList<RequestSynchronObject>();
		SynchronizerServices.crOperationsBank = new LinkedList<RequestSynchronObject>();

	}

	public static LinkedList<RequestSynchronObject> getCrOperationsOtherBanks() {
		return crOperationsOtherBanks;
	}

	public static void setCrOperationsOtherBanks(LinkedList<RequestSynchronObject> crOperationsOtherBanks) {
		SynchronizerServices.crOperationsOtherBanks = crOperationsOtherBanks;
	}

	public static LinkedList<RequestSynchronObject> getCrOperationsBank() {
		return crOperationsBank;
	}

	public static void setCrOperationsBank(LinkedList<RequestSynchronObject> crOperationsBank) {
		SynchronizerServices.crOperationsBank = crOperationsBank;
	}

	public RequestSynchronObject getCurrentRequest() {
		return currentRequest;
	}

	public void setCurrentRequest(RequestSynchronObject currentRequest) {
		this.currentRequest = currentRequest;
	}

	public static long getTimeStamp() {
		return timeStamp;
	}

	public static void setTimeStamp(long timeStamp) {
		SynchronizerServices.timeStamp = timeStamp;
	}

	public void addRequestOtherBanks(RequestSynchronObject synch) {

		if (crOperationsOtherBanks.isEmpty()) {

			crOperationsOtherBanks.add(synch);

		} else {

			int range = 0;

			while (range < crOperationsOtherBanks.size()
					&& crOperationsOtherBanks.get(range).getTimeStamp() > synch.getTimeStamp()) {

				range++;

			}

			crOperationsOtherBanks.add(synch);

		}

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

	public ArrayList<ResponseHttp> requestEnterCriticalRegion(OperationsModel operation) throws ServerConnectionException {

		ArrayList<ResponseHttp> responses = new ArrayList<ResponseHttp>();

		RequestHttp request;
		RequestSynchronObject synchObject;

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
					responses.add(response);
					
				} catch (IOException e) {
					
					e.printStackTrace();
					
				}

			}).start();

		}
		
		while(responses.toArray().length != BanksEnum.values().length);

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

	public Optional<RequestSynchronObject> findByOperation(OperationsModel operation) {

		for (RequestSynchronObject object : crOperationsOtherBanks) {

			if (object.getOperation().equals(object.getOperation())) {

				return Optional.of(object);

			}

		}

		return Optional.empty();

	}

}
