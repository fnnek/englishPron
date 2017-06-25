import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by maciejchrzastek on 29.05.2017.
 */
public class CreateOwnLesson implements Initializable {

    @FXML private TextField answer1pl;

    @FXML private TextField answer1en;

    @FXML private TextField lessonDescription;
    @FXML private ListView<Word> listOfWords = new ListView<>();


    List<String> polishWords = new ArrayList<String>();
    List<String> englishWords = new ArrayList<String>();
    List<Word> words = new ArrayList<>();

    ObservableList<Word> data = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        listOfWords.setItems(data);
        //listOfWords.setCellFactory(wordListView -> new WordListViewCell());


    }

    @FXML private void handleAddWordToList() {
       // polishWords.add(answer1pl.getText());

       // englishWords.add(answer1en.getText());
        String polish = answer1pl.getText();
        String english = answer1en.getText();
        answer1en.clear();
        answer1pl.clear();
        Word word = new Word();
        word.setPolish(polish);
        word.setEnglish(english);
        System.out.println("polish: " + polish);
        System.out.println("english: " + english);
        data.add(word);

        //data = FXCollections.observableArrayList(words);
        listOfWords.setItems(data);
        System.out.println("pętla");
        for(int i = 0;i<data.size();i++) {
            System.out.println(data.get(i).getPolish());
        }
    }

    @FXML private void handleRemoveWord() {
        try {
            int index = listOfWords.getSelectionModel().getSelectedIndex();
            System.out.println("zaznaczone " + index);
            listOfWords.getItems().remove(index);
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("You have to choose item from list!");


            alert.showAndWait();
        }
    }
    @FXML private void handleSaveLessonAction(ActionEvent actionEvent) throws TransformerException {

        if(listOfWords.getItems().size()>0){
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

            for (int i=0;i<listOfWords.getItems().size();i++) {
                Element word = document.createElement("word");
                Element firstEn = document.createElement("english");
                firstEn.appendChild(document.createTextNode(listOfWords.getItems().get(i).getEnglish()));
                Element firstPl = document.createElement("polish");
                firstPl.appendChild(document.createTextNode(listOfWords.getItems().get(i).getPolish()));
                word.appendChild(firstEn);
                word.appendChild(firstPl);
                lesson.appendChild(word);
            }
            fileReader.saveDocumentToFile(document);
            handleBackbtn(actionEvent);
        }else{
            //alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot save this lesson");
            alert.setContentText("You have to add at least one word!");

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