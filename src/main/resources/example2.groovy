def String job_name_prefix = ''
if (binding.variables.JOB_NAME_PREFIX) {
    job_name_prefix = binding.variables.JOB_NAME_PREFIX
}

def String gitUrl = 'SymphonyOSF/iOSTest'
def String androidGitUrl = 'SymphonyOSF/SANDROID-CLIENT-APP'
def String iOSGitUrl = 'SymphonyOSF/SIOS-CLIENT-APP'
def String androidDefaultUrls = '10.240.50.18:4724,10.240.50.36:4724,10.240.50.37:4724'
def String iosDefaultUrls = '10.240.50.18:4723,10.240.50.36:4723,10.240.50.37:4723'

def String testsBlockingJobs = """${job_name_prefix}Build-iOS-App
${job_name_prefix}Build-Android-App
${job_name_prefix}Run-iOS-Tests
${job_name_prefix}Run-Android-Tests
"""
def String prBuilderBlockingJobs = """${job_name_prefix}DSL-iOS-PR-Builder
${job_name_prefix}DSL-Android-PR-Builder
${job_name_prefix}DSL-Mobile-Tests-PR-Builder
"""

def Closure buildNameStep(String macro) {
    return {
        buildName(null)
        fromFile(false)
        macroFirst(true)
        fromMacro(true)
        macroTemplate(macro)
    }
}

def Closure buildParams() {
    return {
        stringParam('BUILD_HOST', '10.240.50.18', '''The URL of the machine where the app will be built''')
        stringParam('BUILD_REMOTE_USER', 'jenkins', '''User of the slave machines to log in''')
        credentialsParam('BUILD_PASS_WORD') {
            defaultValue('JenkinsMachinePassword')
            type('org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl')
            description('Credential of type "Secret Text" with the password used to log in slave machines')
        }
    }
}

def Closure testParams(defaultUrls) {
    return {
        stringParam('REMOTE_USER', 'jenkins', '''User of the slave machines to log in''')
        credentialsParam('PASS_WORD') {
            defaultValue('JenkinsMachinePassword')
            type('org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl')
            description('Credential of type "Secret Text" with the password used to log in slave machines')
        }
        choiceParam('POD', ['cip1-qa', 'cip2-qa', 'cip3-qa', 'cip4-qa', 'qa4', 'qa3', 'qa5', 'qa6', 'qa10', 'qa12', 'qa20', 'qa22', 'qa24', 'qa26', 'qa27', 'warpdrive'], '')
        choiceParam('XPOD', ['cip3-qa', 'cip2-qa', 'cip1-qa', 'cip4-qa', 'qa4', 'qa3', 'qa5', 'qa6', 'qa10', 'qa12', 'qa20', 'qa22', 'qa24', 'qa26', 'qa27', 'warpdrive'], '')
        stringParam('STORIES', 'all', '')
        stringParam('TESTRAIL_RUN_ID', 'None', '''Use this to have the tests post their status into an existing Testrail run instead of creating a new one.
Enter the ID of the run into this field or leave it as "None" to have the tests create a new one.
E.g.: 692, 677, 123''')
        choiceParam('LOG_LEVEL', ['warn', 'debug', 'info'], 'This is the Appium log level')
        stringParam('URLS', defaultUrls, '''List of URL:PORT for all Appium\'s servers
Use ODD Ports for iOS Parallel Tests.
Use EVEN Ports for Android Parallel Tests.
127.0.0.1 for localhost.
Comma separated''')
        choiceParam('METAFILTER', ['none', 'smoke', 'sanity'], '')
        booleanParam('DONT_RESET_APP', false, 'Check this if you want to avoid resetting app between stories')
    }
}

def Closure commonParams() {
    return {
        stringParam('GIT_BRANCH', 'dev', 'Name of the branch that will be checked out from SymphonyOSF/iOSTest Repo')
        stringParam('SELECTED_BINARY', 'none', 'Local path of binary to run the tests against')
        booleanParam('REAL_DEVICE', false, 'Check this if you want to run on a plugged in device instead of the simulator\n' +
                'Make sure only one device is plugged.\n' +
                'UDID will be automatically fetched')
        stringParam('APP_VERSION', 'latest-dev', '''The version of the app used to run the tests against
* The default value "latest-qa" gets the latest github tag starting with "qa-" for the android app repo
* "latest-dev" gets the latest github tag starting with "dev-" from the app repo

Examples:
  latest-qa
  latest-dev
  qa-123
  dev-321

A list of available versions can be found here:
https://github.com/SymphonyOSF/SANDROID-CLIENT-APP/tags''')
    }
}

def Closure deployParams() {
    return {
        booleanParam('DEPLOY_TO_CRASHLYTICS', false, '''If everything runs fine, publish the current build to crashlytics''')
        stringParam('EMAILS', 'aquintiliano@symphony.com', '''The e-mail of users''')
        stringParam('LAST_SUCCESS_COMMIT', '', '''Commit hash of the last success build''')
    }
}

def Closure platformParam() {
    return {
        choiceParam('PLATFORM', ['Android', 'iOS'], 'Select the platform to test')
    }
}

def githubSettings = {
    git {
        remote {
            github(gitUrl, 'https')
            credentials('jenkins-mobile')
        }
        branch('*/${GIT_BRANCH}')
        extensions {
            wipeOutWorkspace()
        }
    }
}

def Closure prBuilderGithubSettings(githubUrl) {
    return {
        git {
            remote {
                github(githubUrl, 'https')
                credentials('jenkins-mobile')
            }
            branch('${ghprbActualCommit}')
            extensions {
                wipeOutWorkspace()
            }
        }
    }
}

def Closure buildSteps(buildName, platform) {
    return {
        buildNameUpdater buildNameStep(buildName)
        shell("""export PLATFORM=${platform}
cd 'iOSTest-AppiumTests/src/scripts/'
fab -f build.py fetch_git build_app upload_app
""")
    }
}

def Closure testSteps(buildName, platform) {
    return {
        buildNameUpdater buildNameStep(buildName)
        shell("""export PLATFORM=${platform}
cd 'iOSTest-AppiumTests/src/scripts/'
fab -f test.py initialize_env run_appium run_tests kill_appium
""")
    }
}

def Closure deploySteps(buildName, platform) {
    return {
        buildNameUpdater buildNameStep(buildName)
        shell("""export PLATFORM=${platform}
cd 'iOSTest-AppiumTests/src/scripts/'
fab -f deploy.py deploy
""")
    }
}

def Closure archiveArtifactories(path) {
    return {
        archiveArtifacts {
            pattern(path)
        }
    }
}

def Closure publishBuild(path) {
    return {
        archiveArtifacts {
            pattern(path)
        }
        richTextPublisher {
            stableText('''${FILE:iOSTest-AppiumTests/testrail_report.html}
${FILE:build_variables.html}''')
            unstableText('')
            failedText('')
            abortedText('')
            nullAction('')
            abortedAsStable(true)
            unstableAsStable(true)
            failedAsStable(true)
            parserName('HTML')
        }
    }
}

def blockJobs = {
    blockLevel('GLOBAL')
    scanQueueFor('DISABLED')
}

def Closure credentials() {
    return {
        credentialsBinding {
            string('PASSWORD', '${PASS_WORD}')
        }
    }
}

def Closure buildCredentials() {
    return {
        credentialsBinding {
            string('PASSWORD', '${BUILD_PASS_WORD}')
            usernamePassword('GITHUB_CREDENTIALS', 'jenkins-mobile')
        }
    }
}

def Closure envVars(platform) {
    return {
        env('PLATFORM', platform)
        keepBuildVariables(true)
    }
}

def Closure pipelineDefinition(githubSettings) {
    return {
        cpsScm {
            scm githubSettings
            scriptPath("iOSTest-AppiumTests/src/scripts/pipeline.groovy")
        }
    }
}

def Closure githubTrigger() {
    return {
        githubPullRequest {
            cron('H/5 * * * *')
            triggerPhrase('\\QJenkins, build this please\\E')
            permitAll()
        }
    }
}

def Closure parameterizedTrigger(pipelineName, defaultUrls, branch) {
    return {
        downstreamParameterized {
            trigger(pipelineName) {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
                parameters {
                    booleanParam('DEPLOY_TO_CRASHLYTICS', false)
                    booleanParam('REAL_DEVICE', false)
                    predefinedProps([EMAILS: 'giulliano.carnielli@symphony.com',
                                     REMOTE_USER: 'jenkins',
                                     POD: 'cip2-qa',
                                     XPOD: 'cip4-qa',
                                     STORIES: 'all',
                                     TESTRAIL_RUN_ID: 'None',
                                     LOG_LEVEL: 'warn',
                                     URLS: defaultUrls,
                                     METAFILTER: 'sanity',
                                     BUILD_HOST: '10.240.50.18',
                                     BUILD_REMOTE_USER: 'jenkins',
                                     GIT_BRANCH: 'dev',
                                     SELECTED_BINARY: 'none',
                                     APP_VERSION: branch])
                }
            }
        }
    }
}

def Closure epodStep() {
    return {
        shell('''if [ "${USE_WARPDRIVE}" == true ]; then
    curl -X POST 'https://warpdrive-lab.dev.symphony.com/jenkins/job/AUT-SFE-EPH/buildWithParameters?ENABLE_SELENIUM_TESTS=false&DEPLOYMENT_TIME_TO_LIVE=14400&token=build_ephemeral_pods\'
    sleep 3
    curl -X GET 'https://warpdrive-lab.dev.symphony.com/jenkins/job/AUT-SFE-EPH/lastBuild/buildNumber' > build-number.txt
else
    echo 'Do not use Epod, skipping\'
fi''')
    }
}

job("${job_name_prefix}Build-iOS-App") {
    logRotator(50)
    parameters commonParams() << buildParams()
    scm githubSettings
    steps buildSteps('Build iOS App #${BUILD_NUMBER} | ${APP_VERSION}', 'iOS')
    publishers publishBuild('build/**/*')
    wrappers buildCredentials()
}

job("${job_name_prefix}Build-Android-App") {
    logRotator(50)
    parameters commonParams() << buildParams()
    scm githubSettings
    steps buildSteps('Build Android App #${BUILD_NUMBER} | ${APP_VERSION}', 'Android')
    publishers publishBuild('build/**/*')
    wrappers buildCredentials()
}

job("${job_name_prefix}Run-iOS-Tests") {
    blockOn(testsBlockingJobs, blockJobs)
    logRotator(50)
    parameters commonParams() << testParams(iosDefaultUrls)
    scm githubSettings
    steps testSteps('Test iOS App #${BUILD_NUMBER} | ${APP_VERSION}', 'iOS')
    publishers publishBuild('iOSTest-AppiumTests/target/test-output/**/*')
    wrappers credentials()
}

job("${job_name_prefix}Run-Android-Tests") {
    blockOn(testsBlockingJobs, blockJobs)
    logRotator(50)
    parameters commonParams() << testParams(androidDefaultUrls)
    scm githubSettings
    steps testSteps('Test Android App #${BUILD_NUMBER} | ${APP_VERSION}', 'Android')
    publishers publishBuild('iOSTest-AppiumTests/target/test-output/**/*')
    wrappers credentials()
}

job("${job_name_prefix}Deploy-iOS-App") {
    blockOn(testsBlockingJobs, blockJobs)
    logRotator(50)
    parameters commonParams() << deployParams() << buildParams()
    scm githubSettings
    steps deploySteps('Deploy iOS App #${BUILD_NUMBER}', 'iOS')
    publishers publishBuild('artifactory/**/*')
    wrappers buildCredentials()
}

job("${job_name_prefix}Deploy-Android-App") {
    blockOn(testsBlockingJobs, blockJobs)
    logRotator(50)
    parameters commonParams() << deployParams() << buildParams()
    scm githubSettings
    steps deploySteps('Deploy Android App #${BUILD_NUMBER}', 'Android')
    publishers publishBuild('artifactory/**/*')
    wrappers buildCredentials()
}

pipelineJob("${job_name_prefix}Android-Pipeline") {
    displayName("${job_name_prefix}Android-Pipeline")
    parameters commonParams() << buildParams() << testParams(androidDefaultUrls) << deployParams()
    environmentVariables envVars('Android')
    definition pipelineDefinition(githubSettings)
    wrappers credentials() << buildCredentials()
    publishers publishBuild('artifactory/**/*')
}

pipelineJob("${job_name_prefix}iOS-Pipeline") {
    displayName("${job_name_prefix}iOS-Pipeline")
    parameters commonParams() << buildParams() << testParams(iosDefaultUrls) << deployParams()
    environmentVariables envVars('iOS')
    definition pipelineDefinition(githubSettings)
    wrappers credentials() << buildCredentials()
}

job("${job_name_prefix}DSL-Android-PR-Builder") {
    disabled()
    blockOn(testsBlockingJobs + prBuilderBlockingJobs, blockJobs)
    logRotator(50)
    scm prBuilderGithubSettings(androidGitUrl)
    triggers githubTrigger()
    steps parameterizedTrigger("${job_name_prefix}Android-Pipeline", androidDefaultUrls, '${ghprbActualCommit}')
}

job("${job_name_prefix}DSL-iOS-PR-Builder") {
    disabled()
    blockOn(testsBlockingJobs + prBuilderBlockingJobs, blockJobs)
    logRotator(50)
    scm prBuilderGithubSettings(iOSGitUrl)
    triggers githubTrigger()
    steps parameterizedTrigger("${job_name_prefix}iOS-Pipeline", iosDefaultUrls, '${ghprbActualCommit}')
}

job("${job_name_prefix}DSL-Mobile-Tests-PR-Builder") {
    disabled()
    blockOn(testsBlockingJobs + prBuilderBlockingJobs, blockJobs)
    logRotator(50)
    scm prBuilderGithubSettings(gitUrl)
    triggers githubTrigger()
    steps parameterizedTrigger('iOS-Pipeline, Android-Pipeline', iosDefaultUrls, 'latest-dev')
}

job("${job_name_prefix}Trigger-Epod-Run-On-Warpdrive") {
    parameters {
        booleanParam('USE_WARPDRIVE', false, 'Define if the job should trigger Epod warpdrive build')
    }
    logRotator(50)
    steps epodStep()
    publishers archiveArtifactories('build-number.txt')
}

listView("${job_name_prefix}MobileTest") {
    jobs {
        name("${job_name_prefix}Build-iOS-App")
        name("${job_name_prefix}Build-Android-App")
        name("${job_name_prefix}Run-iOS-Tests")
        name("${job_name_prefix}Run-Android-Tests")
        name("${job_name_prefix}Deploy-Android-App")
        name("${job_name_prefix}Deploy-iOS-App")
        name("${job_name_prefix}iOS-Pipeline")
        name("${job_name_prefix}Android-Pipeline")
        name("${job_name_prefix}DSL-Android-PR-Builder")
        name("${job_name_prefix}DSL-iOS-PR-Builder")
        name("${job_name_prefix}DSL-Mobile-Tests-PR-Builder")
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
