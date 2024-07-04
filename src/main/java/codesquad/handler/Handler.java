package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public interface Handler {

    HttpResponse service(HttpRequest request, HttpResponse response);

}
