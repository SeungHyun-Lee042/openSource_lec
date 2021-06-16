package week15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
public class week15 {
 
    public static void main(String[] args) throws ParseException, IOException {
        String clientId = "spJm85pr6h9XZNTiQfGV"; 
        String clientSecret = "jmE4wCTdkw"; 
        String text = null;
        
        try {
            Scanner scan = new Scanner(System.in);
            System.out.print("검색어를 입력하세요 : ");
            String title = scan.nextLine();
            text = URLEncoder.encode(title, "UTF-8");
            scan.close();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }
 
        String apiURL = "https://openapi.naver.com/v1/search/movie?query=" + text;
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Naver-Client-Id", clientId);
        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
        
        int responsecode = con.getResponseCode();
        BufferedReader br;
        if (responsecode == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        
        StringBuffer responseBody = new StringBuffer();
        
        String line;
        while ((line = br.readLine()) != null) {
            responseBody.append(line);
        }
        br.close();
        
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(responseBody);
        JSONArray item = (JSONArray) jsonObject.get("items");
        
        for(int i = 0; i < item.size(); i++) {
            System.out.println("=item_" + i + "============================");
            JSONObject itemObject = (JSONObject) item.get(i);
            System.out.println("title:\t" + itemObject.get("title"));
            System.out.println("subtitle:\t" + itemObject.get("subtitle"));
            System.out.println("director:\t" + itemObject.get("director"));
            System.out.println("actor:\t" + itemObject.get("actor"));
            System.out.println("userRating:\t" + itemObject.get("userRating") + "\n");
 
        }
    }
    
}
