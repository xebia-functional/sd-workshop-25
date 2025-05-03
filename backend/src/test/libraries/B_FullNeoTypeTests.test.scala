package backend.libraries

import backend.libraries.B_FullNeoType.*

import utest.*
import neotype.*

object B_FullNeoTypeTests extends TestSuite:

  val tests = Tests {

    test("DNI") {

      test("Compile positives"):
        Seq(
          (DNI.make("12345678Z"), "12345678-Z"),
          (DNI.make("00000001R"), "00000001-R"),
          (DNI.make("99999999R"), "99999999-R")
        ).foreach { case (input, expected) => input.foreach(result => assert(result.toString == expected))
        }    

      test("Compile false positives"):

        test("Too short number"):
          // intercept[IllegalArgumentException](DNI("1234567T"))
          assert(DNI.make("1234567T").isLeft)
  
        test("too long number"):
          //  intercept[IllegalArgumentException](DNI("123456789T"))
          assert(DNI.make("123456789T").isLeft)
        
        test("invalid number"):
          // intercept[IllegalArgumentException](DNI("1234567AT"))
          assert(DNI.make("1234567AT").isLeft)

        test("invalid control letter"):
          // intercept[IllegalArgumentException](DNI("12345678Ñ"))
          assert(DNI.make("12345678Ñ").isLeft)

        test("flipping arguments"):
          // intercept[IllegalArgumentException](DNI("Z12345678"))
          assert(DNI.make("Z12345678").isLeft)
    }

    test("NIE") {
      test("Compile positives"):
        Seq(
          (NIE.make("X0000001R"), "X-0000001-R"),
          (NIE.make("Y2345678Z"), "Y-2345678-Z")
        ).foreach { case (input, expected) => input.foreach(result => assert(result.toString == expected))
        }

      test("Compile false positives"):
        test("invalid nie letter"):
          //intercept[IllegalArgumentException](NIE("A1234567T"))
          assert(
            NIE.make("A1234567T") match
              case Left(error) => error == InvalidNieLetter("A").toString
              case Right(_) => false
          )


        test("too short number"):
          //intercept[IllegalArgumentException](NIE("Y", "234567", "T"))
          assert(
            NIE.make("Y234567T") match
              case Left(error) => error == InvalidNieNumber("234567").toString
              case Right(_) => false
          )

        test("too long number"):
          //intercept[IllegalArgumentException](NIE("Y", "23456789", "T"))
          assert(
            NIE.make("Y23456789T") match
              case Left(error) => error == InvalidNieNumber("23456789").toString
              case Right(_) => false
          )

        test("invalid number"):
          //intercept[IllegalArgumentException](NIE("Y", "234567A", "T"))
          assert(
            NIE.make("Y234567AT") match
              case Left(error) => error == InvalidNaN("234567A").toString
              case Right(_) => false
          )

        test("invalid controll letter"):
          //intercept[IllegalArgumentException](NIE("Y2345678Ñ"))
          assert(
            NIE.make("Y2345678Ñ") match
              case Left(error) => error == InvalidControlLetter("Ñ").toString
              case Right(_) => false
          )

        test("flipping nie letter and control letter"):
          //intercept[IllegalArgumentException](NIE("R0000001X"))
          assert(
            NIE.make("R0000001X") match
              case Left(error) => error == InvalidNieLetter("R").toString
              case Right(_) => false
          )


        test("flipping all arguments"):
          //intercept[IllegalArgumentException](NIE("0000001RX"))
          assert(
            NIE.make("0000001RX") match
              case Left(error) => error == InvalidNieLetter("0").toString
              case Right(_) => false
          )

    }

    test("IDs") {
      test("Compile false positives"):

        test("empty"):
          //intercept[NoSuchElementException](ID(""))
          assert(
            ID.make("") match
              case Left(error) => error  == InvalidInput("").toString
              case Right(_) => false
            )

        test("invisible characters"):
          //intercept[NoSuchElementException](ID("\n\r\t"))
          assert(
            ID.make("\n\r\t") match
              //case Left(error) => error  == InvalidInput("\n\r\t").toString
              // The validation now happens after the pre processing so the information is lost.
              case Left(error) => error  == InvalidInput("").toString
              case Right(_) => false
            )

        test("symbol"):
          //intercept[IllegalArgumentException](ID("@#¢∞¬÷“”≠"))
          assert(
            ID.make("@#¢∞¬÷“”≠") match
              case Left(error) => error  == InvalidInput("@#¢∞¬÷“”≠").toString
              case Right(_) => false
            )
 

        test("absent number and control letter in NIE"):
          //intercept[IllegalArgumentException](ID("Y"))
          assert(
            ID.make("Y") match
              case Left(error) => error  == InvalidNieNumber("").toString
              case Right(_) => false
            )
 
        test("invalid nie letter"):
          //intercept[IllegalArgumentException](ID("A1234567T"))
          assert(
            ID.make("A1234567T") match
              case Left(error) => error == InvalidNieLetter("A").toString
              case Right(_) => false
          )

        test("too short number"):
          //intercept[IllegalArgumentException](ID("1234567T"))
          assert(
            ID.make("1234567T") match
              case Left(error) => error == InvalidDniNumber("1234567").toString
              case Right(_) => false
          )

        test("too long number"):
          //intercept[IllegalArgumentException](ID("123456789T"))
          assert(
            ID.make("123456789T") match
              case Left(error) => error == InvalidDniNumber("123456789").toString
              case Right(_) => false
          )

        test("invalid number"):
          //intercept[IllegalArgumentException](ID("1234567AT"))
          assert(
            ID.make("1234567AT") match
              case Left(error) => error == InvalidNaN("1234567A").toString
              case Right(_) => false
          )

        test(" invalid controll letter"):
          //intercept[IllegalArgumentException](ID("Y2345678Ñ"))
          assert(
            ID.make("Y2345678Ñ") match
              case Left(error) => error == InvalidControlLetter("Ñ").toString
              case Right(_) => false
          )

      test("edge cases"):
        test("whitespace handling"):
          assert(ID.make("  12345678Z  ").map(_.toString) == Right("12345678-Z"))
          assert(ID.make("  X1234567L  ").map(_.toString) == Right("X-1234567-L"))

        test("dash handling"):
          assert(ID.make("12345678-Z").map(_.toString) == Right("12345678-Z"))
          assert(ID.make("X-1234567-L").map(_.toString) == Right("X-1234567-L"))

        test("lower case handling"):
          Seq(
            (ID.make("12345678z"), "12345678-Z"),
            (ID.make("00000001r"), "00000001-R"),
            (ID.make("99999999r"), "99999999-R"),
            (ID.make("X0000001r"), "X-0000001-R"),
            (ID.make("Y2345678z"), "Y-2345678-Z")
          ).foreach { case (input, expected) => input.foreach{result =>
            assert(result.toString == expected)
          }
          }
    }
  }
