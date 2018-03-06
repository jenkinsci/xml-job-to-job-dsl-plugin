package com.adq.jenkins.xmljobtodsl.jenkins;

import com.adq.jenkins.xmljobtodsl.parsers.DSLTranslator;
import com.adq.jenkins.xmljobtodsl.parsers.JobDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.parsers.XmlParser;
import com.adq.jenkins.xmljobtodsl.utils.IOUtils;
import hudson.model.AbstractProject;
import hudson.model.Job;
import jenkins.model.Jenkins;
import org.apache.commons.lang.StringEscapeUtils;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JobCollector {

	public static final String DSL_GROOVY = "dsl.groovy";
	public static final String USER_CONTENT_FOLDER = "userContent";
	private final List<Job> allItems;
	private final List<Job> selectedItems = new ArrayList<>();

	private String parsedItems = null;
	private String nonTranslatedTags = null;
	private String dslFilePath = null;
	private String error = null;

	public JobCollector() {
		allItems = Jenkins.getInstance().getAllItems(Job.class);
	}

	public List<Job> getJobs() {
		return allItems;
	}

	public List<Job> getSelectedJobs() {
		return selectedItems;
	}

	@JavaScriptMethod
	public int getNumberOfJobs() {
		return allItems.size();
	}

	@JavaScriptMethod
	public int getNumberOfSelectedJobs() {
		return selectedItems.size();
	}

	public void add(Job job) {
		selectedItems.add(job);
	}

	@JavaScriptMethod
	public void add(int jobIndex) {
		add(allItems.get(jobIndex));
	}

	public void remove(Job job) {
		selectedItems.remove(job);
	}

	@JavaScriptMethod
	public void remove(int jobIndex) {
		remove(allItems.get(jobIndex));
	}

	@JavaScriptMethod
	public String getParsedItems() {
		return formatString(parsedItems);
	}

	@JavaScriptMethod
	public String getNonTranslatedTags() {
		return nonTranslatedTags;
	}

	@JavaScriptMethod
	public String getError() {
		return error;
	}

	@JavaScriptMethod
	public String getDslFilePath() {
		return dslFilePath;
	}

	@JavaScriptMethod
	public boolean startOperation(String viewName) {
		try {
			convertXmlToDSL(viewName);
			saveDSLtoFile();
			return true;
		} catch (IOException iosException) {
			error = "Error getting XML configuration of the job: " + iosException.getLocalizedMessage() + ": " + selectedItems.get(0).getConfigFile().getFile().getParentFile().getParentFile().getParentFile().getAbsolutePath() + File.separator + "userContent" + File.separator + File.separator + DSL_GROOVY;
		} catch (SAXException | ParserConfigurationException xmlException) {
			error = "Error parsing XML configuration of the job to DSL";
		}
		return false;
	}

	private void convertXmlToDSL(String viewName) throws IOException, SAXException, ParserConfigurationException {
		List<JobDescriptor> jobDescriptorsList = new ArrayList<>();
		for (Job job : selectedItems) {
			String xml = job.getConfigFile().asString();
			XmlParser parser = new XmlParser(job.getDisplayName(), xml);
			JobDescriptor descriptor = parser.parse();
			jobDescriptorsList.add(descriptor);
		}
		JobDescriptor[] jobDescriptors = jobDescriptorsList.toArray(new JobDescriptor[jobDescriptorsList.size()]);
		DSLTranslator translator = new DSLTranslator(jobDescriptors, viewName);
		parsedItems = translator.toDSL();
		nonTranslatedTags = formatNonTranslated(translator.getNotTranslated());
	}

	private void saveDSLtoFile() throws IOException {
		File workspacePath = Jenkins.getInstance().getRootDir();
		dslFilePath = ".." + File.separator + USER_CONTENT_FOLDER + File.separator + DSL_GROOVY;
		String file = workspacePath.getAbsolutePath() + File.separator + USER_CONTENT_FOLDER + File.separator + DSL_GROOVY;
		new IOUtils().saveToFile(parsedItems, file);
	}

	public String formatNonTranslated(List<PropertyDescriptor> nonTranslated) {
		StringBuilder builder = new StringBuilder("The following tags couldn't be translated to DSL:");
		builder.append("<ul>");
		for (PropertyDescriptor descriptor : nonTranslated) {
			builder.append("<li>");
			builder.append(formatString(descriptor.getName()));
			builder.append("</li>");
		}
		builder.append("</ul>");
		builder.append("<br />");
		builder.append("If you need some of them to be translated, please create an issue on our ");
		builder.append("<a href=\"https://github.com/alandoni/xml-job-to-dsl\">GitHub</a>.<br />");
		return builder.toString();
	}

	public String formatString(String text) {
		return StringEscapeUtils.escapeHtml(text);
	}
}
