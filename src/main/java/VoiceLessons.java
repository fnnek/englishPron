import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

/**
 * Created by fnnek on 28.05.17.
 */
public class VoiceLessons {
    private int width = 600;
    private int height = 350;
    @FXML
    private ComboBox<String> chooseLesson;

    @FXML private void handleStartLessonButton(ActionEvent event) {
        System.out.println("You choosed " + chooseLesson.getValue());
        try{
            new SceneChanger().replaceSceneContent("VoiceLesson.fxml",event);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML private void handleGoBackButton(ActionEvent event) {
        try {
            new SceneChanger().replaceSceneContent("StartView.fxml",event);
        } catch (Exception e) {
            System.out.println(e);
        }

    }


}
