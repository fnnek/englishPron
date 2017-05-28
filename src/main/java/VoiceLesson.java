import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fnnek on 28.05.17.
 */
public class VoiceLesson {
    private int actualWord = 1;
    public VoiceLesson() {
        try {
            configureRecognizer();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private static File audioFile;
    private static AudioFormat audioFormat;
    private static TargetDataLine targetDataLine;
    private static StreamSpeechRecognizer recognizer;
    @FXML private Text recognizedText;
    @FXML private Button startB;
    @FXML private Button stopB;
    @FXML private Button previousWord;
    @FXML private Button nextWord;
    @FXML
    protected void handleStartButton(ActionEvent event) {
        recognizedText.setText("Speak now...");
        startB.setDisable(true);
        stopB.setDisable(false);
        captureAudio();

    }

    @FXML protected void handleStopButton(ActionEvent event) {

        targetDataLine.stop();
        targetDataLine.close();
        recognizedText.setText("Wait, I'm thinking...");

        new VoiceLesson.RecognizeThread().start();
        startB.setDisable(false);
        stopB.setDisable(true);
    }

    @FXML protected void handlePreviousWordButton() {

    }

    @FXML protected void handleNextWordButton() {
        
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

    private void configureRecognizer() throws Exception{
        Configuration configuration;
        configuration = new Configuration();

        // Set path to acoustic model.
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        // Set path to dictionary.
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        // Set language model.
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        recognizer = new StreamSpeechRecognizer(configuration);
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
                //configureRecognizer();
                recognizer.startRecognition(stream);

                SpeechResult result;

                if ((result = recognizer.getResult()) != null) {
                    recognizedText.setText("You said: " + result.getHypothesis());
                    System.out.println("Hypothesis: " + result.getHypothesis());
                }
                recognizer.stopRecognition();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
