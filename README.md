# xml-job-to-dsl
The idea is to first create a program to convert Job XML from Jenkins into Job DSL scripts, that are easier to mantain and keep versioned.
After that, change this program to a Jenkins Plugin

# Steps
1. Read the XML file and parse to an internal class set (DONE)
2. Translate the internal class set to DSL and save it to file, it should be easy to read and easy to mantain (IN PROGRESS)
3. Make this program run on command line and test if it works with some XML files
4. Turn it into a Jenkins plugin
5. Create a view where the user can select which jobs he wants to convert to DSL and generate a file to download
