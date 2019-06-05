/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.voicemailapp;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * FXML Controller class
 *
 * @author khaled
 */
public class SendMailController implements Initializable {

    
    @FXML
    private JFXTextArea mailConentTxt;
    
    @FXML
    private JFXTextField recieverMailTxt;

    @FXML
    private JFXTextField subjectTxt;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }    
    
    @FXML
    void sendMail(MouseEvent event) {

      // Recipient's email ID needs to be mentioned.
      String to = recieverMailTxt.getText();

      // Sender's email ID needs to be mentioned
      String from = FXMLDocumentController.user;
      final String username = "user";//change accordingly
      final String password = FXMLDocumentController.pass; //change accordingly

      // Assuming you are sending email through relay.jangosmtp.net
      String emailPort = "587";//gmail's smtp port

        Properties emailProperties;
        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
	emailProperties.put("mail.smtp.auth", "true");
	emailProperties.put("mail.smtp.starttls.enable", "true");

      // Get the Session object.
      Session session = Session.getDefaultInstance(emailProperties, null);

      try {
	   // Create a default MimeMessage object.
	   Message message = new MimeMessage(session);
	
	   // Set To: header field of the header.
	   message.setRecipients(Message.RecipientType.TO,
               InternetAddress.parse(to));
	
	   // Set Subject: header field
	   message.setSubject(subjectTxt.getText());
	
	   // Now set the actual message
	   message.setContent(mailConentTxt.getText(), "text/html");

           String emailHost = "smtp.gmail.com";
           Transport transport = session.getTransport("smtp");
           transport.connect(emailHost, from, password);
           transport.sendMessage(message, message.getAllRecipients());
           transport.close();

	   System.out.println("Sent message successfully....");
    }
    catch (MessagingException e) {
        throw new RuntimeException(e);
    }
    }
    
}
