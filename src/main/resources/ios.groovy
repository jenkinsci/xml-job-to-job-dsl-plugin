job("test") {
	description("""Builds a Crashlytics app from origin/master
""")
	keepDependencies(false)
	scm {
		git {
			remote {
				name("origin")
				github("alandoni/xml-job-to-dsl", "https")
				credentials("abc123")
			}
			branch("refs/heads/master")
			extensions {
				wipeOutWorkspace()
			}
		}
	}
	quietPeriod(5)
	disabled(false)
	triggers {
		scm("H/30 * * * *") {
			ignorePostCommitHooks(false)
		}
	}
	concurrentBuild(false)
	steps {
		shell("""#!/bin/bash -ex

env

YEAR=`date -j -f "%a %b %d %T %Z %Y" "\\`date\\`" "+%Y"`
MONTH=`date -j -f "%a %b %d %T %Z %Y" "\\`date\\`" "+%m"`
DAY=`date -j -f "%a %b %d %T %Z %Y" "\\`date\\`" "+%d"`
version=`/usr/libexec/PlistBuddy -c "Print CFBundleShortVersionString" "\${WORKSPACE}"/ios/ios-Info.plist`
bundleVersion=`/usr/libexec/PlistBuddy -c "Print CFBundleVersion" "\${WORKSPACE}"/ios/ios-Info.plist`
MARKETING_VERSION=\$version.\$bundleVersion

LAST_SUCCESS_REV=\$(curl --fail --silent http://localhost:8080/job/iOS-master/lastSuccessfulBuild/api/xml?xpath=//lastBuiltRevision/SHA1| sed 's|.*<SHA1>\\(.*\\)</SHA1>|\\1|')

# Store some environment variables in this file
# A later build step ingests these env variables
echo"">\${WORKSPACE}/env_vars
echo "MARKETING_VERSION"=\$MARKETING_VERSION>>\${WORKSPACE}/env_vars
# Capture the MM DD YY at the beginning of the build in case the date changes during the build. (a midnight build)
echo "YEAR"=\$YEAR>>\${WORKSPACE}/env_vars
echo "MONTH"=\$MONTH>>\${WORKSPACE}/env_vars
echo "DAY"=\$DAY>>\${WORKSPACE}/env_vars


echo "This build comes from the branch [\${GIT_BRANCH}]" > notes.txt
echo "------------------" >> notes.txt
echo "Marketing Version:" >> notes.txt
echo \$MARKETING_VERSION >> notes.txt
echo "------------------" >> notes.txt
echo "Commits:" >> notes.txt

# For what those %s & %cE mean, see https://git-scm.com/docs/git-log
# %s means subject (first line of commit)
# %cE means committer's email address
#git log --no-merges --format='%s [%cE]' \$LAST_SUCCESS_REV..\${GIT_COMMIT} >> notes.txt

# Below changes by Iwan:
# Change to %cN to show committer's name
# Get only the first 20 commits (avoid Crashlytics' 16KB hard limit on changelog when building a different branch from last succesful build)
git log --no-merges --format='%s [%cN]' --max-count=20 \$LAST_SUCCESS_REV..\${GIT_COMMIT} >> notes.txt

#echo "Server: " >> notes.txt
#echo URL="\$(cat "\${WORKSPACE}"/ios/Constants.h  | grep '^\\#define defaultUrlPrefix ' | cut -c 26- )" >> notes.txt


#Manually cleaning out the DerivedData directory
echo "Manually cleaning out the DerivedData directory"
echo "rm -rf /Users/jenkins/Library/Developer/Xcode/DerivedData"
rm -rf /Users/jenkins/Library/Developer/Xcode/DerivedData || true""")
		shell("""#!/bin/bash -ex

/usr/local/bin/pod install""")
		shell("ditto -c -k --keepParent -rsrc \${WORKSPACE}/Build/Intermediates/ArchiveIntermediates/ios/BuildProductsPath/Release-iphoneos/ios.app.dSYM \${WORKSPACE}/Build/Release-iphoneos/ios-master-\${MARKETING_VERSION}-\${BUILD_NUMBER}-dSYM.zip")
		shell("""echo "\${WORKSPACE}/iOS/Vendor/Crashlytics.framework/submit abc123 abc123 -ipaPath \${WORKSPACE}/Build/Release-iphoneos/*ipa -emails alan_doni@hotmail.com -notesPath \${WORKSPACE}/notes.txt -groupAliases Master -notifications YES"
\${WORKSPACE}/iOS/Vendor/Crashlytics.framework/submit abc123 abc123 -ipaPath \${WORKSPACE}/Build/Release-iphoneos/*ipa -emails alan_doni@hotmail.com -notesPath \${WORKSPACE}/notes.txt -groupAliases Master -notifications YES


# abc123
echo find \${WORKSPACE}/Build/Intermediates/ArchiveIntermediates/ios/BuildProductsPath/Release-iphoneos -name "*.dSYM" -execdir \${WORKSPACE}/iOS/Vendor/upload-symbols -p ios -a abc123 {} \\;
find \${WORKSPACE}/Build/Intermediates/ArchiveIntermediates/ios/BuildProductsPath/Release-iphoneos -name "*.dSYM" -execdir \${WORKSPACE}/iOS/Vendor/upload-symbols -p ios -a abc123 {} \\;""")
		shell("""#cp -v \${WORKSPACE}/Build/*.ipa \$DROPBOX_DIR
#cp -v \${WORKSPACE}/Build/*.zip \$DROPBOX_DIR

#cp -v \${WORKSPACE}/Build/*.ipa \$DROPBOX_DIR""")
	}
	publishers {
		archiveArtifacts {
			pattern("Build/Release-iphoneos/*zip,Build/Release-iphoneos/*ipa")
			allowEmpty(false)
			onlyIfSuccessful(false)
			fingerprint(false)
			defaultExcludes(true)
		}
		githubCommitNotifier()
		postBuildScripts {
			steps {
				steps {
					shell("""git tag "ios-master-\$MARKETING_VERSION-\$BUILD_NUMBER"
git push origin --tags""")
				}
			}
			markBuildUnstable(false)
			onlyIfBuildSucceeds(true)
			onlyIfBuildFails(false)
			markBuildUnstable(false)
		}
		mailer("alan_doni@hotmail.com", false, true)
	}
	wrappers {
		timeout {
			absolute(30)
		}
		timestamps()
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
		it / 'properties' / 'com.coravy.hudson.plugins.github.GithubProjectProperty' {
			'projectUrl'('https://github.com/alandoni/xml-job-to-dsl/')
			displayName()
		}
		it / 'properties' / 'com.sonyericsson.rebuild.RebuildSettings' {
			'autoRebuild'('false')
			'rebuildDisabled'('false')
		}
		it / 'builders' / 'au.com.rayh.XCodeBuilder' {
			cleanBeforeBuild(true)
			cleanTestReports(false)
			configuration("Release")
			target()
			sdk()
			symRoot()
			buildDir()
			xcodeProjectPath()
			xcodeProjectFile()
			xcodebuildArguments()
			xcodeSchema("ios")
			xcodeWorkspaceFile("ios")
			cfBundleVersionValue("\${BUILD_NUMBER}")
			cfBundleShortVersionStringValue()
			buildIpa(true)
			ipaExportMethod("ad-hoc")
			generateArchive(true)
			unlockKeychain(true)
			keychainName("none (specify one below)")
			keychainPath("/Users/jenkins/Library/Keychains/login.keychain")
			keychainPwd("abc123")
			developmentTeamName("none (specify one below)")
			developmentTeamID("abc123")
			allowFailingBuildResults(false)
			ipaName("ios-master-\${MARKETING_VERSION}-\${BUILD_NUMBER}")
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