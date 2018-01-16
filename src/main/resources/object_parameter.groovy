job("test") {
	blockOn("""Build-iOS-App
			Build-Android-App
			Run-iOS-Tests
			Run-Android-Tests""", {
		blockLevel("GLOBAL")
		scanQueueFor("DISABLED")
	})
	configure {
		it / 'properties' / 'com.coravy.hudson.plugins.github.GithubProjectProperty' {
			'projectUrl'('https://github.com/SymphonyOSF/iOSTest/')
		}
	}
}