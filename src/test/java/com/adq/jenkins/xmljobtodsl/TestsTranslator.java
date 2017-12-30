
package com.adq.jenkins.xmljobtodsl;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;

public class TestsTranslator {

    public static String readExampleFile() {
        try {
            return new IOUtils().readFromResource("example1.groovy");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testSimpleDSL() throws IOException {
        assertEquals(TestsConstants.getDSL(),
                new DSLTranslator(new JobDescriptor[] { TestsConstants.getJobDescriptor() }).toDSL());
    }

    @Test
    public void testReadingFile() throws IOException, ParserConfigurationException, SAXException {
        String xml = TestsXmlParser.readExampleFile();
        JobDescriptor actualJobDescriptor = new XmlParser("test", xml).parse();

        String expectedDSL = readExampleFile();

        assertEquals(expectedDSL,
                new DSLTranslator(new JobDescriptor[] { actualJobDescriptor }).toDSL());
    }
}