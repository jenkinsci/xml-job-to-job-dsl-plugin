package com.adq.jenkins.xmljobtodsl;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestsXmlParser {

    private String readExampleFile() {
        try {
            return new IOUtils().readFromResource("example1.xml");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testReadFile() {
        assertNotEquals("", readExampleFile());
    }

    @Test
    public void testParse() throws ParserConfigurationException, SAXException, IOException {
        String xml = "<project>\n" +
        "   <properties>\n" +
        "       <hudson.plugins.buildblocker.BuildBlockerProperty>" +
        "           <useBuildBlocker>true</useBuildBlocker>" +
        "           <blockingJobs>Build-iOS-App</blockingJobs>" +
        "           <blockLevel>GLOBAL</blockLevel>" +
        "           <scanQueueFor>DISABLED</scanQueueFor>" +
        "       </hudson.plugins.buildblocker.BuildBlockerProperty>" +
        "   </properties>\n" +
        "   <builders>\n" +
        "       <org.jenkinsci.plugins.buildnameupdater.BuildNameUpdater plugin=\"build-name-setter@1.6.5\">\n" +
        "           <macroTemplate>Test iOS App #${BUILD_NUMBER} | ${APP_VERSION}</macroTemplate>\n" +
        "           <fromFile>false</fromFile>\n" +
        "           <fromMacro>true</fromMacro>\n" +
        "           <macroFirst>true</macroFirst>\n" +
        "       </org.jenkinsci.plugins.buildnameupdater.BuildNameUpdater>\n" +
        "       <hudson.tasks.Shell>\n" +
        "           <command>export PLATFORM=iOS\n" +
        "               cd 'iOSTest-AppiumTests/src/scripts/'\n" +
        "               fab -f test.py initialize_env run_appium run_tests kill_appium\n" +
        "           </command>\n" +
        "       </hudson.tasks.Shell>\n" +
        "   </builders>\n" +
        "</project>";

        List<PropertyDescriptor> buildBlockProperties = new ArrayList<>();
        buildBlockProperties.add(new PropertyDescriptor("useBuildBlocker", "true"));
        buildBlockProperties.add(new PropertyDescriptor("blockingJobs", "Build-iOS-App"));
        buildBlockProperties.add(new PropertyDescriptor("blockLevel", "GLOBAL"));
        buildBlockProperties.add(new PropertyDescriptor("scanQueueFor", "DISABLED"));

        List<PropertyDescriptor> propertiesProperties = new ArrayList<>();        
        propertiesProperties.add(new PropertyDescriptor("hudson.plugins.buildblocker.BuildBlockerProperty", buildBlockProperties));

        
        List<PropertyDescriptor> buildNameUpdaterProperties = new ArrayList<>();
        buildNameUpdaterProperties.add(new PropertyDescriptor("macroTemplate", "Test iOS App #${BUILD_NUMBER} | ${APP_VERSION}"));
        buildNameUpdaterProperties.add(new PropertyDescriptor("fromFile", "false"));
        buildNameUpdaterProperties.add(new PropertyDescriptor("fromMacro", "true"));
        buildNameUpdaterProperties.add(new PropertyDescriptor("macroFirst", "true"));

        List<PropertyDescriptor> shellProperties = new ArrayList<>();
        shellProperties.add(new PropertyDescriptor("command", "export PLATFORM=iOS\n" +
        "cd 'iOSTest-AppiumTests/src/scripts/'\n" +
        "fab -f test.py initialize_env run_appium run_tests kill_appium\n"));

        List<PropertyDescriptor> buildersProperties = new ArrayList<>();
        buildersProperties.add(new PropertyDescriptor("org.jenkinsci.plugins.buildnameupdater.BuildNameUpdater", buildNameUpdaterProperties));
        buildersProperties.add(new PropertyDescriptor("hudson.tasks.Shell", shellProperties));


        List<PropertyDescriptor> projectProperties = new ArrayList<>();
        projectProperties.add(new PropertyDescriptor("builders", buildersProperties));

        List<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(new PropertyDescriptor("properties", projectProperties));

        JobDescriptor jobDescriptor = new JobDescriptor("Test", properties);
        
        assertEquals(jobDescriptor, new XmlParser(xml).parse());
    }

    @Test
    public void testParseFile() throws ParserConfigurationException, SAXException, IOException {
        String tag = readExampleFile();
        assertEquals("project", new XmlParser(tag).parse().getProperties().get(0).getName());
    }
}