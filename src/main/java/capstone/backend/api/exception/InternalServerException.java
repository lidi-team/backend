package capstone.backend.api.exception;

public class InternalServerException extends Exception {

    public InternalServerException() {
    }

    public InternalServerException(final String message) {
        super(message);
    }

}
