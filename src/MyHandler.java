import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "<html><head><title>Blockchain Explorer</title></head><body>" +
                          "<h1>Blockchain Explorer</h1>" +
                          "<p>Blockchain Integrity: <strong>Valid</strong></p>" +
                          "<button onclick=\"mineBlock()\">Mine Block</button>" +
                          "<button onclick=\"viewBlockchain()\">View Blockchain</button>" +
                          "<script>" +
                          "function mineBlock() {" +
                          "   // 发送挖矿请求到后端" +
                          "}" +
                          "function viewBlockchain() {" +
                          "   // 发送查看区块链请求到后端" +
                          "}" +
                          "</script>" +
                          "</body></html>";

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
