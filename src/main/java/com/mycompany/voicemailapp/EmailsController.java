/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.voicemailapp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import java.io.IOException;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.mail.MessagingException;

/**
 * FXML Controller class
 *
 * @author khaled
 */
public class EmailsController implements Initializable {

    public static int curPos = -1;
    int startIndex = 0;
    int readListSize = 0;

    @FXML
    private JFXListView<Label> emailsListView;

    @FXML
    private JFXButton sendMailBtn;

    @FXML
    private JFXButton logoutBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> emails = new ArrayList<>();

        for (int i = 0, n = FXMLDocumentController.messages.length; i < n; i++) {
            try {
                emailsListView.getItems().add(new Label(FXMLDocumentController.messages[i].getSubject()));
                if (i != 10) {
                    emails.add(FXMLDocumentController.messages[i].getSubject());
                    System.out.println(FXMLDocumentController.messages[i].getSubject());
                } else
                    break;

            } catch (MessagingException ex) {
                Logger.getLogger(EmailsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println(emails.size());

        readNext10Emails();

        emailsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                openemailContent(emailsListView.getSelectionModel().getSelectedIndex());
            }
        });
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //emails.forEach(word -> tts.speak(word, 2.0f, false, true));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();*/
    }

    @FXML
    void sendMail(MouseEvent event) {
        openSendEmail();

    }

    private void openSendEmail(){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/SendMail.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Send Mail");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                if (!stage.isShowing())
                    stage.show();
            }
        });
    }

    private void openemailContent(int pos){
        curPos = pos;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/EmailContent.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Email Content");
                Scene scene = new Scene(root);

                stage.setScene(scene);

                stage.show();
            }
        });
    }

    private void readNext10Emails(){
        List<String> emails = new ArrayList<>();
        int listSize = FXMLDocumentController.messages.length;

        try {
            if (listSize >= startIndex + 5){
                readListSize += 5;
            } else
                readListSize = listSize - readListSize;

            System.out.println("Sizee: "+ startIndex);
            System.out.println("Sizee: "+ readListSize);

            for (int i = startIndex; i < readListSize; i++) {
                startIndex++;
                try {
                    emails.add(FXMLDocumentController.messages[i].getSubject());
                } catch (MessagingException ex) {
                    Logger.getLogger(EmailsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Sizee: "+ emails.size());

        VoiceRecognitionHelper voiceRecognitionHelperCommand = new VoiceRecognitionHelper();
        VoiceUtility voiceUtility = new VoiceUtility();
        voiceUtility.SaySomeThingThenReceiveText(voiceRecognitionHelperCommand,
                voiceUtility.sayEmailTitles(emails).toString(), v->{
                    System.out.println("Command : "+v);
                    voiceRecognitionHelperCommand.destroy();
                    switch (voiceUtility.WhichCommand(v)){
                        case 1:
                            //openemailContent(0);
                        case  2:
                            openSendEmail();
                            voiceRecognitionHelperCommand.destroy();
                        case  3:
                            readNext10Emails();
                    }
                });
    }
}
