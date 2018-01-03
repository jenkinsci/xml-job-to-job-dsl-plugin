
package com.adq.jenkins.xmljobtodsl;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;

public class TestsTranslator {

    public static String readExampleFile() {
        return TestsXmlParser.readFile("example1.groovy");
    }

    @Test
    public void testSimpleDSL() throws IOException {
        assertEquals(TestsConstants.getDSL(),
                new DSLTranslator(TestsConstants.getJobDescriptor()).toDSL());
    }

    @Test
    public void testDSLWithArrays() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("array.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();
        String expectedDSL = TestsXmlParser.readFile("array.groovy");

        assertEquals(expectedDSL, new DSLTranslator(actualJobDescriptor).toDSL());
    }

    @Test
    public void testDSLWithObjectAsParameter() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("object_parameter.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();
        String expectedDSL = TestsXmlParser.readFile("object_parameter.groovy");

        assertEquals(expectedDSL, new DSLTranslator(actualJobDescriptor).toDSL());
    }

    @Test
    public void testDSLScm() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("scm.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();
        String expectedDSL = TestsXmlParser.readFile("scm.groovy");

        assertEquals(expectedDSL, new DSLTranslator(actualJobDescriptor).toDSL());
    }

    @Test
    public void testReadingComplexFile() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("example1.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();

        String expectedDSL = readExampleFile();

        assertEquals(expectedDSL, new DSLTranslator(actualJobDescriptor).toDSL());
    }
}