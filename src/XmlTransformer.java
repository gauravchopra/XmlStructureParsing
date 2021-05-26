import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlTransformer {

     

    public static  void  main(String... args) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
        File file = new File("src/file.xml");
        Map<String, String> attributes1 = new HashMap<>();
        extracted(file, attributes1);
        System.out.println(attributes1);

        File file1 = new File("src/file1.xml");
        Map<String, String> attributes2 = new HashMap<>();
        extracted(file1, attributes2);
        System.out.println(attributes2);

        System.out.println("Difference in xmls::"+areEqualKeyValues(attributes2,attributes1));



    }

    private static Map<String, Boolean> areEqualKeyValues(Map<String, String> first, Map<String, String> second) {
        return first.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> !e.getValue().equals(second.get(e.getKey()))));
    }

    private static void extracted(File file, Map<String, String> attributes1) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression = "//*[not(*)]";

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        for(int i = 0 ; i < nodeList.getLength(); i++) {
            attributes1.put(getXPath(nodeList.item(i)),nodeList.item(i).getFirstChild().getNodeValue());


        }
    }

    private static String getXPath(Node node) {
        Node parent = node.getParentNode();
        if (parent == null) {
            return node.getNodeName();
        }
        return getXPath(parent) + "/" + node.getNodeName();
    }
}
