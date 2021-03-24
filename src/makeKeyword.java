import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class makeKeyword {
    String fileDir;

    public makeKeyword(String fileDir) throws ParserConfigurationException, TransformerException, IOException, SAXException { this.fileDir = fileDir;
        File dir = new File(this.fileDir);
        //File []fileList = dir.listFiles();
        int num = 5;
        String [] fileName  = new String[num];
        String [] fileCon = new String[num];
        String [] keyword = new String[num];

        BufferedReader br = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(this.fileDir);
        String temp = "";

        Element docEl = doc.getDocumentElement();
        NodeList child = docEl.getChildNodes();
        int in=0;
        for(int i=0; i<child.getLength(); i++){
            Node node = child.item(i);
            NodeList childDoc = node.getChildNodes();

            for(int j=0; j<childDoc.getLength();j++){
                Node childNode = childDoc.item(j);
                if(childNode.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) childNode;
                    String title = element.getNodeName();
                    if(title.equals("body")){
                        fileCon[in] = element.getTextContent();
                        in++;
                    }else if(title.equals("title")){
                        fileName[in] = element.getTextContent();
                    }else continue;
                }
            }
        }

        for(int i=0; i<num; i++){
            keyword[i] = useKKma(fileCon[i]);
        }

        DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
        DocumentBuilder Builder = docFac.newDocumentBuilder();
        Document docIndex = Builder.newDocument();

        Element docs = docIndex.createElement("docs");
        docIndex.appendChild(docs);

        for(int i=0; i<num; i++){
            Element docId = docIndex.createElement("doc");
            docs.appendChild(docId);

            docId.setAttribute("id", Integer.toString(i));

            Element title = docIndex.createElement("title");
            title.appendChild(docIndex.createTextNode(fileName[i]));
            docId.appendChild(title);

            Element body = docIndex.createElement("body");
            body.appendChild(docIndex.createTextNode(keyword[i]));
            docId.appendChild(body);
        }


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource source = new DOMSource(docIndex);
        StreamResult result = new StreamResult(new FileOutputStream(new File("example/index.xml")));

        transformer.transform(source, result);


    }

    public String useKKma(String text){
        String result = "";

        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(text, true);

        for(int i=0; i<kl.size(); i++){
            Keyword key = kl.get(i);
            if(i != kl.size()-1){
                result = result + key.getString() + ":" + key.getCnt() + "#";
            }else {
                result = result + key.getString() + ":" + key.getCnt();
            }
        }
        return result;
    }
}
