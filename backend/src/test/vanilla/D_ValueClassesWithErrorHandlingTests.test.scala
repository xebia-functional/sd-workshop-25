package backend.vanilla

import backend.common.*
import backend.vanilla.D_ValueClassesWithErrorHandling.*

import utest.*

object D_ValueClassesWithErrorHandlingTests extends TestSuite:

  val tests = Tests {

    test("DNI") {

      test("Runtime positives"):
        Seq(
          ("12345678Z", "12345678-Z"),
          ("00000001R", "00000001-R"),
          ("99999999R", "99999999-R")
        ).foreach { case (input, expected) =>
          DNI.either(input).foreach: result =>
            assert(result.pretty == expected)
        }

      test("Runtime negatives"):

        test("Too short number"):
          //intercept[IllegalArgumentException](DNI("1234567T"))
          assert(DNI.either("1234567T").isLeft)

        test("too long number"):
          //intercept[IllegalArgumentException](DNI("123456789T"))
          assert(DNI.either("123456789T").isLeft)

        test("invalid number"):
          //intercept[IllegalArgumentException](DNI("1234567AT"))
          assert(DNI.either("1234567AT").isLeft)

        test("invalid control letter"):
          //intercept[IllegalArgumentException](DNI("12345678Ñ"))
          assert(DNI.either("12345678Ñ").isLeft)

        test("flipping arguments"):
          //intercept[IllegalArgumentException](DNI("Z12345678"))
          assert(DNI.either("Z12345678").isLeft)
    }

    test("NIE") {
      test("Runtime positives"):
        Seq(
          ("X0000001R", "X-0000001-R"),
          ("Y2345678Z", "Y-2345678-Z")
        ).foreach { case (input, expected) =>
          NIE.either(input).foreach: result =>
            assert(result.pretty == expected)
        }

      test("Runtime negatives"):

        test("invalid nie letter"):
          //intercept[IllegalArgumentException](NIE("A1234567T"))
          assert(
            NIE.either("A1234567T") match
              case Left(error) => error == InvalidNieLetter("A")
              case Right(_) => false
          )

        test("too short number"):
          //intercept[IllegalArgumentException](NIE("Y234567T"))
          assert(
            NIE.either("Y234567T") match
              case Left(error) => error == InvalidNieNumber("234567")
              case Right(_) => false
          )

        test("too long number"):
          //intercept[IllegalArgumentException](NIE("Y23456789T"))
          assert(
            NIE.either("Y23456789T") match
              case Left(error) => error == InvalidNieNumber("23456789")
              case Right(_) => false
          )

        test("invalid number"):
          //intercept[IllegalArgumentException](NIE("Y234567AT"))
          assert(
            NIE.either("Y234567AT") match
              case Left(error) => error == InvalidNumber("234567A")
              case Right(_) => false
          )

        test("invalid controll letter"):
          //intercept[IllegalArgumentException](NIE("Y2345678Ñ"))
          assert(
            NIE.either("Y2345678Ñ") match
              case Left(error) => error == InvalidControlLetter("Ñ")
              case Right(_) => false
          )

        test("flipping nie letter and control letter"):
          //intercept[IllegalArgumentException](NIE("R0000001X"))
          assert(
            NIE.either("R0000001X") match
              case Left(error) => error == InvalidNieLetter("R")
              case Right(_) => false
          )


        test("flipping all arguments"):
          //intercept[IllegalArgumentException](NIE("0000001RX"))
          assert(
            NIE.either("0000001RX") match
              case Left(error) => error == InvalidNieLetter("0")
              case Right(_) => false
          )
    }

    test("IDs") {

     test("Runtime positives"):
      test("whitespace handling"):
        Seq(
          ("  12345678Z  ", "12345678-Z"),
          ("  X1234567L  ", "X-1234567-L")
        ).foreach{ case (input, expected) =>
          ID.either(input).foreach: result =>
            assert(result.pretty == expected)  
        }

      test("dash handling"):
        Seq(
          ("12345678-Z", "12345678-Z"),
          ("X-1234567-L", "X-1234567-L")
        ).foreach{ case (input, expected) =>
          ID.either(input).foreach: result =>
            assert(result.pretty == expected)  
        }

      test("lower case handling"):
        Seq(
          ("12345678z", "12345678-Z"),
          ("00000001r", "00000001-R"),
          ("99999999r", "99999999-R"),
          ("X0000001r", "X-0000001-R"),
          ("Y2345678z", "Y-2345678-Z")
        ).foreach { case (input, expected) =>
          ID.either(input).foreach: result => 
            assert(result.pretty == expected)
        }

      test("Runtime negatives"):
        test("empty"):
          //intercept[NoSuchElementException](ID(""))
          assert(
            ID.either("") match
              case Left(error) => error  == InvalidInput("")
              case Right(_) => false
            )

        test("invisible characters"):
          //intercept[NoSuchElementException](ID("\n\r\t"))
          assert(
            ID.either("\n\r\t") match
              case Left(error) => error  == InvalidInput("\n\r\t")
              case Right(_) => false
            )

        test("symbol"):
          //intercept[IllegalArgumentException](ID("@#¢∞¬÷“”≠"))
          assert(
            ID.either("@#¢∞¬÷“”≠") match
              case Left(error) => error  == InvalidInput("@#¢∞¬÷“”≠")
              case Right(_) => false
            )
 

        test("absent number and control letter in NIE"):
          //intercept[IllegalArgumentException](ID("Y"))
          assert(
            ID.either("Y") match
              case Left(error) => error  == InvalidInput("Y")
              case Right(_) => false
            )
 
        test("invalid nie letter"):
          //intercept[IllegalArgumentException](ID("A1234567T"))
          assert(
            ID.either("A1234567T") match
              case Left(error) => error == InvalidNieLetter("A")
              case Right(_) => false
          )

        test("too short number"):
          //intercept[IllegalArgumentException](ID("1234567T"))
          assert(
            ID.either("1234567T") match
              case Left(error) => error == InvalidInput("1234567T")
              case Right(_) => false
          )

        test("too long number"):
          //intercept[IllegalArgumentException](ID("123456789T"))
          assert(
            ID.either("123456789T") match
              case Left(error) => error == InvalidInput("123456789T")
              case Right(_) => false
          )

        test("invalid number"):
          //intercept[IllegalArgumentException](ID("1234567AT"))
          assert(
            ID.either("1234567AT") match
              case Left(error) => error == InvalidNumber("1234567A")
              case Right(_) => false
          )

        test(" invalid controll letter"):
          //intercept[IllegalArgumentException](ID("Y2345678Ñ"))
          assert(
            ID.either("Y2345678Ñ") match
              case Left(error) => error == InvalidControlLetter("Ñ")
              case Right(_) => false
          )
    }
  }
