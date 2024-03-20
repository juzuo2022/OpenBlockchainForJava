import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebServer {
    private static final Logger logger = LogManager.getLogger(WebServer.class);
    private static final int MINING_DIFFICULTY = 5;
    private static final String MINING_REWARD = "10";

    public static void main(String[] args) throws IOException {
        // 启动日志记录
        logger.info("Starting the server...");

        // 创建区块链实例
        Blockchain blockchain = new Blockchain(2);
        // 创建HTTP服务器实例并启动
        HTTPServer server = new HTTPServer(blockchain);
        server.startServer();
        logger.info("Server is running on port 8000");

        // 创建初始区块
        Block genesisBlock = new Block(0, getCurrentTimestamp(), "Genesis Block", "0");
        genesisBlock.mineBlock(MINING_DIFFICULTY);
        blockchain.addBlock(genesisBlock);
    }

    // 处理静态文件请求的处理程序
    static class FileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String response = "";

            if (path.equals("/")) {
                path = "/index.html";
            }

            File file = new File("webui" + path);
            if (file.exists() && !file.isDirectory()) {
                try {
                    byte[] fileBytes = Files.readAllBytes(Paths.get(file.getPath()));
                    response = new String(fileBytes);
                } catch (IOException e) {
                    logger.error("Error reading file: " + e.getMessage());
                    response = "Error reading file.";
                }
            } else {
                response = "File not found.";
            }

            // 设置 CORS 头信息
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

            // 发送文件内容作为响应
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // 处理获取区块链的请求的处理程序
    static class BlockchainHandler implements HttpHandler {
        private Blockchain blockchain;

        public BlockchainHandler(Blockchain blockchain) {
            this.blockchain = blockchain;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 设置 CORS 头信息
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

            // 将区块链转换为JSON格式的字符串作为响应
            String response = JSONUtils.toJson(blockchain.getChain());
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // 处理挖矿请求的处理程序
    static class MineHandler implements HttpHandler {
        private Blockchain blockchain;

        public MineHandler(Blockchain blockchain) {
            this.blockchain = blockchain;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Block lastBlock = blockchain.getLatestBlock();
            String data = "Mined Block";
            // 创建新的区块
            Block newBlock = new Block(lastBlock.getIndex() + 1, getCurrentTimestamp(), data, lastBlock.getHash());
            newBlock.mineBlock(MINING_DIFFICULTY);
            blockchain.addBlock(newBlock);

            // 给矿工发送奖励
            Block rewardBlock = new Block(newBlock.getIndex() + 1, getCurrentTimestamp(), "Mining Reward: " + MINING_REWARD, newBlock.getHash());
            blockchain.addBlock(rewardBlock);

            // 设置 CORS 头信息
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

            // 将新挖的区块转换为JSON格式的字符串作为响应
            String response = JSONUtils.toJson(newBlock);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // 区块链类
    private static class Blockchain {
        private List<Block> chain;
        private int difficulty;

        public Blockchain(int difficulty) {
            this.chain = new ArrayList<>();
            this.difficulty = difficulty;
        }

        public List<Block> getChain() {
            return chain;
        }

        public Block getLatestBlock() {
            return chain.get(chain.size() - 1);
        }

        public void addBlock(Block newBlock) {
            newBlock.mineBlock(difficulty);
            chain.add(newBlock);
        }
    }

    // 区块类
    private static class Block {
        private int index;
        private String timestamp;
        private String data;
        private String previousHash;
        private String hash;
        private int nonce;

        public Block(int index, String timestamp, String data, String previousHash) {
            this.index = index;
            this.timestamp = timestamp;
            this.data = data;
            this.previousHash = previousHash;
            this.hash = calculateHash();
        }

        public String calculateHash() {
            return StringUtil.applySha256(index + previousHash + timestamp + data + nonce);
        }

        public void mineBlock(int difficulty) {
            String target = StringUtil.getDificultyString(difficulty);
            while (!hash.substring(0, difficulty).equals(target)) {
                nonce++;
                hash = calculateHash();
            }
            logger.info("Block Mined!!! : " + hash);
        }

        public int getIndex() {
            return index;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getData() {
            return data;
        }

        public String getPreviousHash() {
            return previousHash;
        }

        public String getHash() {
            return hash;
        }
    }

    // 字符串工具类
    private static class StringUtil {
        public static String applySha256(String input) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(input.getBytes());
               
                StringBuilder hexString = new StringBuilder();
                for (byte elem : hash) {
                    String hex = Integer.toHexString(0xff & elem);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }
                return hexString.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String getDificultyString(int difficulty) {
            return new String(new char[difficulty]).replace('\0', '0');
        }
    }

    // JSON工具类
    private static class JSONUtils {
        private static final Gson gson = new Gson();

        public static String toJson(Object object) {
            return gson.toJson(object);
        }
    }

    // HTTP服务器类
    static class HTTPServer {
        private Blockchain blockchain;

        public HTTPServer(Blockchain blockchain) {
            this.blockchain = blockchain;
        }

        // 启动HTTP服务器
        public void startServer() throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new FileHandler());
            server.createContext("/blockchain", new BlockchainHandler(blockchain));
            server.createContext("/mine", new MineHandler(blockchain));
            server.setExecutor(null);
            server.start();
        }
    }

    // 获取当前时间戳的方法
    private static String getCurrentTimestamp() {
        return new Date().toString(); // 返回当前时间戳
    }
}
