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
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.util.Properties;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.sound.sampled.*;
import javax.xml.soap.Text;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        mailSelection.getItems().add("Gmail");
        //mailSelection.getItems().add("Yahoo");
        
        
    }
    
    @FXML
    void login(final ActionEvent event) throws java.io.IOException {

        int selected = mailSelection.getSelectionModel().getSelectedIndex();
        user = "";
        pass = "";
        logged = true;
        check("pop.gmail.com", "pop3", user, pass);

        /*mic = new Microphone(FLACFileWriter.FLAC);
        //Don't use the below google api key , make your own !!! :)
        duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

        duplex.setLanguage("en");
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
                *//*if (gr.getResponse() == null) {
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
                /*if (!gr.getOtherPossibleResponses().isEmpty()) {
                    output = output + " (" + (String) gr.getOtherPossibleResponses().get(0) + ")";
                }*//*
                //System.out.println(output);

                *//*if (isType){
                    if (output.contains("Gmail")){
                        isType = false;
                        isEmail = true;
                        //check("pop.gmail.com", "pop3", user, pass);
                        //mic.close();
                        //duplex.stopSpeechRecognition();
                        System.out.println("type: "+output);
                    }
                }else if (isEmail){
                    isEmail = false;
                    isPass = true;
                    user = output;
                    System.out.println("email:  "+user);
                } else {
                    isPass = false;
                    pass = output;
                    System.out.println("pass:  "+pass);
                }

                if (output.contains("Gmail")){
                    //isType = false;
                    //isEmail = true;
                    //check("pop.gmail.com", "pop3", user, pass);
                    //mic.close();
                    //duplex.stopSpeechRecognition();
                    type = output;
                    System.out.println("type: "+output);
                } else if (user.isEmpty() && !type.isEmpty()) {
                    user = output;
                    System.out.println("email:  " + user);
                } else if (pass.isEmpty() && !user.isEmpty()){
                    pass = output;
                    System.out.println("pass:  " + pass);
                }*//*
            }
        });

        if(emailTxt.getText().isEmpty() || passTxt.getText().isEmpty())
             return;

        if(selected == 0){
            logged = check("pop.gmail.com", "pop3", user, pass);

        if(logged)
        {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("Emails.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Emails");
            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } else {

        }
    }*/
    }

    public static void streamingMicRecognize() throws Exception {

        ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
        try (SpeechClient client = SpeechClient.create()) {

            responseObserver =
                    new ResponseObserver<StreamingRecognizeResponse>() {
                        ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

                        public void onStart(StreamController controller) {}

                        public void onResponse(StreamingRecognizeResponse response) {
                            responses.add(response);
                        }

                        public void onComplete() {
                            for (StreamingRecognizeResponse response : responses) {
                                StreamingRecognitionResult result = response.getResultsList().get(0);
                                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                                System.out.printf("Transcript : %s\n", alternative.getTranscript());
                            }
                        }

                        public void onError(Throwable t) {
                            System.out.println(t);
                        }
                    };

            ClientStream<StreamingRecognizeRequest> clientStream =
                    client.streamingRecognizeCallable().splitCall(responseObserver);

            RecognitionConfig recognitionConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode("en-US")
                            .setSampleRateHertz(16000)
                            .build();
            StreamingRecognitionConfig streamingRecognitionConfig =
                    StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).build();

            StreamingRecognizeRequest request =
                    StreamingRecognizeRequest.newBuilder()
                            .setStreamingConfig(streamingRecognitionConfig)
                            .build(); // The first request in a streaming call has to be a config

            clientStream.send(request);
            // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true,
            // bigEndian: false
            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info targetInfo =
                    new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat); // Set the system information to read from the microphone audio stream

            if (!AudioSystem.isLineSupported(targetInfo)) {
                System.out.println("Microphone not supported");
                System.exit(0);
            }
            // Target data line captures the audio stream the microphone produces.
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            System.out.println("Start speaking");
            long startTime = System.currentTimeMillis();
            // Audio Input Stream
            AudioInputStream audio = new AudioInputStream(targetDataLine);
            while (true) {
                long estimatedTime = System.currentTimeMillis() - startTime;
                byte[] data = new byte[6400];
                audio.read(data);
                if (estimatedTime > 60000) { // 60 seconds
                    System.out.println("Stop speaking.");
                    targetDataLine.stop();
                    targetDataLine.close();
                    break;
                }
                request =
                        StreamingRecognizeRequest.newBuilder()
                                .setAudioContent(ByteString.copyFrom(data))
                                .build();
                clientStream.send(request);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        responseObserver.onComplete();
    }

    private void getUserEmail(){
        duplex.addResponseListener(new GSpeechResponseListener() {
            String old_text = "";

            public void onResponse(GoogleResponse gr) {
                String output = "";
                output = gr.getResponse();
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
                    output = output + " (" + (String) gr.getOtherPossibleResponses().get(0) + ")";
                }
                System.out.println(output);

                mic.close();
                duplex.stopSpeechRecognition();
                user = output;
                System.out.println("email: "+output);
            }
        });
    }
    
    public void check(final String host, String storeType, final String user, final String password) {
        progBar.setVisible(true);
        Platform.runLater(()->{
            try {
                //create properties field
                Properties properties = new Properties();

                properties.put("mail.pop3.host", host);
                properties.put("mail.pop3.port", "995");
                properties.put("mail.pop3.starttls.enable", "true");
                Session emailSession = Session.getDefaultInstance(properties);

                //create the POP3 store object and connect with the pop server
                Store store = emailSession.getStore("pop3s");

                store.connect(host, user, password);

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
                progBar.setVisible(false);
                // Hide this current window (if this is what you want)
                //((Node)(event.getSource())).getScene().getWindow().hide();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
                progBar.setVisible(false);
            } catch (MessagingException e) {
                e.printStackTrace();
                progBar.setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
                progBar.setVisible(false);
            }
        });
    }
}
