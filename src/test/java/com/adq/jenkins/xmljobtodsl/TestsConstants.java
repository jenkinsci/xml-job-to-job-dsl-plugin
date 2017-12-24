package com.adq.jenkins.xmljobtodsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestsConstants {

    public static final JobDescriptor getJobDescriptor() {
        List<PropertyDescriptor> propertiesProperties = new ArrayList<>();

        List<PropertyDescriptor> buildBlockProperties = new ArrayList<>();

        List<PropertyDescriptor> projectProperties = new ArrayList<>();
        PropertyDescriptor projectProperty = new PropertyDescriptor("project", null, projectProperties);

        projectProperties.add(new PropertyDescriptor("properties", projectProperty, propertiesProperties));

        List<PropertyDescriptor> buildersProperties = new ArrayList<>();

        PropertyDescriptor builder = new PropertyDescriptor("builders", projectProperty, buildersProperties);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("plugin", "build-name-setter@1.6.5");

        List<PropertyDescriptor> buildNameUpdaterProperties = new ArrayList<>();
        PropertyDescriptor buildName = new PropertyDescriptor("org.jenkinsci.plugins.buildnameupdater.BuildNameUpdater", builder,null, buildNameUpdaterProperties, attributes);

        buildNameUpdaterProperties.add(new PropertyDescriptor("macroTemplate", buildName,"Test iOS App #${BUILD_NUMBER} | ${APP_VERSION}"));
        buildNameUpdaterProperties.add(new PropertyDescriptor("fromFile", buildName,"false"));
        buildNameUpdaterProperties.add(new PropertyDescriptor("fromMacro", buildName,"true"));
        buildNameUpdaterProperties.add(new PropertyDescriptor("macroFirst", buildName,"true"));
        buildersProperties.add(buildName);

        List<PropertyDescriptor> shellProperties = new ArrayList<>();
        PropertyDescriptor shell = new PropertyDescriptor("hudson.tasks.Shell", builder, shellProperties);
        shellProperties.add(new PropertyDescriptor("command", shell, "export PLATFORM=${platform}\n" +
                "               cd 'iOSTest-AppiumTests/src/scripts/'\n" +
                "               fab -f build.py initialize_env run_appium run_tests kill_appium"));
        buildersProperties.add(shell);

        projectProperties.add(builder);

        PropertyDescriptor buildBlocker = new PropertyDescriptor("hudson.plugins.buildblocker.BuildBlockerProperty", builder, buildBlockProperties);

        buildBlockProperties.add(new PropertyDescriptor("useBuildBlocker", buildBlocker, "true"));
        buildBlockProperties.add(new PropertyDescriptor("blockingJobs", buildBlocker,"Build-iOS-App"));
        buildBlockProperties.add(new PropertyDescriptor("blockLevel", buildBlocker,"GLOBAL"));
        buildBlockProperties.add(new PropertyDescriptor("scanQueueFor", buildBlocker,"DISABLED"));

        propertiesProperties.add(buildBlocker);

        List<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(projectProperty);

        return new JobDescriptor("Test", properties);
    }

    public static final String getXml() {
        return "<project>\n" +
                "   <properties>\n" +
                "       <hudson.plugins.buildblocker.BuildBlockerProperty>\n" +
                "           <useBuildBlocker>true</useBuildBlocker>\n" +
                "           <blockingJobs>Build-iOS-App</blockingJobs>\n" +
                "           <blockLevel>GLOBAL</blockLevel>\n" +
                "           <scanQueueFor>DISABLED</scanQueueFor>\n" +
                "       </hudson.plugins.buildblocker.BuildBlockerProperty>\n" +
                "   </properties>\n" +
                "   <builders>\n" +
                "       <org.jenkinsci.plugins.buildnameupdater.BuildNameUpdater plugin=\"build-name-setter@1.6.5\">\n" +
                "           <macroTemplate>Test iOS App #${BUILD_NUMBER} | ${APP_VERSION}</macroTemplate>\n" +
                "           <fromFile>false</fromFile>\n" +
                "           <fromMacro>true</fromMacro>\n" +
                "           <macroFirst>true</macroFirst>\n" +
                "       </org.jenkinsci.plugins.buildnameupdater.BuildNameUpdater>\n" +
                "       <hudson.tasks.Shell>\n" +
                "           <command>export PLATFORM=${platform}\n" +
                "               cd 'iOSTest-AppiumTests/src/scripts/'\n" +
                "               fab -f build.py initialize_env run_appium run_tests kill_appium\n" +
                "           </command>\n" +
                "       </hudson.tasks.Shell>\n" +
                "   </builders>\n" +
                "</project>";
    }

    public static final String getDSL() {
        return "job(\"Test\") {\n" +
                "   blockOn(\"Build-iOS-App\", {\n" +
                "       blockLevel(\"GLOBAL\")\n" +
                "       scanQueueFor(\"DISABLED\")\n" +
                "   }\n" +
                ")\n" +
                "   steps {\n" +
                "       buildNameUpdater {\n" +
                "           macroTemplate(\"Test iOS App #${BUILD_NUMBER} | ${APP_VERSION}\")\n" +
                "           fromFile(false)\n" +
                "           fromMacro(true)\n" +
                "           macroFirst(true)\n" +
                "       }\n" +
                "       shell(\"\"\"export PLATFORM=${platform}\n" +
                "cd \"iOSTest-AppiumTests/src/scripts/\"\n" +
                "fab -f build.py fetch_git build_app upload_app\n" +
                "\"\"\")\n" +
                "   }\n" +
                "}\n";
    }
}
