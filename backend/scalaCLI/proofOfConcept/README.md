## Scala-CLI mini guide

### Install Scala-CLI
For macOS:
```shell
brew install Virtuslab/scala-cli/scala-cli
```
For Linux:
```shell
curl -sSLf https://scala-cli.virtuslab.org/get | sh
```
For Windows:
```shell
winget install virtuslab.scalacli
```
If some of the above methods does not work, visit the [official installation page](https://scala-cli.virtuslab.org/docs/overview/#installation).

#### IDE support for Scala-CLI
If you use `IntelliJ Idea`, `VS Code` or any other alternative that uses `Metals`, then run the following command so `scala-cli` generates the `Build Server Protocol` (BSP) Json file needed for your IDE to understand the Scala files.
````shell
scala-cli setup-ide . --scala <your_scala_version>
````
More information about IDE support [here](https://scala-cli.virtuslab.org/docs/commands/setup-ide/).

Once you are located in the root folder of the project, you can do many things with Scala-CLI. The usual suspects are:
1. [Compile](https://scala-cli.virtuslab.org/docs/commands/compile/)
2. [Test](https://scala-cli.virtuslab.org/docs/commands/test/)
3. [Format](https://scala-cli.virtuslab.org/docs/commands/fmt/)
4. [Document](https://scala-cli.virtuslab.org/docs/commands/doc/)
5. [Run](https://scala-cli.virtuslab.org/docs/commands/run/)

### 0. Experimental flag

In the presence of the directive `//> using target.scope test` in a test file, the option `--power` has to be 
included in the Scala-CLI command.

### 1. Compile
To compile the current folder - every file inside, run:
````shell
scala-cli --power compile .
````

To compile only the test files, you need to add the `--test` option. For example:
````shell
scala-cli --power compile --test . 
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
//> using test.dep com.lihaoyi::utest:0.9.0
````

Testing all the tests suites in the current directory and subdirectories:
````shell
scala-cli --power test .
````

Testing a specific test suite:
````shell
scala-cli --power test <path_from_the_root>/MyFile.test.scala
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
scala-cli --power doc . -o scala-doc
````

As a result, a directory `scala-doc` will be created. To see the documentation, just open `/index.html` inside the `scala-doc` directory.

### 5. Run
To run your main class you can use:
````shell
scala-cli --power run D_IdValidator.scala .
````
But since `run` is the default mode of Scala-CLI, the following is equivalent:
```shell
scala-cli --power D_IdValidator.scala .
```
