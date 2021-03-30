import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import javax.swing.*;
import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException {

        String menu = args[0];
        String dir;

        if(menu.equals("-c")){
            makeCollection collection = new makeCollection(args[1]);
        }else if(menu.equals("-k")){
            dir = "./example/" + args[1];
            makeKeyword keyword = new makeKeyword(dir);
        }else if(menu.equals("-i")){
            dir = "./example/" + args[1];
            indexer index = new indexer(dir);

        }
    }
}
