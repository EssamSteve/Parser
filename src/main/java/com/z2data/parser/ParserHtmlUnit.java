/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z2data.parser;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author essam.nasr
 */
public class ParserHtmlUnit {
    public static void main(String[] args) {
        try {
        // getFrameData("http://www.javatpoint.com/");
           getFrameData("http://allwebco-templates.com/support/S_script_IFrame.htm");
        } catch (IOException ex) {
            Logger.getLogger(ParserHtmlUnit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public static void getFrameData(String url) throws IOException {
         WebClient client=new WebClient();
         HtmlPage pageframe=client.getPage(url);
         List<FrameWindow> WindowFrameList=pageframe.getFrames();
         for (Iterator<FrameWindow> iterator = WindowFrameList.iterator(); iterator.hasNext();) {
             FrameWindow window=iterator.next();
             System.out.println("Window Datat:"+window.getFrameElement().getAttribute("src"));
             HtmlPage page =(HtmlPage)window.getEnclosedPage();
             System.out.println("Image data:>"+page.getByXPath("//div/img").get(0).toString());
             //System.out.println("Html iframe page data\n"+page.asXml());
             break;
         }
         
       //  System.out.println("***********************************************\n"+pageframe.getFrames().size());
    }
    
}
