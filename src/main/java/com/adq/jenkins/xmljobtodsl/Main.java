package com.adq.jenkins.xmljobtodsl;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by alanquintiliano on 20/12/17.
 */
public class Main {

    public static void main(String args[]) {
        try {
            new InitialArgumentsHandler(args).process();
        } catch (IOException e) {
            System.out.println("Couldn't find file");
            e.printStackTrace();
        } catch (ParserConfigurationException | SAXException e) {
            System.out.println("Couldn't parse XML file");
            e.printStackTrace();
        }
    }
}
