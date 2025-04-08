package xebia

import cats.effect.IO
import tyrian.Html.*
import tyrian.*
import tyrian.http.*
import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object sdtyrian extends TyrianIOApp[Msg, Model]:

  private val validatorEndpoints: Map[String, String] = Map(
    "Raw Class" -> "raw_class",
    "Type Alias" -> "type_alias",
    "Raw Value Class" -> "value_class",
    "Class" -> "raw_class_validation",
    "Value Class Error Handling" -> "value_class_error_handling",
    "Neo Type" -> "neo_type",
    "Opaque Type Validation" -> "opaque_type_validation",
    "Opaque Type Error Handling" -> "opaque_type_error_handling",
    "iron" -> "iron"
  )

  def router: Location => Msg =
    Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model("No validator selected", ""), Cmd.None)

  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.SelectValidator(name) => 
      (model.copy(currentValidator = name, submissionStatus = None), Cmd.None)
    case Msg.UpdateInput(value) => 
      (model.copy(inputValue = value, submissionStatus = None), Cmd.None)
    case Msg.Submit =>
      val endpoint = validatorEndpoints.getOrElse(model.currentValidator, "unknown")
      (model.copy(submissionStatus = None), Http.send(
        Request.post(s"http://localhost:8080/$endpoint", 
          Body.PlainText("application/json", model.inputValue)
        ),
        Msg.fromHttpResponse
      ))
    case Msg.SubmissionSucceeded(response) =>
      (model.copy(submissionStatus = Some(Right(response))), Cmd.None)
    case Msg.SubmissionFailed(error) =>
      (model.copy(submissionStatus = Some(Left(error))), Cmd.None)
    case Msg.Reset =>
      (Model("No validator selected", ""), Cmd.None)
    case Msg.NoOp => 
      (model, Cmd.None)

  def view(model: Model): Html[Msg] =
    div(
      h1(text("Welcome to ScalaDays 25 ID validator")),
      div(
        validatorEndpoints.keys.map { validatorName =>
          button(
            onClick(Msg.SelectValidator(validatorName))
          )(validatorName)
        }.toList
      ),
      h2(model.currentValidator),
      
      if model.currentValidator == "No validator selected" then Empty
      else div(
        input(
          placeholder := "Enter ID",
          onInput(Msg.UpdateInput(_)),
          value := model.inputValue
        ),
        button(onClick(Msg.Submit))("Validate"),
        button(onClick(Msg.Reset))("Reset"),
        model.submissionStatus match {
          case Some(Right(successMsg)) => 
            div(styles("color" -> "green"))(text(s"✓ $successMsg"))
          case Some(Left(errorMsg)) => 
            div(styles("color" -> "red"))(text(s"✗ $errorMsg"))
          case None => 
            Empty
        }
      )
    )

  def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None

final case class Model(
  currentValidator: String,
  inputValue: String,
  submissionStatus: Option[Either[String, String]] = None
)

enum Msg:
  case SelectValidator(name: String)
  case UpdateInput(value: String)
  case Submit
  case SubmissionSucceeded(response: String)
  case SubmissionFailed(error: String)
  case Reset
  case NoOp

object Msg:
  val fromHttpResponse: Decoder[Msg] =
    Decoder[Msg](
      response => SubmissionSucceeded(response.body),
      error => SubmissionFailed(error.toString)
    )
