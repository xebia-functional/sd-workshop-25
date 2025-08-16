package proofOfConcept

import proofOfConcept.A_Invariants.ControlLetter
import proofOfConcept.A_Invariants.NieLetter
import proofOfConcept.B_Rules.requirements.*
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object D_IdValidator:

  class DNI(dni: String):
    requireValidInput(dni)
    private val (number, letter) = dni.splitAt(8)
    requireValidNumber(number)
    requireValidDniNumber(number)
    requireValidControlLetter(letter)
    private val _number = number.toInt
    private val _letter = ControlLetter.valueOf(letter)
    requireValidDni(number, _letter)

    override def toString: String = dni
  end DNI

  class NIE(nie: String):
    private val nieLetter = nie.head.toString
    requireValidNieLetter(nieLetter)
    private val (number, letter) = nie.tail.splitAt(7)
    requireValidNumber(number)
    requireValidNieNumber(number)
    requireValidControlLetter(letter)
    private val _nieLetter = NieLetter.valueOf(nieLetter)
    private val _number = number.toInt
    private val _letter = ControlLetter.valueOf(letter)
    requireValidNie(_nieLetter, number, _letter)

    override def toString: String = nie
  end NIE

  @main def run(): Unit =
    println("""
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
          case Success(id)    => println(s"${id.toString} is a valid ID")
          case Failure(error) =>
            println(
              s"$userInput is not a valid ID. Reason: ${error.getMessage}"
            )
        loop()
      }

    loop()
