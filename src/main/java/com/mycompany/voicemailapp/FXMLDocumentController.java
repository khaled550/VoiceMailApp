/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.voicemailapp;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import org.apache.commons.lang.text.StrBuilder;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author khaled
 */
public class FXMLDocumentController implements Initializable {

    private GSpeechDuplex duplex;
    private Microphone mic;
    public static final ObservableList data =
        FXCollections.observableArrayList();
    
    public static String user, pass, type = "";
    boolean logged = false;
    static boolean checked = false;
    
    public static Message[] messages;
    
    @FXML
    private JFXComboBox<String> mailSelection;
    
    @FXML
    private JFXTextField emailTxt;

    @FXML
    private JFXPasswordField passTxt;

    @FXML
    private JFXProgressBar progBar;

    @FXML
    private JFXButton loginBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        mailSelection.getItems().add("Gmail");
        //mailSelection.getItems().add("Yahoo");
        
        
    }
    
    @FXML
    void login(final ActionEvent event) throws java.io.IOException {

        int selected = mailSelection.getSelectionModel().getSelectedIndex();
        user = emailTxt.getText();
        pass = passTxt.getText();
        if(emailTxt.getText().isEmpty() || passTxt.getText().isEmpty()){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enter email anf password", ButtonType.OK);
                    alert.showAndWait();
                }
            });
            return;
        }

        check(user, pass);

        //StringBuilder output8 = new StringBuilder("my email is kha@gmail.com my password is 98989898");
        //System.out.println("length iss:" + output8.length());
        //System.out.println("email iss:  " + pass);
        /*mic = new Microphone(FLACFileWriter.FLAC);
        //Don't use the below google api key , make your own !!! :)
        duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

        duplex.setLanguage("en");

        Platform.setImplicitExit(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        duplex.addResponseListener(new GSpeechResponseListener() {
            String old_text = "";

            public void onResponse(GoogleResponse gr) {
                String output = "";
                output = gr.getAllPossibleResponses().get(0);
                if (gr.getResponse() == null) {
                    if (this.old_text.contains("(")) {
                        this.old_text = this.old_text.substring(0, this.old_text.indexOf('('));
                    }
                    System.out.println("Paragraph Line Added");
                    this.old_text = this.old_text.replace(")", "").replace("( ", "");
                    return;
                }
                if (output.contains("(")) {
                    output = output.substring(0, output.indexOf('('));
                }
                if (!gr.getOtherPossibleResponses().isEmpty()) {
                    //output = output + " (" + (String) gr.getOtherPossibleResponses().get(0) + ")";
                }

                System.out.println("output: "+output);
                if (output.contains("email") && output.contains("password")){
                    try {
                        StringBuilder output8 = new StringBuilder("my email is kha@gmail.com my password is 98989898");
                        //user = output8.substring(output8.indexOf("email is")+8, output8.indexOf("com")+3).trim();
                        //pass = output8.substring(output8.indexOf("password is")+11).trim();
                        user = output.substring(output.indexOf("email is")+8, output.indexOf("com")+3).trim();
                        pass = output.substring(output.indexOf("password is")+11).trim();
                        user = user.replaceAll("\\s+","");
                        pass = pass.replaceAll("\\s+","");

                        if (!user.isEmpty() && !pass.isEmpty()){
                            check(user, pass);
                        *//*Parent root;
                        root = FXMLLoader.load(getClass().getResource("Emails.fxml"));
                        Stage stage = new Stage();
                        stage.setTitle("Emails");
                        Scene scene = new Scene(root);

                        stage.setScene(scene);

                        stage.show();
                        // Hide this current window (if this is what you want)
                        ((Node)(event.getSource())).getScene().getWindow().hide();*//*
                        }
                        System.out.println("email is : "+user);
                        System.out.println("password is : "+pass);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });*/
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

                    ObservableList<String> messagesList =FXCollections.observableArrayList ();

                    for (int i = 0, n = messages.length; i < n; i++) {
                        Message message = messages[i];
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
                            Alert alert = new Alert(Alert.AlertType.ERROR, "incorrect email or password", ButtonType.OK);
                            alert.showAndWait();
                        }
                    });
                }
            }
        }).start();

        Platform.setImplicitExit(false);
    }
}
