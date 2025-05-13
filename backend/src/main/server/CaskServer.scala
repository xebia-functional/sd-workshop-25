package backend.server

import scala.util.{Try, Success, Failure}
import backend.vanilla.{
  A_RawClasses,
  B_TypeAliases,
  C_ValueClasses,
  D_ValueClassesWithErrorHandling,
  E_OpaqueTypesWithErrorHandling
}

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


  // Raw Class endpoints
  @cask.post("/raw_class")
  def rawClass(request: cask.Request): cask.Response[String] = 
    Try(A_RawClasses.ID(request.text())) match
      case Success(result) => successResponse(result.formatted)
      case Failure(error) => errorResponse(error.getMessage)
    
  @cask.options("/raw_class")
  def optionsRawClass(): cask.Response[String] = 
    cask.Response("Options of Raw Class", headers = corsHeaders)


  // Type Alias endpoints
  @cask.post("/type_alias")
  def typeAlias(request: cask.Request): cask.Response[String] = 
    Try(B_TypeAliases.ID(request.text())) match
      case Success(result) => successResponse(result.formatted)
      case Failure(error) => errorResponse(error.getMessage)

  @cask.options("/type_alias")
  def optionsTypeAlias(): cask.Response[String] = 
    cask.Response("Options of Type Alias", headers = corsHeaders)


  // Value Class endpoints
  @cask.post("/value_class")
  def valueClass(request: cask.Request): cask.Response[String] = 
    Try(C_ValueClasses.ID(request.text())) match
      case Success(result) => successResponse(result.formatted)
      case Failure(error) => errorResponse(error.getMessage)

  @cask.options("/value_class")
  def optionsValueClass(): cask.Response[String] = 
    cask.Response("Options of Value Class", headers = corsHeaders)


  // Value Class with Error Handling endpoints
  @cask.post("/value_class_error_handling")
  def valueClassErrorHandling(request: cask.Request): cask.Response[String] = 
    D_ValueClassesWithErrorHandling.ID.either(request.text()) match 
      case Right(result) => successResponse(result.formatted)
      case Left(error) => errorResponse(error.cause)

  @cask.options("/value_class_error_handling")
  def optionsValueClassErrorHandling(): cask.Response[String] = 
    cask.Response("Options of Value Class Error Handling", headers = corsHeaders)


  // Opaque Type with Error Handling endpoints
  @cask.post("/opaque_type_error_handling")
  def opaqueTypeErrorHandling(request: cask.Request): cask.Response[String] = 
    E_OpaqueTypesWithErrorHandling.ID.either(request.text()) match 
      case Right(result) => successResponse(result.formatted)
      case Left(error) => errorResponse(error.cause)

  @cask.options("/opaque_type_error_handling")
  def optionsOpaqueTypeErrorHandling(): cask.Response[String] = 
    cask.Response("Options of Opaque Type Error Handling", headers = corsHeaders)


  // Unimplemented endpoints
  @cask.post("/neo_type")
  def neoType(request: cask.Request): Nothing = ??? // NeoType.ID(request.text())

  @cask.options("/neo_type")
  def optionsNeoType(): cask.Response[String] = 
    cask.Response("Options of NeoType", headers = corsHeaders)

  @cask.post("/iron")
  def iron(request: cask.Request): Nothing = ??? // Iron.ID(request.text())

  @cask.options("/iron")
  def optionsIron(): cask.Response[String] = 
    cask.Response("Options of Iron", headers = corsHeaders)

  initialize()
