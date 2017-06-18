import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by fnnek on 28.05.17.
 */
public class StartView implements Initializable {

    @FXML
    private Label loggedUser;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        loggedUser.setText("Hello, " + Globals.userName + "!");

    }

    @FXML
    private void handleVoiceLessonsButton(ActionEvent event){
        try {
            new SceneChanger().replaceSceneContent("VoiceLessons.fxml",event);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.toString());

            alert.showAndWait();
            System.out.println(e);
        }

    }

    @FXML
    private void handleRandomLessonButton(ActionEvent event){
        try {
            Random generator = new Random();
            XmlReader reader = new XmlReader();
            int randomLessonID = reader.getNumberOfLessons();
            System.out.println("Number of lessons: "+randomLessonID);
            int newRandomLessonID = generator.nextInt(randomLessonID + 1);
            int lessonId = generator.nextInt(reader.getNumberOfLessons()+1);
            System.out.println("New random lesson number: "+lessonId);
            if(lessonId < 1){
                lessonId = 1;
            }
            Globals.lessonName = String.valueOf(lessonId);//reader.getRandomLesson(lessonId);
            new SceneChanger().replaceSceneContent("VoiceLesson.fxml",event);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.toString());

            alert.showAndWait();
            System.out.println(e);
        }

    }
    @FXML
    private void createOwnLessonButton(ActionEvent event){
        try {
            new SceneChanger().replaceSceneContent("CreateOwnLesson.fxml",event);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

    @FXML
    private void changeUserButton(ActionEvent event) {
        try {
            new SceneChanger().replaceSceneContent("Users.fxml",event);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void showProgressButton(ActionEvent event) {
        try {
            new SceneChanger().replaceSceneContent("Progress.fxml",event);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
