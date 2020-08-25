package capstone.backend.base.exception;

public class ForbiddenException extends Exception {
    public ForbiddenException() {

    }

    public ForbiddenException(final String message) {
        super(message);
    }
}
