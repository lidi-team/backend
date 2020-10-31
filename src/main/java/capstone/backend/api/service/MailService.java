package capstone.backend.api.service;

import capstone.backend.api.entity.User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;

@Service
public interface MailService {
    void CreateMailVerifyCode(String email, String code) throws MessagingException;

    void sendWelcomeEmail(HashMap<User, String> map) throws MessagingException;
}
