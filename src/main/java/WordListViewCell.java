import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Created by fnnek on 25.06.17.
 */
public class WordListViewCell extends ListCell<Word> {

    @FXML
    private Label label1;

    @FXML
    private Label label2;
    @FXML
    private GridPane gridPane;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Word item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            if(mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("ListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                label1.setText(item.getPolish());
                label2.setText(item.getEnglish());

                setText(null);
                setGraphic(gridPane);
            }
        }
    }
}
