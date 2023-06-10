package app.services;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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

	private static LinkedList<RequestSynchronObject> crOperationsAll;
	private static LinkedList<RequestSynchronObject> crOperationsBank;
	private RequestSynchronObject currentRequest;

	private static long timeStamp = 0;

	public SynchronizerServices() {

		SynchronizerServices.crOperationsAll = new LinkedList<RequestSynchronObject>();
		SynchronizerServices.crOperationsBank = new LinkedList<RequestSynchronObject>();

	}

	public static LinkedList<RequestSynchronObject> getCrOperations() {
		return crOperationsAll;
	}

	public static void setCrOperations(LinkedList<RequestSynchronObject> crOperations) {
		SynchronizerServices.crOperationsAll = crOperations;
	}

	public static long getTimeStamp() {
		return timeStamp;
	}

	public static void setTimeStamp(long timeStamp) {
		SynchronizerServices.timeStamp = timeStamp;
	}

	public void addRequestAll(RequestSynchronObject synch) {

		if (crOperationsAll.isEmpty()) {

			crOperationsAll.add(synch);

		} else {

			int range = 0;

			while (range < crOperationsAll.size() && crOperationsAll.get(range).getTimeStamp() > synch.getTimeStamp()) {

				range++;

			}

			crOperationsAll.add(synch);

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

	public boolean execCriticalRegion(OperationsModel operation) throws ServerConnectionException {

		ArrayList<ResponseHttp> requests = requestEnterCriticalRegion(operation);
		if (verifyResponses(requests) && requests.toArray().length == BanksEnum.values().length) {

			return true;

		}
		
		return false;


	}

	public ArrayList<ResponseHttp> requestEnterCriticalRegion(OperationsModel operation) throws ServerConnectionException {

		ArrayList<ResponseHttp> responses = new ArrayList<ResponseHttp>();

		RequestHttp request;
		ResponseHttp response;
		Optional<RequestSynchronObject> resultSearch;
		RequestSynchronObject synchObject;

		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		resultSearch = findByOperation(operation);
		if (findByOperation(operation).isEmpty()) {

			timeStamp++;
			synchObject = new RequestSynchronObject(timeStamp, operation);
			addRequestAll(synchObject);
			addRequestBank(synchObject);

		} else {

			synchObject = resultSearch.get();

		}

		for (BanksEnum bank : BanksEnum.values()) {

			try {

				request = new RequestHttp(HttpMethods.GET.getMethod(), "/account/reply",HttpVersion.HTTP_1_1.toString(), header, synchObject.toJSON());
				response = Http.sendHTTPRequestAndGetHttpResponse(request, bank.getBank().getIp());
				responses.add(response);

			} catch (IOException e) {

				throw new ServerConnectionException();

			}

		}

		return responses;

	}

	public ReplySynchronObject replyMessage(RequestSynchronObject synch) throws UnknownHostException {

		boolean wait = true;
		ReplySynchronObject message = null;
		
		while(wait) {
			
			if ((!synch.getOperation().getAccountOrigin().equals(crOperationsBank.getFirst().getOperation().getAccountOrigin())) || (synch.getOperation().getAccountOrigin().equals(crOperationsBank.getFirst().getOperation().getAccountOrigin()) && crOperationsBank.getFirst().getTimeStamp() > synch.getTimeStamp())) {

				message = new ReplySynchronObject(timeStamp);
				wait = false;
				
			} 
			
		}
		
		return message;
		
	

//		if(timeStamp > synch.getTimeStamp() &&  ) {
//			
//			
//			ReplySynchronObject message = new ReplySynchronObject(timeStamp);
//			
//			return message;
//			
//		}else if() {
//			
//			
//			
//		}else if() {
//			
//			
//			
//		}
//		
//		String ip = InetAddress.getLocalHost().getHostAddress();
//
//		if (synch.getOperation().getAccountOrigin().getBank().getIp().equals(ip) && crOperations.getFirst().equals(operation)&& operation.getTimeStamp() <= crOperations.getFirst().getTimeStamp()) {
//
//			return true;
//
//		} else {
//
//			return false;
//
//		}
	}

	public void releaseCR() {

	}

//	public Optional<OperationsModel> findCROperation() {
//		
//		for(OperationsModel operation:crOperations) {
//			
//			if(operation.getAccountOrigin().equals(operation.get) {
//				
//				
//				
//			}
//			
//		}
//		
//		
//	}

	public boolean verifyResponses(ArrayList<ResponseHttp> responses) {

		for (ResponseHttp response : responses) {

			if (!response.getStatusLine().equals(HttpStatus.OK.getReasonPhrase())) {

				return false;

			}

		}

		return true;

	}

	public Optional<RequestSynchronObject> findByOperation(OperationsModel operation) {

		for (RequestSynchronObject object : crOperationsAll) {

			if (object.getOperation().equals(object.getOperation())) {

				return Optional.of(object);

			}

		}

		return Optional.empty();

	}

}
