package com.adq.jenkins.xmljobtodsl;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestsXmlParser {

    private static String readExampleFile() {
        return readFile("example1.xml");
    }

    public static String readFile(String name) {
        try {
            return new IOUtils().readFromResource(name).trim();
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
        String expected = TestsConstants.getJobDescriptor().toString();
        expected = expected.replaceAll("(\\\\r)", "");
        String actual = new XmlParser("Test", TestsConstants.getXml()).parse().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFile() throws ParserConfigurationException, SAXException, IOException {
        String tag = readExampleFile();
        JobDescriptor jobDescriptor = new XmlParser("Test", tag).parse();
        assertEquals("project", jobDescriptor.getProperties().get(0).getName());
    }
}