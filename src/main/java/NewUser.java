import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

/**
 * Created by fnnek on 16.06.17.
 */
public class NewUser {
    @FXML
    private TextField newUserName;
    @FXML
    private Button newUserBtn;

    @FXML
    private void handleNewUserButton(ActionEvent event){
        XmlReader reader = new XmlReader();
        if(newUserName.getText().contains(" ")) {
            System.out.println("Zawiera spacje!!!11");
        } else{
            System.out.println("Nie nawiera spacji!!!11");

        }
        if(!newUserName.getText().isEmpty() && !newUserName.getText().contains(" ")) {
            reader.addNewUser(newUserName.getText());
            try {
                new SceneChanger().replaceSceneContent("Users.fxml",event);
            }catch (Exception e) {
                System.out.println(e);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Name cannot be blank or contain spaces!");

            alert.showAndWait();

        }

    }

    @FXML private void handleGoBackButton(ActionEvent event) {
        try {
            new SceneChanger().replaceSceneContent("Users.fxml",event);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
