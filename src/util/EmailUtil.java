package util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    private static final String FROM_EMAIL = "sandevnireh@gmail.com";
    private static final String PASSWORD   = "buxmhddtttgqrohd";

    public static boolean sendAssignmentConfirmation(String toEmail,
                                                     String touristName,
                                                     String tourName,
                                                     String tourDate,
                                                     String guideName) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth",            "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host",            "smtp.gmail.com");
            props.put("mail.smtp.port",            "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Tour Assignment Confirmation - " + tourName);

            String body = "Dear " + touristName + ",\n\n" +
                    "Your tour assignment has been confirmed!\n\n" +
                    "Tour Details:\n" +
                    "  Tour Name : " + tourName + "\n" +
                    "  Date      : " + tourDate + "\n" +
                    "  Guide     : " + guideName + "\n\n" +
                    "Thank you for choosing our tour service!\n\n" +
                    "Best regards,\n" +
                    "Tour Guide Management System";

            message.setText(body);
            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            System.err.println("Email error: " + e.getMessage());
            return false;
        }
    }
}