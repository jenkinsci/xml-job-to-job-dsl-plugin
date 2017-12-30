job("test") {
    blockOn("""Build-iOS-App
Build-Android-App
Run-iOS-Tests
Run-Android-Tests""", {
        blockLevel('GLOBAL')
        scanQueueFor('DISABLED')
    })
    parameters {
        stringParam('GIT_BRANCH', 'dev', 'Name of the branch that will be checked out from SymphonyOSF/iOSTest Repo')
        stringParam('SELECTED_BINARY', 'none', 'Local path of binary to run the tests against')
        booleanParam('REAL_DEVICE', false, 'Check this if you want to run on a plugged in device instead of the simulator\n' +
'Make sure only one device is plugged.\n' +
'UDID will be automatically fetched')
        choiceParam('PLATFORM', ['Android', 'iOS'], 'Select the platform to test')
    }
    disabled()
    steps {
        buildNameUpdater {
            buildName(null)
            fromFile(false)
            macroFirst(true)
            fromMacro(true)
            macroTemplate('Build iOS App #${BUILD_NUMBER} | ${APP_VERSION}')
        }
        shell("""export PLATFORM=iOS
cd 'iOSTest-AppiumTests/src/scripts/'
fab -f build.py fetch_git build_app upload_app
""")
        downstreamParameterized {
            trigger("${job_name_prefix}Android-Pipeline") {
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
                                     URLS: '123.123.123.123',
                                     METAFILTER: 'sanity',
                                     BUILD_HOST: '10.240.50.18',
                                     BUILD_REMOTE_USER: 'jenkins',
                                     GIT_BRANCH: 'dev',
                                     SELECTED_BINARY: 'none',
                                     APP_VERSION: '${ghprbActualCommit}'])
                }
            }
        }
    }
    publishers {
        archiveArtifacts {
            pattern('build/**/*')
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
    wrappers {
        credentialsBinding {
            string('PASSWORD', '${BUILD_PASS_WORD}')
            usernamePassword('GITHUB_CREDENTIALS', 'jenkins-mobile')
        }
    }
    logRotator(50)
    scm {
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
    displayName("${job_name_prefix}iOS-Pipeline")
    environmentVariables {
        env('PLATFORM', 'iOS')
        keepBuildVariables(true)
    }
    definition {
        cpsScm {
            scm githubSettings
            scriptPath("iOSTest-AppiumTests/src/scripts/pipeline.groovy")
        }
    }
    triggers {
        githubPullRequest {
            cron('H/5 * * * *')
            triggerPhrase('\\QJenkins, build this please\\E')
            permitAll()
        }
    }
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
