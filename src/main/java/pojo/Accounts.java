package pojo;

public class Accounts {


    private int accountId;
    private int userId;
    private long balance;
    private String currency;

    public Accounts (int accountId, int userId, long balance, String currency){
        this.accountId = accountId;
        this.userId = userId;
        this.balance =balance;
        this.currency = currency;
    }

    public int getAccountId(){
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public  long getBalance(){
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
    public String getCurrency() {return currency;}
    public void setCurrency(String currency){
        this.currency = currency;
    }
    @Override
    public String toString() {
        return "Account{" + "AccountId=" + accountId + ", UserId='" + userId + '\'' + ", Balance=" + balance + ", Currency" + currency +'}'; }


}
