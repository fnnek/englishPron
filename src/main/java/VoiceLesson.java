import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import javax.sound.sampled.*;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by fnnek on 28.05.17.
 */
public class VoiceLesson implements Initializable {

    private int actualWord = 1;
    private static File audioFile;
    private static AudioFormat audioFormat;
    private static TargetDataLine targetDataLine;
    private static StreamSpeechRecognizer recognizer;
    private List<Boolean> list;
    @FXML
    private Text recognizedText;
    @FXML
    private Button startB;
    @FXML
    private Button stopB;
    @FXML
    private Button previousWord;
    @FXML
    private Button nextWord;
    @FXML
    private ProgressBar lessonProgressBar;
    @FXML
    private Label word;
    @FXML
    private ImageView wordOk;

    public VoiceLesson() {
        try {
            configureRecognizer();
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

        previousWord.setDisable(true);
        word.setText("Word #" + actualWord);
        list = new ArrayList<Boolean>();

        for(int i = 0; i<10;i++) {
            list.add(false);
        }
    }

    @FXML
    protected void handleStartButton(ActionEvent event) {
        recognizedText.setText("Speak now...");
        startB.setDisable(true);
        stopB.setDisable(false);
        captureAudio();

    }

    @FXML
    protected void handleStopButton(ActionEvent event) {

        targetDataLine.stop();
        targetDataLine.close();
        recognizedText.setText("Wait, I'm thinking...");

        new VoiceLesson.RecognizeThread().start();
        startB.setDisable(false);
        stopB.setDisable(true);
    }

    @FXML
    protected void handlePreviousWordButton() {
        nextWord.setDisable(false);
        if (actualWord > 2) {
            actualWord--;
        } else {
            actualWord--;
            previousWord.setDisable(true);
        }

        word.setText("Word #" + actualWord);

        lessonProgressBar.setProgress(actualWord / 10.0);
    }

    @FXML
    protected void handleNextWordButton() {
        previousWord.setDisable(false);
        if (actualWord < 9) {
            actualWord++;
        } else {
            actualWord++;
            nextWord.setDisable(true);
        }

        word.setText("Word #" + actualWord);
        lessonProgressBar.setProgress(actualWord / 10.0);


    }
    @FXML
    protected void handleSymButton(){
        boolean actualBool = list.get(actualWord - 1);
        actualBool = !actualBool;
        list.set(actualWord-1,actualBool);
        wordOk.setVisible(actualBool);
    }

    private void captureAudio() {
        try {
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo =
                    new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat);
            targetDataLine = (TargetDataLine)
                    AudioSystem.getLine(dataLineInfo);
            new CaptureThread().start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private AudioFormat getAudioFormat() {
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

    private void configureRecognizer() throws Exception {
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

            try {
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                AudioSystem.write(
                        new AudioInputStream(targetDataLine),
                        fileType,
                        audioFile);
            } catch (Exception e) {
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
