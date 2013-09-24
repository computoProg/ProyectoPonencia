package beans;

import java.security.Security;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail2Google {

 private static final String SMTP_HOST_NAME = "smtp.gmail.com";
 private static final String SMTP_PORT = "465";
 private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

 private static final String emailGoogle = "giorodriguezg@gmail.com";
 private static final String emailGooglePassword = "universidad2010";
public Mail2Google(){

}

 public void recupera(String mail) throws MessagingException {
     
  String[] sendTo = {mail};
     Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

     String emailSubjectTxt = "Titulo/Subject del mail";
     String emailMsgTxt = "Mensaje/Body del mail";

     new Mail2Google().sendSSLMessage(sendTo, emailSubjectTxt,
                                             emailMsgTxt, emailGoogle);

     System.out.println("Mail enviado a los destinatarios");

 }

 public void sendSSLMessage(String recipients[], String subject,
   String message, String from) throws MessagingException {
  boolean debug = false;

  Properties props = new Properties();
  props.put("mail.smtp.host", SMTP_HOST_NAME);
  props.put("mail.smtp.auth", "true");
  props.put("mail.debug", "false");
  props.put("mail.smtp.port", SMTP_PORT);
  props.put("mail.smtp.socketFactory.port", SMTP_PORT);
  props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
  props.put("mail.smtp.socketFactory.fallback", "false");

  Session session = Session.getDefaultInstance(props,
    new javax.mail.Authenticator() {

   protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(emailGoogle, emailGooglePassword);
   }
  });

  session.setDebug(debug);

  Message msg = new MimeMessage(session);
  InternetAddress addressFrom = new InternetAddress(from);
  msg.setFrom(addressFrom);

  InternetAddress[] addressTo = new InternetAddress[recipients.length];
  for (int i = 0; i < recipients.length; i++) {
   addressTo[i] = new InternetAddress(recipients[i]);
  }
  msg.setRecipients(Message.RecipientType.TO, addressTo);

  msg.setSubject(subject);
  msg.setContent(message, "text/plain");
  Transport.send(msg);
 }
} 