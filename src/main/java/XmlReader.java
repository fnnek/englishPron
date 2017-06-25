import javafx.scene.control.Alert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fnnek on 29.05.17.
 */
public class XmlReader {

    public Document getDocument(String file) {
        Document doc = null;

        try {
            File fXmlFile = new File(file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }

    public List<String> getAllLessons(){
        List<String> list = new ArrayList<String>();
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("lesson");

        for (int i = 0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String s = element.getAttribute("name");
            String id = element.getAttribute("id");
            Lesson lesson = new Lesson();
            lesson.setId(id);
            lesson.setName(s);
            list.add(s);
            System.out.println("-----------------");
            System.out.println(list.get(i));
            System.out.println("-----------------");
            //nodeList.item(i).getNodeValue()
        }

        return list;
    }
    public List<String> getAllLessonsIDs(){
        List<String> list = new ArrayList<String>();
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("lesson");

        for (int i = 0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String id = element.getAttribute("id");
            list.add(id);
        }

        return list;
    }

    public List<String> getAllLessonsCombinedWithDescriptions(){
        List<String> list = new ArrayList<String>();
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("lesson");

        for (int i = 0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String name = element.getAttribute("name");
            String id = element.getAttribute("id");
            String description = element.getAttribute("lessonDescription");
//            Lesson lesson = new Lesson();
//            lesson.setId(id);
//            lesson.setName(name);
//            lesson.setDescription(description);
            list.add(name+" | "+description);
            System.out.println("-----------------");
            System.out.println(list.get(i));
            System.out.println("-----------------");
            //nodeList.item(i).getNodeValue()
        }
        return list;
    }

    public List<String> getPolishWords(){
        List<String> list = new ArrayList<String>();
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("lesson");
        Node node = nodeList.item(0);
        Element element = (Element) node;

        NodeList word = element.getElementsByTagName("word");

        for(int i = 0;i<word.getLength();i++)
        {
            Node node1 = word.item(i);
            Element element1 = (Element) node1;

            String polish = element.getElementsByTagName("polish").item(i).getTextContent();
            list.add(polish);
        }


        return list;
    }

    public List<String> getEnglishWords(){
        List<String> list = new ArrayList<String>();
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("lesson");
        Node node = nodeList.item(0);
        Element element = (Element) node;

        NodeList word = element.getElementsByTagName("word");

        for(int i = 0;i<word.getLength();i++)
        {
            Node node1 = word.item(i);
            Element element1 = (Element) node1;

            String polish = element.getElementsByTagName("english").item(i).getTextContent();
            System.out.print(polish);
            list.add(polish);
        }


        return list;
    }

    public List<String> getWords(String lessonID, String which){
        List<String> list = new ArrayList<String>();
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("lesson");
        Node node = null;
        for(int i = 0; i<nodeList.getLength();i++) {
            if(nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(lessonID)) {
                node = nodeList.item(i);
            }
        }

        Element element = (Element) node;

        try {
            NodeList word = element.getElementsByTagName("word");
            for(int i = 0;i<word.getLength();i++)
            {
                Node node1 = word.item(i);
                Element element1 = (Element) node1;

                String polish = element.getElementsByTagName(which).item(i).getTextContent();
                System.out.print(polish);
                list.add(polish);
            }
        }catch (NullPointerException e) {
            System.out.println(e);
        }


        return list;
    }

    public int getNumberOfLessons() {
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("lesson");

        return nodeList.getLength();
    }

    public String getRandomLesson(int lessonId) {
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("lesson");
        Node node = nodeList.item(lessonId);
        Element element = (Element) node;

        return element.getAttribute("name");
    }

    public List<String> getUsers(){
        List<String> users = new ArrayList<String>();
        Document document = getDocument("users.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("user");
        for (int i = 0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String s = element.getAttribute("name");

            users.add(s);
            System.out.println("-----------------");
            System.out.println(users.get(i));
            System.out.println("-----------------");
            //nodeList.item(i).getNodeValue()
        }

        return users;

    }

    public void addNewUser(String name) {
        Document document = getDocument("users.xml");
        Element root = document.getDocumentElement();
        Element user = document.createElement("user");
        user.setAttribute("name",name);
        root.appendChild(user);

        TransformerFactory factory = TransformerFactory.newInstance();
        saveToFile(document, factory);


        DOMSource source = new DOMSource(document);
    }

    public void saveLessonResult(String lessonName, int result) {
        Document document = getDocument("users.xml");
        Element root = document.getDocumentElement();

        NodeList usersList = root.getElementsByTagName("user");

        //results for every lesson for given user
        Node user = null;
        for(int i = 0; i<usersList.getLength();i++) {
            if(usersList.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(Globals.userName)) {//
                user = usersList.item(i);
            }
        }

        Element lessonResult = document.createElement("Lesson_result");
        if(!user.hasChildNodes()) {
            lessonResult.setAttribute("lessonName",lessonName);
            lessonResult.setAttribute("result",String.valueOf(result));
        } else {
            NodeList lessonResultsList = user.getChildNodes();
            System.out.println("First lesson"+lessonResultsList.item(0).getNodeName());
            for(int i = 0; i<lessonResultsList.getLength();i++) {
                Node lesson = lessonResultsList.item(i);
                Element lessonElement = (Element) lesson;

                if(lessonElement.getAttribute("lessonName").equals(Globals.lessonName)) {
                    user.removeChild(lessonElement);
                    lessonResult.setAttribute("lessonName",lessonName);
                    lessonResult.setAttribute("result",String.valueOf(result));
                }
                else {
                    lessonResult.setAttribute("lessonName",lessonName);
                    lessonResult.setAttribute("result",String.valueOf(result));
                }
            }
        }

        user.appendChild(lessonResult);

        TransformerFactory factory = TransformerFactory.newInstance();
        saveToFile(document, factory);


        DOMSource source = new DOMSource(document);
    }
    public void clearLessonResult(String userName){
        Document document = getDocument("users.xml");
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("user");
        Node user = null;
        for(int i = 0; i<nodeList.getLength();i++) {
            if(nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(userName)) {
                user = nodeList.item(i);
                while (user.hasChildNodes())
                    user.removeChild(user.getFirstChild());
            }
        }
        TransformerFactory factory = TransformerFactory.newInstance();
        saveToFile(document, factory);
    }
    public int getResultOfLesson(String lessonName) {
        int result = 0;
        Document document = getDocument("users.xml");
        Element root = document.getDocumentElement();

        NodeList nodeList = root.getElementsByTagName("user");
        Node node = null;
        for(int i = 0; i<nodeList.getLength();i++) {
            if(nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(Globals.userName)) {
                node = nodeList.item(i);
            }
        }

        Element lessonResult = document.createElement("Lesson_result");
        if(!node.hasChildNodes()) {
            result = 0;
        } else {
            NodeList list = node.getChildNodes();
            for(int i = 0; i<list.getLength();i++) {
                Node tmpNode = list.item(i);
                Element tmpElement = (Element) tmpNode;

                if(tmpElement.getAttribute("lessonName").equals(lessonName)) {
                    System.out.println("Jestem tutaj!!!");
                    result = Integer.valueOf(tmpElement.getAttribute("result"));
                    System.out.println("Jestem tutaj result: " + result);
                    return result;

                }
                else {
                    result = 0;
                }

            }
        }
        System.out.println("result: " + result);

        return result;

    }

    public String getLastLessonID(){
        List<String> list = new ArrayList<String>();
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("lesson");
        //String id = nodeList.item(nodeList.getLength() - 1).toString();

        Node node = nodeList.item(nodeList.getLength()-1);
        Element element = (Element) node;
        String id = element.getAttribute("id");
        return id;
    }

    private void saveToFile(Document document, TransformerFactory factory) {
        try {
            Transformer transformer = factory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("users.xml"));
            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            System.out.println(e);
            showAlert();
        }
    }

    public void saveDocumentToFile(Document document){
        // create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            showAlert();
        }
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File("shema.xml"));

        try {
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
            showAlert();
        }
        System.out.println("Done creating XML File");
    }

    public int getNumberOfWords(String lessonID){
        Document document = getDocument("shema.xml");
        Element root = document.getDocumentElement();
        int ile;

        NodeList nodeList = root.getElementsByTagName("lesson");
        Node node = null;
        for(int i = 0; i<nodeList.getLength();i++) {
            if(nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(lessonID)) {
                node = nodeList.item(i);
            }
        }

        Element element = (Element) node;

        NodeList word = element.getElementsByTagName("word");


        return word.getLength();
    }

    private void showAlert() {
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("Error");
        alert2.setHeaderText("Application couldn't save data to file.");
        alert2.setContentText("If you are using Windows 7/8/10, you probably need to run this app with administrator rights (check out the documentation) ");
        alert2.show();
    }

}
