package xebia

import cats.effect.IO
import tyrian.Html.*
import tyrian.*
import tyrian.http.*
import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object sdtyrian extends TyrianIOApp[Msg, Model]:

  private val validatorEndpoints: List[(String, String)] = List()

  def router: Location => Msg =
    Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model("No validator selected", "", Right("Please enter ID")), Cmd.None)

  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.SelectValidator(name) =>
      (model.copy(currentValidator = name), Cmd.None)
    case Msg.UpdateInput(value) =>
      (model.copy(inputValue = value), Cmd.None)
    case Msg.Submit =>
      val endpoint = validatorEndpoints
        .find(_._1 == model.currentValidator)
        .map(_._2)
        .getOrElse("unknown")
      val url = s"http://localhost:8080/$endpoint"
      (
        model,
        Http.send(
          Request
            .post(url, Body.PlainText("application/json", model.inputValue)),
          Msg.fromHttpResponse
        )
      )
    case Msg.SubmissionSucceeded(response) =>
      (model.copy(submissionStatus = Right(response)), Cmd.None)
    case Msg.SubmissionFailed(error) =>
      (model.copy(submissionStatus = Left(error)), Cmd.None)
    case Msg.Reset =>
      (Model(model.currentValidator, "", Right("Please enter ID")), Cmd.None)
    case Msg.NoOp =>
      (model, Cmd.None)

  def view(model: Model): Html[Msg] =
    div(
      styles(
        "display"         -> "flex",
        "flex-direction"  -> "column",
        "align-items"     -> "center",
        "justify-content" -> "center",
        "gap"             -> "24px",
        "max-width"       -> "900px",
        "margin"          -> "0 auto",
        "padding"         -> "1rem"
      )
    )(
      h1(text("Welcome to ScalaDays 2025 ID validator")),
      div(
        styles(
          "display"         -> "flex",
          "flex-wrap"       -> "wrap",
          "gap"             -> "1rem",
          "justify-content" -> "center",
          "align-items"     -> "center"
        )
      )(
        validatorEndpoints.map { case (name, _) =>
          button(
            onClick(Msg.SelectValidator(name)),
            styles(
              "padding"       -> "0.5rem 1rem",
              "border-radius" -> "4px",
              "background" -> (if model.currentValidator == name then
                                 "#00800029"
                               else ""),
              "cursor" -> (if model.currentValidator == name then "default"
                           else "pointer"),
              "pointer-events" -> (if model.currentValidator == name then "none"
                                   else "auto")
            )
          )(name)
        }
      ),
      h2(text(model.currentValidator)),
      if model.currentValidator == "No validator selected" then Empty
      else
        div(
          styles(
            "display"        -> "flex",
            "flex-direction" -> "column",
            "align-items"    -> "center",
            "gap"            -> "24px",
            "max-width"      -> "900px",
            "width"          -> "100%"
          )
        )(
          input(
            placeholder := "Enter ID",
            onInput(Msg.UpdateInput(_)),
            value := model.inputValue,
            styles(
              "width"         -> "100%",
              "max-width"     -> "350px",
              "padding"       -> "0.8rem",
              "border-radius" -> "4px",
              "border"        -> "1px solid #ccc",
              "margin-bottom" -> "1rem"
            )
          ),
          div(
            styles(
              "display"       -> "flex",
              "gap"           -> "1rem",
              "margin-bottom" -> "1.5rem"
            )
          )(
            button(
              onClick(Msg.Submit),
              styles(
                "padding"       -> "0.8rem 2rem",
                "border-radius" -> "4px",
                "cursor" -> (if model.inputValue.nonEmpty then "pointer"
                             else "default"),
                "opacity" -> (if model.inputValue.nonEmpty then "1" else "0.5"),
                "pointer-events" -> (if model.inputValue.nonEmpty then "all"
                                     else "none")
              )
            )("Validate"),
            button(
              onClick(Msg.Reset),
              styles(
                "padding"          -> "0.8rem 2rem",
                "border-radius"    -> "4px",
                "background-color" -> "#f0f0f0",
                "border"           -> "1px solid #ddd",
                "cursor" -> (if model.inputValue.nonEmpty then "pointer"
                             else "default"),
                "opacity" -> (if model.inputValue.nonEmpty then "1" else "0.5"),
                "pointer-events" -> (if model.inputValue.nonEmpty then "all"
                                     else "none")
              )
            )("Reset")
          ),
          model.submissionStatus match {
            case Right(successMsg) =>
              div(
                styles(
                  "color"            -> "green",
                  "font-size"        -> "1.2rem",
                  "text-align"       -> "center",
                  "padding"          -> "1rem",
                  "min-width"        -> "350px",
                  "border"           -> "1px solid #e5e5e5",
                  "border-radius"    -> "4px",
                  "background-color" -> "#f8fff8"
                )
              )(text(s"✓ $successMsg"))

            case Left(errorMsg) =>
              div(
                styles(
                  "color"            -> "#dc3545",
                  "font-size"        -> "1.2rem",
                  "text-align"       -> "center",
                  "padding"          -> "1rem",
                  "min-width"        -> "350px",
                  "border"           -> "1px solid #f8d7da",
                  "border-radius"    -> "4px",
                  "background-color" -> "#f8f8f8"
                )
              )(text(s"✗ $errorMsg"))

          }
        )
    )

  def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None

final case class Model(
    currentValidator: String,
    inputValue: String,
    submissionStatus: Either[String, String]
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
      response =>
        if response.status.code >= 200 && response.status.code < 300 then
          SubmissionSucceeded(response.body)
        else SubmissionFailed(response.body),
      error => SubmissionFailed(error.toString)
    )
