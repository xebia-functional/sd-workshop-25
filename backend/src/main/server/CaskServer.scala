package backend.server

import scala.util.{Try, Success, Failure}
import backend.vanilla.A_RawClasses
import backend.vanilla.B_TypeAliases
import backend.vanilla.C_ValueClasses
import backend.vanilla.D_ValueClassesWithErrorHandling
import backend.vanilla.E_OpaqueTypes
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

  private def mapEndpoint[Error, Succes](block: Either[Error, Succes]): cask.Response[String] = {
    block match {
      case Right(result) =>
        cask.Response(
          result.toString,
          statusCode = 200,
          headers = corsHeaders
        )
      case Left(error) =>
        cask.Response(
          error.toString,
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
  def rawClass(request: cask.Request) = handleEndpoint(A_RawClasses.ID(request.text()))
  @cask.options("/raw_class")
  def optionsRawClass() = cask.Response("Options of Raw Class", headers = corsHeaders)

  @cask.post("/type_alias")
  def typeAlias(request: cask.Request) = handleEndpoint(B_TypeAliases.ID(request.text()))
  @cask.options("/type_alias")
  def optionsTypeAlias() = cask.Response("Options of Type Alias", headers = corsHeaders)

  @cask.post("/value_class")
  def valueClass(request: cask.Request) = handleEndpoint(C_ValueClasses.ID(request.text()))
  @cask.options("/value_class")
  def optionsValueClass() = cask.Response("Options of Value Class", headers = corsHeaders)

  @cask.post("/raw_class_validation")
  def rawClassValidation(request: cask.Request) = mapEndpoint(D_ValueClassesWithErrorHandling.ID(request.text()))
  @cask.options("/raw_class_validation")
  def optionsRawClassValidation() = cask.Response("Options of Raw Class Validation", headers = corsHeaders)

  @cask.post("/opaque_type_validation")
  def opaqueTypeValidation(request: cask.Request) = handleEndpoint(E_OpaqueTypes.ID(request.text()))
  @cask.options("/opaque_type_validation")
  def optionsOpaqueTypeValidation() = cask.Response("Options of Opaque Type Validation", headers = corsHeaders)

  @cask.post("/vaule_class_error_handling")
  def valueClassErrorHandling(request: cask.Request) = mapEndpoint(
    D_ValueClassesWithErrorHandling.ID(request.text())
  )
  @cask.options("/vaule_class_error_handling")
  def optionsValueClassErrorHandling() = cask.Response("Options of Value Class Error Handling", headers = corsHeaders)

  @cask.post("/opaque_type_error_handling")
  def opaqueTypeErrorHandling(request: cask.Request) = handleEndpoint(
    G_OpaqueTypesWithErrorHandling.ID.either(request.text())
  )
  @cask.options("/opaque_type_error_handling")
  def optionsOpaqueTypeErrorHandling() = cask.Response("Options of Opaque Type Error Handling", headers = corsHeaders)

  @cask.post("/neo_type")
  def neoType(request: cask.Request) = ??? // NeoType.ID(request.text())
  @cask.options("/neo_type")
  def optionsNeoType() = cask.Response("Options of NeoType", headers = corsHeaders)

  @cask.post("/iron")
  def iron(request: cask.Request) = ??? // Iron.ID(request.text())
  @cask.options("/iron")
  def optionsIron() = cask.Response("Options of Iron", headers = corsHeaders)

  initialize()
}
