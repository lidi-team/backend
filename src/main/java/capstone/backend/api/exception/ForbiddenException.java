package capstone.backend.api.exception;

public class ForbiddenException extends Exception {

    public ForbiddenException() {
    }

    public ForbiddenException(final String message) {
        super(message);
    }

}
