package capstone.backend.api.exception;

public class DataDuplicatedException extends Exception {

    public DataDuplicatedException() {
    }

    public DataDuplicatedException(final String message) {
        super(message);
    }

}
