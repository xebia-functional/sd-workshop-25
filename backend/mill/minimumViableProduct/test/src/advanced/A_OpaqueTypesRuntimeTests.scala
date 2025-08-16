package advanced

import A_OpaqueTypesRuntime.*

import scala.compiletime.testing.typeCheckErrors
import utest.*
object A_OpaqueTypesRuntimeTests extends TestSuite:

  val tests = Tests {

    test("DNI"):

      test("Compiletime happy path"):
        Seq(
          (DNI(12345678, "Z"), "12345678-Z"),
          (DNI(1, "R"), "00000001-R"),
          (DNI(99999999, "R"), "99999999-R")
        ).foreach:
          case (input, expected) => assert(input.formatted == expected)

      test("Compiletime unhappy path"):

        test("Invalid DNI Number: negative number"):
          assert(
            typeCheckErrors(
              """DNI(-1, "T")"""
            ).head.message == "Invalid DNI number '-1' should be positive and contain 8 digits"
          )

        test("Invalid DNI Number: Too big number"):
          assert(
            typeCheckErrors(
              """DNI(123456789,"T")"""
            ).head.message == "Invalid DNI number '123456789' should be positive and contain 8 digits"
          )

        test("Invalid Control Letter"):
          assert(
            typeCheckErrors(
              """DNI(12345678, "Ñ")"""
            ).head.message == "Invalid ControlLetter: 'Ñ' is not a valid Control letter"
          )

        test("Invalid DNI"):
          assert(
            typeCheckErrors(
              """DNI(12345678, "A")"""
            ).head.message == "DNI number '12345678' does not match the control letter 'A', expected 'Z'"
          )

    test("NIE"):

      test("Compiletime happy path"):
        Seq(
          (NIE("X", 1, "R"), "X-0000001-R"),
          (NIE("Y", 2345678, "Z"), "Y-2345678-Z")
        ).foreach:
          case (input, expected) => assert(input.formatted == expected)

      test("Compiletime unhappy path"):
        test("Invalid Nie Letter"):
          assert(
            typeCheckErrors(
              """NIE("A", 1234567, "T")"""
            ).head.message == "Invalid NIE Letter: 'A' is not a valid NIE letter"
          )

        test("Invalid NIE Number: negative number"):
          assert(
            typeCheckErrors(
              """NIE("Y", -234567, "T")"""
            ).head.message == "Invalid NIE Number: '-234567' should be positive and contain 7 digits"
          )

        test("Invalid NIE Number: Too big"):
          assert(
            typeCheckErrors(
              """NIE("Y", 23456789, "T")"""
            ).head.message == "Invalid NIE Number: '23456789' should be positive and contain 7 digits"
          )

        test("Invalid Control Letter"):
          assert(
            typeCheckErrors(
              """NIE("Y", 2345678, "Ñ")"""
            ).head.message == "Invalid ControlLetter: 'Ñ' is not a valid Control letter"
          )

        test("Invalid NIE"):
          assert(
            typeCheckErrors(
              """NIE("Y", 2345678, "A")"""
            ).head.message == "NIE number '12345678' does not match the control letter 'A', expected 'Z'"
          )

  }
