import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by maciejchrzastek on 29.05.2017.
 */
public class CreateOwnLesson implements Initializable {

    @FXML private TextField answer1pl;
    @FXML private TextField answer2pl;
    @FXML private TextField answer3pl;
    @FXML private TextField answer4pl;
    @FXML private TextField answer5pl;
    @FXML private TextField answer6pl;
    @FXML private TextField answer7pl;
    @FXML private TextField answer8pl;
    @FXML private TextField answer9pl;
    @FXML private TextField answer10pl;
    @FXML private TextField answer1en;
    @FXML private TextField answer2en;
    @FXML private TextField answer3en;
    @FXML private TextField answer4en;
    @FXML private TextField answer5en;
    @FXML private TextField answer6en;
    @FXML private TextField answer7en;
    @FXML private TextField answer8en;
    @FXML private TextField answer9en;
    @FXML private TextField answer10en;
    @FXML private TextField lessonDescription;

    List<String> polishWords = new ArrayList<String>();
    List<String> englishWords = new ArrayList<String>();

    public void initialize(URL url, ResourceBundle resourceBundle) {



    }
    @FXML private void handleSaveLessonAction(ActionEvent actionEvent) throws TransformerException {
        polishWords.add(answer1pl.getText());
        polishWords.add(answer2pl.getText());
        polishWords.add(answer3pl.getText());
        polishWords.add(answer4pl.getText());
        polishWords.add(answer5pl.getText());
        polishWords.add(answer6pl.getText());
        polishWords.add(answer7pl.getText());
        polishWords.add(answer8pl.getText());
        polishWords.add(answer9pl.getText());
        polishWords.add(answer10pl.getText());
        englishWords.add(answer1en.getText());
        englishWords.add(answer2en.getText());
        englishWords.add(answer3en.getText());
        englishWords.add(answer4en.getText());
        englishWords.add(answer5en.getText());
        englishWords.add(answer6en.getText());
        englishWords.add(answer7en.getText());
        englishWords.add(answer8en.getText());
        englishWords.add(answer9en.getText());
        englishWords.add(answer10en.getText());
        boolean polishWordsTyped = true;
        boolean englishWordsTyped = true;
        for(int i=0;i<polishWords.size();i++){
            if (polishWords.get(i).length() < 1){
                polishWordsTyped = false;
                break;
                // System.out.println("Nie wszystkie polskie slowa zostaly wprowadzone");
            }
        }
        for(int i=0;i<englishWords.size();i++){
            if (englishWords.get(i).length() < 1){
                englishWordsTyped = false;
                break;
                // System.out.println("Nie wszystkie angielskie slowa zostaly wprowadzone");
            }
        }

        if(polishWordsTyped && englishWordsTyped){
            //get xmlreader object
            XmlReader fileReader = new XmlReader();
            int lastID = Integer.parseInt(fileReader.getLastLessonID());
            System.out.print("lessons id: "+lastID);

            Document document = fileReader.getDocument("shema.xml");
            Element root = document.getDocumentElement();
            // new lesson element
            Element lesson = document.createElement("lesson");
            root.appendChild(lesson);
            // set an attribute to lesson element
            //id
            int nextID = ++lastID;
            Attr attr = document.createAttribute("id");
            attr.setValue(String.valueOf(nextID));
            lesson.setAttributeNode(attr);
            //name
            Attr attrName = document.createAttribute("name");
            attrName.setValue("Lesson "+String.valueOf(nextID));
            lesson.setAttributeNode(attrName);
            //lesson description
            Attr attrDesc = document.createAttribute("lessonDescription");
            attrDesc.setValue(lessonDescription.getText());
            lesson.setAttributeNode(attrDesc);

            for (int i=0;i<polishWords.size();i++) {
                Element word = document.createElement("word");
                Element firstEn = document.createElement("english");
                firstEn.appendChild(document.createTextNode(englishWords.get(i)));
                Element firstPl = document.createElement("polish");
                firstPl.appendChild(document.createTextNode(polishWords.get(i)));
                word.appendChild(firstEn);
                word.appendChild(firstPl);
                lesson.appendChild(word);
            }
            fileReader.saveDocumentToFile(document);
            handleBackbtn(actionEvent);
        }else{
            //alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while saving");
            alert.setHeaderText("Cannot save this lesson");
            alert.setContentText("You have to provide all 10 words and their translations in order to save the lesson.");

            alert.showAndWait();
        }



    }


    @FXML private void handleBackbtn(ActionEvent actionEvent) {
        try{
            new SceneChanger().replaceSceneContent("StartView.fxml",actionEvent);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}