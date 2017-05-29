import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

    public List<Lesson> getAllLessons(){
        List<Lesson> list = new ArrayList<Lesson>();
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
            list.add(lesson);
            System.out.println("-----------------");
            System.out.println(s);
            System.out.println("-----------------");
            //nodeList.item(i).getNodeValue()
        }

        return list;
    }

}
