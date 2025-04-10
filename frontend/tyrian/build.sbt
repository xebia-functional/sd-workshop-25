import scala.sys.process._
import scala.language.postfixOps

import sbtwelcome._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val sdtyrianfrontend =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin)
    .settings( // Normal settings
      name         := "sdtyrianfrontend",
      version      := "0.0.1",
      scalaVersion := "3.6.3",
      organization := "xebia",
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "tyrian-io" % "0.13.0",
        "io.circe" %%% "circe-core" % "0.14.12",
        "io.circe" %%% "circe-generic" % "0.14.12",
        "io.circe" %%% "circe-parser" % "0.14.12",
        "org.scalameta" %%% "munit" % "1.1.0" % Test,
      ),
      testFrameworks += new TestFramework("munit.Framework"),
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
      scalafixOnCompile := true,
      semanticdbEnabled := true,
      semanticdbVersion := scalafixSemanticdb.revision,
      autoAPIMappings   := true
    )
    .settings( // Welcome message
      logo := List(
        "",
        "sdtyrian (v" + version.value + ")",
        ""
      ).mkString("\n"),
      usefulTasks := Seq(
        UsefulTask("fastLinkJS", "Rebuild the JS (use during development)").noAlias,
        UsefulTask("fullLinkJS", "Rebuild the JS and optimise (use in production)").noAlias
      ),
      logoColor        := scala.Console.MAGENTA,
      aliasColor       := scala.Console.BLUE,
      commandColor     := scala.Console.CYAN,
      descriptionColor := scala.Console.WHITE
    )
