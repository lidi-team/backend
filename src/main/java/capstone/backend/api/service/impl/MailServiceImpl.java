package capstone.backend.api.service.impl;

import capstone.backend.api.entity.User;
import capstone.backend.api.service.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    private final TemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void createMailVerifyCode(String userEmail, String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("title", "Mật khẩu mới của bạn tại LiDiOKRs");
        context.setVariable("code", code);

        String body = templateEngine.process("getVerificationCodeTempl", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(userEmail);
        helper.setSubject("Mật khẩu mới cho tài khoản của bạn tại LiDiOKRs");
        helper.setText(body, true);
        javaMailSender.send(message);
        logger.info("email sent to email: " + userEmail);
    }

    @Override
    public void sendWelcomeEmail(HashMap<User, String> map) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        map.forEach((user, code) -> {
            Context context = new Context();
            context.setVariable("title", "Chào mừng bạn đến với Hệ thống quản trị Mục tiêu - Kết quả then chốt LiDi OKRs!");
            context.setVariable("fullName", user.getFullName());
            context.setVariable("code", code);

            String body = templateEngine.process("welcomeUser",context);
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(user.getEmail());
                helper.setSubject("Mật khẩu mới cho tài khoản của bạn tại LiDiOKRs");
                helper.setText(body, true);
                javaMailSender.send(message);
                logger.info("email sent to email: " + user.getEmail());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }


}
