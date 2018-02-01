package com.adq.jenkins.xmljobtodsl;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLParameterStrategy;
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
		Assert.assertEquals("alandoni/xml-job-to-dsl", repoInfo);

		repoInfo = strategy.getRepositoryInformationFromUrl("git@github.com:alandoni/xml-job-to-dsl.git");
		Assert.assertEquals("alandoni/xml-job-to-dsl", repoInfo);

		repoInfo = strategy.getRepositoryInformationFromUrl("https://www.github.com/alandoni/xml-job-to-dsl");
		Assert.assertEquals("alandoni/xml-job-to-dsl", repoInfo);

		repoInfo = strategy.getRepositoryInformationFromUrl("git@github.com:alandoni/xml-job-to-dsl");
		Assert.assertEquals("alandoni/xml-job-to-dsl", repoInfo);
	}

	@Test
	public void testPrintValueAccordingOfItsType() {
		DSLParameterStrategy strategy = new DSLParameterStrategy(null);

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

		actual = strategy.printValueAccordingOfItsType("10.240.50.36");
		Assert.assertEquals("\"10.240.50.36\"", actual);

		actual = strategy.printValueAccordingOfItsType("1.2.1");
		Assert.assertEquals("\"1.2.1\"", actual);

		actual = strategy.printValueAccordingOfItsType("1,2");
		Assert.assertEquals("\"1,2\"", actual);

		actual = strategy.printValueAccordingOfItsType("1 2");
		Assert.assertEquals("\"1 2\"", actual);

		actual = strategy.printValueAccordingOfItsType("test");
		Assert.assertEquals("\"test\"", actual);

		actual = strategy.printValueAccordingOfItsType("hey \"test\" it");
		Assert.assertEquals("\"hey \\\"test\\\" it\"", actual);

		actual = strategy.printValueAccordingOfItsType("test\ntest");
		Assert.assertEquals("\"\"\"test\ntest\"\"\"", actual);

		actual = strategy.printValueAccordingOfItsType("");
		Assert.assertEquals("\"\"", actual);

		actual = strategy.printValueAccordingOfItsType("test ${test}");
		Assert.assertEquals("\"test \\${test}\"", actual);

		actual = strategy.printValueAccordingOfItsType("test \n ${test}");
		Assert.assertEquals("\"\"\"test \n \\${test}\"\"\"", actual);

		actual = strategy.printValueAccordingOfItsType("$(echo \"test\")");
		Assert.assertEquals("\"\\$(echo \\\"test\\\")\"", actual);

		actual = strategy.printValueAccordingOfItsType("$(echo \"test\")\n$(echo \"test\")");
		Assert.assertEquals("\"\"\"\\$(echo \"test\")\n\\$(echo \"test\")\"\"\"", actual);

		actual = strategy.printValueAccordingOfItsType("origin/master&#xd;");
		Assert.assertEquals(String.format("\"origin/master&#xd;\""), actual);

		actual = strategy.printValueAccordingOfItsType("origin/$BRANCH");
		Assert.assertEquals(String.format("\"origin/\\$BRANCH\""), actual);

		actual = strategy.printValueAccordingOfItsType("test \"\"\" test \n test \"\"\" test");
		Assert.assertEquals("\"\"\"test \\\"\\\"\\\" test \n test \\\"\\\"\\\" test\"\"\"", actual);
	}
}
