import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Obfuscator {
    private static String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static String target = "HIJ1LMNOPQRS8U40XYZAB2DEFGhijkl3nop7rstu5wxy9a6cdefgWKCmVvbqTz";

    public static void obfuscate(File file) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        processXML(file,true);
    }
    public static void unobfuscate(File file) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        processXML(file,false);
    }

    private static void processXML(File file, boolean isObfuscate) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputStream targetStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
        Document document = documentBuilder.parse(targetStream);
        Element root = document.getDocumentElement();

        editXML(root, isObfuscate ? Obfuscator::obfuscateString : Obfuscator::unobfuscateString);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    private static void editXML(Node targetNode, StringTransformer transformer) {
        switch (targetNode.getNodeType()) {
            case Node.ELEMENT_NODE:
                Element element = (Element) targetNode;
                for (int i = 0; i < element.getAttributes().getLength(); i++) {
                    element.setAttribute(element.getAttributes().item(i).getNodeName(), transformer.transform(element.getAttributes().item(i).getNodeValue()));
                }
                for (int i = 0; i < targetNode.getChildNodes().getLength(); i++) {
                    editXML(targetNode.getChildNodes().item(i), transformer);
                }
                break;
            case Node.TEXT_NODE:
                targetNode.setTextContent(transformer.transform(targetNode.getTextContent()));
                break;
        }
    }

    private static String obfuscateString(String s) {
        char[] result = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int index = source.indexOf(c);
            result[i] = index != -1 ? target.charAt(index) : c;
        }
        return new String(result);
    }

    private static String unobfuscateString(String s) {
        char[] result = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int index = target.indexOf(c);
            result[i] = index != -1 ? source.charAt(index) : c;
        }
        return new String(result);
    }
}
