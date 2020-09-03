package capstone.backend.api.service;



import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public interface MailService {

    public MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException;

    public Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException;

    public Message sendMessage(Gmail service, String userId, MimeMessage emailContent) throws MessagingException, IOException;
}
