package com.adq.jenkins.xmljobtodsl;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLValueStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.custom.DSLGitHubMethodStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import org.junit.Assert;
import org.junit.Test;

public class DSLStrategiesTests {

	@Test
	public void testGitHubRepositoryInformation() {
		PropertyDescriptor descriptor = new PropertyDescriptor("git", null, "https");
		DSLGitHubMethodStrategy strategy = new DSLGitHubMethodStrategy(0, descriptor,  null);

		String repoInfo = strategy.getRepositoryInformationFromUrl("https://www.github.com/alandoni/xml-job-to-dsl.git");
		Assert.assertEquals(repoInfo, "alandoni/xml-job-to-dsl");

		repoInfo = strategy.getRepositoryInformationFromUrl("git@github.com:alandoni/xml-job-to-dsl.git");
		Assert.assertEquals(repoInfo, "alandoni/xml-job-to-dsl");
	}

	@Test
	public void testPrintValueAccordingOfItsType() {
		DSLValueStrategy strategy = new DSLValueStrategy(null);

		String actual = strategy.printValueAccordingOfItsType(null);
		Assert.assertEquals("\"\"", actual);

		actual = strategy.printValueAccordingOfItsType("true");
		Assert.assertEquals("true", actual);

		actual = strategy.printValueAccordingOfItsType("false");
		Assert.assertEquals("false", actual);

		actual = strategy.printValueAccordingOfItsType("12");
		Assert.assertEquals("12", actual);

		actual = strategy.printValueAccordingOfItsType("1.2");
		Assert.assertEquals("1.2", actual);

		actual = strategy.printValueAccordingOfItsType("1,2");
		Assert.assertEquals("\"1,2\"", actual);

		actual = strategy.printValueAccordingOfItsType("1 2");
		Assert.assertEquals("\"1 2\"", actual);

		actual = strategy.printValueAccordingOfItsType("test");
		Assert.assertEquals("\"test\"", actual);

		actual = strategy.printValueAccordingOfItsType("test\ntest");
		Assert.assertEquals("\"\"\"test\ntest\"\"\"", actual);

		actual = strategy.printValueAccordingOfItsType("");
		Assert.assertEquals("\"\"", actual);

		actual = strategy.printValueAccordingOfItsType("test ${test}");
		Assert.assertEquals("'test ${test}'", actual);

		actual = strategy.printValueAccordingOfItsType("test \n ${test}");
		Assert.assertEquals("\"\"\"test \n ${test}\"\"\"", actual);

		actual = strategy.printValueAccordingOfItsType("$(echo \"test\")");
		Assert.assertEquals("\"\\$(echo \"test\")\"", actual);

		actual = strategy.printValueAccordingOfItsType("$(echo \"test\")\n$(echo \"test\")");
		Assert.assertEquals("\"\"\"\\$(echo \"test\")\n\\$(echo \"test\")\"\"\"", actual);
	}
}
