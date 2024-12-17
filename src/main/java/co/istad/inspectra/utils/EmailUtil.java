package co.istad.inspectra.utils;

import co.istad.inspectra.features.issue.dto.IssuesResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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


  public void sendScanMessage(String email, String username, List<IssuesResponse> issues, String project) throws MessagingException {

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

    // Create a Thymeleaf context
    Context context = new Context();
    context.setVariable("email", email);
    context.setVariable("userName", username);
    context.setVariable("dates", LocalDateTime.now());
    context.setVariable("issues", issues);
    context.setVariable("project", project);
    context.setVariable("totalIssues", 1);


    // Render the Thymeleaf template as a String
    String htmlContent = templateEngine.process("scan-message", context);

    mimeMessageHelper.setTo(email);
    mimeMessageHelper.setSubject("Scan Completed");
    mimeMessageHelper.setText(htmlContent, true);

    javaMailSender.send(mimeMessage);

  }

  public void sendBlogApprove(String email, String userName,String approvalDate,String blogLink) throws MessagingException {

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

    // Create a Thymeleaf context
    Context context = new Context();
    context.setVariable("email", email);
    context.setVariable("userName", userName);
    context.setVariable("approvalDate", approvalDate);
    context.setVariable("blogLink", blogLink);

    // Render the Thymeleaf template as a String
    String htmlContent = templateEngine.process("blog-approval-email", context);

    mimeMessageHelper.setTo(email);
    mimeMessageHelper.setSubject("Blog Approved");
    mimeMessageHelper.setText(htmlContent, true);

    javaMailSender.send(mimeMessage);

  }

}

