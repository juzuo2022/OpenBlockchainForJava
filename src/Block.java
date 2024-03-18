import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data; // 区块包含的数据
    private long timeStamp; // 时间戳
    private int nonce; // 随机数，用于工作量证明
    private ArrayList<Transaction> transactions = new ArrayList<>(); // 交易列表

    // 区块的构造函数
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    // 计算区块的哈希值
    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                Long.toString(timeStamp) +
                Integer.toString(nonce) +
                data
        );
    }

    // 区块的挖矿（工作量证明）
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); // 创建一个前导0字符串

        while(!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    // 添加交易到区块
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
    }
}
