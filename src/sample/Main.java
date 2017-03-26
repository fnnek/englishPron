package sample;

import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

public class Main extends Application implements EventHandler<ActionEvent> {

    Button recordBtn;
    private Boolean isRecording = false;
    private Configuration configuration = new Configuration();
    private LiveSpeechRecognizer recognizer;
    @Override
    public void start(Stage primaryStage) throws Exception{
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        recognizer = new LiveSpeechRecognizer(configuration);
        primaryStage.setTitle("english smth test");

        recordBtn = new Button("Record");
        recordBtn.setOnAction(this);//method that takes care of this button tap is inside THIS class

        StackPane layout = new StackPane();
        layout.getChildren().add(recordBtn);
        primaryStage.setScene(new Scene(layout, 500, 500));
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == recordBtn){
            //record button
            if (isRecording){
                //stop
                isRecording = false;
                recordBtn.setText("Record");
                SpeechResult result = result = recognizer.getResult();
                System.out.println("results: ");
                System.out.println(result.getHypothesis());
                recognizer.stopRecognition();
            }else{
                //start recording
                isRecording = true;
                recordBtn.setText("Stop");
                recognizer.startRecognition(true);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
