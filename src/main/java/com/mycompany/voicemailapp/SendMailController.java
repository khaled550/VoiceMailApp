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

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sent message successfully....", ButtonType.OK);
                alert.showAndWait();
            }
        });*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Recipient's email ID needs to be mentioned.
                String to = recieverMailTxt.getText();

                // Sender's email ID needs to be mentioned
                String from = FXMLDocumentController.user;
                final String username = "user";//change accordingly
                final String password = FXMLDocumentController.pass; //change accordingly

                // Assuming you are sending email through relay.jangosmtp.net
                String emailPort = "587";//gmail's smtp port

                Properties mailProp;
                mailProp = System.getProperties();
                mailProp.put("mail.smtp.port", emailPort);
                mailProp.put("mail.transport.protocol", "smtp");
                mailProp.put("mail.smtp.auth", "true");
                mailProp.put("mail.smtp.starttls.enable", "true");
                mailProp.put("mail.smtp.starttls.required", "true");
                mailProp.put("mail.debug", "true");
                mailProp.put("mail.smtp.ssl.enable", "true");
                mailProp.put("mail.smtp.user", "username");

                // Get the Session object.
                Session session = Session.getDefaultInstance(mailProp, null);

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

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sent message successfully....", ButtonType.OK);
                            alert.showAndWait();
                        }
                    });
                    System.out.println("Sent message successfully....");
                }
                catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}
