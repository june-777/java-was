package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public interface Handler {

    void service(HttpRequest request, HttpResponse response);

}
