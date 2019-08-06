# jvm-brotli-example
Example showing how to use jvm-brotli (https://github.com/nixxcode/jvm-brotli)

Also a great way to test if your current JVM platform is supported.

Simply clone this repository and run `mvn package` to build, then: 
- execute the "Main" class via your favourite IDE, or 
- navigate to "target" dir and run `java -jar jvmb-example.jar`

If the execution is successful, two new files should appear in the src folder: **encoded.br** and **decoded.txt**

If you get a ClassNotFoundException, it means jvm-brotli currently doesn't support your JVM platform. If this is the case, please head on over to the jvm-brotli [issue tracker](https://github.com/nixxcode/jvm-brotli/issues) and check if a support request for your platform already exists. If not, please create one using the "request platform support" issue template, so that we can get your platform on board ASAP! :)

Thank you [@tipsy](https://github.com/tipsy) for creating the base template for this example. :)
