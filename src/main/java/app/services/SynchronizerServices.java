package app.services;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpVersion;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import app.exceptions.ServerConnectionException;
import app.model.OperationsModel;
import app.model.ReplySynchronObject;
import app.model.RequestSynchronObject;
import app.utilities.BanksEnum;
import app.utilities.Http;
import app.utilities.HttpMethods;
import app.utilities.RequestHttp;
import app.utilities.ResponseHttp;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SynchronizerServices {

	private CopyOnWriteArrayList<RequestSynchronObject> requestCrOperationsBank;
	private CopyOnWriteArrayList<RequestSynchronObject> logList;
	private CopyOnWriteArrayList<RequestSynchronObject> activeCrOperationsBank;

	private AtomicLong timeStamp;

	public SynchronizerServices() {

		this.timeStamp = new AtomicLong();
		this.timeStamp.set(0);
		this.requestCrOperationsBank = new CopyOnWriteArrayList<RequestSynchronObject>();
		this.logList = new CopyOnWriteArrayList<RequestSynchronObject>();
		this.activeCrOperationsBank = new CopyOnWriteArrayList<RequestSynchronObject>();

	}

	public CopyOnWriteArrayList<RequestSynchronObject> getRequestCrOperationsBank() {
		return requestCrOperationsBank;
	}

	public void setRequestCrOperationsBank(CopyOnWriteArrayList<RequestSynchronObject> requestCrOperationsBank) {
		this.requestCrOperationsBank = requestCrOperationsBank;
	}

	public CopyOnWriteArrayList<RequestSynchronObject> getLogList() {
		return logList;
	}

	public void setLogList(CopyOnWriteArrayList<RequestSynchronObject> logList) {
		this.logList = logList;
	}

	public CopyOnWriteArrayList<RequestSynchronObject> getActiveCrOperationsBank() {
		return activeCrOperationsBank;
	}

	public void setActiveCrOperationsBank(CopyOnWriteArrayList<RequestSynchronObject> activeCrOperationsBank) {
		this.activeCrOperationsBank = activeCrOperationsBank;
	}

	public AtomicLong getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(AtomicLong timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void addRequestBank(RequestSynchronObject synch) {

		if (requestCrOperationsBank.isEmpty()) {

			requestCrOperationsBank.add(synch);

		} else {

			int range = 0;

			while (range < requestCrOperationsBank.size()
					&& requestCrOperationsBank.get(range).getTimeStamp() < synch.getTimeStamp()) {

				range++;

			}

			requestCrOperationsBank.add(synch);

		}

	}

	public void addCrRegionsBank(RequestSynchronObject synch) {

		if (activeCrOperationsBank.isEmpty()) {

			activeCrOperationsBank.add(synch);

		} else {

			int range = 0;

			while (range < activeCrOperationsBank.size()
					&& activeCrOperationsBank.get(range).getTimeStamp() < synch.getTimeStamp()) {

				range++;

			}

			requestCrOperationsBank.add(synch);

		}

	}

	public void exitCriticalRegion(OperationsModel operation) {

		RequestSynchronObject request = findByOperation(operation).get();
		activeCrOperationsBank.remove(request);
		logList.add(request);

	}

	public Optional<RequestSynchronObject> findByOperation(OperationsModel operation) {

		for (RequestSynchronObject request : requestCrOperationsBank) {

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

		timeStamp.incrementAndGet();
		synchObject = new RequestSynchronObject(timeStamp.get(), operation);
		addRequestBank(synchObject);
		request = new RequestHttp(HttpMethods.GET.getMethod(), "/account/reply", HttpVersion.HTTP_1_1.toString(),
				header, synchObject.toJSON());

		for (BanksEnum bank : BanksEnum.values()) {

			new Thread(() -> {

				try {

					ResponseHttp response = Http.sendHTTPRequestAndGetHttpResponse(request, bank.getBank().getIp());
					ReplySynchronObject resp = gson.fromJson(request.getBody(), ReplySynchronObject.class);
					timeStamp.set(Math.max(timeStamp.get(), resp.getCurrentTimeStamp()) + 1);
					responses.add(response);

				} catch (IOException e) {

					e.printStackTrace();

				}

			}).start();

		}

		while (responses.toArray().length != BanksEnum.values().length)
			;

		requestCrOperationsBank.remove(synchObject);
		addCrRegionsBank(synchObject);

		return responses;

	}

	public ReplySynchronObject replyMessage(RequestSynchronObject synch) throws UnknownHostException {

		boolean replyDeferred = true;
		ReplySynchronObject message = null;

		while (replyDeferred) {

			Optional<RequestSynchronObject> resultSearchRequestList = isContainsRequestCriticalZone(synch.getOperation(), requestCrOperationsBank);
			Optional<RequestSynchronObject> resultSearchActiveList = isContainsRequestCriticalZone(synch.getOperation(),activeCrOperationsBank);

			if ((resultSearchRequestList.isEmpty() && resultSearchActiveList.isEmpty())|| (!resultSearchRequestList.isEmpty() && resultSearchRequestList.get().getTimeStamp() > synch.getTimeStamp())) {

				replyDeferred = false;
				message = new ReplySynchronObject(timeStamp.get());

			}

		}

		return message;
	}

	public Optional<RequestSynchronObject> isContainsRequestCriticalZone(OperationsModel operation,CopyOnWriteArrayList<RequestSynchronObject> list) {

		for (RequestSynchronObject request : list) {

			if (request.getOperation().getAccountOrigin().equals(operation.getAccountOrigin())) {

				return Optional.of(request);

			}

		}

		return Optional.empty();

	}

}
