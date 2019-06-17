package com.mycompany.voicemailapp;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import javafx.application.Platform;
import net.sourceforge.javaflacencoder.FLACFileWriter;

public class VoiceRecognitionHelper {


    private GSpeechDuplex duplex;
    private Microphone mic;
    private Thread micThread;
    public VoiceRecognitionHelper() {
        this.mic = new Microphone(FLACFileWriter.FLAC);
        this.duplex = new GSpeechDuplex("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");
        micThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    public void  Init(){
        duplex.setLanguage("en");
        Platform.setImplicitExit(false);
    }

    public void StartListener(TextListener textListener){
        micThread.start();
        duplex.addResponseListener(v-> {
            if (v.isFinalResponse())
                textListener.getString(v.getResponse());
        });
    }


    public void destroy(){
        duplex.stopSpeechRecognition();
        micThread.interrupt();
    }
}