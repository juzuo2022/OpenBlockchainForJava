public class Transaction {
    public String sender; // 发送者地址
    public String recipient; // 接收者地址
    public double amount; // 交易金额

    // 交易的构造函数
    public Transaction(String sender, String recipient, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }
}
