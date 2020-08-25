package capstone.backend.base.exception;

public class BadRequestException extends Exception {
    public BadRequestException() {

    }

    public BadRequestException(final String message) {
        super(message);
    }
}
