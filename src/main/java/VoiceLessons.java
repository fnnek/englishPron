import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by fnnek on 28.05.17.
 */
public class VoiceLessons implements Initializable{

    private List<String> list;
    private List<String> listOfLessonsName;
    @FXML
    private ComboBox<String> chooseLesson;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        XmlReader xmlReader = new XmlReader();

        list = xmlReader.getAllLessonsCombinedWithDescriptions();
        listOfLessonsName = xmlReader.getAllLessons();
        chooseLesson.getStylesheets().add("/myres/comboBox.css");
        ObservableList<String> observableList = FXCollections.observableArrayList(list);
        for(int i = 0; i<observableList.size();i++)
        {
            System.out.println(observableList.get(i));
        }
        chooseLesson.setItems(observableList);

    }

    @FXML private void handleStartLessonButton(ActionEvent event) {
        System.out.println("You choosed " + chooseLesson.getValue());
        int index = list.indexOf(chooseLesson.getValue());
        if(!(chooseLesson.getValue() == null)) {
            try{
                System.out.println("list of lesson name: "+listOfLessonsName.get(index));
                Globals.lessonName = String.valueOf(index+1);//listOfLessonsName.get(index);
                new SceneChanger().replaceSceneContent("VoiceLesson.fxml",event);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You have to choose lesson from the list!");

            alert.showAndWait();
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
