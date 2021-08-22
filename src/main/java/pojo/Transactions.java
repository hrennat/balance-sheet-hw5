package pojo;

public class Transactions {

    private int transactionId;
    private int accountId;
    private long amount;

    public Transactions (int transactionId, int accountId, long amount){
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount =amount;
    }


    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    public int getAccountId(){
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }


    public long getAmount(){
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" + "TransactionId=" + transactionId + ", AccountId='" + accountId + '\'' + ", Amount=" + amount + '}'; }


}
