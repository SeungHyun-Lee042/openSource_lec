import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class indexer {
    String fileDir;
    int num = 5;
    String [] fileName  = new String[num];
    String [] fileCon = new String[num];
    ArrayList<ArrayList<String>> TF = new ArrayList();
    ArrayList<ArrayList<String>> converge = new ArrayList();

    public indexer(String fileDir) throws ParserConfigurationException, IOException, SAXException {
        this.fileDir = fileDir;

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(this.fileDir);

        Element docEl = doc.getDocumentElement();
        NodeList child = docEl.getChildNodes();
        int in=0;
        String [][] content = new String[num][];
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
                        content[in] = fileCon[i].split("#");
                        in++;
                    }else if(title.equals("title")){
                        fileName[in] = element.getTextContent();
                    }else continue;
                }
            }
        }

        String [] key = new String[2];
        ArrayList<String> divide;
        for(int i =0; i<content.length; i++){
             divide = new ArrayList<>();
            for(int j=0; j<content[i].length;j++){
                key = content[i][j].split(":");
                divide.add(key[0]);
                divide.add(key[1]);

            }
            converge.add(divide);
        }


        for(int i=0; i<converge.size(); i++){
            for(int j=0; j<converge.get(i).size(); j++){
                if(j % 2 == 0){
                    if(!TF.contains(converge.get(i).get(j))){
                        TF.add(hashMap(converge.get(i).get(j)));
                    }
                }else continue;
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream("./example/index.post");
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);

        HashSet<ArrayList<String>> set = new HashSet<>(TF);
        ArrayList<ArrayList<String>> result = new ArrayList(set);

        HashMap map = new HashMap();
        for(int i=0; i<result.size(); i++){
            String str="";
            for(int j=0; j<num; j++){
                if(j==0) str = "[" + str+ result.get(i).get(j+1) + "]";
                else str = str+ ", "+"[" + result.get(i).get(j+1)+ "]";
            }
            map.put(result.get(i).get(0), str);
        }
        outputStream.writeObject(map);
        outputStream.close();

        Iterator<String> it = map.keySet().iterator();
        while(it.hasNext()){
            String Key = it.next();
            String value = (String) map.get(Key);
            System.out.println(Key + " -> " + value);
        }

    }

    public ArrayList<String> hashMap(String key){
        int count =0;
        int [] weight = new int [num];
        double val = 0;
        ArrayList <String> hash = new ArrayList<>();
        hash.add(key);

        for(int i=0; i<converge.size(); i++){
            if(converge.get(i).contains(key)){
                for(int j=0; j<converge.get(i).size(); j++){
                    if(j%2 ==0){
                        if(converge.get(i).get(j).equals(key)){
                            count++;
                            weight[i] = Integer.parseInt(converge.get(i).get(j+1));
                        }
                    }else continue;
                }
            }else continue;
        }
        for(int i=0; i<weight.length; i++){
            if(count != 0){
                val = Math.round(weight[i] * Math.log((double)num/(double)count) * 100) / 100.0;
                String temp = i + ", " + val;
                hash.add(temp);
            }
        }
        return hash;
    }

}
