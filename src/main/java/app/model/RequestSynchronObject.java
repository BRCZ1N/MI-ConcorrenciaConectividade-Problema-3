package app.model;

import com.google.gson.Gson;

/**
 * Classe que representa um objeto de solicitação de sincronização.
 */
public class RequestSynchronObject {

    private long timeStamp;
    private OperationsModel operation;

    /**
     * Construtor da classe RequestSynchronObject.
     *
     * @param timeStamp O timestamp da solicitação.
     * @param operation O objeto de operações associado à solicitação.
     */
    public RequestSynchronObject(long timeStamp, OperationsModel operation) {
        this.timeStamp = timeStamp;
        this.operation = operation;
    }

    /**
     * Retorna o timestamp da solicitação.
     *
     * @return O timestamp da solicitação.
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Define o timestamp da solicitação.
     *
     * @param timeStamp O timestamp da solicitação.
     */
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Retorna o objeto de operações associado à solicitação.
     *
     * @return O objeto de operações associado à solicitação.
     */
    public OperationsModel getOperation() {
        return operation;
    }

    /**
     * Define o objeto de operações associado à solicitação.
     *
     * @param operation O objeto de operações associado à solicitação.
     */
    public void setOperation(OperationsModel operation) {
        this.operation = operation;
    }

    /**
     * Converte o objeto RequestSynchronObject em uma representação JSON.
     *
     * @return Uma string JSON que representa o objeto RequestSynchronObject.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
