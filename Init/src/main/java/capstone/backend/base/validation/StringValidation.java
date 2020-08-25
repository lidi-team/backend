package capstone.backend.base.validation;

public class StringValidation {
    /**
     * validate information of user
     */

    public boolean usernameValidation(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
