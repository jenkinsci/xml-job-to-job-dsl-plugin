
package com.adq.jenkins.xmljobtodsl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        DSLTranslator dslTranslator = new DSLTranslator(actualJobDescriptor);
        String actualDSL = dslTranslator.toDSL();
        logAllNotKnownTags(dslTranslator.getNotTranslated());

        assertEquals(expectedDSL, actualDSL);
    }

    @Test
    public void testDSLWithObjectAsParameter() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("object_parameter.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();
        String expectedDSL = TestsXmlParser.readFile("object_parameter.groovy");

        DSLTranslator dslTranslator = new DSLTranslator(actualJobDescriptor);
        String actualDSL = dslTranslator.toDSL();
        logAllNotKnownTags(dslTranslator.getNotTranslated());

        assertEquals(expectedDSL, actualDSL);
    }

    @Test
    public void testDSLScm() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("scm.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();
        String expectedDSL = TestsXmlParser.readFile("scm.groovy");

        DSLTranslator dslTranslator = new DSLTranslator(actualJobDescriptor);
        String actualDSL = dslTranslator.toDSL();
        logAllNotKnownTags(dslTranslator.getNotTranslated());

        assertEquals(expectedDSL, actualDSL);
    }

    @Test
    public void testReadingComplexFile() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("example1.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();

        String expectedDSL = readExampleFile();

        DSLTranslator dslTranslator = new DSLTranslator(actualJobDescriptor);
        String actualDSL = dslTranslator.toDSL();
        logAllNotKnownTags(dslTranslator.getNotTranslated());

        assertEquals(expectedDSL, actualDSL);
    }

    private void logAllNotKnownTags(List<PropertyDescriptor> notKnownTags) {
        for (PropertyDescriptor property : notKnownTags) {
            Logger.getAnonymousLogger().log(Level.WARNING, property.getName());
        }
    }
}