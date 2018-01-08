pipelineJob("test") {
	description("This builds app from pull requests")
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
		choiceParam("PLATFORM", ["Android", "iOS"], "Select the platform to test")
	}
	environmentVariables {
		env("PLATFORM", "iOS")
		env("VARIABLE", "value")
		keepBuildVariables(true)
	}
	disabled()
	displayName("Pipeline")
	steps {
		buildNameUpdater {
			buildName(null)
			macroTemplate("Build App")
			fromFile(false)
			fromMacro(true)
			macroFirst(true)
		}
		shell("""export PLATFORM=iOS
                cd 'xml-job-to-dsl/src/scripts/'
                ./run.sh""")
		downstreamParameterized {
			trigger("Pipeline") {
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
									 LOG_LEVEL: "warn",
									 GIT_BRANCH: "dev",
									 SELECTED_BINARY: "none"])
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
				name("origin")
				github("https://github.com/alandoni/xml-job-to-dsl.git", "https")
				credentials("jenkins")
			}
			branch("*/${GIT_BRANCH}")
			extensions {
				wipeOutWorkspace()
			}
		}
	}
	definition {
		cpsScm {
			scm {
				git {
					remote {
						github("https://github.com/alandoni/xml-job-to-dsl.git", "https")
						credentials("jenkins")
					}
					branch("*/${GIT_BRANCH}")
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
