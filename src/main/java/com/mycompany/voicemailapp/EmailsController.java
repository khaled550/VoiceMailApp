/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.voicemailapp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import java.io.File;
import java.io.IOException;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
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

        readNext5Emails();

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

    @FXML
    void restartApplication(ActionEvent event) throws Throwable {

        startIndex = 0;
        readListSize = 0;
        this.finalize();
        ((Node)(event.getSource())).getScene().getWindow().hide();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/FXMLDocument.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("VoiceMailApp");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                if (!stage.isShowing())
                    stage.show();
            }
        });
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

    private void readNext5Emails(){
        List<String> emails = new ArrayList<>();
        int listSize = FXMLDocumentController.messages.length;
        int titleIndex = 1;
        try {
            if (listSize >= startIndex + 5){
                readListSize += 5;
            } else
                readListSize = listSize - readListSize;

            System.out.println("Sizee: "+ startIndex);
            System.out.println("Sizee: "+ readListSize);
            titleIndex = startIndex+1;
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

        requestCommand(emails, titleIndex);
    }

    private void requestCommand(List<String> emails, int titleIndex){
        VoiceRecognitionHelper voiceRecognitionHelperCommand = new VoiceRecognitionHelper();
        VoiceUtility voiceUtility = new VoiceUtility();
        voiceUtility.SaySomeThingThenReceiveText(voiceRecognitionHelperCommand,
                voiceUtility.sayEmailTitles(emails, titleIndex).toString(), v->{
                    voiceRecognitionHelperCommand.destroy();
                    System.out.println("Command : "+v);
                    if(v.contains("send")){
                        //send email
                        openSendEmail();
                    }else if (v.contains("continue")){
                        //read next 5 emails
                        readNext5Emails();
                    } else
                        requestCommand(emails, titleIndex);
                });
    }
}
