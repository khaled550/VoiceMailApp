/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.voicemailapp;

import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.voicemailapp.FXMLDocumentController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

/**
 * FXML Controller class
 *
 * @author khaled
 */
public class EmailContentController implements Initializable {

    
     @FXML
    private JFXTextArea emailContentTxt;
     
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
                
        try {
                String messageContent="";
        String type = FXMLDocumentController.messages[EmailsController.curPos].getContentType();
        Message message = FXMLDocumentController.messages[EmailsController.curPos];
        if ( type.contains("multipart")) {
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
            messageContent = part.getContent().toString();
            }
         }
            else if (type.contains("text/plain")
                    || type.contains("text/html")) {
            Object content = message.getContent();
            if (content != null) {
            messageContent = content.toString();
            }
         }
                System.out.println(" Content: ");
                messageContent = messageContent.replace("&lt;", "<");
                messageContent = messageContent.replace("&gt;", ">");
                messageContent = messageContent.replaceAll("<[^>]*>", "");
                System.out.println(messageContent);
                emailContentTxt.setText(messageContent);
            } catch (MessagingException ex) {
                Logger.getLogger(EmailsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
             Logger.getLogger(EmailContentController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }    
    
}
