package app.model;

import com.google.gson.Gson;

/**
 * Classe que representa o modelo de conta de login.
 */
public class LoginAccountModel {

    private String id;
    private String password;

    /**
     * Construtor da classe LoginAccountModel.
     *
     * @param id       O ID da conta de login.
     * @param password A senha da conta de login.
     */
    public LoginAccountModel(String id, String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * Retorna o ID da conta de login.
     *
     * @return O ID da conta de login.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID da conta de login.
     *
     * @param id O ID da conta de login.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna a senha da conta de login.
     *
     * @return A senha da conta de login.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define a senha da conta de login.
     *
     * @param password A senha da conta de login.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Converte o objeto LoginAccountModel para uma representação JSON.
     *
     * @return A representação JSON do objeto LoginAccountModel.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
