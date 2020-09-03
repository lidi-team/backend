package capstone.backend.api.exception;

public class UnauthorizedException extends Exception {

    public UnauthorizedException() {
    }

    public UnauthorizedException(final String message) {
        super(message);
    }

}
