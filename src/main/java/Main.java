import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

public class Main {

    private static boolean isObfMode = true;
    private static String targetXML = "./target.xml";

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("-mode=")) {
                isObfMode = ArgsParser.parseIsObfMode(arg);
            } else if (arg.startsWith("-file=")) {
                targetXML = ArgsParser.parseFilename(arg);
            }
        }
        try {
            File targetFile = new File(targetXML);
            if(isObfMode){
                Obfuscator.obfuscate(targetFile);
            } else {
                Obfuscator.unobfuscate(targetFile);
            }
        } catch (ParserConfigurationException | SAXException | TransformerException | IOException e) {
            e.printStackTrace();
        }
    }
}