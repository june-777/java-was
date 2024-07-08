package codesquad;

import codesquad.webserver.Connector;
import codesquad.webserver.Container;

public class Main {
    public static void main(String[] args) {
        Container container = new Container();
        Connector connector = container.connector();
        connector.start();
    }
}
