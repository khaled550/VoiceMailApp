/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.voicemailapp;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.jfoenix.controls.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author khaled
 */
public class FXMLDocumentController implements Initializable {

    Alert alert = new Alert(Alert.AlertType.ERROR, "incorrect email or password", ButtonType.OK);
    private GSpeechDuplex duplex;
    private Microphone mic;
    public static final ObservableList data =
        FXCollections.observableArrayList();
    
    public static String user, pass, type = "";
    boolean logged = false;
    static boolean checked = false;
    
    public static Message[] messages;
    
    @FXML
    private JFXTextField emailTxt;

    @FXML
    private JFXPasswordField passTxt;

    @FXML
    private JFXProgressBar progBar;

    @FXML
    private JFXButton loginBtn;


    VoiceRecognitionHelper voiceRecognitionHelperemail;
    VoiceRecognitionHelper voiceRecognitionHelperpass;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //mailSelection.getItems().add("Yahoo");
        voiceRecognitionHelperpass = new VoiceRecognitionHelper();
        voiceRecognitionHelperemail = new VoiceRecognitionHelper();

        getEmail();
    }

    VoiceUtility voiceUtilit;
    private void getEmail(){
        voiceUtilit = new VoiceUtility();
        voiceUtilit.SaySomeThingThenReceiveText(voiceRecognitionHelperemail,"please say your email",
                v->{
                    /*user = v;
                    user = user.replaceAll("\\s+","");
                    emailTxt.setText(user + "@gmail.com");
                    voiceRecognitionHelperemail.destroy();
                    getPass();*/
                });
        voiceRecognitionHelperemail.StartListener(v -> {
            System.out.println(v);
            user = v;
            user = user.replaceAll("\\s+","");
            emailTxt.setText(user + "@gmail.com");
            getPass();
        });
    }

    boolean  checkedLogin =true;

    private void getPass() {
        voiceRecognitionHelperemail.destroy();
        voiceUtilit.SaySomeThingThenReceiveText(voiceRecognitionHelperpass, "enter your password now",
                v -> {
                    /*voiceRecognitionHelperpass.destroy();
                    user = emailTxt.getText();
                    pass = passTxt.getText();
                    user.replaceAll("\\s+", "");
                    pass.replaceAll("\\s+", "");
                    check(user, pass);*/
                });
        voiceRecognitionHelperpass.StartListener(v -> {
            voiceRecognitionHelperpass.destroy();
            pass = v;
            pass = pass.replaceAll("\\s+","");
            pass = pass.substring(0, 1).toUpperCase() + pass.substring(1);
            passTxt.setText(v);
            check(user, pass);
        });
    }

    @FXML
    void login(final ActionEvent event) {


        voiceRecognitionHelperpass.destroy();
        voiceRecognitionHelperemail.destroy();
        check("Voicetestu", "Testtest990");
        /*if (checkedLogin){
            checkedLogin = false;
            user = emailTxt.getText();
            pass = passTxt.getText();
            if(emailTxt.getText().isEmpty() || passTxt.getText().isEmpty()){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enter email and password", ButtonType.OK);
                        alert.showAndWait();
                        checkedLogin=true;
                    }
                });
                return;
            }

            //check(user, pass);
        }*/
    }

    private void check(final String user, final String password) {
        progBar.setVisible(true);
        loginBtn.setDisable(true);

        System.out.println("email : "+user);
        System.out.println("password : "+password);

        Platform.setImplicitExit(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //create properties field
                    Properties properties = new Properties();

                    properties.put("mail.pop3.host", "pop.gmail.com");
                    properties.put("mail.pop3.port", "995");
                    properties.put("mail.pop3.starttls.enable", "true");
                    Session emailSession = Session.getDefaultInstance(properties);

                    //create the POP3 store object and connect with the pop server
                    Store store = emailSession.getStore("pop3s");

                    store.connect("pop.gmail.com", user, password);

                    //create the folder object and open it
                    Folder emailFolder = store.getFolder("INBOX");
                    emailFolder.open(Folder.READ_ONLY);

                    // retrieve the messages from the folder in an array and print it
                    messages = emailFolder.getMessages();
                    System.out.println("messages.length---" + messages.length);

                    for (Message message : messages) {
                        System.out.println("Subject: " + message.getSubject());
                    }

                    //close the store and folder objects
                    //emailFolder.close(false);
                    //store.close();
                    logged = true;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("/fxml/Emails.fxml"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Stage stage = new Stage();
                            stage.setTitle("Emails");
                            Scene scene = new Scene(root);

                            stage.setScene(scene);

                            stage.show();
                            Stage loginWindow = (Stage) emailTxt.getScene().getWindow();
                            loginWindow.hide();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    progBar.setVisible(false);
                    loginBtn.setDisable(false);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!alert.isShowing())
                                alert.showAndWait();

                        }
                    });
                }
            }
        }).start();

        Platform.setImplicitExit(false);
    }
}
