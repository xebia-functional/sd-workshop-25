## Scala-CLI mini guide

Once you are located in the root folder of the project, you can do many things with Scala-CLI. The usual suspects are:
1. [Compile](https://scala-cli.virtuslab.org/docs/commands/compile/)
2. [Test](https://scala-cli.virtuslab.org/docs/commands/test/)
3. [Format](https://scala-cli.virtuslab.org/docs/commands/fmt/)
4. [Document](https://scala-cli.virtuslab.org/docs/commands/doc/)
5. [Run](https://scala-cli.virtuslab.org/docs/commands/run/)

### 1. Compile
To compile the current folder - every file inside, run:
````shell
scala-cli compile .
````

To compile only the test files, you need to add the `--test` option. For example:
````shell
scala-cli compile --test . 
````

When you want Scala-CLI to recompile your code on any changes, add the option `--watch`. For example:
````shell
scala-cli compile --watch . 
````


### 2. Test
To create a test file, add `test` between the file name and the extension `scala`.
For example: `MyFile.test.scala`

To add dependencies to your tests, use directives with a `test` prefix on the dependency. For example:

````scala
//> using test.dep org.scalameta::munit:1.1.0
````

Testing all the tests suites in the current directory and subdirectories:
````shell
scala-cli test .
````

Testing a specific test suite:
````shell
scala-cli test <path_from_the_root>/MyFile.test.scala
````

### 3. Format
Scala CLI can format your code based on a default configuration of `scalafmt` that is set up by the Scala CLI team.
If you want to use a specific configuration, you have to add a `.scalafmt.conf` file in the root directory.

To format the code, run:
````shell
scala-cli fmt .
````

If you want to include a format check in your `CI/CD`, use:
```shell
scala-cli fmt --check .
```
### 4. Document

Scala CLI will read all your `scaladoc` in the source code and generate a documentation webpage.

The scope of the generated documentation will be limited to the directory where you execute the following command:
````shell
scala-cli doc . -o scala-doc
````

As a result, a directory `scala-doc` will be created. To see the documentation, just open `/index.html` inside the `scala-doc` directory.

### 5. Run
To run your main class you can use:
````shell
scala-cli run MyMainClass.scala
````
But since `run` is the default mode of Scala-CLI, the following is equivalent:
```shell
scala-cli MyMainCalss.scala
```
To pass arguments to the main method, do it after `--` and separate them with a space, like this:
```shell
scala-cli MyMainClass.scala -- first_argument second_argument
```
The `--watch` option makes Scala CLI watch your code for changes, and re-runs it upon any change or when the ENTER key is passed from the command line:
```shell
scala-cli run Hello.scala  --watch
```

Scala.js applications can also be compiled and run with the `--js` option. 
Note that this requires `node` to be installed on your system:
```shell
scala-cli MyScalaJsApp.scala --js
```