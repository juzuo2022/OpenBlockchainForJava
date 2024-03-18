import java.util.ArrayList;

public class Blockchain {
    private ArrayList<Block> blockchain;
    private ArrayList<Transaction> pendingTransactions;
    private int difficulty;
    private double miningReward;

    // 构造函数
    public Blockchain(int difficulty, double miningReward) {
        blockchain = new ArrayList<>();
        pendingTransactions = new ArrayList<>();
        this.difficulty = difficulty;
        this.miningReward = miningReward;
        blockchain.add(createGenesisBlock());
    }

    // 创建创世区块
    private Block createGenesisBlock() {
        return new Block("Genesis Block", "0");
    }

    // 创建新的交易
    public Transaction createTransaction(String sender, String recipient, double amount) {
        return new Transaction(sender, recipient, amount);
    }

    // 添加新的交易到待处理交易列表
    public void addTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    // 验证交易的合法性
    private boolean processTransaction(Transaction transaction) {
        // 在真实应用中，我们可以校验交易的签名、余额等
        return true;
    }

    // 验证区块链的完整性和执行工作量证明
    public void minePendingTransactions(String minerAddress) {
        Block block = new Block("0", blockchain.get(blockchain.size() - 1).hash); // 创建新的区块
        for(Transaction transaction : pendingTransactions) {
            if(processTransaction(transaction)) {
                block.addTransaction(transaction);
            }
        }
        block.mineBlock(difficulty); // 执行工作量证明
        blockchain.add(block);
        pendingTransactions = new ArrayList<>(); // 清空待处理交易列表
        // 发放挖矿奖励
        pendingTransactions.add(new Transaction("0", minerAddress, miningReward));
    }

    // 验证区块链的完整性
    public boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        for(int i=1; i<blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            // 检查当前区块的哈希值是否正确
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            // 检查前一个区块的哈希值是否等于当前区块中存储的前一个哈希值
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }
}
