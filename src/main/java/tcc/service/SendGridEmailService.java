    package tcc.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente para disparo de emails pelo SendGrid.
 */
@Slf4j
public class SendGridEmailService {

    private final SendGrid sendGrid;
    private final String remetenteDefault;

    public SendGridEmailService(String apiKey, String remetenteDefault) {
        this.sendGrid = new SendGrid(apiKey);
        this.remetenteDefault = remetenteDefault;
    }

    public void sendEmail(String toEmail, String subject, String html) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and html={}",
                false, true, toEmail, subject, html);

        Email from = new Email(remetenteDefault);
        Email to = new Email(toEmail);
        Content content = new Content("text/html", html);
        Mail mail = new Mail(from, subject, to, content);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        try {
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, ex);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, ex.getMessage());
            }
        }
    }

}