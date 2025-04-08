//> using dependency com.lihaoyi::cask:0.10.2
//> using dependencies com.lihaoyi::upickle:4.1.0
package backend

import scala.util.{Try, Success, Failure}
import backend.vanilla.A_RawClasses
import backend.vanilla.B_TypeAliases
import backend.vanilla.C_ValueClasses
import backend.vanilla.D_RawClassesWithValidation
import backend.vanilla.E_OpaqueTypesWithValidation
import backend.vanilla.F_ValueClassesWithErrorHandling
import backend.vanilla.G_OpaqueTypesWithErrorHandling

object CaskServer extends cask.MainRoutes {

  private val corsHeaders = Seq(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Allow-Methods" -> "POST, OPTIONS",
    "Access-Control-Allow-Headers" -> "Content-Type",
    "Access-Control-Max-Age" -> "86400"
  )

  /** Wraps endpoint execution in Try and handles response formatting
    *
    * @param block
    *   The endpoint logic to execute
    * @return
    *   A formatted cask.Response with appropriate status and headers
    */
  private def handleEndpoint[T](block: => T): cask.Response[String] = {
    Try(block) match {
      case Success(result) =>
        cask.Response(
          result.toString,
          statusCode = 200,
          headers = corsHeaders
        )
      case Failure(error) =>
        cask.Response(
          error.getMessage,
          statusCode = 400,
          headers = corsHeaders
        )
    }
  }

  @cask.options("/*")
  def handleOptions() = {
    cask.Response("", headers = corsHeaders)
  }

  @cask.get("/")
  def welcome() = {
    cask.Response(
      "Welcome to Scala Days 2025 DDD workshop!",
      headers = Seq(
        "Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Methods" -> "GET",
        "Access-Control-Allow-Headers" -> "Content-Type"
      )
    )
  }

  @cask.get("/give_me_info")
  def noInfo() = {
    cask.Response(
      "No info for you Sir.",
      headers = Seq(
        "Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Methods" -> "GET",
        "Access-Control-Allow-Headers" -> "Content-Type"
      )
    )
  }

  @cask.post("/raw_class")
  def rawClass(request: cask.Request) = {
    handleEndpoint(A_RawClasses.ID(request.text()))
  }

  @cask.post("/type_alias")
  def typeAlias(request: cask.Request) = {
    handleEndpoint(B_TypeAliases.ID(request.text()))
  }

  @cask.post("/value_class")
  def valueClass(request: cask.Request) = {
    handleEndpoint(C_ValueClasses.ID(request.text()))
  }

  @cask.post("/raw_class_validation")
  def rawClassValidation(request: cask.Request) = {
    handleEndpoint(D_RawClassesWithValidation.ID(request.text()))
  }

  @cask.post("/opaque_type_validation")
  def opaqueTypeValidation(request: cask.Request) = {
    handleEndpoint(E_OpaqueTypesWithValidation.ID(request.text()))
  }

  @cask.post("/vaule_class_error_handling")
  def valueClassErrorHandling(request: cask.Request) = {
    handleEndpoint(F_ValueClassesWithErrorHandling.ID.either(request.text()))
  }

  @cask.post("/opaque_type_error_handling")
  def opaqueTypeErrorHandling(request: cask.Request) = {
    handleEndpoint(G_OpaqueTypesWithErrorHandling.ID.either(request.text()))
  }

  @cask.post("/neo_type")
  def neoType(request: cask.Request) = ???
  // NeoType.ID(request.text())

  @cask.post("/iron")
  def iron(request: cask.Request) = ???
  // Iron.ID(request.text())

  @cask.post("/test_ok")
  def testOk(request: cask.Request) = {
    cask.Response(
      ujson
        .Obj("status" -> "success", "message" -> "Form submitted successfully!")
        .render(),
      headers = Seq(
        "Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Methods" -> "POST",
        "Access-Control-Allow-Headers" -> "Content-Type"
      )
    )
  }

  @cask.post("/test_ko")
  def testKo(request: cask.Request) = ???

  initialize()
}
