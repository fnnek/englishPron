/**
 * Created by fnnek on 26.03.17.
 */

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import javax.sound.sampled.*;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import java.io.IOException;

public class Application extends javafx.application.Application{

    private Stage stage;
    private static Configuration configuration;
    private static File audioFile;
    private static AudioFormat audioFormat;
    private static TargetDataLine targetDataLine;
    private static StreamSpeechRecognizer recognizer;
    @FXML private Text recognizedText;
    @FXML private Button startB;
    @FXML private Button stopB;
    @FXML private Button voiceLessons;
    private int width = 600;
    private int height = 350;
    private static Application instance;
    public Application() {
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    @FXML private void handleVoiceLessonsButton(ActionEvent event){
        try {
            replaceSceneContent("VoiceLessons.fxml",event);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

    @FXML protected void handleStartButton(ActionEvent event) {
        recognizedText.setText("Speak now...");
        startB.setDisable(true);
        stopB.setDisable(false);
        captureAudio();

    }

    @FXML protected void handleStopButton(ActionEvent event) {

        targetDataLine.stop();
        targetDataLine.close();
        recognizedText.setText("Wait, I'm thinking...");

       new RecognizeThread().start();
        startB.setDisable(false);
        stopB.setDisable(true);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        this.stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("StartView.fxml"));
        this.stage.setTitle("engPron");
        this.stage.setScene(new Scene(root, width, height));

        stage.show();
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

    private void replaceSceneContent(String fxml, ActionEvent event) throws Exception {
        Parent blah = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(blah,width,height);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
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
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 2;
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
            audioFile = new File("junk.wav");

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

    class RecognizeThread extends Thread {
        public void run() {
            try {
                InputStream stream = new FileInputStream(audioFile);
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
    }


}
