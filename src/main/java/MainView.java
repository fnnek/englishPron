import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Created by fnnek on 09.04.17.
 */
public class MainView {

    @FXML
    private Text recognizedText;

    @FXML protected void handleReconButton(ActionEvent event) {

        Configuration configuration = new Configuration();
        // Set path to acoustic model.
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
// Set path to dictionary.
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
// Set language model.
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
try {


    LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
// Start recognition process pruning previously cached data.
    recognizer.startRecognition(true);
    SpeechResult result;
    if ((result = recognizer.getResult()) != null) {
     //   System.out.format("Hypothesis: %s\n", result.getHypothesis());
        recognizedText.setText("Recognized: " + result.getHypothesis());
    }
// Pause recognition process. It can be resumed then with startRecognition(false).
    recognizer.stopRecognition();
}
catch (IOException e) {

}
    }

}
