package sandbox

import sandbox.Invariants.ControlLetter
import sandbox.Invariants.NieLetter
import sandbox.Errors.requirements.*
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import scala.annotation.tailrec

object NaiveImplementation:

  class DNI private (dniNumber: Int, controlLetter: ControlLetter)
  object DNI:
    def apply(input: String): DNI =
      requireValidInput(input)
      val (number, letter) = input.splitAt(8)
      requireValidNumber(number)
      requireValidDniNumber(number)
      requireValidControlLetter(letter)
      val _number = number.toInt
      val _letter = ControlLetter.valueOf(letter)
      requireValidDni(number,_letter)
      new DNI(_number, _letter)
    end apply
  end DNI

  class NIE private (nieLetter: NieLetter, number: Int, controlLetter: ControlLetter)
  object NIE:
    def apply(input: String): NIE =
      val nieLetter = input.head.toString
      requireValidNieLetter(nieLetter)
      val (number, letter) = input.tail.splitAt(7)
      requireValidNumber(number)
      requireValidNieNumber(number)
      requireValidControlLetter(letter)
      val _nieLetter = NieLetter.valueOf(nieLetter)
      val _number = number.toInt
      val _letter = ControlLetter.valueOf(letter)
      requireValidNie(_nieLetter, number, _letter)
      new NIE(_nieLetter, _number, _letter)
    end apply
  end NIE


  @main def run() =
    println(
      """
        | *----------------------*
        | | Spanish ID validator |
        | *----------------------*
        |
        | Introduce any ID. For example:
        | - 12345678-Z
        | - 12345678-z
        | - 12345678Z
        | - 12345678z
        | - Y-2345678-Z
        | - Y2345678Z
        | - y-2345678-z
        | - y2345678z
        |
        | write QUIT, quit, Q or q to exit the program
        |
        |""".stripMargin)

    @tailrec
    def loop(): Unit =
      val userInput = scala.io.StdIn.readLine("Enter an ID: ")
      val exitCommand = Set("QUIT", "Q", "quit", "q")
      if exitCommand.contains(userInput) then ()
      else {
        Try(
          if userInput.head.isDigit
          then DNI.apply(userInput)
          else NIE.apply(userInput)
        ) match
          case Success(id) => println(s"$id is a valid ID")
          case Failure(error) => println(s"$userInput is not a valid ID. Reason: $error")
        loop()
      }

    loop()
