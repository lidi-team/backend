package capstone.backend.base.exception;

public class DataDuplicateException extends Exception {
    public DataDuplicateException() {

    }

    public DataDuplicateException(final String message) {
        super(message);
    }
}
