package codesquad.servlet.database.exception;

public class InvalidDataAccessException extends IllegalArgumentException {

    public InvalidDataAccessException(String s) {
        super(s);
    }
}
