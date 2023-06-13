package app.model;

import com.google.gson.Gson;

/**
 * Classe que representa um modelo de mensagem.
 */
public class MessageModel {

    private String message;

    /**
     * Construtor padrão da classe MessageModel.
     */
    public MessageModel() {

    }

    /**
     * Construtor da classe MessageModel.
     *
     * @param message A mensagem.
     */
    public MessageModel(String message) {
        this.message = message;
    }

    /**
     * Retorna a mensagem.
     *
     * @return A mensagem.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Define a mensagem.
     *
     * @param message A mensagem.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Converte o objeto MessageModel em uma representação JSON.
     *
     * @return Uma string JSON que representa o objeto MessageModel.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
    /**
     * Retorna uma representação em string do objeto MessageModel.
     *
     * @return Uma string que representa o objeto MessageModel.
     */
    @Override
    public String toString() {
        return this.message;
    }
}
