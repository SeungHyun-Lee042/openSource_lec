import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class genSnippet {
    public static void main(String[] args){
        String menu = args[0];
        String menu2 = args[2];
        String[] content = new String[5];
        ArrayList<ArrayList<String>> word = new ArrayList<>();
        String[] temp;
        String dir;
        BufferedReader br = null;
        ArrayList<String> keyword = new ArrayList<>();
        int[] count = {0,0,0,0,0};

        if(menu.equals("-f")) {
            dir = "./example/" + args[1];
            if(menu2.equals("-q")){
                String key = args[3];
                String[] temp_key = key.split(" ");
                for(int i=0; i<temp_key.length; i++){
                    keyword.add(temp_key[i]);
                }
                File file = new File(dir);
                for(int i=0; i<5; i++){
                    try{
                        FileReader reader = new FileReader(file);
                        br = new BufferedReader(reader);
                        StringBuffer sb = new StringBuffer();

                        while(( content[i] = br.readLine()) != null){
                            System.out.println(content[i]);
                            temp = content[i].split(" ");
                            for(int j=0; j<temp.length; j++){
                                word.get(i).add(temp[j]);
                            }
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }finally{
                        try{
                            if(br != null) br.close();
                        }catch(Exception e) {}
                    }
                }

                for(int i=0; i<5; i++){
                    for(int j=0; j<word.get(i).size(); j++){
                        for(int k=0; k<keyword.size(); k++){
                            if(word.get(i).get(j).equals(keyword.get(k))){
                                count[i]++;
                            }
                        }
                    }
                }

                int result = 0;
                for(int i=0; i<5; i++){
                    int max = 0;
                    if(count[max] < count[i]){
                        result = i;
                    }
                }

                for(int i=0; i<result; i++){
                    System.out.print(word.get(result).get(i));
                }

            }
        }
    }
}
