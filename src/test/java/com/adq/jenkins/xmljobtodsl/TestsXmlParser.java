package com.adq.jenkins.xmljobtodsl;

import com.adq.jenkins.xmljobtodsl.parsers.JobDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.XmlParser;
import com.adq.jenkins.xmljobtodsl.utils.IOUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestsXmlParser {

    private static String readExampleFile() {
        return readFile("example-job.xml");
    }

    public static String readFile(String name) {
        try {
            return new IOUtils().readFromResource(name).trim();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file: " + name);
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