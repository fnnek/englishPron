/**
 * Created by fnnek on 26.03.17.
 */

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import javax.sound.sampled.*;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import java.io.IOException;

public class Application extends javafx.application.Application{

    private static Configuration configuration;
    //private static LiveSpeechRecognizer recognizer;
    private static File audioFile;
    private static AudioFormat audioFormat;
    private static TargetDataLine targetDataLine;
    private static StreamSpeechRecognizer recognizer;
    @FXML private Text recognizedText;
    @FXML private Text info;

    @FXML protected void handleStartButton(ActionEvent event){
        recognizedText.setText("Speak now...");
        captureAudio();

    }

    @FXML protected void handleStopButton(ActionEvent event) {

        targetDataLine.stop();
        targetDataLine.close();
        try {

            InputStream stream = new FileInputStream(audioFile);
            //recognizedText.setText("Wait, I'm thinking...");
            recognizer.startRecognition(stream);

            SpeechResult result;

            if ((result = recognizer.getResult()) != null) {
                recognizedText.setText("You said: " + result.getHypothesis());
                System.out.println("Hypothesis: " + result.getHypothesis());
            }
            recognizer.stopRecognition();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        primaryStage.setTitle("engPron");
        primaryStage.setScene(new Scene(root, 400, 375));




        primaryStage.show();
    }
    public static void main(String ...arg) throws Exception{

        configuration = new Configuration();

        // Set path to acoustic model.
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        // Set path to dictionary.
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        // Set language model.
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        recognizer = new StreamSpeechRecognizer(configuration);

        launch(arg);


    }

    private void captureAudio(){
        try{
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo =
                    new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat);
            targetDataLine = (TargetDataLine)
                    AudioSystem.getLine(dataLineInfo);
            new CaptureThread().start();
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private AudioFormat getAudioFormat(){
        float sampleRate = 8000.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    class CaptureThread extends Thread {
        public void run() {
            AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
            audioFile = new File("junk.wav");;

            try{
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                AudioSystem.write(
                        new AudioInputStream(targetDataLine),
                        fileType,
                        audioFile);
            }catch (Exception e){
                e.printStackTrace();
            }




        }
    }
}
