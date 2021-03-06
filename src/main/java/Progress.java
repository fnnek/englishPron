import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fnnek on 16.06.17.
 */
public class Progress implements Initializable{
    @FXML
    private Label topLabel;
    @FXML
    private GridPane progressPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topLabel.setText(topLabel.getText()+ " " + Globals.userName);
        XmlReader reader = new XmlReader();
        List<String> lessons = reader.getAllLessonsIDs();
        for(int i = 0; i<lessons.size();i++) {
            System.out.println("Lesson id: "+lessons.get(i));
            int result = reader.getResultOfLesson(lessons.get(i));

            Label label = new Label(lessons.get(i) + ": " + result+ "/"+reader.getWords(String.valueOf(i+1),"english").size()*2);
            label.setFont(new Font("Arial", 16));
            if (result < 1){
                label.setTextFill(Color.RED);
            }else{
                label.setTextFill(Color.GREEN);
            }
            progressPane.addRow(i+1,label);
        }
   }
   @FXML private void handleClearProgressButton(ActionEvent event){
       XmlReader reader = new XmlReader();
       System.out.println("Removing lesson results");
       reader.clearLessonResult(Globals.userName);
       final AtomicInteger count = new AtomicInteger();

       progressPane.getChildren().forEach((node) -> {

        int id = count.incrementAndGet()-1;
           if(node.getClass()==Label.class){
               Label lbl = (Label)node;
               if(lbl != topLabel){

                   String s = lbl.getText();
                   int startIndex = s.indexOf(":");
                   String newString = s.substring(0, startIndex+1) + " 0/" + reader.getNumberOfWords(String.valueOf(id))*2;

                   lbl.setText(newString);
                   lbl.setTextFill(Color.RED);

               }
           }



       });
   }

    @FXML
    private void handleGoBackButton(ActionEvent event) {
        try {
            new SceneChanger().replaceSceneContent("StartView.fxml",event);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
