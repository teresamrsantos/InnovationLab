package pt.uc.dei.paj.general;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import pt.uc.dei.paj.entity.User;

public class SendEmail {
	private static final String appPassword = "fejndkkqpylqoics";
	private static final String from = "acertarorumo2022@gmail.com";
	private static final String host = "smtp.gmail.com";

	public static void emailRegister(User userToRegister, String activation) {
		String to = userToRegister.getEmail();
		String subject = "Activate your account, " + userToRegister.getFirstName() + "!";
		String linkActivation = "<a href=\"http://localhost:3000/validateAccount/" + activation
				+ "\">click here</a>";
		String contentEmail = "To activate the account " + linkActivation;
		sendEmail(contentEmail, subject, to);
	}

	public static void emailResetPassword(User userToReset, String resetPassword) {
		String to = userToReset.getEmail();
		String subject = "You forgot your Password, " + userToReset.getFirstName() + " ?";
		String linkActivation = "<a href=\"http://localhost:3000/resetPassword/"
				+ resetPassword + "\">click here</a>";
		String contentEmail = "To reset your password please, " + linkActivation;

		sendEmail(contentEmail, subject, to);
	}

	public static void sendEmail(String contentEmail, String subject, String to) {

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(from, appPassword);

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			// message.setText(contentEmail);
			message.setContent(contentEmail, "text/html");
	
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}
}
