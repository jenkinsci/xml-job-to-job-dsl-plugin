
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

    private void readFilesAndTest(String xmlFile, String groovyFile) throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readFile(xmlFile);
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();
        String expectedDSL = TestsXmlParser.readFile(groovyFile);

        DSLTranslator dslTranslator = new DSLTranslator(actualJobDescriptor);
        String actualDSL = dslTranslator.toDSL();
        logAllNotKnownTags(dslTranslator.getNotTranslated());

        assertEquals(expectedDSL, actualDSL);
    }

    @Test
    public void testDSLWithArrays() throws IOException, ParserConfigurationException, SAXException {
        readFilesAndTest("array.xml", "array.groovy");
    }

    @Test
    public void testDSLWithObjectAsParameter() throws IOException, ParserConfigurationException, SAXException {
        readFilesAndTest("object_parameter.xml", "object_parameter.groovy");
    }

    @Test
    public void testDSLScm() throws IOException, ParserConfigurationException, SAXException {
        readFilesAndTest("scm.xml", "scm.groovy");
    }

    @Test
    public void testDSLDescription() throws IOException, ParserConfigurationException, SAXException {
        readFilesAndTest("description.xml", "description.groovy");
    }

    @Test
    public void testReadingConfigureBlockFile() throws IOException, ParserConfigurationException, SAXException {
        readFilesAndTest("configure-block.xml","configure-block.groovy");
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

        Logger.getAnonymousLogger().log(Level.INFO, actualDSL);
    }

    @Test
    public void testReadingPipelineJobFile() throws IOException, ParserConfigurationException, SAXException {
        readFilesAndTest("example-pipelinejob.xml","example-pipelinejob.groovy");
    }

    @Test
    public void testRealJobFile() throws IOException, ParserConfigurationException, SAXException {
        readFilesAndTest("config2.xml","config2.groovy");
    }

    @Test
    public void testRealJobFile2() throws IOException, ParserConfigurationException, SAXException {
        readFilesAndTest("ios.xml", "ios.groovy");
    }

    private void logAllNotKnownTags(List<PropertyDescriptor> notKnownTags) {
        for (PropertyDescriptor property : notKnownTags) {
            Logger.getAnonymousLogger().log(Level.WARNING, property.getName());
        }
    }
}