package capstone.backend.api.exception;

public class BadRequestException extends Exception {

    public BadRequestException() {
    }

    public BadRequestException(final String message) {
        super(message);
    }

}
