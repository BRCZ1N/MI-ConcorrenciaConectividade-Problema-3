package app.services;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

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

@Component
/**
 * Classe responsável por representar os serviços de sincronização
 */
public class SynchronizerServices {

	private CopyOnWriteArrayList<RequestSynchronObject> requestCrOperationsBank;
	private CopyOnWriteArrayList<RequestSynchronObject> logList;
	private CopyOnWriteArrayList<RequestSynchronObject> activeCrOperationsBank;
	private AtomicLong timeStamp;

	/**
	 * Construtor da classe SynchronizerServices.
	 * Inicializa as listas e o contador de timestamp.
	 */
	public SynchronizerServices() {

		this.timeStamp = new AtomicLong();
		this.timeStamp.set(0);
		this.requestCrOperationsBank = new CopyOnWriteArrayList<RequestSynchronObject>();
		this.logList = new CopyOnWriteArrayList<RequestSynchronObject>();
		this.activeCrOperationsBank = new CopyOnWriteArrayList<RequestSynchronObject>();

	}

	/**
	 * Obtém a lista de requisições de operações bancárias críticas.
	 * @return A lista de requisições de operações bancárias críticas.
	 */
	public CopyOnWriteArrayList<RequestSynchronObject> getRequestCrOperationsBank() {
		return requestCrOperationsBank;
	}

	/**
	 * Define a lista de requisições de operações bancárias críticas.
	 * @param requestCrOperationsBank A lista de requisições de operações bancárias críticas.
	 */
	public void setRequestCrOperationsBank(CopyOnWriteArrayList<RequestSynchronObject> requestCrOperationsBank) {
		this.requestCrOperationsBank = requestCrOperationsBank;
	}

	/**
	 * Obtém a lista de registros de log.
	 * @return A lista de registros de log.
	 */
	public CopyOnWriteArrayList<RequestSynchronObject> getLogList() {
		return logList;
	}

	/**
	 * Define a lista de registros de log.
	 * @param logList A lista de registros de log.
	 */
	public void setLogList(CopyOnWriteArrayList<RequestSynchronObject> logList) {
		this.logList = logList;
	}

	/**
	 * Obtém a lista de operações bancárias críticas ativas.
	 * @return A lista de operações bancárias críticas ativas.
	 */
	public CopyOnWriteArrayList<RequestSynchronObject> getActiveCrOperationsBank() {
		return activeCrOperationsBank;
	}

	/**
	 * Define a lista de operações bancárias críticas ativas.
	 * @param activeCrOperationsBank A lista de operações bancárias críticas ativas.
	 */
	public void setActiveCrOperationsBank(CopyOnWriteArrayList<RequestSynchronObject> activeCrOperationsBank) {
		this.activeCrOperationsBank = activeCrOperationsBank;
	}
	
	/**
	 * Obtém o contador de timestamp.
	 * @return O contador de timestamp.
	 */
	public AtomicLong getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Define o contador de timestamp.
	 * @param timeStamp O contador de timestamp.
	 */
	public void setTimeStamp(AtomicLong timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Adiciona uma requisição a lista de operações críticas em espera do banco.
	 * @param synch O objeto de requisição de sincronização.
	 */
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

	/**
	 * Adiciona uma requisição a lista de operações críticas ativas do banco.
	 * @param synch O objeto de requisição de sincronização.
	 */
	public void addCrRegionsBank(RequestSynchronObject synch) {

		if (activeCrOperationsBank.isEmpty()) {

			activeCrOperationsBank.add(synch);

		} else {

			int range = 0;

			while (range < activeCrOperationsBank.size()
					&& activeCrOperationsBank.get(range).getTimeStamp() < synch.getTimeStamp()) {

				range++;

			}

			activeCrOperationsBank.add(synch);

		}

	}

	/**
	 * Sai da região crítica para uma determinada operação.
	 * @param operation A operação a ser finalizada na região crítica.
	 */
	public void exitCriticalRegion(OperationsModel operation) {

		RequestSynchronObject request = findByOperation(operation).get();
		activeCrOperationsBank.remove(request);
		logList.add(request);

	}

	/**
	 * Localiza uma requisição de sincronização com base em uma operação.
	 * @param operation A operação a ser pesquisada.
	 * @return Um objeto Optional contendo a requisição de sincronização encontrada, ou um Optional vazio se não encontrada.
	 */
	public Optional<RequestSynchronObject> findByOperation(OperationsModel operation) {

		for (RequestSynchronObject request : activeCrOperationsBank) {

			if (request.getOperation().equals(operation)) {

				return Optional.of(request);

			}

		}

		return Optional.empty();

	}

	/**
	 * Envia as requisições aos outros bancos do sistema para uma operação que precisa acessar uma zona crítica.
	 * @param operation A operação para a qual a mensagem será enviada.
	 * @throws ServerConnectionException Exceção lançada em caso de falha na conexão com o servidor.
	 */
	public void requestMessage(OperationsModel operation) throws ServerConnectionException {

		ArrayList<ResponseHttp> responses = new ArrayList<ResponseHttp>();

		RequestHttp request;
		RequestSynchronObject synchObject;

		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		timeStamp.incrementAndGet();
		synchObject = new RequestSynchronObject(timeStamp.get(), operation);
		addRequestBank(synchObject);
		request = new RequestHttp(HttpMethods.POST.getMethod(), "/account/reply", HttpVersion.HTTP_1_1.toString(),header, synchObject.toJSON());

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

		while (responses.toArray().length != BanksEnum.values().length);

		releaseTimeStamp(responses);
		requestCrOperationsBank.remove(synchObject);
		addCrRegionsBank(synchObject);

	}

	/**
	 * Responde a uma requisição a entrada de seção crítica de um banco solicitante ou atrasa a mesma caso não possa permitir.
	 * @param synch O objeto de requisição de sincronização.
	 * @return O objeto ReplySynchronObject contendo a mensagem de resposta.
	 * @throws UnknownHostException Exceção lançada em caso de falha na conexão com o host desconhecido.
	 */
	public ReplySynchronObject replyMessage(RequestSynchronObject synch) throws UnknownHostException {

		boolean replyDeferred = true;
		ReplySynchronObject message = null;

		while (replyDeferred) {

			Optional<RequestSynchronObject> resultSearchRequestList = isContainsRequestCriticalZone(synch.getOperation(), requestCrOperationsBank);
			Optional<RequestSynchronObject> resultSearchActiveList = isContainsRequestCriticalZone(synch.getOperation(),activeCrOperationsBank);

			//Se está presente na lista de regiões criticas ativas e não está presente na lista de requisições
			//Se não está presente na lista de regiões crítica ativas e se estiver na lista de requisições e o timeStamp do servidor que recebeu a request for menor 
			//Se não está presente na lista de regiões crítica ativas e se estiver na lista de requisições e o timeStamp do servidor que recebeu a request for igual e o id do banco for menor
			
			//Obs:Criterio de desempate pelo ID do banco, nenhum banco terá o mesmo ID.
			
			if ((resultSearchActiveList.isEmpty() && resultSearchRequestList.isEmpty()) || (resultSearchActiveList.isEmpty() && !resultSearchRequestList.isEmpty() 
				&& resultSearchRequestList.get().getTimeStamp() > synch.getTimeStamp()) || (resultSearchActiveList.isEmpty() && !resultSearchRequestList.isEmpty() 
				&& resultSearchRequestList.get().getTimeStamp() == synch.getTimeStamp() && resultSearchRequestList.get().getOperation().getAccountOrigin().getBank().getId() > synch.getOperation().getAccountOrigin().getBank().getId())) {

				replyDeferred = false;
				message = new ReplySynchronObject(timeStamp.get());

			}

		}

		return message;
	}

	/**
	 * Verifica se uma operação está presente na lista de regiões críticas ativas ou em espera.
	 * @param operation A operação a ser verificada.
	 * @param list A lista de regiões críticas ativas ou em espera.
	 * @return Um objeto Optional contendo a requisição de sincronização encontrada, ou um Optional vazio se não encontrada.
	 */
	public Optional<RequestSynchronObject> isContainsRequestCriticalZone(OperationsModel operation,
			CopyOnWriteArrayList<RequestSynchronObject> list) {

		for (RequestSynchronObject request : list) {

			if (request.getOperation().getAccountOrigin().equals(operation.getAccountOrigin())) {

				return Optional.of(request);

			}

		}

		return Optional.empty();

	}
	
	/**
	 * Atualiza o timestamp com base nas respostas recebidas.
	 * @param responses As respostas HTTP recebidas dos servidores.
	 */
	public void releaseTimeStamp(ArrayList<ResponseHttp> responses) {
		
		for(ResponseHttp response: responses) {
			
			Gson gson = new Gson();
			ReplySynchronObject resp = gson.fromJson(response.getBody(), ReplySynchronObject.class);
			timeStamp.set(Math.max(timeStamp.get(), resp.getCurrentTimeStamp()) + 1);
			
		}
		
	}

}
