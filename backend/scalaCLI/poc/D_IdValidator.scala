package poc

import poc.A_Invariants.ControlLetter
import poc.A_Invariants.NieLetter
import poc.B_Rules.requirements.*
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

/**
 * Implement the 2 classes:
 * - DNI
 * - NIE
 *  In a way that only correct input will generate an instance of the class (use requirements)
 *  Also, modify the method `toString` to return the value on the instance capitalize and without dashes
 */
object D_IdValidator:

  class DNI(dni: String)

  class NIE(nie: String)

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
          then DNI(userInput)
          else NIE(userInput)
        ) match
          case Success(id)    => println(s"${id.toString} is a valid ID")
          case Failure(error) =>
            println(
              s"$userInput is not a valid ID. Reason: ${error.getMessage}"
            )
        loop()
      }

    loop()
