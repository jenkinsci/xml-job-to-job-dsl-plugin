job("test") {
	configure {
		it / 'properties' / 'jenkins.model.BuildDiscarderProperty' {
			strategy {
				'daysToKeep'('50')
				'numToKeep'('-1')
				'artifactDaysToKeep'('-1')
				'artifactNumToKeep'('-1')
			}
		}
		it / 'builders' / 'au.com.rayh.XCodeBuilder' {
			cleanBeforeBuild(true)
			cleanTestReports(false)
			configuration("Release")
			target()
			sdk()
			symRoot()
			buildDir()
			xcodeProjectPath("/Users/jenkins/jobs/job-name/workspace/Framework/FrameworkDemo")
			xcodeProjectFile()
			xcodebuildArguments()
			xcodeSchema("ios")
			xcodeWorkspaceFile("ios")
			cfBundleVersionValue("\${BUILD_NUMBER}")
			cfBundleShortVersionStringValue()
			buildIpa(false)
			ipaExportMethod("ad-hoc")
			generateArchive(true)
			unlockKeychain(true)
			keychainName("none (specify one below)")
			keychainPath("/Users/Shared/Jenkins/Library/Keychains/login.keychain")
			keychainPwd("App-Name")
			developmentTeamName("none (specify one below)")
			developmentTeamID("ASD1321ASD")
			allowFailingBuildResults(false)
			ipaName("\${MARKETING_VERSION}-\${VERSION}")
			ipaOutputDirectory()
			provideApplicationVersion(true)
			changeBundleID(false)
			bundleID()
			bundleIDInfoPlistPath()
			interpretTargetAsRegEx(false)
			ipaManifestPlistUrl()
		}
	}
}