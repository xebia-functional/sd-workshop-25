# ScalaDays 2025 - DDD workshop

## Pre-requisites

There is no prior knowledge required.

Knowledge in Domain Driven Design, Scala and metaprogramming are a plus.

## Requirements
1. Install Scala-CLI
   1. IDE support for Scala-CLI

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

You can check this [Scala-CLI mini guide](SCALACLI.md) to get up to speed.

#### IDE support for Scala-CLI
If you use `IntelliJ Idea`, `VS Code` or any other alternative that uses `Metals`, then run the following command so `scala-cli` generates the `Build Server Protocol` (BSP) Json file needed for your IDE to understand the Scala files.
````shell
scala-cli setup-ide . --scala <your_scala_version>
````
More information about IDE support [here](https://scala-cli.virtuslab.org/docs/commands/setup-ide/).

## Full stack DDD ID validator
The app consist of two components that work slightly differently:
1. Front end
2. Back end
3. ID Validator

The front end relies on `ScalaJS` while the back end is `scala` on the `JVM`.

### Front end
`Tyrian` is an Elm-inspired ScalaJS library for frontend and game development.

Website: https://tyrian.indigoengine.io/

### Back end

#### Cask
`Cask` is a lightweight scala library similar to Python's `Flask` that belongs to the `Haoyi Li` stack.
It spins a server with almost no ceremony, making it a perfect library for prototyping and figuring things out.

Website: https://com-lihaoyi.github.io/cask/

### Validator
The validator will be implemented in many of the various ways Scala allows us to do it.
This way, we will experience the pros and cons of each technique.
The techniques are grouped in 3 conceptual units:
1. Scala's building blocks
2. Scala's basic metaprogramming
3. Scala's Domain Driven Design libraries

#### Scala's building blocks
Many of Scala's building blocks are present in other languages.
If you are not a Scala developer, these similarity will help you understand the second and third block better.

Each of the building blocks provides you with some way of validating inputs and building valid instances of the IDs.
We will explore and compare: 
- Algebraic Data types (ADTs)
- Enums (Enumerations)
- Classes
- Type aliases
- Value classes
- Classes with validation (assertions)
- Value Classes with error handling 

#### Scala's basic metaprogramming
Scala 3 metaprogramming has been redesigned from Scala 2 metaprogramming. 
Opaque types -a zero cost abstraction - allows you to use techniques of metaprogramming. 
Opaque types are flexible and powerful enough to create robust DDD types.
It just looks like normal code.
The block explores 3 usages of opaque types:
- Opaque Types with compile time errors
- Opaque Types with validation
- Opaque Types with error handling 

#### Scala's DDD libraries
1. NeoType
2. Iron

##### NeoType

##### Iron
