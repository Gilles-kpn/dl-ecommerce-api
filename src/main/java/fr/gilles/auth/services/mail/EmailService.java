package fr.gilles.auth.services.mail;


import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService  {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;


    public void sendRegistrationEmail(String to, String activeUrl ) throws MessagingException {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper  email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        email.setTo(to);
        email.setSubject("Registration");
        final Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("url", activeUrl);
        final String content = templateEngine.process("register", context);
        email.setText(content,true);
        javaMailSender.send(mimeMessage);

    }

    public void sendPasswordChangeEmail(String to, String activeUrl ) throws MessagingException {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper  email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        email.setTo(to);
        email.setSubject("Password Changed");
        final Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("url", activeUrl);
        final String content = templateEngine.process("password", context);
        email.setText(content,true);
        javaMailSender.send(mimeMessage);
    }
}
