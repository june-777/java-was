package codesquad.servlet.execption;

import codesquad.webserver.http.HttpStatus;

public class ServerException extends RuntimeException {

    private HttpStatus httpStatus;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
