package capstone.backend.base.exception;

public class InternalServerException extends Exception {
    public InternalServerException() {

    }

    public InternalServerException(final String message) {
        super(message);
    }
}
