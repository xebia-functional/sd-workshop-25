package implementations.libraries

import utest.{assert, *}
import implementations.libraries.C_Iron.*
import implementations.common.*
import io.github.iltotore.iron.*

object C_IronTests extends TestSuite:

  val tests = Tests {

    test("DNI"):

      test("Compile positives"):
        Seq(
          ("12345678Z", "12345678-Z"),
          ("00000001R", "00000001-R"),
          ("99999999R", "99999999-R")
        ).foreach:
          case (input, expected) =>
            DNI
              .either(input)
              .foreach: result =>
                assert(result.formatted == expected)

      test("Invalid Dni Number: Too short"):
        DNI.either("1234567T") match
          case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
          case Right(_)    => assert(false)

      test("Invalid Dni Number: Too long"):
        DNI.either("123456789T") match
          case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
          case Right(_)    => assert(false)

      test("Invalid Number"):
        DNI.either("1234567AT") match
          case Left(error) => assert(error == "Should contain 8 digits")
          case Right(_)    => assert(false)

      test("Invalid ControlLetter"):
        DNI.either("12345678Ñ") match
          case Left(error) => assert(error == InvalidControlLetter("Ñ").cause)
          case Right(_)    => assert(false)

      test("Invalid Dni"):
        DNI.either("00000001Z") match
          case Left(error) => assert(error == InvalidDni("00000001", ControlLetter.Z).cause)
          case Right(_)    => assert(false)

    test("NIE"):

      test("Compile positives"):
        Seq(
          ("X0000001R", "X-0000001-R"),
          ("Y2345678Z", "Y-2345678-Z")
        ).foreach:
          case (input, expected) =>
            NIE
              .either(input)
              .foreach: result =>
                assert(result.formatted == expected)

      test("Compile false positives"):
        test("invalid nie letter"):
          NIE.either("A1234567T") match
            case Left(error) => assert(error == InvalidNieLetter("A").cause)
            case Right(_)    => false

        test("too short number"):
          NIE.either("Y234567T") match
            case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
            case Right(_)    => false

        test("too long number"):
          NIE.either("Y23456789T") match
            case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
            case Right(_)    => false

        test("invalid number"):
          NIE.either("Y234567AT") match
            case Left(error) => assert(error == "Should contain 7 digits")
            case Right(_)    => false

        test("invalid controll letter"):
          NIE.either("Y2345678Ñ") match
            case Left(error) => assert(error == InvalidControlLetter("Ñ").cause)
            case Right(_)    => false

        test("flipping nie letter and control letter"):
          NIE.either("R0000001X") match
            case Left(error) => assert(error == InvalidNieLetter("R").cause)
            case Right(_)    => false

        test("flipping all arguments"):
          NIE.either("0000001RX") match
            case Left(error) => assert(error == InvalidNieLetter("0").cause)
            case Right(_)    => false

    test("IDs"):

      test("Compile false positives"):

        test("empty"):
          ID.either("") match
            case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
            case Right(_)    => false

        test("invisible characters"):
          ID.either("\n\r\t") match
            case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
            case Right(_)    => false

        test("symbol"):
          ID.either("@#¢∞¬÷“”≠") match
            case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
            case Right(_)    => false

        test("absent number and control letter in NIE"):
          ID.either("Y") match
            case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
            case Right(_)    => false

        test("invalid nie letter"):
          ID.either("A1234567T") match
            case Left(error) => assert(error == InvalidNieLetter("A").cause)
            case Right(_)    => false

        test("too short number"):
          ID.either("1234567T") match
            case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
            case Right(_)    => false

        test("too long number"):
          ID.either("123456789T") match
            case Left(error) => assert(error == "Should be AlphaNumeric and have 9 characters")
            case Right(_)    => false

        test("invalid number"):
          ID.either("1234567AT") match
            case Left(error) => assert(error == "Should contain 8 digits")
            case Right(_)    => false

        test("invalid control letter"):
          ID.either("Y2345678Ñ") match
            case Left(error) => assert(error == InvalidControlLetter("Ñ").cause)
            case Right(_)    => false

      test("edge cases"):
        test("whitespace handling"):
          assert(ID.either("  12345678Z  ").map(_.formatted) == Right("12345678-Z"))
          assert(ID.either("  X1234567L  ").map(_.formatted) == Right("X-1234567-L"))

        test("dash handling"):
          assert(ID.either("12345678-Z").map(_.formatted) == Right("12345678-Z"))
          assert(ID.either("X-1234567-L").map(_.formatted) == Right("X-1234567-L"))

        test("lower case handling"):
          Seq(
            ("12345678z", "12345678-Z"),
            ("00000001r", "00000001-R"),
            ("99999999r", "99999999-R"),
            ("X0000001r", "X-0000001-R"),
            ("Y2345678z", "Y-2345678-Z")
          ).foreach:
            case (input, expected) =>
              ID.either(input)
                .foreach: id =>
                  assert(id.formatted == expected)
  }
