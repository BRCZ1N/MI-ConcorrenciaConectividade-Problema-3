package app.model;

import com.google.gson.Gson;

/**
 * Classe que representa um modelo de conta de operação.
 */
public class OperationAccountModel {

    private String idAccount;
    private BankModel bank;

    /**
     * Construtor padrão da classe OperationAccountModel.
     */
    public OperationAccountModel() {

    }

    /**
     * Construtor da classe OperationAccountModel.
     *
     * @param idAccount O ID da conta.
     * @param bank      O modelo de banco.
     */
    public OperationAccountModel(String idAccount, BankModel bank) {
        this.idAccount = idAccount;
        this.bank = bank;
    }

    /**
     * Retorna o ID da conta.
     *
     * @return O ID da conta.
     */
    public String getIdAccount() {
        return idAccount;
    }

    /**
     * Define o ID da conta.
     *
     * @param idAccount O ID da conta.
     */
    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    /**
     * Retorna o modelo de banco.
     *
     * @return O modelo de banco.
     */
    public BankModel getBank() {
        return bank;
    }

    /**
     * Define o modelo de banco.
     *
     * @param bank O modelo de banco.
     */
    public void setBank(BankModel bank) {
        this.bank = bank;
    }

    /**
     * Converte o objeto OperationAccountModel em uma representação JSON.
     *
     * @return Uma string JSON que representa o objeto OperationAccountModel.
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

