/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.voicemailapp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import marytts.signalproc.effects.*;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

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
        TextToSpeech tts = new TextToSpeech();

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
        //Loop infinitely

        Label mail = new Label("Mails");
        emailsListView.getItems().add(mail);
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
        emailsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                curPos = emailsListView.getSelectionModel().getSelectedIndex();

                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/EmailContent.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Email Content");
                    Scene scene = new Scene(root);

                    stage.setScene(scene);

                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(EmailsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        emails.forEach(word -> tts.speak(word, 2.0f, false, true));
    }

    @FXML
    void sendMail(MouseEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("SendMail.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Send Mail");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(EmailsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
