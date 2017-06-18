import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by fnnek on 28.05.17.
 */
public class VoiceLesson implements Initializable {

    private int actualWord = 0;
    private int correctAnswers = 0;
    private static File audioFile;
    private static AudioFormat audioFormat;
    private static TargetDataLine targetDataLine;
    private static StreamSpeechRecognizer recognizer;
    private List<String> polish = new ArrayList<String>();
    private List<String> english = new ArrayList<String>();
    private List<Boolean> list;
    private String recognizedWord = "";
    private int points=0;
    private int wordCount = 1;
    private List<PointsPerWord> pointsEarnedPerWord = new ArrayList<PointsPerWord>();
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
    private Button checkbtn;
    @FXML
    private TextField wordField;
    @FXML
    private Label typedWordRightLbl;
    @FXML
    private Label recordedWordRightLbl;
    @FXML
    private Label pointsLbl;
    @FXML
    private Label wordCountLbl;

    public VoiceLesson() {
        try {
            configureRecognizer();
        } catch (Exception e) {
            System.out.println(e);
        }
        XmlReader xmlReader = new XmlReader();
        System.out.println("\nNazwa lekcji: "+Globals.lessonName);
        polish = xmlReader.getWords(Globals.lessonName,"polish");
        english = xmlReader.getWords(Globals.lessonName,"english");
        list = new ArrayList<Boolean>();

        for(int i = 0; i<10;i++) {
            list.add(false);
            pointsEarnedPerWord.add(new PointsPerWord(0,0));
        }

        if(Globals.lessonName!= null){
            System.out.println(Globals.lessonName);
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

        previousWord.setDisable(true);
        //word.setText("Word #" + actualWord);
        word.setText(polish.get(actualWord));
        recognizedText.setText("Click start to record your english pronunciation of \"" + word.getText() +"\"");
        wordField.setPromptText("Type in \""+word.getText() + "\" in english");
        list = new ArrayList<Boolean>();

        for(int i = 0; i<10;i++) {
            list.add(false);
        }
        Application.stage.setTitle("Eng-Pron: Voice lesson");


    }

    @FXML
    protected void handleStartButton(ActionEvent event) {
        recognizedText.setText("Speak now...");
        startB.setDisable(true);
        stopB.setDisable(false);
        checkbtn.setDisable(true);
        nextWord.setDisable(true);
        previousWord.setDisable(true);
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
        if (actualWord > 1){
            previousWord.setDisable(false);
        }
        if (actualWord <8){
            nextWord.setDisable(false);
        }
    }

    @FXML
    protected void handlePreviousWordButton() {
        nextWord.setDisable(false);
        if (actualWord > 1) {
            actualWord--;
        } else {
            actualWord--;
            previousWord.setDisable(true);
        }
        wordCount--;
        wordCountLbl.setText("Word: "+wordCount+"/"+english.size());
        word.setText(polish.get(actualWord));
        recognizedText.setText("Click start to record your english pronunciation of \"" + word.getText() +"\"");
        wordField.clear();
        wordField.setStyle("-fx-text-fill: black;");
        wordField.setPromptText("Type in \""+word.getText() + "\" in english");
        recognizedWord = " ";
        //lessonProgressBar.setProgress(correctAnswers / 10.0);
        boolean actualBool = list.get(actualWord );
        recordedWordRightLbl.setText("");
        typedWordRightLbl.setText("");
//        recordedWordRightLbl.setText("✓");
//        recordedWordRightLbl.setVisible(actualBool);
        checkbtn.setDisable(actualBool);
    }

    @FXML
    protected void handleNextWordButton() {
        previousWord.setDisable(false);
        if (actualWord < 8) {
            actualWord++;
        } else {
            actualWord++;
            nextWord.setDisable(true);
        }
        wordCount++;
        wordCountLbl.setText("Word: "+wordCount+"/"+english.size());
        word.setText(polish.get(actualWord));
       // lessonProgressBar.setProgress(correctAnswers / 10.0);
        recognizedText.setText("Click start to record your english pronunciation of \"" + word.getText() +"\"");
        wordField.clear();
        wordField.setStyle("-fx-text-fill: black;");
        wordField.setPromptText("Type in \""+word.getText() + "\" in english");
        recognizedWord = " ";
        boolean actualBool = list.get(actualWord );
        recordedWordRightLbl.setText("");
        typedWordRightLbl.setText("");
       // recordedWordRightLbl.setText("✓");
       // recordedWordRightLbl.setVisible(actualBool);
        checkbtn.setDisable(actualBool);
    }

    @FXML
    protected void handleCheck(){

        System.out.println(english.get(actualWord));
        System.out.println(polish.get(actualWord));
        System.out.println(wordField.getPromptText());
        recognizedText.setText("You said: "+recognizedWord);
        if(english.get(actualWord).equalsIgnoreCase(recognizedWord))
        {
            recognizedText.setStyle("-fx-text-fill: green;");
            list.set(actualWord,true);
            recordedWordRightLbl.setTextFill(Color.GREEN);
            recordedWordRightLbl.setText("✓");
            recordedWordRightLbl.setFont(Font.font ("Verdana", 20));
            if (pointsEarnedPerWord.get(wordCount-1).getPronunciation() == 0){
                points++;
                pointsEarnedPerWord.get(wordCount-1).setPronunciation(1);
            }
            //points++;
            pointsLbl.setText("Points: "+points+"/"+english.size()*2);

        }else{
            recognizedText.setStyle("-fx-text-fill: red;");
            recordedWordRightLbl.setTextFill(Color.RED);
            recordedWordRightLbl.setText("X");
            recordedWordRightLbl.setFont(Font.font ("Verdana", 20));
        }
        if(english.get(actualWord).equalsIgnoreCase(wordField.getText()))
        {
            wordField.setStyle("-fx-text-fill: green;");
            list.set(actualWord,true);
            typedWordRightLbl.setTextFill(Color.GREEN);
            typedWordRightLbl.setText("✓");
            typedWordRightLbl.setFont(Font.font ("Verdana", 20));
            if (pointsEarnedPerWord.get(wordCount-1).getWriting() == 0){
                points++;
                pointsEarnedPerWord.get(wordCount-1).setWriting(1);
            }
            //points++;
            pointsLbl.setText("Points: "+points+"/"+english.size()*2);
        }else{
            wordField.setStyle("-fx-text-fill: red;");
            typedWordRightLbl.setTextFill(Color.RED);
            typedWordRightLbl.setText("X");
            typedWordRightLbl.setFont(Font.font ("Verdana", 20));
        }
    }


    @FXML
    protected void handleBackbtn(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("End Lesson");
        alert.setHeaderText("Do you want to save your result?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                XmlReader reader = new XmlReader();
                System.out.println("Saving lesson");
                reader.saveLessonResult(Globals.lessonName,points);
                System.out.println("Lesson Saved");
                new SceneChanger().replaceSceneContent("StartView.fxml",event);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            try{
                new SceneChanger().replaceSceneContent("StartView.fxml",event);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

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
                    recognizedWord = result.getHypothesis();
                    recognizedText.setText("You said: ***");
                    checkbtn.setDisable(false);
                    System.out.println("Hypothesis: " + result.getHypothesis());
                }
                recognizer.stopRecognition();
            } catch (Exception e) {
                e.printStackTrace();
                checkbtn.setDisable(false);
            }
        }
    }

    class PointsPerWord {
        private int pronunciation;
        private int writing;

        public PointsPerWord(int pronunciation, int writing) {
            this.pronunciation = pronunciation;
            this.writing = writing;
        }
        public void setPronunciation(int pronunciation){
            this.pronunciation = pronunciation;
        }
        public int getPronunciation(){
            return this.pronunciation;
        }
        public void setWriting(int writing){
            this.writing = writing;
        }
        public int getWriting(){
            return this.writing;
        }
    }

}
