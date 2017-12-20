package com.adq.jenkins.xmljobtodsl;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestsXmlParser {

    private String readExampleFile() {
        try {
            return new IOUtils().readFromResource("example1.xml");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testReadFile() {
        assertNotEquals("", readExampleFile());
    }

    @Test
    public void testParse() throws ParserConfigurationException, SAXException, IOException {
        String tag = "<actions>\n" +
                "   <description/>\n" +
                "   <keepDependencies>false</keepDependencies>\n" +
                "   <properties>\n" +
                "       <hudson.plugins.buildblocker.BuildBlockerProperty/>" +
                "   </properties>\n" +
                "</actions>";
        assertEquals("actions", new XmlParser(tag).parse().getProperties().get(0).getName());
    }

    @Test
    public void testParseFile() throws ParserConfigurationException, SAXException, IOException {
        String tag = readExampleFile();
        assertEquals("project", new XmlParser(tag).parse().getProperties().get(0).getName());
    }
}