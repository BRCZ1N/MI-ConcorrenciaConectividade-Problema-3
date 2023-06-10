package app.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import app.exceptions.ServerConnectionException;
import app.model.OperationsModel;
import app.utilities.BanksEnum;
import app.utilities.Http;
import app.utilities.HttpMethods;
import app.utilities.RequestHttp;
import app.utilities.ResponseHttp;
import io.netty.handler.codec.http.HttpVersion;

@Component
public class SynchronizerServices {

	private static LinkedList<OperationsModel> crOperations;
	private static long timeStamp = 0;

	public SynchronizerServices() {

		SynchronizerServices.crOperations = new LinkedList<OperationsModel>();

	}

	public static LinkedList<OperationsModel> getCrOperations() {
		return crOperations;
	}

	public static void setCrOperations(LinkedList<OperationsModel> crOperations) {
		SynchronizerServices.crOperations = crOperations;
	}

	public static long getTimeStamp() {
		return timeStamp;
	}

	public static void setTimeStamp(long timeStamp) {
		SynchronizerServices.timeStamp = timeStamp;
	}

	public void addRequest(OperationsModel operation) {

		if (crOperations.isEmpty()) {

			crOperations.add(operation);

		} else {

			int range = 0;

			while (range < crOperations.size() && crOperations.get(range).getTimeStamp() > operation.getTimeStamp()) {

				range++;

			}

			crOperations.add(operation);

		}

	}

	public ArrayList<ResponseHttp> requestEnterCriticalRegion(OperationsModel operation) throws ServerConnectionException {

		ArrayList<ResponseHttp> responses = new ArrayList<ResponseHttp>();

		RequestHttp request;
		ResponseHttp response;

		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		timeStamp++;
		operation.setTimeStamp(timeStamp);
		crOperations.add(operation);

		for (BanksEnum bank : BanksEnum.values()) {

			try {

				request = new RequestHttp(HttpMethods.GET.getMethod(), "/synchronizer/reply",HttpVersion.HTTP_1_1.toString(), header);
				response = Http.sendHTTPRequestAndGetHttpResponse(request, bank.getBank().getIp());
				responses.add(response);

			} catch (IOException e) {

				throw new ServerConnectionException();

			}

		}

		
		return responses;
		
//		if (verifyResponses(responses) && crOperations.getFirst().equals(operation)) {
//
//			return true;
//
//		}

	}

	public boolean replyMessage(OperationsModel operation) throws UnknownHostException {

		String ip = InetAddress.getLocalHost().getHostAddress();

		if (operation.getAccountOrigin().getBank().getIp().equals(ip) && crOperations.getFirst().equals(operation)) {

			return true;

		} else {

		}

		return false;

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

}
