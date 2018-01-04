package com.adq.jenkins.xmljobtodsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestsConstants {

    public static final String getXml() {
        return "<project>" + System.lineSeparator() +
                "\t<properties>" + System.lineSeparator() +
                "\t\t<hudson.plugins.buildblocker.BuildBlockerProperty>" + System.lineSeparator() +
                "\t\t\t<useBuildBlocker>true</useBuildBlocker>" + System.lineSeparator() +
                "\t\t\t<blockingJobs>Build-iOS-App</blockingJobs>" + System.lineSeparator() +
                "\t\t\t<blockLevel>GLOBAL</blockLevel>" + System.lineSeparator() +
                "\t\t\t<scanQueueFor>DISABLED</scanQueueFor>" + System.lineSeparator() +
                "\t\t</hudson.plugins.buildblocker.BuildBlockerProperty>" + System.lineSeparator() +
                "\t</properties>" + System.lineSeparator() +
                "\t<builders>" + System.lineSeparator() +
                "\t\t<org.jenkinsci.plugins.buildnameupdater.BuildNameUpdater plugin=\"build-name-setter@1.6.5\">" + System.lineSeparator() +
                "\t\t\t<macroTemplate>Test iOS App #${BUILD_NUMBER} | ${APP_VERSION}</macroTemplate>" + System.lineSeparator() +
                "\t\t\t<fromFile>false</fromFile>" + System.lineSeparator() +
                "\t\t\t<fromMacro>true</fromMacro>" + System.lineSeparator() +
                "\t\t\t<macroFirst>true</macroFirst>" + System.lineSeparator() +
                "\t\t</org.jenkinsci.plugins.buildnameupdater.BuildNameUpdater>" + System.lineSeparator() +
                "\t\t<hudson.tasks.Shell>" + System.lineSeparator() +
                "\t\t\t<command>export PLATFORM=${platform}" + System.lineSeparator() +
                "cd 'iOSTest-AppiumTests/src/scripts/'" + System.lineSeparator() +
                "fab -f build.py fetch_git build_app upload_app" + System.lineSeparator() +
                "\t\t\t</command>" + System.lineSeparator() +
                "\t\t</hudson.tasks.Shell>" + System.lineSeparator() +
                "\t</builders>" + System.lineSeparator() +
                "</project>";
    }

    public static final String getDSL() {
        return "job(\"Test\") {" + System.lineSeparator() +
                "\tblockOn(\"Build-iOS-App\", {" + System.lineSeparator() +
                "\t\tblockLevel(\"GLOBAL\")" + System.lineSeparator() +
                "\t\tscanQueueFor(\"DISABLED\")" + System.lineSeparator() +
                "\t})" + System.lineSeparator() +
                "\tsteps {" + System.lineSeparator() +
                "\t\tbuildNameUpdater {" + System.lineSeparator() +
                "\t\t\tmacroTemplate(\"Test iOS App #${BUILD_NUMBER} | ${APP_VERSION}\")" + System.lineSeparator() +
                "\t\t\tfromFile(false)" + System.lineSeparator() +
                "\t\t\tfromMacro(true)" + System.lineSeparator() +
                "\t\t\tmacroFirst(true)" + System.lineSeparator() +
                "\t\t}" + System.lineSeparator() +
                "\t\tshell(\"\"\"export PLATFORM=${platform}" + System.lineSeparator() +
                "cd 'iOSTest-AppiumTests/src/scripts/'" + System.lineSeparator() +
                "fab -f build.py fetch_git build_app upload_app\"\"\")" + System.lineSeparator() +
                "\t}" + System.lineSeparator() +
                "}";
    }
}
