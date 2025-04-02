package backend

//> using dependency com.lihaoyi::cask:0.10.2

object CaskServer extends cask.MainRoutes {
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
  // A_RawClasses.ID(request.text())

  @cask.post("/type_alias")
  def typeAlias(request: cask.Request) = ???
  // B_TypeAliases.ID(request.text())

  @cask.post("/value_class")
  def valueClass(request: cask.Request) = ???
  // C_ValueClasses.ID(request.text())

  @cask.post("/raw_class_validation")
  def rawClassValidation(request: cask.Request) = ???
  // D_RawClassesWithValidation.ID(request.text())

  @cask.post("/opaque_type_validation")
  def opaqueTypeValidation(request: cask.Request) = ???
  // E_OpaqueTypesWithValidation.ID(request.text())

  @cask.post("/vaule_class_error_handling")
  def valueClassErrorHandling(request: cask.Request) = ???
  // F_ValueClassesWithErrorHandling.ID.parse(request.text())

  @cask.post("/opaque_type_error_handling")
  def opaqueTypeErrorHandling(request: cask.Request) = ???
  // G_OpaqueTypesWithErrorHandling.ID.parse(request.text())

  @cask.post("/neo_type")
  def neoType(request: cask.Request) = ???
  // NeoType.ID(request.text())

  @cask.post("/iron")
  def iron(request: cask.Request) = ???
  // Iron.ID(request.text())

  @cask.post("/test_ok")
  def testOk(request: cask.Request) = ???

  @cask.post("/test_ko")
  def testKo(request: cask.Request) = ???

  initialize()
}
