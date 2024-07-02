package codesquad;

import codesquad.handler.HttpRequestHandler;
import codesquad.http.HttpRequest;
import codesquad.http.HttpRequestMapper;
import codesquad.http.HttpResponse;
import codesquad.http.HttpVersion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebApplicationServer {

    public static final String CRLF = "\r\n";
    public static final int PORT = 8080;
    private static final Logger logger = LoggerFactory.getLogger(WebApplicationServer.class);

    private final HttpRequestHandler httpRequestHandler;

    public WebApplicationServer(HttpRequestHandler httpRequestHandler) {
        this.httpRequestHandler = httpRequestHandler;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Listening for connection on port 8080 ....");
            run(serverSocket);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void run(ServerSocket serverSocket) {
        while (true) {
            try (Socket socket = serverSocket.accept();
                 InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()
            ) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            } catch (IOException | RuntimeException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
