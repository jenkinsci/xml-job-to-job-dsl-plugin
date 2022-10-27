package com.adq.jenkins.xmljobtodsl;

import com.adq.jenkins.xmljobtodsl.parsers.DSLTranslator;
import com.adq.jenkins.xmljobtodsl.parsers.JobDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.XmlParser;
import com.adq.jenkins.xmljobtodsl.utils.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class InitialArgumentsHandler {

	private String input;
	private String output;
	private InputType inputType;

	private String username;
	private String password;

	private List<PropertyDescriptor> unknownTags;

	private enum InputType {
		WEB, FILE, DIRECTORY
	}

	public InitialArgumentsHandler(String[] args) {
		if (args.length == 0) {
			throw new RuntimeException("Missing arguments, initialize with at least --file or --url option");
		}

		Map<String, String> argsMap = new HashMap<>();
		for (int i = 0; i < args.length; i++) {
			if (i % 2 == 0) {
				argsMap.put(args[i], args[i + 1]);
			}
		}

		if (argsMap.containsKey("--file") || argsMap.containsKey("-f")) {
			input = getValueForKeyOrAbbreviation(argsMap,"--file", "-f");
			inputType = InputType.FILE;
		}
		if (argsMap.containsKey("--url") || argsMap.containsKey("-u")) {
			input = getValueForKeyOrAbbreviation(argsMap, "--url", "-u");
			inputType = InputType.WEB;
		}
		if (argsMap.containsKey("--directory") || argsMap.containsKey("-d")) {
			input = getValueForKeyOrAbbreviation(argsMap, "--directory", "-d");
			inputType = InputType.DIRECTORY;
		}
		if (argsMap.containsKey("--save-file") || argsMap.containsKey("-s")) {
			output = getValueForKeyOrAbbreviation(argsMap, "--save-file", "-s");
		}
		if (argsMap.containsKey("--username") || argsMap.containsKey("-us")) {
			username = getValueForKeyOrAbbreviation(argsMap, "--username", "-us");
		}
		if (argsMap.containsKey("--password") || argsMap.containsKey("-p")) {
			password = getValueForKeyOrAbbreviation(argsMap, "--password", "-p");
		}
	}

	private String getValueForKeyOrAbbreviation(Map<String, String> map, String key, String abbreviationKey) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return map.get(abbreviationKey);
	}

	public void process() throws IOException, ParserConfigurationException, SAXException {
		String xml = null;
		String jobName = null;
		IOUtils ioUtils = new IOUtils();

		JobDescriptor[] descriptors = null;

		if (inputType == InputType.FILE) {
			File file = new File(input);
			File[] files = new File[1];
			files[0] = file;
			descriptors = getJobDescriptors(files);
		}

		if (inputType == InputType.WEB) {
			String[] segments = input.split("/");
			jobName = segments[segments.length - 2];
			xml = ioUtils.readFromUrl(input, username, password);
			JobDescriptor descriptor = new XmlParser(jobName, xml).parse();

			descriptors = new JobDescriptor[1];
			descriptors[0] = descriptor;
		}
		if (inputType == InputType.DIRECTORY) {
			File[] files = ioUtils.getJobXmlFilesInDirectory(input);
			descriptors = getJobDescriptors(files);
		}

		DSLTranslator translator = new DSLTranslator(descriptors);

		String dsl = translator.toDSL();
		if (output != null) {
			ioUtils.saveToFile(dsl, output);
		} else {
			System.out.println(dsl);
		}
		unknownTags = translator.getNotTranslated();
		System.out.println("\n\nWARNING:\nThe following tags couldn't be translated:");
		for (PropertyDescriptor property : unknownTags) {
			System.out.println(String.format("* %s", property.getName()));
		}
	}

	public static String getJobNameBasedOnPath(File file) {
		String pattern = File.separator;
		String[] segments = file.getAbsolutePath().split(Pattern.quote(pattern));
		return segments[segments.length - 2];
	}

	private JobDescriptor[] getJobDescriptors(File[] files)
			throws IOException, ParserConfigurationException, SAXException {
		IOUtils ioUtils = new IOUtils();

		List<JobDescriptor> descriptors = new ArrayList<>();

		for (File file : files) {
			String jobName = getJobNameBasedOnPath(file);
			String xml = ioUtils.readFromFile(file);
			JobDescriptor descriptor = new XmlParser(jobName, xml).parse();
			descriptors.add(descriptor);
		}
		return descriptors.toArray(new JobDescriptor[descriptors.size()]);
	}
}
