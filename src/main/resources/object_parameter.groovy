job("test") {
	blockOn("""Build-iOS-App
			Build-Android-App
			Run-iOS-Tests
			Run-Android-Tests""", {
		blockLevel("GLOBAL")
		scanQueueFor("DISABLED")
	})
}