job("test") {
	description()
	keepDependencies(false)
	scm {
		git {
			remote {
				name("origin")
				github("alandony/xml-job-to-dsl", "ssh")
			}
			branch("*/master")
		}
	}
	disabled(true)
	triggers {
		scm("H/45 * * * *") {
			ignorePostCommitHooks(false)
		}
	}
	concurrentBuild(false)
	steps {
		shell("""touch app/fabric.properties
echo "apiSecret=asdasd
apiKey=asdasd" > app/fabric.properties

cp app/fabric.properties dsl/fabric.properties

echo "This build comes from the branch [\${GIT_BRANCH}]" > dsl/notes.txt

#disable release notes because they're constantly too long
LAST_SUCCESS_REV=\$(curl --silent http://localhost:8080/job/DSL/lastSuccessfulBuild/api/xml?xpath=//lastBuiltRevision/SHA1| sed 's|.*<SHA1>\\(.*\\)</SHA1>|\\1|')
git log --no-merges --format='%s [%cE]' \$LAST_SUCCESS_REV..\${GIT_COMMIT} >> dsl/notes.txt""")
		gradle {
			switches()
			tasks("clean assembleDebug crashlyticsUploadDistributionDebug")
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
			pattern("dsl/build/outputs/apk/*.apk")
			allowEmpty(false)
			onlyIfSuccessful(false)
			fingerprint(false)
			defaultExcludes(true)
		}
		mailer("alan_doni@hotmail.com", true, false)
	}
	wrappers {
		environmentVariables {
			env("ANDROID_HOME", "/Users/jenkins/android-sdk/")
			env("JAVA_HOME", "/Library/Java/JavaVirtualMachines/jdk1.8.0_05.jdk/Contents/Home")
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
		it / 'properties' / 'com.sonyericsson.rebuild.RebuildSettings' {
			'autoRebuild'('false')
			'rebuildDisabled'('false')
		}
	}
}