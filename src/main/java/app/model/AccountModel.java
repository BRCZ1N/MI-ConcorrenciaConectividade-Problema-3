package app.model;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONObject;

/**
 * Modelo de conta bancária.
 */
public class AccountModel {

    private String id;
    private ArrayList<UserModel> beneficiares;
    private String password;
    private Double balance;
    private BankModel bank;
    private BlockingQueue<OperationsModel> queueOperations;

    /**
     * Construtor padrão.
     */
    public AccountModel() {
    }

    /**
     * Construtor que recebe informações da conta bancária.
     *
     * @param password     A senha da conta.
     * @param bank         O modelo do banco da conta.
     * @param beneficiares Lista de beneficiários da conta.
     */
    public AccountModel(String password, BankModel bank, ArrayList<UserModel> beneficiares) {
        this.password = password;
        this.bank = bank;
        this.beneficiares = beneficiares;
        this.queueOperations = new LinkedBlockingQueue<>();
    }

    // Métodos getters e setters

    /**
     * Obtém o ID da conta.
     *
     * @return O ID da conta.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID da conta.
     *
     * @param id O ID da conta.
     */
    public void setId(String id) {
        this.id = id;
    }

    // Restante dos métodos e atributos

    /**
     * Adiciona um elemento à fila de operações da conta.
     *
     * @param operation A operação a ser adicionada.
     */
    public void queueAddElement(OperationsModel operation) {
        this.queueOperations.add(operation);
    }

    /**
     * Obtém a representação JSON da conta.
     *
     * @return A representação JSON da conta.
     */
    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("password", this.password);
        json.put("bank", this.bank);
        json.put("beneficiares", this.beneficiares);
        return json.toString();
    }

}
