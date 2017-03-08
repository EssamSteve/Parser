/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z2data.parser;

import static com.oracle.nio.BufferSecrets.instance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

/**
 *
 * @author essam.nasr
 */
public class NewClass {

    public static void main(String[] args) throws IOException, JSONException {
          loadUrls("C:\\Users\\essam.nasr\\Desktop\\urls_test.txt");
          //doParsing(url);
    }
    
    public static void loadUrls(String filePath) throws FileNotFoundException {
        BufferedReader bfreder=new BufferedReader(new FileReader(new File(filePath)));
        String line =null;
        try {
            for (int i=0;(line=bfreder.readLine())!=null;i++) {
                //apply extraction
                String data=doParsing(line);
                //insert data to file
                if(data!=null){
                String path = "C:\\Users\\essam.nasr\\Desktop\\output\\output"+i+".txt";
                 // Use relative path for Unix systems
                File f = new File(path);
                f.getParentFile().mkdirs();
                f.createNewFile();
                addDataToFile(data,path);
                }
                System.out.println("Process url(line) number:>"+i);  
            }
        } catch (IOException ex) {
            System.err.println("ERROR:::::"+ex);
        }
    }
      public static void addDataToFile(String data,String filePath) {
        try { 
             Files.write(Paths.get(filePath),data.getBytes(), StandardOpenOption.WRITE);
        } catch (IOException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String doParsing(String url) throws IOException {
        
            JSONObject jo = null;
            JSONObject prevjo = null;
            Elements tds = null;
            int tdsSize = 0;
            String key = null;
            String value = null;
        // final String HTML = "<table cellspacing=\"0\" style=\"height: 24px;\">\r\n<tr class=\"tr-hover\">\r\n<th rowspan=\"15\" scope=\"row\">Network</th>\r\n<td class=\"ttl\"><a href=\"network-bands.php3\">Technology</a></td>\r\n<td class=\"nfo\"><a href=\"#\" class=\"link-network-detail collapse\">GSM</a></td>\r\n</tr>\r\n<tr class=\"tr-toggle\">\r\n<td class=\"ttl\"><a href=\"network-bands.php3\">2G bands</a></td>\r\n<td class=\"nfo\">GSM 900 / 1800 - SIM 1 & SIM 2</td>\r\n</tr>   \r\n<tr class=\"tr-toggle\">\r\n<td class=\"ttl\"><a href=\"glossary.php3?term=gprs\">GPRS</a></td>\r\n<td class=\"nfo\">Class 12</td>\r\n</tr>   \r\n<tr class=\"tr-toggle\">\r\n<td class=\"ttl\"><a href=\"glossary.php3?term=edge\">EDGE</a></td>\r\n<td class=\"nfo\">Yes</td>\r\n</tr>\r\n</table>";
          Document document = Jsoup.connect(url).timeout(60*1000).get();
       // File input = new File("C:\\Users\\essam.nasr\\Desktop\\table to json.html");
       // Document document = Jsoup.parse(new File(url), "UTF-8");
        // Document document = Jsoup.parse("C:\\Users\\essam.nasr\\Desktop\\table to json.html") ;
       // Elements tables = document.select("table").attr("class", "comparisonTable table table-bordered table-striped");
        //get table by  xpath
       Elements tables = document.select("table[class =comparisonTable table table-bordered table-striped]");
       String arrayNameOfTables=document.title();
       JSONObject allJsonObjsTablesInPage=new JSONObject();
       JSONArray jsonArrayOFTables=new JSONArray();
        if(tables.size()>0){
        for (int t=0;t<tables.size();t++) {
            String arrayName = tables.get(t).attr("name");
            Elements trs = tables.get(t).getElementsByTag("tr");
           // Elements trs = table.getElementsByTag("tr");
            //System.out.println(document.html());
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();
            for (int i = 1; i < trs.size(); i++) {
                jo = new JSONObject();
                tds = trs.get(i).select("td");
                tdsSize = tds.size();
                if (tdsSize == trs.get(0).select("th").size()) {
                    for (int j = 0; j < tds.size(); j++) {
                        key = trs.get(0).select("th").get(j).text();
                        String val = tds.get(j).getAllElements().attr("data-dtmname", "RoHS / Pb Free").text();
                        // int r=val.length();
                        if (val.length() == 0) {
                            value = tds.get(j).getElementsByTag("a").first().attr("href");
                        } else {
                            value = tds.get(j).text();
                        }
                        //value = tds.get(j).text();
                        jo.put(key, value);
                        System.out.println("-->" + key + ":" + value);
                    }
                    prevjo = jo;
                } else {
                    jo.put("Package Description", prevjo.getString("Package Description"));
                    jo.put("Outline Version", prevjo.getString("Outline Version"));

                    for (int j = 2; j < tds.size() + 2; j++) {
                        // suppose that thoose cells belong to parent cell---->add key value of last key and value  of them 
                        key = trs.get(0).select("th").get(j).text();
                        String val = tds.get(j - 2).getAllElements().attr("data-dtmname", "RoHS / Pb Free").text();
                        if (val.length() == 0) {
                            value = tds.get(j - 2).getElementsByTag("a").first().attr("href");
                        } else {
                            value = tds.get(j - 2).text();
                        }
                        jo.put(key, value);
                        System.out.println("-->" + key + ":" + value);
                    }
                }
                System.out.println("-------------------------------------------");
                jsonArr.put(i - 1, jo);

            }
           // jsonObj.put("any", jsonArr);
            System.out.println(jsonObj.toString());
            //put each table jason array as json aobject  
            jsonObj.put(arrayName, jsonArr);
            jsonArrayOFTables.put(t,jsonObj);
       }
         allJsonObjsTablesInPage.put(arrayNameOfTables,jsonArrayOFTables);
         System.err.println("*************"+arrayNameOfTables.length()+"************");
        return allJsonObjsTablesInPage.toString();
       }
         return null;
       
    }
    
    
    
    
    public static String doAnotherParsing(String url) throws IOException {
            // Document document = Jsoup.parse("C:\\Users\\essam.nasr\\Desktop\\table to json.html") ;
        // Document document = Jsoup.parse(new File("C:\\Users\\essam.nasr\\Desktop\\table to json.html"), "UTF-8");
        Document document = Jsoup.connect("http://www.nxp.com/products/microcontrollers-and-processors/arm-processors/kinetis-cortex-m-mcus/ea-series-automotive-m0-plus/ultra-reliable-kea-automotive-microcontrollers-mcus-based-on-arm-cortex-m0-plus-core:KEA?tab=Package_Quality_Tab").get();
        Element table = document.select("#iw_comp1472709613017 > div > div.table-responsive > table").first();
        
        String arrayName = "DONT TELL ANY ONE";
        Elements trs=table.getElementsByTag("tr");
        //System.out.println(document.html());
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        
        JSONObject jo = null;
        Elements tds=null;
        int tdsSize=0;
        String key=null;
        String value=null;
        for (int i = 1; i < trs.size(); i++) {
            jo = new JSONObject();
             tds=trs.get(i).select("td");
             tdsSize=tds.size();
            if (tdsSize == trs.get(0).select("th").size()) {
                for (int j = 0; j < tds.size(); j++) {
                     key = trs.get(0).select("th").get(j).text();
                     value = tds.get(j).text();
                     jo.put(key, value);
                     System.out.println("-->" + key + ":" + value);
                }
            }else {
                for (int j = 2; j < tds.size()+2; j++) {
                    // suppose that thoose cells belong to parent cell---->add key value of last key and value  of them 
                     key = trs.get(0).select("th").get(j).text();
                     value = tds.get(j-2).text();
                    jo.put(key, value);
                    System.out.println("-->" + key + ":" + value);
                }
            }
            System.out.println("-------------------------------------------");
            jsonArr.put(i-1,jo);
            
        }
        jsonObj.put(arrayName, jsonArr);
       System.out.println(jsonObj.toString());
       return jsonObj.toString();
    }
    
    
    
}

/*
                if(mergedCell)
                   //get them taking ---> step forward count--->spansize
                   // i need to trs ,first tr(Header) 
                   for(int rs=0;rs<spansize;rs++){
                     //add 
                     // loop through <tds>
                     for (int j = 0; j < tds.size(); j++) {
                        key = trs.get(0).select("th").get(j).text();
                        value = tds.get(j).text();
                        jo.put(key, value);
                        System.out.println("-->" + key + ":" + value);
                     }
                    }
 */
