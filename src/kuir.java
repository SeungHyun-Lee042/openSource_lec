import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.swing.*;
import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class kuir {
//    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException, ClassNotFoundException {
//
//        String menu = args[0];
//        String dir;
//
//        if(menu.equals("-c")){
//            makeCollection collection = new makeCollection(args[1]);
//        }else if(menu.equals("-k")){
//            dir = "./example/" + args[1];
//            makeKeyword keyword = new makeKeyword(dir);
//        }else if(menu.equals("-i")){
//            dir = "./example/" + args[1];
//            indexer index = new indexer(dir);
//        }else if(menu.equals("-s")){
//            dir = "./example/";
//            if(args[2].equals("-q")){
//                String query = args[3];
//                searcher searcher = new searcher(dir, query);
//            }
//        }
//    }

    public static void main(String[] args) {
        String menu = args[0];
        String menu2 = args[2];
        String[] content = new String[5];
        ArrayList<ArrayList<String>> word = new ArrayList<>();
        String[] temp;
        String dir;
        BufferedReader br = null;
        ArrayList<String> keyword = new ArrayList<>();
        int[] count = {0, 0, 0, 0, 0};

        if (menu.equals("-f")) {
            dir = "./example/" + args[1];
            if (menu2.equals("-q")) {
                String key = args[3];
                String[] temp_key = key.split(" ");
                for (int i = 0; i < temp_key.length; i++) {
                    keyword.add(temp_key[i]);
                }
                File file = new File(dir);
                for (int i = 0; i < 5; i++) {
                    try {
                        FileReader reader = new FileReader(file);
                        br = new BufferedReader(reader);
                        StringBuffer sb = new StringBuffer();

                        while ((content[i] = br.readLine()) != null) {
                            System.out.println(content[i]);
                            temp = content[i].split(" ");
                            for (int j = 0; j < temp.length; j++) {
                                word.get(i).add(temp[j]);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (br != null) br.close();
                        } catch (Exception e) {
                        }
                    }
                }

                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < word.get(i).size(); j++) {
                        for (int k = 0; k < keyword.size(); k++) {
                            if (word.get(i).get(j).equals(keyword.get(k))) {
                                count[i]++;
                            }
                        }
                    }
                }

                int result = 0;
                for (int i = 0; i < 5; i++) {
                    int max = 0;
                    if (count[max] < count[i]) {
                        result = i;
                    }
                }

                for (int i = 0; i < result; i++) {
                    System.out.print(word.get(result).get(i));
                }

            }
        }
    }
}
