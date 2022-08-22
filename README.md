# XML job to Job DSL plugin
A Jenkins plugin to convert XML jobs in scripts of Job DSL Plugin

## Steps
1. Read the XML file and parse to an internal class set (DONE)
2. Translate the internal class set to DSL and save it to file, it should be easy to read and easy to mantain (DONE)
3. Make this program run on command line and test if it works with some XML files (DONE)
4. Create a view where the user can select which jobs he wants to convert to DSL and generate a file to download (DONE)
5. Turn it into a Jenkins plugin (DONE)
6. Increase the number of known tags to translate and improve the plugin
7. Automatically refactor the generated code to avoid duplicated blocks

## Set up
It will be easier to work with Java with IntelliJ IDE. The community edition is fine for working with this plugin.

1. Install Java `brew install java` -- install latest version jdk11
  - Check version with `java --version`
2. Install Maven `brew install maven`
  - Check version with `mvn -version`
3. Edit run configurations -- you may select XML files by file, URL or directory. 
   - Directory: `--directory xml/` where `xml` is a directory of job directories with config.xml files inside.
   - File: `--file xml/jobname/config.xml` where the name of the enclosing directory is the name of the job.
   - See InitialArgumentsHandler.java for more options
4. Run `pom.xml` by opening it as a project in IntelliJ

## Do you want to help?
You can find the list of known tags in the file translator.properties under the path /src/main/resources

The key (on left) represents the tag in XML, and the value is the tag in Job DSL:
```
flow-definition = pipelineJob
```

The key can also have a parent, separated by a dot ".":
```
//In this case, "project" is the parent, so this tag "description" will only be parsed if its parent is the tag "project"
project.description = description

//But in this case, "flow-definition" is the parent, so this tag "description" will only be parsed if its parent is the tag "flow-definition"
flow-definition.description = description
```

The key yet can have a suffix ".type":
```
blockingJobs.type = PARAMETER
```

These are the possible types:
* INNER 
* OBJECT
* METHOD
* PARAMETER
* PROPERTY
* ARRAY
* CLOSURE
* CONFIGURE

Or a custom type, just pass a type the whole package of the class to render a custom type:
```
url.type = com.adq.jenkins.xmljobtodsl.dsl.strategies.custom.DSLGitHubMethodStrategy
```

Let's describe each type:

### INNER
```
hudson.tasks.Shell = INNER
```

The type can be the type of the tag, because it doesn't have a DSL tag, we will just render its children tags

### OBJECT
It renders an object in groovy like:
```
remote {
    name("origin")
    github("SymphonyOSF/SANDROID-CLIENT-APP", "ssh")
}
```

### METHOD
This is the default type, if you don't define a type, METHOD will be used. It render a method in groovy like:
```
description()
```

### PARAMETER
It renders a parameter if its parent is a method, It detects its type (String, integer, boolean, etc)
```
name("origin") //origin is the parameter
```

### PROPERTY
It renders a property like:
```
def git = {
    remote {
        name("origin")
        github("alandoni/xml-job-to-dsl-plugin", "ssh")
    }
    branch("*/\${branch}")
}

job("test") {
    scm git //this is a property
}
```

### ARRAY
It defines an array of objects:
```
choiceParam("METAFILTER", ["all tests", "smoke tests", "sanity tests"], "")
```

### CLOSURE
It defines a closure:
```
trigger("") { // Closure
    block { // Object in the closure
        buildStepFailure("FAILURE")
        unstable("UNSTABLE")
        failure("FAILURE")
    }
}
```

### CONFIGURE
Configure is used to represent tags not supported by Job DSL, its syntax is a bit different, and it is always inserted inside an object named "Configure", the job can only have one object configure

```
configure {
    it / 'properties' / 'jenkins.model.BuildDiscarderProperty' { // This is the tag of type CONFIGURE
        strategy {
            'daysToKeep'('-1')
            'numToKeep'('20')
            'artifactDaysToKeep'('-1')
            'artifactNumToKeep'('-1')
        }
    }
    it / 'properties' / 'com.coravy.hudson.plugins.github.GithubProjectProperty' { // This is the tag of type CONFIGURE
        'projectUrl'('https://github.com/SymphonyOSF/SANDROID-CLIENT-APP/')
        'displayName'()
    }
}
```

### Custom type
To create a custom type, you will need to extend the class ```AbstractDSLStrategy```

It demands you to create a constructor:
```
/**
 * Creates an object of type DSLCustomStrategy
 * @param tabs: number of tabs for indentation purposes, it will be increased automatically
 * @param propertyDescriptor: the object that represents a class in XML, with its attributes, children, parent and values
 * @param name: the name in DSL Groovy
 */
public DSLCustomStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
    this(tabs, propertyDescriptor, name, true); // In case you need to change the children tags of this class
                                                      // change the last parameter to false
}
```

And override the following method:
```
@Override
public String toDSL() {
    return ""; //This renders the text in the script
}
```
