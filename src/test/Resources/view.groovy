listView("test") {
	jobs {
		name("test1")
		name("test2")
	}
	columns {
		status()
		weather()
		name()
		lastSuccess()
		lastFailure()
		lastDuration()
		buildButton()
	}
}