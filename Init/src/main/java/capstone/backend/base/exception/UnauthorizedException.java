package capstone.backend.base.exception;

public class UnauthorizedException extends Exception {
    public UnauthorizedException() {

    }

    public UnauthorizedException(final String message) {
        super(message);
    }
}
