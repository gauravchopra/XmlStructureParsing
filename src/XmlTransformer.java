import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class XmlTransformer {

     

    public static  void  main(String... args) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException, TransformerException {

        Source inputXml1 = new StreamSource(new File("src/file.xml"));
        Map<String, String> attributes1 = getStringStringMap(inputXml1);
        Source inputXml2 = new StreamSource(new File("src/file1.xml"));
        Map<String, String> attributes2 = getStringStringMap(inputXml2);


        MapDifference<String,String> stringMapDifference= Maps.difference(attributes1,attributes2);

        Map<String, MapDifference.ValueDifference<String>> valuesChanged = stringMapDifference.entriesDiffering();
        System.out.println("Tags Values Changed::"+valuesChanged);

        System.out.println("Tags Deleted::"+stringMapDifference.entriesOnlyOnLeft());

        System.out.println("Tags Added::"+stringMapDifference.entriesOnlyOnRight());
    }

    private static Map<String, String> getStringStringMap(Source inputXml) throws TransformerException, UnsupportedEncodingException {
        Source streamSource=new StreamSource(new File("src/transform.xslt"));
        TransformerFactory transformerFactory=TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(streamSource);


        StreamResult standardResult = new StreamResult(new ByteArrayOutputStream());
        transformer.transform(inputXml, standardResult);

        String outputTags=((ByteArrayOutputStream) standardResult.getOutputStream()).toString("UTF-8");
        String[] arrString=outputTags.split("\\n");

        Map<String, String> attributes = new TreeMap<>();
        Arrays.stream(arrString).forEach(attrib ->{
            String [] tagValue=attrib.split("=");
            attributes.put(tagValue[0],tagValue[1]);
        });
        return attributes;
    }

    private static Map<String, Boolean> areEqualKeyValues(Map<String, String> first, Map<String, String> second) {
        return first.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                        e -> !e.getValue().equals(second.get(e.getKey()))));
    }

}
