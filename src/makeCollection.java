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

public class makeCollection {
    String fileDir;
    public makeCollection(String fileDir) throws ParserConfigurationException, TransformerException, FileNotFoundException {
        this.fileDir = fileDir;
        File dir = new File(this.fileDir);
        File []fileList = dir.listFiles();
        int num = fileList.length;
        String [] fileName  = new String[num];
        String [] fileCon = new String[num];
        BufferedReader br = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        String temp = "";

        for(int i=1; i<num; i++){
            temp = "";
            int count = 0;
            File file = fileList[i];
            if(file.isFile()){
                String name= file.getName();
                fileName[i] = name.substring(0,name.lastIndexOf("."));
            }

            try{
                FileReader reader = new FileReader(file);
                br = new BufferedReader(reader);
                StringBuffer sb = new StringBuffer();

                while((fileCon[i] = br.readLine()) != null){
                    if(count >= 8){
                        temp += fileCon[i].replaceAll("<[^>]*>", " ");
                        temp += "\n";
                    }
                    count++;

                }
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try{
                    if(br != null) br.close();
                }catch(Exception e) {}
            }
            //System.out.println("content : "+temp);
            fileCon[i] = temp;
        }
        Element docs = doc.createElement("docs");
        doc.appendChild(docs);

        for(int i=1; i<num-1; i++){
            Element docId = doc.createElement("doc");
            docs.appendChild(docId);

            docId.setAttribute("id", Integer.toString(i-1));

            Element title = doc.createElement("title");
            title.appendChild(doc.createTextNode(fileName[i+1]));
            docId.appendChild(title);

            Element body = doc.createElement("body");
            body.appendChild(doc.createTextNode(fileCon[i+1]));
            docId.appendChild(body);
        }


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(new File("example/collection.xml")));

        transformer.transform(source, result);


    }
}





