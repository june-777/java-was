package codesquad;

import static codesquad.utils.StringUtils.CRLF;

import codesquad.handler.HttpRequestHandler;
import codesquad.http.HttpRequest;
import codesquad.http.HttpRequestMapper;
import codesquad.http.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpProcessor {

    private static final Logger logger = LoggerFactory.getLogger(WebApplicationServer.class);

    private final HttpRequestMapper httpRequestMapper;
    private final HttpRequestHandler httpRequestHandler;
    private final Socket socket;

    public HttpProcessor(HttpRequestMapper httpRequestMapper, HttpRequestHandler httpRequestHandler, Socket socket) {
        this.httpRequestMapper = httpRequestMapper;
        this.httpRequestHandler = httpRequestHandler;
        this.socket = socket;
    }

    public void process() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()
        ) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            HttpRequest httpRequest = httpRequestMapper.from(bufferedReader);
            HttpResponse httpResponse = httpRequestHandler.handle(httpRequest);

            sendResponse(outputStream, httpResponse);
        } catch (IOException | RuntimeException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Socket Close Error");
                e.printStackTrace();
            }
        }
    }

    public void sendResponse(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        sendStatus(outputStream, httpResponse);
        sendHeaders(outputStream, httpResponse);
        sendBody(outputStream, httpResponse);
    }

    private void sendStatus(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        String status = httpResponse.getHttpVersion() + " " + httpResponse.getHttpStatus().getCode() + " "
                + httpResponse.getHttpStatus().getRepresentation();
        outputStream.write(status.getBytes());
        outputStream.write(CRLF.getBytes());
    }

    private void sendHeaders(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        Map<String, String> headers = httpResponse.getHeaders();
        for (String headerName : headers.keySet()) {
            String headerValue = headers.get(headerName);
            String header = headerName + ": " + headerValue;
            outputStream.write(header.getBytes());
        }
        outputStream.write(CRLF.getBytes());
        outputStream.write(CRLF.getBytes());
    }

    private void sendBody(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        outputStream.write(httpResponse.getBody().getBytes());
    }
}
