package errorHandling

import B_OpaqueTypesWithErrorHandling.*
import utest.*
import mvp.common.*
object B_OpaqueTypesWithErrorHandlingTests extends TestSuite:

  val tests = Tests {

    test("DNI"):

      test("Runtime happy path"):

        test("Apply"):
          Seq(
            ("12345678Z", "12345678-Z"),
            ("00000001R", "00000001-R"),
            ("99999999R", "99999999-R")
          ).foreach:
            case (input, expected) => assert(DNI(input).formatted == expected)

        test("Either"):
          Seq(
            ("12345678Z", "12345678-Z"),
            ("00000001R", "00000001-R"),
            ("99999999R", "99999999-R")
          ).foreach:
            case (input, expected) =>
              DNI
                .either(input)
                .foreach: dni =>
                  assert(dni.formatted == expected)

      test("Runtime unhappy path"):

        test("Apply"):

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

        test("Either"):

          test("Invalid Dni Number: Too short"):
            DNI.either("1234567T") match
              case Left(error) => assert(error == InvalidDniNumber("1234567"))
              case Right(_)    => assert(false)

          test("Invalid Dni Number: Too long"):
            DNI.either("123456789T") match
              case Left(error) => assert(error == InvalidDniNumber("123456789"))
              case Right(_)    => assert(false)

          test("Invalid Number"):
            DNI.either("1234567AT") match
              case Left(error) => assert(error == InvalidNumber("1234567A"))
              case Right(_)    => assert(false)

          test("Invalid ControlLetter"):
            DNI.either("12345678Ñ") match
              case Left(error) => assert(error == InvalidControlLetter("Ñ"))
              case Right(_)    => assert(false)

          test("Invalid Dni"):
            DNI.either("00000001Z") match
              case Left(error) => assert(error == InvalidDni("00000001", ControlLetter.Z))
              case Right(_)    => assert(false)

    test("NIE"):

      test("Runtime happy path"):

        test("Apply"):
          Seq(
            ("X0000001R", "X-0000001-R"),
            ("Y2345678Z", "Y-2345678-Z")
          ).foreach:
            case (input, expected) => assert(NIE(input).formatted == expected)

        test("Either"):
          Seq(
            ("X0000001R", "X-0000001-R"),
            ("Y2345678Z", "Y-2345678-Z")
          ).foreach:
            case (input, expected) =>
              NIE
                .either(input)
                .foreach: nie =>
                  assert(nie.formatted == expected)

      test("Runtime unhappy path"):

        test("Apply"):

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

        test("Either"):

          test("Invalid Nie Letter"):
            NIE.either("A1234567T") match
              case Left(error) => assert(error == InvalidNieLetter("A"))
              case Right(_)    => assert(false)

          test("Invalid Nie Number: Too short"):
            NIE.either("Y234567T") match
              case Left(error) => assert(error == InvalidNieNumber("234567"))
              case Right(_)    => assert(false)

          test("Invalid Nie Number: Too long"):
            NIE.either("Y23456789T") match
              case Left(error) => assert(error == InvalidNieNumber("23456789"))
              case Right(_)    => assert(false)

          test("Invalid Number"):
            NIE.either("Y234567AT") match
              case Left(error) => assert(error == InvalidNumber("234567A"))
              case Right(_)    => assert(false)

          test("Invalid Control Letter"):
            NIE.either("Y2345678Ñ") match
              case Left(error) => assert(error == InvalidControlLetter("Ñ"))
              case Right(_)    => assert(false)

          test("Invalid Nie"):
            NIE.either("X0000001Z") match
              case Left(error) => assert(error == InvalidNie(NieLetter.X, "0000001", ControlLetter.Z))
              case Right(_)    => assert(false)

    test("IDs"):

      test("Runtime happy path"):

        test("Valid input"):

          test("Apply"):
            Seq(
              ("12345678Z", "12345678-Z"),
              ("00000001R", "00000001-R"),
              ("99999999R", "99999999-R"),
              ("X0000001R", "X-0000001-R"),
              ("Y2345678Z", "Y-2345678-Z")
            ).foreach:
              case (input, expected) => assert(ID(input).formatted == expected)

          test("Either"):
            Seq(
              ("12345678Z", "12345678-Z"),
              ("00000001R", "00000001-R"),
              ("99999999R", "99999999-R"),
              ("X0000001R", "X-0000001-R"),
              ("Y2345678Z", "Y-2345678-Z")
            ).foreach:
              case (input, expected) =>
                ID.either(input)
                  .foreach: id =>
                    id.formatted == expected

        test("Handling"):

          test("white spaces"):

            test("Apply"):
              Seq(
                ("  12345678Z  ", "12345678-Z"),
                ("  X1234567L  ", "X-1234567-L")
              ).foreach:
                case (input, expected) => assert(ID(input).formatted == expected)

            test("Either"):
              Seq(
                ("  12345678Z  ", "12345678-Z"),
                ("  X1234567L  ", "X-1234567-L")
              ).foreach:
                case (input, expected) =>
                  ID.either(input)
                    .foreach: id =>
                      assert(id.formatted == expected)

          test("Dash"):

            test("Apply"):
              Seq(
                ("12345678-Z", "12345678-Z"),
                ("X-1234567-L", "X-1234567-L")
              ).foreach:
                case (input, expected) => assert(ID(input).formatted == expected)

            test("Either"):
              Seq(
                ("12345678-Z", "12345678-Z"),
                ("X-1234567-L", "X-1234567-L")
              ).foreach:
                case (input, expected) =>
                  ID.either(input)
                    .foreach: id =>
                      assert(id.formatted == expected)

          test("Lower case"):

            test("Apply"):
              Seq(
                ("12345678z", "12345678-Z"),
                ("00000001r", "00000001-R"),
                ("99999999r", "99999999-R"),
                ("X0000001r", "X-0000001-R"),
                ("Y2345678z", "Y-2345678-Z")
              ).foreach:
                case (input, expected) => assert(ID(input).formatted == expected)

            test("Either"):
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

        test("Runtime unhappy path"):

          test("Apply"):
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

          test("Either"):

            test("InvalidInput: empty"):
              ID.either("         ") match
                case Left(error) => assert(error == InvalidInput("         "))
                case Right(_)    => assert(false)

            test("InvalidInput: invisible characters"):
              ID.either("\n\r\t\n\r\t\n\r\t") match
                case Left(error) => assert(error == InvalidInput("\n\r\t\n\r\t\n\r\t"))
                case Right(_)    => assert(false)

            test("InvalidInput: symbols"):
              ID.either("@#¢∞¬÷“”≠") match
                case Left(error) => assert(error == InvalidInput("@#¢∞¬÷“”≠"))
                case Right(_)    => assert(false)

            test("InvalidInput: too short"):
              ID.either("Y") match
                case Left(error) => assert(error == InvalidInput("Y"))
                case Right(_)    => assert(false)

            test("InvalidInput: too long - number"):
              ID.either("123456789-Z") match
                case Left(error) => assert(error == InvalidInput("123456789-Z"))
                case Right(_)    => assert(false)

            test("InvalidInput: too long - underscore"):
              ID.either("12345678_Z") match
                case Left(error) => assert(error == InvalidInput("12345678_Z"))
                case Right(_)    => assert(false)

            test("InvalidInput: too long - dot"):
              ID.either("12345678.Z") match
                case Left(error) => assert(error == InvalidInput("12345678.Z"))
                case Right(_)    => assert(false)
  }
