import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * Created by fnnek on 16.06.17.
 */
public class Users implements Initializable{

    @FXML
    private ComboBox<String> chooseUser;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        XmlReader reader = new XmlReader();
        List<String> users = reader.getUsers();

        chooseUser.getStylesheets().add("/myres/comboBox.css");

        ObservableList<String> list = FXCollections.observableList(users);
        chooseUser.setItems(list);
    }

    @FXML
    private void handleChangeUserButton(ActionEvent event) {
        if(!(chooseUser.getValue() == null)){
            try {
                Globals.userName = chooseUser.getValue();
                new SceneChanger().replaceSceneContent("StartView.fxml",event);
            }catch (Exception e) {
                System.out.println(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You have to choose user from the list!");

            alert.showAndWait();
        }

    }

    @FXML
    private void handleBackbtn(ActionEvent event){
        try {
            new SceneChanger().replaceSceneContent("StartView.fxml",event);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML
    private void handleNewUserButton(ActionEvent event){

        try {
            new SceneChanger().replaceSceneContent("NewUser.fxml",event);
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}
