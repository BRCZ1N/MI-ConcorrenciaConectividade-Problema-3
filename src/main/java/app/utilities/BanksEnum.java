package app.utilities;

import app.model.BankModel;

/**
 * Enumeração que representa os bancos disponíveis.
 */
public enum BanksEnum {

    BANK_1(new BankModel("B0001", "http://172.16.103.3:8000")),
    BANK_2(new BankModel("B0002", "http://172.16.103.4:8000")),
    BANK_3(new BankModel("B0003", "http://172.16.103.5:8000")),
    BANK_4(new BankModel("B0004", "http://172.16.103.6:8000"));

    private BankModel bank;

    /**
     * Construtor da enumeração BanksEnum.
     *
     * @param bank O objeto BankModel representando o banco.
     */
    private BanksEnum(BankModel bank) {
        this.bank = bank;
    }

    /**
     * Retorna o objeto BankModel do banco.
     *
     * @return O objeto BankModel do banco.
     */
    public BankModel getBank() {
        return bank;
    }

    /**
     * Define o objeto BankModel do banco.
     *
     * @param bank O objeto BankModel do banco.
     */
    public void setBank(BankModel bank) {
        this.bank = bank;
    }

}

}
