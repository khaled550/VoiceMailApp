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
import marytts.signalproc.effects.*;

import javax.mail.MessagingException;

/**
 * FXML Controller class
 *
 * @author khaled
 */
public class EmailsController implements Initializable {

    public static int curPos = -1;

    @FXML
    private JFXListView<Label> emailsListView;

    @FXML
    private JFXButton sendMailBtn;

    @FXML
    private JFXButton logoutBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        /*TextToSpeech tts = new TextToSpeech();

        //=========================================================================
        //======================= Print available AUDIO EFFECTS ====================
        //=========================================================================

        //Print all the available audio effects
        tts.getAudioEffects().stream().forEach(audioEffect -> {
            System.out.println("-----Name-----");
            System.out.println(audioEffect.getName());
            System.out.println("-----Examples-----");
            System.out.println(audioEffect.getExampleParameters());
            System.out.println("-----Help Text------");
            System.out.println(audioEffect.getHelpText() + "\n\n");

        });

        //=========================================================================
        //========================= Print available voices =========================
        //=========================================================================

        //Print all the available voices
        tts.getAvailableVoices().stream().forEach(voice -> System.out.println("Voice: " + voice));

        // Setting the Current Voice
        tts.setVoice("cmu-slt-hsmm");

        //=========================================================================
        //========================= Let's try different effects=====================
        //=========================================================================

        //----- Hey you !-> check the help that is being printed on the console
        //----- You will understand how to use the effects better :)

        //VocalTractLinearScalerEffect
        VocalTractLinearScalerEffect vocalTractLSE = new VocalTractLinearScalerEffect(); //russian drunk effect
        vocalTractLSE.setParams("amount:10");

        //JetPilotEffect
        JetPilotEffect jetPilotEffect = new JetPilotEffect(); //epic fun!!!
        jetPilotEffect.setParams("amount:5");

        //RobotiserEffect
        RobotiserEffect robotiserEffect = new RobotiserEffect();
        robotiserEffect.setParams("amount:5");

        //StadiumEffect
        StadiumEffect stadiumEffect = new StadiumEffect();
        stadiumEffect.setParams("amount:5");

        //LpcWhisperiserEffect
        LpcWhisperiserEffect lpcWhisperiserEffect = new LpcWhisperiserEffect(); //creepy
        lpcWhisperiserEffect.setParams("amount:5");

        //VolumeEffect
        VolumeEffect volumeEffect = new VolumeEffect(); //be careful with this i almost got heart attack
        volumeEffect.setParams("amount:0");

        //Apply the effects
        //----You can add multiple effects by using the method `getFullEffectAsString()` and + symbol to connect with the other effect that you want
        //----check the example below
        tts.getMarytts().setAudioEffects(stadiumEffect.getFullEffectAsString());// + "+" + stadiumEffect.getFullEffectAsString());

        //=========================================================================
        //===================== Now let's troll user ==============================
        //=========================================================================
        //Loop infinitely*/

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

        VoiceRecognitionHelper voiceRecognitionHelperCommand = new VoiceRecognitionHelper();

        VoiceUtility voiceUtility = new VoiceUtility();
        voiceUtility.SaySomeThingThenReceiveText(voiceRecognitionHelperCommand,
                voiceUtility.sayEmailTitles(emails).toString(), v->{
                    System.out.println("Command : "+v);
                    voiceUtility.WhichCommand(v);
                    switch (voiceUtility.WhichCommand(v)){
                        case 1:
                            openemailContent(0);
                        case  2:
                            openSendEmail();
                        case  3:
                            //return 3;
                    }
                });

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
}
