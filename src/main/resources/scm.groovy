job("test") {
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
}