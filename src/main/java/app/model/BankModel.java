package app.model;

import com.google.gson.Gson;

/**
 * Classe que representa um modelo de banco.
 */
public class BankModel {

    private String id;
    private String ip;

    /**
     * Construtor da classe BankModel.
     *
     * @param id O ID do banco.
     * @param ip O endereço IP do banco.
     */
    public BankModel(String id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    /**
     * Construtor vazio da classe BankModel.
     */
    public BankModel() {
    }

    /**
     * Obtém o ID do banco.
     *
     * @return O ID do banco.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID do banco.
     *
     * @param id O ID do banco.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtém o endereço IP do banco.
     *
     * @return O endereço IP do banco.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Define o endereço IP do banco.
     *
     * @param ip O endereço IP do banco.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Converte o objeto BankModel em uma representação JSON.
     *
     * @return Uma string JSON que representa o objeto BankModel.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
