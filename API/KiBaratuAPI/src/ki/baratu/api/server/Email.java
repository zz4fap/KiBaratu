package ki.baratu.api.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

	public static void sendSimpleMail() {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress("zz4fap@gmail.com", "Mr. User"));
			msg.setSubject("Your Example.com account has been activated");
			Transport.send(msg);
		} catch (AddressException e) {
			// ...
		} catch (MessagingException e) {
			// ...
		} catch (UnsupportedEncodingException e) {
			// ...
		}
	}
	
	public static void sendMail(String email) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(email, "Mr. User"));
			msg.setSubject("Seu alerta foi atingido!!");
			Transport.send(msg);
		} catch (AddressException e) {
			// ...
		} catch (MessagingException e) {
			// ...
		} catch (UnsupportedEncodingException e) {
			// ...
		}
	}

}
