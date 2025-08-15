## Mill Build Tool miniguide

This project uses version 1 of Mill Build Tool.

To run the project successfully you must locate your terminal in the path of `~/backend/`


### Installing Mill
Check the last available installing information [here](https://mill-build.org/mill/cli/installation-ide.html).

### Basic commands
The following 3 groups of commands are enough to run the MVP of the Spanish ID Validator.
You can run them directly from here using an IDE.

#### Compile
```shell
./mill implementations.compile
```

#### Test
```shell
./mill implementations.test

```
- Test specific tests
```shell
./mill implementations.test.testOnly implementations.vanilla.A_ClassesTests

```

#### Format
```shell
./mill mill.scalalib.scalafmt/
```
```shell
./mill mill.scalalib.scalafmt/checkFormatAll
```

### Running the server

Since there is only one main class in the module `implementations`, we can use the command
```shell
./mill implementations.run
```
If there were more main classes, then, we could indicate the main class in the `build.mill` like it
is explained in [Specifying the Main Class](https://mill-build.org/mill/scalalib/module-config.html#_specifying_the_main_class).