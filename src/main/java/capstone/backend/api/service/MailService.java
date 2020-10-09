package capstone.backend.api.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface MailService {
    void CreateMailVerifyCode(String email,String code) throws MessagingException;
}
