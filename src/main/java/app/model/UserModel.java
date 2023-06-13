package app.model;

import com.google.gson.Gson;

/**
 * Classe que representa um usuário.
 */
public class UserModel {

    private String name;
    private String cpf;

    /**
     * Construtor da classe UserModel.
     *
     * @param name O nome do usuário.
     * @param cpf  O CPF do usuário.
     */
    public UserModel(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

    /**
     * Retorna o nome do usuário.
     *
     * @return O nome do usuário.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do usuário.
     *
     * @param name O nome do usuário.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna o CPF do usuário.
     *
     * @return O CPF do usuário.
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do usuário.
     *
     * @param cpf O CPF do usuário.
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Converte o objeto UserModel em uma representação JSON.
     *
     * @return Uma string JSON que representa o objeto UserModel.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
