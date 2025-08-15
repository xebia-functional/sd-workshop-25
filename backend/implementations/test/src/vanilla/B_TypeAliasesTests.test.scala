package implementations.vanilla

import implementations.vanilla.B_TypeAliases.*
import utest.{assert, *}

object B_TypeAliasesTests extends TestSuite:

  val tests = Tests {

    test("DNI"):

      test("Runtime happy path"):
        Seq(
          ("12345678Z", "12345678-Z"),
          ("00000001R", "00000001-R"),
          ("99999999R", "99999999-R")
        ).foreach:
          case (input, expected) => assert(DNI(input).formatted == expected)

      test("Runtime unhappy path"):

        test("Invalid Dni Number: Too short"):
          assertThrows[IllegalArgumentException](DNI("1234567T"))

        test("Invalid Dni Number: Too long"):
          assertThrows[IllegalArgumentException](DNI("123456789T"))

        test("Invalid Number"):
          assertThrows[IllegalArgumentException](DNI("1234567AT"))

        test("Invalid ControlLetter"):
          assertThrows[IllegalArgumentException](DNI("12345678Ñ"))

        test("Invalid Dni"):
          assertThrows[IllegalArgumentException](DNI("00000001Z"))

    test("NIE"):

      test("Runtime happy path"):
        Seq(
          ("X0000001R", "X-0000001-R"),
          ("Y2345678Z", "Y-2345678-Z")
        ).foreach:
          case (input, expected) => assert(NIE(input).formatted == expected)

      test("Runtime unhappy path"):

        test("Invalid Nie Letter"):
          assertThrows[IllegalArgumentException](NIE("A1234567T"))

        test("Invalid Nie Number: Too short"):
          assertThrows[IllegalArgumentException](NIE("Y234567T"))

        test("Invalid Nie Number: Too long"):
          assertThrows[IllegalArgumentException](NIE("Y23456789T"))

        test("Invalid Number"):
          assertThrows[IllegalArgumentException](NIE("Y234567AT"))

        test("Invalid Control Letter"):
          assertThrows[IllegalArgumentException](NIE("Y2345678Ñ"))

        test("Invalid Nie"):
          assertThrows[IllegalArgumentException](NIE("X0000001Z"))

    test("IDs"):

      test("Runtime happy path"):

        test("Valid input"):
          Seq(
            ("12345678Z", "12345678-Z"),
            ("00000001R", "00000001-R"),
            ("99999999R", "99999999-R"),
            ("X0000001R", "X-0000001-R"),
            ("Y2345678Z", "Y-2345678-Z")
          ).foreach:
            case (input, expected) => assert(ID(input).formatted == expected)

        test("Handling"):

          test("white spaces"):
            Seq(
              ("  12345678Z  ", "12345678-Z"),
              ("  X1234567L  ", "X-1234567-L")
            ).foreach:
              case (input, expected) => assert(ID(input).formatted == expected)

          test("Dash"):
            Seq(
              ("12345678-Z", "12345678-Z"),
              ("X-1234567-L", "X-1234567-L")
            ).foreach:
              case (input, expected) => assert(ID(input).formatted == expected)

          test("Lower case"):
            Seq(
              ("12345678z", "12345678-Z"),
              ("00000001r", "00000001-R"),
              ("99999999r", "99999999-R"),
              ("X0000001r", "X-0000001-R"),
              ("Y2345678z", "Y-2345678-Z")
            ).foreach:
              case (input, expected) => assert(ID(input).formatted == expected)

      test("Runtime unhappy path"):

        test("InvalidInput: empty"):
          assertThrows[IllegalArgumentException](ID("         "))

        test("InvalidInput: invisible characters"):
          assertThrows[IllegalArgumentException](ID("\n\r\t\n\r\t\n\r\t"))

        test("InvalidInput: symbols"):
          assertThrows[IllegalArgumentException](ID("@#¢∞¬÷“”≠"))

        test("InvalidInput: too short"):
          assertThrows[IllegalArgumentException](ID("Y"))

        test("InvalidInput: too long - number"):
          assertThrows[IllegalArgumentException](ID("123456789-Z"))

        test("InvalidInput: too long - underscore"):
          assertThrows[IllegalArgumentException](ID("12345678_Z"))

        test("InvalidInput: too long - dot"):
          assertThrows[IllegalArgumentException](ID("12345678.Z"))
  }
