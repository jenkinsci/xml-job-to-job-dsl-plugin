package com.adq.jenkins.xmljobtodsl;

import com.adq.jenkins.xmljobtodsl.parsers.DSLView;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TestsDSLView {

	@Test
	public void testRealJobFile() throws IOException, ParserConfigurationException, SAXException {
		String expected = TestsXmlParser.readFile("view.groovy");
		String actual = new DSLView("test", new String[] { "test1", "test2" }).generateViewDSL();
		Assert.assertEquals(expected, actual);
	}
}
