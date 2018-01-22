job("test") {
	description("Build Android")
	keepDependencies(false)
	parameters {
		stringParam("branch", "master", "")
	}
	scm {
		git {
			remote {
				name("origin")
				github("alandoni/xml-job-to-dsl", "ssh")
			}
			branch("*/\${branch}")
		}
	}
	disabled(false)
	concurrentBuild(false)
	steps {
		shell("""touch app/fabric.properties
                echo "apiSecret=asd
                apiKey=asd" > app/fabric.properties

                cp app/fabric.properties messagingapp/fabric.properties

                tools/buildnotes.sh""")
		gradle {
			switches()
			tasks("clean assembleRelease")
			fromRootBuildScriptDir()
			buildFile()
			gradleName("(Default)")
			useWrapper(true)
			makeExecutable(true)
			useWorkspaceAsHome(false)
		}
		gradle {
			switches()
			tasks("crashlyticsUploadDistributionRelease")
			fromRootBuildScriptDir()
			buildFile()
			gradleName("(Default)")
			useWrapper(true)
			makeExecutable(true)
			useWorkspaceAsHome(false)
		}
	}
	publishers {
		archiveArtifacts {
			pattern("job/build/outputs/apk/release/*.apk")
			allowEmpty(false)
			onlyIfSuccessful(false)
			fingerprint(false)
			defaultExcludes(true)
		}
		mailer("alan_doni@hotmail.com", false, false)
	}
	wrappers {
		environmentVariables {
			env("ANDROID_HOME", "/Users/Shared/Jenkins/android-sdk-macosx/")
			env("KEYSTORE_LOCATION", "/Users/Shared/Jenkins/release.jks")
			env("KEYSTORE_PASSWORD", "devsonly")
			env("KEY_NAME", "androidDevs")
			env("KEY_PASSWORD", "androidDevsOnly")
			groovy()
			loadFilesFromMaster(false)
		}
	}
	configure {
		it / 'properties' / 'jenkins.model.BuildDiscarderProperty' {
			strategy {
				'daysToKeep'('-1')
				'numToKeep'('200')
				'artifactDaysToKeep'('-1')
				'artifactNumToKeep'('-1')
			}
		}
	}
}