package backend.vanilla

import backend.vanilla.F_OpaqueTypesRuntime.*
import scala.compiletime.testing.typeChecks
import scala.compiletime.testing.typeCheckErrors
import scala.compiletime.testing.Error

import utest.*
import scala.compiletime.testing.ErrorKind

object F_OpaqueTypesRuntimeTests extends TestSuite:

  val tests = Tests {

    test("DNI") {

      test("Compile positives"):
        Seq(
          (DNI("12345678Z"), "12345678-Z"),
          (DNI("00000001R"), "00000001-R"),
          (DNI("99999999R"), "99999999-R")
        ).foreach { case (input, expected) => assert(input.toString == expected)}

      test("Compile false positives"):

        test("Too short number"):
          //intercept[IllegalArgumentException](DNI("1234567T"))
          assert(typeCheckErrors("""DNI("1234567T")""").head.message == "'1234567T' must have lenght of 9")

        test("too long number"):
          //intercept[IllegalArgumentException](DNI("123456789T"))
          assert(typeCheckErrors("""DNI("123456789T")""").head.message == "'123456789T' must have lenght of 9")

        test("invalid number"):
          //intercept[IllegalArgumentException](DNI("1234567AT"))
          assert(typeCheckErrors("""DNI("1234567AT")""").head.message == "Number '1234567A' should contain 8 digits")

        test("invalid control letter"):
          //intercept[IllegalArgumentException](DNI("12345678Ñ"))
          assert(typeCheckErrors("""DNI("12345678Ñ")""").head.message == "'Ñ' is not a valid Control letter")

        test("flipping arguments"):
          //intercept[IllegalArgumentException](DNI("Z12345678"))
          assert(typeCheckErrors("""DNI("Z12345678")""").head.message == "Number 'Z1234567' should contain 8 digits")     
    }

    test("NIE") {
      test("Compile positives"):
        Seq(
          (NIE("X0000001R"), "X-0000001-R"),
          (NIE("Y2345678Z"), "Y-2345678-Z")
        ).foreach { case (input, expected) => assert(input.toString == expected)
        }

      test("Compile false positives"):
        test("invalid nie letter"):
          //intercept[IllegalArgumentException](NIE("A1234567T"))
          assert(typeCheckErrors("""NIE("A1234567T")""").head.message == "'A' is not a valid NIE letter")

        test("too short number"):
          //intercept[IllegalArgumentException](NIE("Y234567T"))
          assert(typeCheckErrors("""NIE("Y234567T")""").head.message == "'Y234567T' must have lenght of 9")

        test("too long number"):
          //intercept[IllegalArgumentException](NIE("Y23456789T"))
          assert(typeCheckErrors("""NIE("Y23456789T")""").head.message == "'Y23456789T' must have lenght of 9")

        test("invalid number"):
          //intercept[IllegalArgumentException](NIE("Y234567AT"))
          assert(typeCheckErrors("""NIE("Y234567AT")""").head.message == "'234567A' should not contain letters")

        test("invalid controll letter"):
          //intercept[IllegalArgumentException](NIE("Y2345678Ñ"))
          assert(typeCheckErrors("""NIE("Y2345678Ñ")""").head.message == "'Ñ' is not a valid Control letter")

        test("flipping nie letter and control letter"):
          //intercept[IllegalArgumentException](NIE("R0000001X"))
          assert(typeCheckErrors("""NIE("R0000001X")""").head.message == "'R' is not a valid NIE letter")

        test("flipping all arguments"):
          //intercept[IllegalArgumentException](NIE("0000001RX"))
          assert(typeCheckErrors("""NIE("0000001RX")""").head.message == "'0' is not a valid NIE letter")
          
    }

    test("IDs") {

      test("Compile false positives"):

        test("empty"):
          // intercept[IllegalArgumentException](ID(""))
          assert(typeCheckErrors("""ID("")""").head.message == "'' should be AlphaNumeric and contains 9 characters")

        test("invisible characters"):
          // intercept[IllegalArgumentException](ID("\n\r\t"))
          assert(typeCheckErrors("""ID("\n\r\t")""").head.message == "'\n\r\t' should be AlphaNumeric and contains 9 characters")

        test("symbol"):
          // intercept[IllegalArgumentException](ID("@#¢∞¬÷“”≠"))
          assert(typeCheckErrors("""ID("@#¢∞¬÷“”≠")""").head.message == "'@#¢∞¬÷“”≠' should be AlphaNumeric and contains 9 characters")

        test("absent number and control letter in NIE"):
          // intercept[IllegalArgumentException](ID("Y"))
          assert(typeCheckErrors("""ID("Y")""").head.message == "'Y' should be AlphaNumeric and contains 9 characters")

        test("invalid nie letter"):
          // intercept[IllegalArgumentException](ID("A1234567T"))
          assert(typeCheckErrors("""ID("A1234567T")""").head.message == "'A' is not a valid NIE letter")

        test("too short number"):
          // intercept[IllegalArgumentException](ID("1234567T"))
          assert(typeCheckErrors("""ID("1234567T")""").head.message == "'1234567T' must have lenght of 9")

        test("too long number"):
          // intercept[IllegalArgumentException](ID("123456789T"))
          println(typeCheckErrors("""ID("123456789T")""").head.message)
          assert(typeCheckErrors("""ID("123456789T")""").head.message == "'123456789T' should be AlphaNumeric and contains 9 characters")

        test("invalid number"):
          // intercept[IllegalArgumentException](ID("1234567AT"))
          assert(typeCheckErrors("""ID("1234567AT")""").head.message == "Number '1234567A' should contain 8 digits")

        test(" invalid controll letter"):
          // intercept[IllegalArgumentException](ID("Y2345678Ñ"))
          println(typeCheckErrors("""ID("Y2345678Ñ")""").head.message)
          assert(typeCheckErrors("""ID("Y2345678Ñ")""").head.message == "'Ñ' is not a valid Control letter")

      /*    
      test("edge cases"):
        
        test("whitespace handling"):
          assert(ID("  12345678Z  ").toString == "12345678-Z")
          assert(ID("  X1234567L  ").toString == "X-1234567-L")

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
          ).foreach { case (input, expected) => assert(input.toString == expected)
          }
      */
    }
  }
