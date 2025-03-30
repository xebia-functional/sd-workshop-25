//> using dependency com.lihaoyi::cask:0.10.2

object CaskServer extends cask.MainRoutes{
  @cask.get("/")
  def welcome() = {
    "Welcome to Scala Days 2025 DDD workshop!"
  }

  @cask.get("/give_me_info")
  def noInfo() = {
    "No info for you Sir."
  }

  @cask.post("/raw_class")
  def rawClass(request: cask.Request) = ???

  @cask.post("/type_alias")
  def typeAlias(request: cask.Request) = ???

  @cask.post("/value_class")
  def valueClass(request: cask.Request) = ???

  @cask.post("/raw_class_validation")
  def rawClassValidation(request: cask.Request) = ???

  @cask.post("/opaque_type_validation")
  def opaqueTypeValidation(request: cask.Request) = ???

  @cask.post("/vaule_class_error_handling")
  def valueClassErrorHandling(request: cask.Request) = ???

  @cask.post("/opaque_type_error_handling")
  def opaqueTypeErrorHandling(request: cask.Request) = ???

  @cask.post("/neo_type")
  def neoType(request: cask.Request) = ???

  @cask.post("/iron")
  def iron(request: cask.Request) = ???

  initialize()
}
