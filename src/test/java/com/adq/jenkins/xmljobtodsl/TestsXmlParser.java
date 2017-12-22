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
        assertEquals(TestsConstants.getJobDescriptor().toString(),
                new XmlParser("Test", TestsConstants.getXml()).parse().toString());
    }

    @Test
    public void testParseFile() throws ParserConfigurationException, SAXException, IOException {
        String tag = readExampleFile();
        JobDescriptor jobDescriptor = new XmlParser("Test", tag).parse();
        assertEquals("project", jobDescriptor.getProperties().get(0).getName());
    }
}