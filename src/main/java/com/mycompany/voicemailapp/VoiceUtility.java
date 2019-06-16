package com.mycompany.voicemailapp;

import com.sun.xml.internal.bind.util.Which;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VoiceUtility {
    public void SaySomeThingThenReceiveText(VoiceRecognitionHelper voiceRecognitionHelper,String speakText, TextListener textListener){
        TextToSpeech textToSpeech = new TextToSpeech();
        textToSpeech.speak(speakText,1.0f, false, true);

        isTTSSpeaking(textToSpeech, finish -> {
            if (finish){
                voiceRecognitionHelper.Init();
                voiceRecognitionHelper.StartListener(text -> textListener.getString(text));
                voiceRecognitionHelper.destroy();
            }
        });
    }
    public void isTTSSpeaking(TextToSpeech textToSpeech, TextToSpeechListener textToSpeechListener){
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                if (!textToSpeech.isSpeaking()) {
                    textToSpeechListener.onFinish(true);
                }
            }
        }, 100, TimeUnit.MILLISECONDS);
    }

    public String testEmailSayTitles(List<String> emails){
        String speak_txt = "";
        int start_index = 0;
        int end_index = (emails.size() <start_index + 10)? emails.size():start_index + 10;
        while (end_index < emails.size()){

            for (int i = start_index; i < end_index; i++) {
                speak_txt +="  "+emails.get(i)+".  ";
            }

            //////// here you should speak
            start_index = end_index;
            end_index = (emails.size() <start_index + 10)? emails.size():start_index + 10;
        }
        return speak_txt;
    }

    public StringBuilder sayEmailTitles(List<String> emails){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < emails.size(); i++) {
            stringBuilder.append("Email number ");
            stringBuilder.append(i+1);
            stringBuilder.append(" ");
            stringBuilder.append(emails.get(i));
            stringBuilder.append(" , ");
        }
        return stringBuilder;
    }

    public int WhichCommand(String text){
        text= text.toLowerCase();
        if (text.contains("open")){
            text = text.split("open")[0].split(" ")[0];
            if (getMailNumber(text) > 0)
                System.out.println(getMailNumber(text)+"");
            else{
                //say command again
            }

        }else if(text.contains("send")){
            //send email
            return 2;
        }else if (text.contains("continue")){
            //read next 10 emails
            return 3;
        }else {
            //say command again
        }
        return -1;
    }


    private int getMailNumber(String txt){
        switch (txt.toLowerCase()){
            case "first":
                return 1;
            case  "second":
                return 2;
            case  "Third":
                return 3;
            case  "Fourth":
                return 4;
            case  "Fifth":
                return 5;
            case  "Sixth":
                return 6;
            case  "Seventh":
                return 7;
            case  "Eighth":
                return 8;
            case  "Ninth":
                return 9;
            case  "Tenth":
                return 10;
            default:
                return -1;
        }
    }
}
