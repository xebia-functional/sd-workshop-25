package server

import libraries.*
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

  @cask.post("/neo_type")
  def neoType(request: cask.Request): cask.Response[String] =
    A_NeoType.ID.either(request.text()) match
      case Right(result) => successResponse(result.formatted)
      case Left(error)   => errorResponse(error)

  @cask.options("/neo_type")
  def optionsNeoType(): cask.Response[String] =
    cask.Response("Options of NeoType", headers = corsHeaders)

  @cask.post("/iron")
  def iron(request: cask.Request): cask.Response[String] =
    C_Iron.ID.either(request.text()) match
      case Right(result) => successResponse(result.formatted)
      case Left(error)   => errorResponse(error)

  @cask.options("/iron")
  def optionsIron(): cask.Response[String] =
    cask.Response("Options of Iron", headers = corsHeaders)

  initialize()
