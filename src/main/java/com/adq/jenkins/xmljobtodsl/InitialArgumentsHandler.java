package com.adq.jenkins.xmljobtodsl;

import com.adq.jenkins.xmljobtodsl.parsers.DSLTranslator;
import com.adq.jenkins.xmljobtodsl.parsers.JobDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.XmlParser;
import com.adq.jenkins.xmljobtodsl.utils.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class InitialArgumentsHandler {

	private String input;
	private String output;
	private InputType inputType;

	private enum InputType {
		WEB, FILE
	}

	public InitialArgumentsHandler(String[] args) {
		if (args[0].equals("--file") || args[0].equals("-f")) {
			input = args[1];
			inputType = InputType.FILE;
		}
		if (args[0].equals("--url") || args[0].equals("-u")) {
			input = args[1];
			inputType = InputType.WEB;
		}
		if (args.length > 2 && (args[2].equals("--save-file") || args[2].equals("-s"))) {
			output = args[3];
		}
	}

	public void process() throws IOException, ParserConfigurationException, SAXException {
		String xml = null;
		String jobName = null;
		IOUtils ioUtils = new IOUtils();
		if (inputType == InputType.FILE) {
			String pattern = Pattern.quote(File.separator);
			String[] segments = new File(input).getAbsolutePath().split(pattern);
			jobName = segments[segments.length - 2];
			xml = ioUtils.readFromFile(input);
		}
		if (inputType == InputType.WEB) {
			String[] segments = input.split("/");
			jobName = segments[segments.length - 2];
			xml = ioUtils.readFromUrl(input);
		}
		JobDescriptor descriptor = new XmlParser(jobName, xml).parse();
		String dsl = new DSLTranslator(descriptor).toDSL();
		if (output != null) {
			ioUtils.saveToFile(dsl, output);
		} else {
			System.out.println(dsl);
		}
	}
}
