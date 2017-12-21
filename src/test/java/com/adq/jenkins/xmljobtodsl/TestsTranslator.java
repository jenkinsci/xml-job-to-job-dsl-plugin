
package com.adq.jenkins.xmljobtodsl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestsTranslator {

    @Test
    public void testReadFile() throws IOException {
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

        String dsl = "job(\"Test\") {\n" +
                    "   blockOn(\"Build-iOS-App\", {\n" +
                    "       blockLevel('GLOBAL')\n" +
                    "       scanQueueFor('DISABLED')\n" +
                    "   }\n" +
                    "   steps {\n" +
                    "       buildNameUpdater {\n" +
                    "           fromFile(false)\n" +
                    "           macroFirst(true)\n" +
                    "           fromMacro(true)\n" +
                    "           macroTemplate(\"Test iOS App #${BUILD_NUMBER} | ${APP_VERSION}\")\n" +
                    "       }\n" +
                    "       shell(\"\"\"export PLATFORM=${platform}\n" +
                    "cd 'iOSTest-AppiumTests/src/scripts/'\n" +
                    "fab -f build.py fetch_git build_app upload_app\n" + 
                    "\"\"\")\n" +
                    "   }\n" +
                    "}\n";

        assertEquals(dsl, new Translator(new JobDescriptor[] { jobDescriptor }).toDSL());
    }
}