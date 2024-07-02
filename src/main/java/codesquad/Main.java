package codesquad;

import codesquad.handler.HttpRequestHandler;

public class Main {
    public static void main(String[] args) {
        HttpRequestHandler httpRequestHandler = new HttpRequestHandler();
        WebApplicationServer webApplicationServer = new WebApplicationServer(httpRequestHandler);
        webApplicationServer.start();
    }
}
