job("test") {
	blockOn("""Build-iOS-App
			Build-Android-App
			Run-iOS-Tests
			Run-Android-Tests""", {
		blockLevel("GLOBAL")
		scanQueueFor("DISABLED")
	})
	parameters {
		stringParam("GIT_BRANCH", "dev", "Name of the branch that will be checked out from repo")
		stringParam("SELECTED_BINARY", "none", "Local path of binary to run the tests against")
		booleanParam("REAL_DEVICE", false, """Check this if you want to run on a plugged in device instead of the simulator
						Make sure only one device is plugged.
						UDID will be automatically fetched""")
		choiceParam(["Android", "iOS"], "PLATFORM", "Select the platform to test")
	}
	disabled()
	steps {
		buildNameUpdater {
            macroTemplate("Build App")
			fromFile(false)
            fromMacro(true)
			macroFirst(true)
		}
		shell("""export PLATFORM=iOS
                cd 'xml-job-to-dsl/src/scripts/'
                ./run.sh""")
		downstreamParameterized {
			trigger("iOS-Pipeline") {
				block {
					buildStepFailure("FAILURE")
					failure("FAILURE")
					unstable("UNSTABLE")
				}
				parameters {
					booleanParam("DEPLOY_TO_CRASHLYTICS", false)
					booleanParam("REAL_DEVICE", false)
					predefinedProps([EMAILS: "alan_doni@hotmail.com",
									 REMOTE_USER: "jenkins",
									 STORIES: "all",
									 LOG_LEVEL: "warn",
									 URLS: "123.123.123.123",
									 METAFILTER: "sanity",
									 BUILD_HOST: "192.168.0.1",
									 BUILD_REMOTE_USER: "jenkins",
									 GIT_BRANCH: "master",
									 SELECTED_BINARY: "none",
									 APP_VERSION: "${ghprbActualCommit}"])
				}
			}
		}
	}
	publishers {
		archiveArtifacts {
			pattern("build/**/*")
		}
		richTextPublisher {
			stableText("""${FILE:xml-job-to-dsl/tests_report.html}
				${FILE:build_variables.html}""")
			unstableText("")
			failedText("")
			abortedText("")
			nullAction("")
			unstableAsStable(true)
			failedAsStable(true)
			abortedAsStable(true)
			parserName("HTML")
		}
	}
	wrappers {
		credentialsBinding {
			string("PASSWORD", "${PASS_WORD}")
			usernamePassword("GITHUB_CREDENTIALS", "jenkins")
		}
	}
	logRotator(50)
	scm {
		git {
			remote {
				github("https://github.com/alandoni/xml-job-to-dsl", "https")
				credentials("jenkins")
			}
			branch("*/${GIT_BRANCH}")
			extensions {
				wipeOutWorkspace()
			}
		}
	}
	displayName("iOS-Pipeline")
	environmentVariables {
		env("PLATFORM", "iOS")
		keepBuildVariables(true)
	}
	definition {
		cpsScm {
			scm {
				git {
					remote {
						github("https://github.com/alandoni/xml-job-to-dsl", "https")
						credentials("jenkins")
					}
					branch("${ghprbActualCommit}")
					extensions {
						wipeOutWorkspace()
					}
				}
			}
			scriptPath("xml-job-to-dsl/src/scripts/pipeline.groovy")
		}
	}
	triggers {
		githubPullRequest {
			cron("H/5 * * * *")
			triggerPhrase("\\QJenkins, build this please\\E")
			permitAll()
		}
	}
}

listView("ViewTests") {
	jobs {
		name("Build-iOS-App")
		name("Build-Android-App")
		name("Run-iOS-Tests")
		name("Run-Android-Tests")
		name("Deploy-Android-App")
		name("Deploy-iOS-App")
		name("iOS-Pipeline")
		name("Android-Pipeline")
		name("DSL-Android-PR-Builder")
		name("DSL-iOS-PR-Builder")
		name("DSL-Mobile-Tests-PR-Builder")
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
