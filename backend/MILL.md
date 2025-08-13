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
