package com.adq.jenkins.xmljobtodsl;

import com.adq.jenkins.xmljobtodsl.parsers.DSLView;
import org.junit.Assert;
import org.junit.Test;

public class TestsDSLView {

	@Test
	public void testViewFile() {
		String expected = TestsXmlParser.readFile("view.groovy");
		String actual = new DSLView("test", new String[] { "test1", "test2" }).generateViewDSL();
		Assert.assertEquals(expected, actual);
	}
}
