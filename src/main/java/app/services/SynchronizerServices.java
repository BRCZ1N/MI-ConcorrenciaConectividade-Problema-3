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
@Component
public class SynchronizerServices {

	private static CopyOnWriteArrayList<RequestSynchronObject> crOperationsBank;
	private static CopyOnWriteArrayList<RequestSynchronObject> logList;
	
	// olhar a biblioteca java.util.concurrent, pode servir na criação de listas para cenarios de concorrencia 
	private static long timeStamp = 0;

	public SynchronizerServices() {

		SynchronizerServices.crOperationsBank = new CopyOnWriteArrayList<RequestSynchronObject>();
		SynchronizerServices.logList = new CopyOnWriteArrayList<RequestSynchronObject>();

	}

	public static CopyOnWriteArrayList<RequestSynchronObject> getCrOperationsBank() {
		return crOperationsBank;
	}

	public static void setCrOperationsBank(CopyOnWriteArrayList<RequestSynchronObject> crOperationsBank) {
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

	    long initialTime = System.currentTimeMillis();
	    long timeout = 11000; 

	    while (replyDeferred && (System.currentTimeMillis() - initialTime) < timeout) {
	        if ((!synch.getOperation().getAccountOrigin().equals(crOperationsBank.get(0).getOperation().getAccountOrigin())) ||
	                (synch.getOperation().getAccountOrigin().equals(crOperationsBank.get(0).getOperation().getAccountOrigin()) &&
	                        crOperationsBank.get(0).getTimeStamp() > synch.getTimeStamp())) {
	            message = new ReplySynchronObject(timeStamp);
	            replyDeferred = false;
	        }
	    }

	    if (replyDeferred) {
	  
	        throw new RuntimeException("Tempo limite atingido ao aguardar a resposta.");
	      //serve pra caso demore muito, não lembro o tempo que precisa por no timeout
	    }

	    return message;
	}


}
