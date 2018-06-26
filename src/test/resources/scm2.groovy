job("test") {
	scm {
		git {
			remote {
				github("https://git.ourdomain.com:8444/scm/chef/chef-job-dsl-config.git", "https")
				credentials("gituser")
			}
			branch("branches/INFRA-2353")
		}
	}
}
