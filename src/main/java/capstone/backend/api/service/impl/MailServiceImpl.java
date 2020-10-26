package capstone.backend.api.service.impl;

import capstone.backend.api.service.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    private TemplateEngine templateEngine;

    private JavaMailSender javaMailSender;

    @Override
    public void CreateMailVerifyCode(String userEmail,String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("title","Get new password for your Account at LiDiOKRs");
        context.setVariable("code", code);

        String body = templateEngine.process("getVerificationCodeTempl",context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setTo(userEmail);
        helper.setSubject("Get new password for your Account at LiDiOKRs");
        helper.setText(body,true);
        javaMailSender.send(message);
        logger.info("email sent to email: "+ userEmail);
    }
}
