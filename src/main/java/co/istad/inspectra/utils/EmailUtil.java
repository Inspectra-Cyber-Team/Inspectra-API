package co.istad.inspectra.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class EmailUtil {

  private final JavaMailSender javaMailSender;

  private final TemplateEngine templateEngine;

  public void sendOtpEmail(String email, String otp) throws MessagingException {

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

    // Create a Thymeleaf context
    Context context = new Context();
    context.setVariable("email", email);
    context.setVariable("otp", otp);

    // Render the Thymeleaf template as a String
    String htmlContent = templateEngine.process("otp-email", context);

    mimeMessageHelper.setTo(email);
    mimeMessageHelper.setSubject("Verify OTP");
    mimeMessageHelper.setText(htmlContent, true);

    javaMailSender.send(mimeMessage);
  }


  public void sendScanMessage(String email, String message) throws MessagingException {


    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

    // Create a Thymeleaf context
    Context context = new Context();
    context.setVariable("email", email);
    context.setVariable("message", message);

    // Render the Thymeleaf template as a String
    String htmlContent = templateEngine.process("scan-message", context);

    mimeMessageHelper.setTo(email);
    mimeMessageHelper.setSubject("Scan Completed");
    mimeMessageHelper.setText(htmlContent, true);

    javaMailSender.send(mimeMessage);

  }

}

