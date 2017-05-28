import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Created by fnnek on 28.05.17.
 */
public class StartView {
    @FXML
    private void handleVoiceLessonsButton(ActionEvent event){
        try {
            new SceneChanger().replaceSceneContent("VoiceLessons.fxml",event);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
