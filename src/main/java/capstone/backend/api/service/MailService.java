package capstone.backend.api.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface MailService {
    public void CreateMailVerifyCode(String email,String code) throws MessagingException;
}
