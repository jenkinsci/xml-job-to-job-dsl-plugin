job("test") {
	scm {
		git {
			remote {
				github("alandoni/xml-job-to-dsl", "https")
				credentials("jenkins")
			}
			branch('*/${GIT_BRANCH}')
			extensions {
				wipeOutWorkspace()
			}
		}
	}
}