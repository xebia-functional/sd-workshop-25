package server

import basic.*
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

  // Class endpoints
  @cask.post("/class")
  def rawClass(request: cask.Request): cask.Response[String] =
    Try(A_Classes.ID(request.text())) match
      case Success(result) => successResponse(result.formatted)
      case Failure(error)  => errorResponse(error.getMessage)

  @cask.options("/class")
  def optionsRawClass(): cask.Response[String] =
    cask.Response("Options of Class", headers = corsHeaders)

  // Type Alias endpoints
  @cask.post("/type_alias")
  def typeAlias(request: cask.Request): cask.Response[String] =
    Try(B_TypeAliases.ID(request.text())) match
      case Success(result) => successResponse(result.formatted)
      case Failure(error)  => errorResponse(error.getMessage)

  @cask.options("/type_alias")
  def optionsTypeAlias(): cask.Response[String] =
    cask.Response("Options of Type Alias", headers = corsHeaders)

  // Value Class endpoints
  @cask.post("/value_class")
  def valueClass(request: cask.Request): cask.Response[String] =
    Try(C_ValueClasses.ID(request.text())) match
      case Success(result) => successResponse(result.formatted)
      case Failure(error)  => errorResponse(error.getMessage)

  @cask.options("/value_class")
  def optionsValueClass(): cask.Response[String] =
    cask.Response("Options of Value Class", headers = corsHeaders)

  initialize()
