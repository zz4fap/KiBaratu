package ki.baratu.api.server;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

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
			// ...sendMail
		} catch (UnsupportedEncodingException e) {
			// ...
		}
	}
	
	public static void sendMail(String email) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Logger log = Logger.getLogger(ProcuraAlerta.class.getName());
		
		log.info("sendMail...");

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("zz4fap@gmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject("O preço do seu alerta foi atingido!!");
			msg.setText("Alerta atingido!");
			Transport.send(msg);
			
			log.info("Email enviado para: "+email);
		} catch (AddressException e) {
			log.info("AddressException");
			log.info(e.getMessage());
		} catch (MessagingException e) {
			log.info("MessagingException");
			log.info(e.getMessage());
		}
	}
	
	public static void sendMail(String email, Produto produto) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Logger log = Logger.getLogger(ProcuraAlerta.class.getName());
		
		log.info("sendMail...");

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("zz4fap@gmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject("O preco do seu alerta foi atingido!!");
			msg.setText("O produto: "+produto.getNome()+" está com preço de R$ "+produto.getPreco());
			Transport.send(msg);
			
			log.info("Email enviado para: "+email);
		} catch (AddressException e) {
			log.info("AddressException");
			log.info(e.getMessage());
		} catch (MessagingException e) {
			log.info("MessagingException");
			log.info(e.getMessage());
		}
	}

}
