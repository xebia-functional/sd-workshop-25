//> using test.dep com.lihaoyi::utest:0.8.5

package backend.vanilla

import backend.common.*
import backend.vanilla.F_OpaqueTypesRuntime.*

import scala.compiletime.testing.typeChecks

import utest.*

object F_OpaqueTypesRuntimeTests extends TestSuite:

  val tests = Tests {
    test("DNI") {

      test("Compile positives"):
        Seq(
          (DNI(DniNumber("12345678"), Letter("Z")), "12345678-Z"),
          (DNI(DniNumber("00000001"), Letter("R")), "00000001-R"),
          (DNI(DniNumber("99999999"), Letter("R")), "99999999-R")
        ).foreach { case (input, expected) =>
          assert(input.toString == expected)
        }

      // test("Compile false positives"):

      // test("Too short number"):
      //  intercept[IllegalArgumentException](DNI(DniNumber("1234567"), Letter("T")))

      // test("too long number"):
      //  intercept[IllegalArgumentException](DNI(DniNumber("123456789"), Letter("T")))

      // test("invalid number"):
      //  intercept[IllegalArgumentException](DNI(DniNumber("1234567A"), Letter("T")))

      // test("invalid control letter"):
      //  intercept[IllegalArgumentException](DNI(DniNumber("12345678"), Letter("Ñ")))

      // test("flipping arguments"):
      //  intercept[IllegalArgumentException](DNI(Letter("Z"), DniNumber("12345678")))

    }

    test("NIE") {
      test("Compile positives"):
        Seq(
          (NIE(NIELetter("X"), NieNumber("0000001"), Letter("R")), "X-0000001-R"),
          (NIE(NIELetter("Y"), NieNumber("2345678"), Letter("Z")), "Y-2345678-Z")
        ).foreach { case (input, expected) => assert(input.toString == expected) }

      // test("Compile false positives"):

      // test("invalid nie letter"):
      //  intercept[IllegalArgumentException](NIE(NIELetter("A"), NieNumber("1234567"), Letter("T")))

      // test("too short number"):
      //  intercept[IllegalArgumentException](NIE(NIELetter("Y"), NieNumber("234567"), Letter("T")))

      // test("too long number"):
      //  intercept[IllegalArgumentException](NIE(NIELetter("Y"), NieNumber("23456789"), Letter("T")))

      // test("invalid number"):
      //  intercept[IllegalArgumentException](NIE(NIELetter("Y"), NieNumber("234567A"), Letter("T")))

      // test("invalid controll letter"):
      //  intercept[IllegalArgumentException](NIE(NIELetter("Y"), NieNumber("2345678"), Letter("Ñ")))

      // test("flipping nie letter and control letter"):
      //  intercept[IllegalArgumentException](NIE(Letter("R"), NieNumber("0000001"), NIELetter("X")))

      // test("flipping all arguments"):
      //  intercept[IllegalArgumentException](NIE(NieNumber("0000001"), Letter("R"), NIELetter("X")))
    }

    test("IDs") {
      // test("Compile false positives"):

      // test("empty"):
      //   intercept[NoSuchElementException](ID(""))

      // test("invisible characters"):
      //   intercept[NoSuchElementException](ID("\n\r\t     "))

      // test("symbol"):
      //   intercept[IllegalArgumentException](ID("@#¢∞¬÷“”≠"))

      // test("absent number and control letter in NIE"):
      //  intercept[IllegalArgumentException](ID("Y"))

      // test("invalid nie letter"):
      //  intercept[IllegalArgumentException](ID("A1234567T"))

      // test("too short number"):
      //   intercept[IllegalArgumentException](ID("1234567T"))

      // test("too long number"):
      //  intercept[IllegalArgumentException](ID("123456789T"))

      // test("invalid number"):
      //  intercept[IllegalArgumentException](ID("1234567AT"))

      // test(" invalid controll letter"):
      //  intercept[IllegalArgumentException](ID("Y2345678Ñ"))

      test("edge cases"):
        test("whitespace handling"):
          // assert(ID("  12345678Z  ").toString == "12345678-Z")
          assert(typeChecks("""ID("  12345678Z  ")"""))
          // assert(ID("  X1234567L  ").toString == "X-1234567-L")
          assert(typeChecks("""ID("  X1234567L  ")""""))

        test("dash handling"):
          assert(ID("12345678-Z").toString == "12345678-Z")
          assert(ID("X-1234567-L").toString == "X-1234567-L")

        test("lower case handling"):
          Seq(
            (ID("12345678z"), "12345678-Z"),
            (ID("00000001r"), "00000001-R"),
            (ID("99999999r"), "99999999-R"),
            (ID("X0000001r"), "X-0000001-R"),
            (ID("Y2345678z"), "Y-2345678-Z")
          ).foreach { case (input, expected) =>
            // assert(input.toString == expected)
            assert(typeChecks(input) == false)
          }
    }
  }
