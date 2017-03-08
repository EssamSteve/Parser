/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z2data.parser;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author essam.nasr
 */
public class Parser {
//    public static void main( String[] args ) throws IOException{  
//              //  getJsoupData("http://www.javatpoint.com/");
//               // getHtmlUnitData("http://www.javatpoint.com/");
//               // getHtmlUnitData("https://www.alibaba.com//Transformers_pid141907?spm=a2700.8270666-5.201612262000.83.eIKB34");
//              // getHtmlFormData("https://www.javatpoint.com/");
//             // getJsoupData("https://www.w3schools.com/xml/xml_xpath.asp");
//    }
    
    
     public static void getHtmlFormData(String url) throws IOException{
        WebClient client=new WebClient();
        HtmlPage page=client.getPage(url);
         System.out.println("*****Form ELEMENTS IN PAGE:\n************"+page.getByXPath("//form[@]").size());
    }
    
    
    
    public static void getJsoupData(String url) throws IOException{
        List<String> list=new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements rows=doc.select("table[style]");
        for (Element row:rows) {
              System.out.println(row.text());
              list.add(row.text());
        }
        addDataToFile(list,"D:\\Essam\\Projects\\Test\\mytext_jsoup.txt");
    }
    public static void getHtmlUnitData(String url) throws IOException{
        List<String> list=new ArrayList<>();
        List<HtmlAnchor> anchrList=new ArrayList<>();
        WebClient webClient = new WebClient();
	HtmlPage currentPage = webClient.getPage(url);
        anchrList=currentPage.getAnchors();
        for (Iterator<HtmlAnchor> iterator = anchrList.iterator(); iterator.hasNext();) {
            HtmlAnchor next = iterator.next();
            System.out.println("****************Ancohr List*********************\n"+next.asXml());
        }
     //   HtmlDivision div=(HtmlDivision)currentPage.getByXPath("//div[@id='uprf']");
        //div.asText();
        //System.out.println("******************"+div.asText());
//        HtmlTable table = (HtmlTable) currentPage.getByXPath("//table[not(@style)]").get(0);
//        for (HtmlTableRow row : table.getRows()) {
//            for ( HtmlTableCell cell : row.getCells()) {
//                list.add(cell.getTextContent());
//               // addDataToFile(cell.asText(),"D:\\Essam\\Projects\\Test\\mytext_htmlUnit.txt");
//            }
//        }
        
      //  addDataToFile(list,"D:\\Essam\\Projects\\Test\\mytext_htmlUnit.txt");
     // addDataToFile(currentPage.asXml(),"D:\\Essam\\Projects\\Test\\mytext_htmlUnit.txt");
    }
    public static void addDataToFile(List<String>data,String filePath) {
        try {
            for (Iterator <String>iterator = data.iterator(); iterator.hasNext();) {
                Files.write(Paths.get(filePath),iterator.next().getBytes(), StandardOpenOption.WRITE);
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
     public static void addDataToFile(String data,String filePath) {
        try {
          
                Files.write(Paths.get(filePath),data.getBytes(), StandardOpenOption.WRITE);
            
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
