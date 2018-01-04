
package com.adq.jenkins.xmljobtodsl;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;

public class TestsTranslator {

    public static String readFile(String name) {
        try {
            return new IOUtils().readFromResource(name).trim();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readExampleFile() {
        return readFile("example1.groovy");
    }

    @Test
    public void testSimpleDSL() throws IOException, TransformerException, SAXException, ParserConfigurationException {
        assertEquals(TestsConstants.getDSL(),
                new DSLTranslator(TestsConstants.getXml()).toDSL());
    }

    @Test
    public void testDSLWithArrays() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        String xml = readFile("array.xml");
        String expectedDSL = readFile("array.groovy");

        DSLTranslator dslTranslator = new DSLTranslator(xml);
        String actualDSL = dslTranslator.toDSL();

        assertEquals(expectedDSL, actualDSL);
    }

    @Test
    public void testDSLWithObjectAsParameter() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        String xml = readFile("object_parameter.xml");
        String expectedDSL = readFile("object_parameter.groovy");

        DSLTranslator dslTranslator = new DSLTranslator(xml);
        String actualDSL = dslTranslator.toDSL();

        assertEquals(expectedDSL, actualDSL);
    }

    @Test
    public void testDSLScm() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        String xml = readFile("scm.xml");
        String expectedDSL = readFile("scm.groovy");

        DSLTranslator dslTranslator = new DSLTranslator(xml);
        String actualDSL = dslTranslator.toDSL();

        assertEquals(expectedDSL, actualDSL);
    }

    @Test
    public void testReadingComplexFile() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        String xml = readFile("example1.xml");

        String expectedDSL = readExampleFile();

        DSLTranslator dslTranslator = new DSLTranslator(xml);
        String actualDSL = dslTranslator.toDSL();

        assertEquals(expectedDSL, actualDSL);
    }
}