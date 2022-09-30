job("test") {
    wrappers {
        BuildNameSetter {
            template("#\${BUILD_NUMBER} (\${GIT_BRANCH})")
            runAtStart(true)
            runAtEnd(true)
            descriptionTemplate()
        }
    }
}