import jdk.swing.interop.SwingInterOpUtils;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Double.parseDouble;

public class searcher {
    String fileDir;
    String query;
    int num;

    ArrayList<String> Title = new ArrayList<>();
    ArrayList<String> word;
    ArrayList<Double> weight;
    String [] result = new String[3];

    public searcher(String fileDir, String query) throws IOException, ClassNotFoundException {
        this.fileDir = fileDir;
        File dir = new File(this.fileDir);
        File []fileTemp = dir.listFiles();
        this.num = fileTemp.length;
        this.query = query;
        this.word = this.useKKma();

        //html 파일 명 가져오기
        for(int i=0; i<num; i++){
            File file = fileTemp[i];
            if(file.isFile()){
                if(file.getName().toLowerCase().endsWith(".html") == true) {
                    Title.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
                }
            }
        }
        num = Title.size();
        rank();
    }

    public ArrayList<String> useKKma(){
        ArrayList<String> word = new ArrayList<>();
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(this.query, true);

        for(int i=0; i<kl.size(); i++){
            Keyword key = kl.get(i);
            word.add(key.getString());
        }
        return word;
    }

    public HashMap getHashmap() throws IOException, ClassNotFoundException {
        //hashmap 선언
        this.fileDir = this.fileDir + "index.post";
        FileInputStream fileInputStream = new FileInputStream(this.fileDir);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap map = (HashMap)object;

        return map;
    }

    public ArrayList<Double> CalcSim() throws IOException, ClassNotFoundException {
        HashMap map = getHashmap();
        //가중치 계산
        this.weight = new ArrayList<Double>();
        double split = 0;
        for(int i=0; i<num; i++){
            for(int j=0; j<word.size(); j++){
                String hash = (String) map.get(word.get(j));
                String [] spt_1 = hash.split(", ");
                String [] spt_2 = spt_1[2*i+1].split("]");
                split = split + Math.pow(Double.parseDouble(spt_2[0]),2.0);
            }
            weight.add(split);
            split =0;
        }
        System.out.println("\n");
        /*System.out.println("------가중치------");
        System.out.println(weight);
        System.out.println("-------상위 문서-------");*/

        return weight;
    }

    public void rank() throws IOException, ClassNotFoundException {
        ArrayList<Double> weight = CalcSim();

        //순위 판단
        int rank=0,position=0;
        double score = 0;

        while(result[2] == null){
            for(int i=0; i<weight.size();i++){
                if(score < weight.get(i)){
                    score = weight.get(i);
                    position = i;
                }
            }
            result[rank] = Title.get(position);
            weight.set(position, -100.0);
            for(int i=0; i<weight.size(); i++){
                if(weight.get(i) == 0){
                    position = i;
                    break;
                }else{
                    continue;
                }
            }
            rank++;
            score = 0;
        }
        for(int i=0; i<result.length; i++){
            System.out.println(i+1+ " => "+result[i]);
        }
    }
}
