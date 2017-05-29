import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by fnnek on 28.05.17.
 */
public class VoiceLessons implements Initializable{
    private int width = 600;
    private int height = 350;
    @FXML
    private ComboBox<Lesson> chooseLesson;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        XmlReader xmlReader = new XmlReader();
        List<Lesson> list;
        list = xmlReader.getAllLessons();
        ObservableList<Lesson> observableList = FXCollections.observableArrayList(list);
        for(int i = 0; i<observableList.size();i++)
        {
            System.out.println(observableList.get(i));
        }

        chooseLesson.setItems(observableList);

    }

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
