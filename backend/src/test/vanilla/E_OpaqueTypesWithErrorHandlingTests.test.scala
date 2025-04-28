package backend.vanilla

import backend.vanilla.E_OpaqueTypesWithErrorHandling.*

import utest.*

object E_OpaqueTypesWithErrorHandlingTests extends TestSuite:

  val tests = Tests {

    test("DNI") {

      test("Compile positives"):
        Seq(
          (("12345678Z"), "12345678-Z"),
          (("00000001R"), "00000001-R"),
          (("99999999R"), "99999999-R")
        ).foreach { case (input, expected) =>
          DNI(input).foreach{ result =>
            assert(result.toString == expected)
        }}

      test("Compile false positives"):

        test("Too short number"):
          // intercept[IllegalArgumentException](DNI("1234567T"))
          assert(DNI("1234567T").isLeft)
  
        test("too long number"):
          //  intercept[IllegalArgumentException](DNI("123456789T"))
          assert(DNI("123456789T").isLeft)
        
        test("invalid number"):
          // intercept[IllegalArgumentException](DNI("1234567AT"))
          assert(DNI("1234567AT").isLeft)

        test("invalid control letter"):
          // intercept[IllegalArgumentException](DNI("12345678Ñ"))
          assert(DNI("12345678Ñ").isLeft)

        test("flipping arguments"):
          // intercept[IllegalArgumentException](DNI("Z12345678"))
          assert(DNI("Z12345678").isLeft)
    }

    test("NIE") {
      test("Compile positives"):
        Seq(
          (("X0000001R"), "X-0000001-R"),
          (("Y2345678Z"), "Y-2345678-Z")
        ).foreach { case (input, expected) =>
          NIE(input).foreach{ result => 
          assert(result.toString() == expected)
          }  
        }

      test("Compile false positives"):
        test("invalid nie letter"):
          //intercept[IllegalArgumentException](NIE("A1234567T"))
          assert(
            NIE("A1234567T") match
              case Left(error) => error == InvalidNieLetter("A")
              case Right(_) => false
          )


        test("too short number"):
          //intercept[IllegalArgumentException](NIE("Y", "234567", "T"))
          assert(
            NIE("Y234567T") match
              case Left(error) => error == InvalidNieNumber("234567")
              case Right(_) => false
          )

        test("too long number"):
          //intercept[IllegalArgumentException](NIE("Y", "23456789", "T"))
          assert(
            NIE("Y23456789T") match
              case Left(error) => error == InvalidNieNumber("23456789")
              case Right(_) => false
          )

        test("invalid number"):
          //intercept[IllegalArgumentException](NIE("Y", "234567A", "T"))
          assert(
            NIE("Y234567AT") match
              case Left(error) => error == InvalidNaN("234567A")
              case Right(_) => false
          )

        test("invalid controll letter"):
          //intercept[IllegalArgumentException](NIE("Y2345678Ñ"))
          assert(
            NIE("Y2345678Ñ") match
              case Left(error) => error == InvalidControlLetter("Ñ")
              case Right(_) => false
          )

        test("flipping nie letter and control letter"):
          //intercept[IllegalArgumentException](NIE("R0000001X"))
          assert(
            NIE("R0000001X") match
              case Left(error) => error == InvalidNieLetter("R")
              case Right(_) => false
          )


        test("flipping all arguments"):
          //intercept[IllegalArgumentException](NIE("0000001RX"))
          assert(
            NIE("0000001RX") match
              case Left(error) => error == InvalidNieLetter("0")
              case Right(_) => false
          )

    }

    test("IDs") {
      test("Compile false positives"):

        test("empty"):
          //intercept[NoSuchElementException](ID(""))
          assert(
            ID("") match
              case Left(error) => error  == InvalidInput("")
              case Right(_) => false
            )

        test("invisible characters"):
          //intercept[NoSuchElementException](ID("\n\r\t"))
          assert(
            ID("\n\r\t") match
              case Left(error) => error  == InvalidInput("\n\r\t")
              case Right(_) => false
            )

        test("symbol"):
          //intercept[IllegalArgumentException](ID("@#¢∞¬÷“”≠"))
          assert(
            ID("@#¢∞¬÷“”≠") match
              case Left(error) => error  == InvalidInput("@#¢∞¬÷“”≠")
              case Right(_) => false
            )
 

        test("absent number and control letter in NIE"):
          //intercept[IllegalArgumentException](ID("Y"))
          assert(
            ID("Y") match
              case Left(error) => error  == InvalidNieNumber("")
              case Right(_) => false
            )
 
        test("invalid nie letter"):
          //intercept[IllegalArgumentException](ID("A1234567T"))
          assert(
            ID("A1234567T") match
              case Left(error) => error == InvalidNieLetter("A")
              case Right(_) => false
          )

        test("too short number"):
          //intercept[IllegalArgumentException](ID("1234567T"))
          assert(
            ID("1234567T") match
              case Left(error) => error == InvalidDniNumber("1234567")
              case Right(_) => false
          )

        test("too long number"):
          //intercept[IllegalArgumentException](ID("123456789T"))
          assert(
            ID("123456789T") match
              case Left(error) => error == InvalidDniNumber("123456789")
              case Right(_) => false
          )

        test("invalid number"):
          //intercept[IllegalArgumentException](ID("1234567AT"))
          assert(
            ID("1234567AT") match
              case Left(error) => error == InvalidNaN("1234567A")
              case Right(_) => false
          )

        test(" invalid controll letter"):
          //intercept[IllegalArgumentException](ID("Y2345678Ñ"))
          assert(
            ID("Y2345678Ñ") match
              case Left(error) => error == InvalidControlLetter("Ñ")
              case Right(_) => false
          )

      test("edge cases"):
        test("whitespace handling"):
          assert(ID("  12345678Z  ").map(_.toString) == Right("12345678-Z"))
          assert(ID("  X1234567L  ").map(_.toString) == Right("X-1234567-L"))

        test("dash handling"):
          assert(ID("12345678-Z").map(_.toString) == Right("12345678-Z"))
          assert(ID("X-1234567-L").map(_.toString) == Right("X-1234567-L"))

        test("lower case handling"):
          Seq(
            ("12345678z", "12345678-Z"),
            ("00000001r", "00000001-R"),
            ("99999999r", "99999999-R"),
            ("X0000001r", "X-0000001-R"),
            ("Y2345678z", "Y-2345678-Z")
          ).foreach { case (input, expected) =>
            val result = ID(input)
            assert(result.map(_.toString) == Right(expected))
          }
    }
  }
