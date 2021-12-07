package tcss559.utilities;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
	    
    public static void gmailSender(String recipient, String subject, String content) 
    		throws AddressException, MessagingException {
    	// Get a Properties object
    	Properties props = new Properties();
    	gmailssl(props);
    	final String username = "tcss559a2021@gmail.com";
    	final String password = "TCSS559A2021..";
    	Session session = Session.getDefaultInstance(props,
    	new Authenticator() {
	    	protected PasswordAuthentication getPasswordAuthentication() {
	    		return new PasswordAuthentication(username, password);
	    	}
    	});
    	Message msg = new MimeMessage(session);
    	msg.setFrom(new InternetAddress(username));
    	msg.setRecipients(Message.RecipientType.TO,
    	InternetAddress.parse(recipient));
    	msg.setSubject(subject);
    	msg.setText(content);
    	Transport.send(msg);
    	System.out.println("Message sent.");
    	}

    private static void gmailssl(Properties props) {
	    final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	    props.put("mail.debug", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.ssl.enable", "true");
	    props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
	    props.put("mail.smtp.port", "465");
	    props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.auth", "true");
    }

}


