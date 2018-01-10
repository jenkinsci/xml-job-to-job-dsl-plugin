
package com.adq.jenkins.xmljobtodsl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adq.jenkins.xmljobtodsl.parsers.DSLTranslator;
import com.adq.jenkins.xmljobtodsl.parsers.JobDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.XmlParser;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;

public class TestsTranslator {

    public static String readExampleFile() {
        return TestsXmlParser.readFile("example-job.groovy");
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
    public void testReadingConfigureBlockFile() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("configure-block.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();

        String expectedDSL = TestsXmlParser.readFile("configure-block.groovy");

        DSLTranslator dslTranslator = new DSLTranslator(actualJobDescriptor);
        String actualDSL = dslTranslator.toDSL();
        logAllNotKnownTags(dslTranslator.getNotTranslated());

        assertEquals(expectedDSL, actualDSL);
    }

    @Test
    public void testReadingJobFile() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("example-job.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();

        String expectedDSL = readExampleFile();

        DSLTranslator dslTranslator = new DSLTranslator(actualJobDescriptor);
        String actualDSL = dslTranslator.toDSL();
        logAllNotKnownTags(dslTranslator.getNotTranslated());

        assertEquals(expectedDSL, actualDSL);
    }

    @Test
    public void testReadingPipelineJobFile() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile("example-pipelinejob.xml");
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();

        String expectedDSL = TestsXmlParser.readFile("example-pipelinejob.groovy");

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