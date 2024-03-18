public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain(4, 100); // 创建一个区块链，设置挖矿难度为4，挖矿奖励为100

        // 创建一些交易
        blockchain.addTransaction(blockchain.createTransaction("Alice", "Bob", 50));
        blockchain.addTransaction(blockchain.createTransaction("Bob", "Charlie", 25));

        // 挖矿
        blockchain.minePendingTransactions("Miner1");

        // 验证区块链的完整性
        System.out.println("Is blockchain valid? " + blockchain.isChainValid());
    }
}
