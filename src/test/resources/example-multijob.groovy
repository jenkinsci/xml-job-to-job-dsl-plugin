multiJob("test") {
	description()
	keepDependencies(false)
	parameters {
		textParam("POD_NAME", "mobile-aut2", "")
	}
	disabled(false)
	concurrentBuild(false)
	steps {
		phase("create-pod") {
			phaseJob("create-pod-mobile-automation") {
				currentJobParameters(true)
				exposedScm(false)
				disableJob(false)
				abortAllJobs(false)
				killPhaseCondition("NEVER")
			}
			continuationCondition("ALWAYS")
			executionType("PARALLEL")
		}
		shell("sleep 1200s")
		phase("run smoke tests") {
			phaseJob("ios-smoke-tests") {
				currentJobParameters(true)
				exposedScm(false)
				disableJob(false)
				abortAllJobs(false)
				parameters {
					predefinedProp("POD", "warpdrive:\${POD_NAME}-pod1")
					predefinedProp("XPOD", "warpdrive-xpod:\${POD_NAME}-pod2")
					predefinedProp("METAFILTER", "smoke")
				}
				killPhaseCondition("NEVER")
			}
			continuationCondition("ALWAYS")
			executionType("PARALLEL")
		}
	}
	pollSubjobs(false)
}