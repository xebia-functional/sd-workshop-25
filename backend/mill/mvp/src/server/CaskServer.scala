package server

import errorHandling.*
import scala.util.*

object CaskServer extends cask.MainRoutes:

  private val corsHeaders = Seq(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Allow-Methods" -> "POST, OPTIONS",
    "Access-Control-Allow-Headers" -> "Content-Type",
    "Access-Control-Max-Age" -> "86400"
  )

  private def successResponse(formattedId: String): cask.Response[String] =
    cask.Response(
      formattedId,
      statusCode = 200,
      headers = corsHeaders
    )

  private def errorResponse(errorMessage: String): cask.Response[String] =
    cask.Response(
      errorMessage,
      statusCode = 400,
      headers = corsHeaders
    )

  @cask.options("/*")
  def handleOptions(): cask.Response[String] =
    cask.Response("", headers = corsHeaders)

  @cask.get("/")
  def welcome(): cask.Response[String] =
    cask.Response(
      "Welcome to Scala Days 2025 DDD workshop!",
      headers = corsHeaders
    )

  @cask.get("/give_me_info")
  def noInfo(): cask.Response[String] =
    cask.Response(
      "No info for you Sir.",
      headers = corsHeaders
    )

  @cask.post("/value_class_error_handling")
  def valueClassErrorHandling(request: cask.Request): cask.Response[String] =
    A_ValueClassesWithErrorHandling.ID.either(request.text()) match
      case Right(result) => successResponse(result.formatted)
      case Left(error)   => errorResponse(error.cause)

  @cask.options("/value_class_error_handling")
  def optionsValueClassErrorHandling(): cask.Response[String] =
    cask.Response("Options of Value Class Error Handling", headers = corsHeaders)

  @cask.post("/opaque_type_error_handling")
  def opaqueTypeErrorHandling(request: cask.Request): cask.Response[String] =
    B_OpaqueTypesWithErrorHandling.ID.either(request.text()) match
      case Right(result) => successResponse(result.formatted)
      case Left(error)   => errorResponse(error.cause)

  @cask.options("/opaque_type_error_handling")
  def optionsOpaqueTypeErrorHandling(): cask.Response[String] =
    cask.Response("Options of Opaque Type Error Handling", headers = corsHeaders)

  initialize()
