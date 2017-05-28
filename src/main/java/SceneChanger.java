import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by fnnek on 28.05.17.
 */
public class SceneChanger {
    public void replaceSceneContent(String fxml, ActionEvent event) throws Exception {
        Parent blah = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(blah,600,350);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }
}
