package app.model;

import com.google.gson.Gson;

/**
 * Classe que representa um objeto de resposta de sincronização.
 */
public class ReplySynchronObject {

    private long currentTimeStamp;

    /**
     * Construtor da classe ReplySynchronObject.
     *
     * @param currentTimeStamp O timestamp atual.
     */
    public ReplySynchronObject(long currentTimeStamp) {
        this.currentTimeStamp = currentTimeStamp;
    }

    /**
     * Retorna o timestamp atual.
     *
     * @return O timestamp atual.
     */
    public long getCurrentTimeStamp() {
        return currentTimeStamp;
    }

    /**
     * Define o timestamp atual.
     *
     * @param currentTimeStamp O timestamp atual.
     */
    public void setCurrentTimeStamp(long currentTimeStamp) {
        this.currentTimeStamp = currentTimeStamp;
    }

    /**
     * Converte o objeto ReplySynchronObject em uma representação JSON.
     *
     * @return Uma string JSON que representa o objeto ReplySynchronObject.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
