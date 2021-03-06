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

    public static Stage stage;
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
    private int height = 600;
    private static Application instance;
    public Application() {
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        this.stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("StartView.fxml"));
        this.stage.setTitle("Eng-Pron");
        this.stage.setScene(new Scene(root, width, height));

        stage.show();
    }
    public static void main(String ...arg) throws Exception{



        launch(arg);


    }

}
