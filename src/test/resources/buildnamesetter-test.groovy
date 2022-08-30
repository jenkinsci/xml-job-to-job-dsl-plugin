job("test") {
    configure {
        it / 'properties' / 'org.jenkinsci.plugins.buildnamesetter.BuildNameSetter' {
            template("#\${BUILD_NUMBER} (\${GIT_BRANCH})")
            runAtStart(true)
            runAtEnd(true)
        }
    }
}