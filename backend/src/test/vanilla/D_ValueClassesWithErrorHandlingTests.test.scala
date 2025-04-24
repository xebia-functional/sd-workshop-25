//> using test.dep com.lihaoyi::utest:0.8.5

package backend.vanilla

import backend.common.*
import backend.vanilla.D_ValueClassesWithErrorHandling.*

import utest.*

object D_ValueClassesWithErrorHandlingTests extends TestSuite:

  val tests = Tests {
    test("DNI") {

      test("Compile positives"):
        Seq(
          (("12345678", "Z"), "12345678-Z"),
          (("00000001", "R"), "00000001-R"),
          (("99999999", "R"), "99999999-R")
        ).foreach { case (input, expected) =>
          val result = for
            dniNumber <- DniNumber(input._1)
            letter <- Letter(input._2)
          yield DNI(dniNumber, letter)
          result.foreach(dni => assert(dni.toString == expected))
        }

      test("Compile false positives"):

        test("Too short number"):
          //  intercept[IllegalArgumentException](DNI(DniNumber("1234567"), Letter("T")))
          val result = for
            number <- DniNumber("1234567")
            letter <- Letter("T")
          yield DNI(number, letter)
          result match
            case Left(error) => assert(error == "number 1234567 should contain 8 digits")
            case Right       => assert(false)

        test("too long number"):
          //  intercept[IllegalArgumentException](DNI(DniNumber("123456789"), Letter("T")))
          val result = for
            number <- DniNumber("123456789")
            letter <- Letter("T")
          yield DNI(number, letter)
          result match
            case Left(error) => assert(error == "number 123456789 should contain 8 digits")
            case Right       => assert(false)

        test("invalid number"):
          //  intercept[IllegalArgumentException](DNI(DniNumber("1234567A"), Letter("T")))
          val result = for
            number <- DniNumber("1234567A")
            letter <- Letter("T")
          yield DNI(number, letter)
          result match
            case Left(error) => assert(error == "number 1234567A should not contain letters")
            case Right       => assert(false)

        test("invalid control letter"):
          //  intercept[IllegalArgumentException](DNI(DniNumber("12345678"), Letter("Ñ")))
          val result = for
            number <- DniNumber("12345678")
            letter <- Letter("Ñ")
          yield DNI(number, letter)
          result match
            case Left(error) => assert(error == "'Ñ' is not a valid ID letter")
            case Right       => assert(false)
        // test("flipping arguments"):
        //  intercept[IllegalArgumentException](DNI(Letter("Z"), DniNumber("12345678")))

    }

    test("NIE") {
      test("Compile positives"):
        Seq(
          (("X", "0000001", "R"), "X-0000001-R"),
          (("Y", "2345678", "Z"), "Y-2345678-Z")
        ).foreach { case (input, expected) =>
          val result = for
            nieLetter <- NIELetter(input._1)
            number <- NieNumber(input._2)
            letter <- Letter(input._3)
          yield NIE(nieLetter, number, letter)
          result.foreach(nie => assert(nie.toString == expected))

        }

      test("Compile false positives"):

        test("invalid nie letter"):
          //  intercept[IllegalArgumentException](NIE(NIELetter("A"), NieNumber("1234567"), Letter("T")))
          val result = for
            nieLetter <- NIELetter("A")
            number <- NieNumber("1234567")
            letter <- Letter("T")
          yield NIE(nieLetter, number, letter)
          result match
            case Left(error) => assert(error == "'A' is not a valid NIE letter")
            case Right       => assert(false)

        test("too short number"):
          //  intercept[IllegalArgumentException](NIE(NIELetter("Y"), NieNumber("234567"), Letter("T")))
          val result = for
            nieLetter <- NIELetter("Y")
            number <- NieNumber("234567")
            letter <- Letter("T")
          yield NIE(nieLetter, number, letter)
          result match
            case Left(error) => assert(error == "number 234567 should contain 7 digits")
            case Right       => assert(false)

        test("too long number"):
          //  intercept[IllegalArgumentException](NIE(NIELetter("Y"), NieNumber("23456789"), Letter("T")))
          val result = for
            nieLetter <- NIELetter("Y")
            number <- NieNumber("23456789")
            letter <- Letter("T")
          yield NIE(nieLetter, number, letter)
          result match
            case Left(error) => assert(error == "number 23456789 should contain 7 digits")
            case Right       => assert(false)

        test("invalid number"):
          //  intercept[IllegalArgumentException](NIE(NIELetter("Y"), NieNumber("234567A"), Letter("T")))
          val result = for
            nieLetter <- NIELetter("Y")
            number <- NieNumber("234567A")
            letter <- Letter("T")
          yield NIE(nieLetter, number, letter)
          result match
            case Left(error) => assert(error == "number 234567A should not contain letters")
            case Right       => assert(false)

        test("invalid controll letter"):
          //  intercept[IllegalArgumentException](NIE(NIELetter("Y"), NieNumber("2345678"), Letter("Ñ")))
          val result = for
            nieLetter <- NIELetter("Y")
            number <- NieNumber("2345678")
            letter <- Letter("Ñ")
          yield NIE(nieLetter, number, letter)
          result match
            case Left(error) => assert(error == "'Ñ' is not a valid ID letter")
            case Right       => assert(false)

        // test("flipping nie letter and control letter"):
        //  intercept[IllegalArgumentException](NIE(Letter("R"), NieNumber("0000001"), NIELetter("X")))

        // test("flipping all arguments"):
        //  intercept[IllegalArgumentException](NIE(NieNumber("0000001"), Letter("R"), NIELetter("X")))
    }

    test("IDs") {
      test("Compile false positives"):

        test("empty"):
          intercept[NoSuchElementException](ID(""))

        test("invisible characters"):
          intercept[NoSuchElementException](ID("\n\r\t"))

        test("symbol"):
          //  intercept[IllegalArgumentException](ID("@#¢∞¬÷“”≠"))
          ID("@#¢∞¬÷“”≠") match
            case Left(error) => assert(error == "'@' is not a valid NIE letter")
            case Right       => assert(false)

        test("absent number and control letter in NIE"):
          //  intercept[IllegalArgumentException](ID("Y"))
          ID("Y") match
            case Left(error) => assert(error == "number  should contain 7 digits")
            case Right       => assert(false)

        test("invalid nie letter"):
          //  intercept[IllegalArgumentException](ID("A1234567T"))
          ID("A1234567T") match
            case Left(error) => assert(error == "'A' is not a valid NIE letter")
            case Right       => assert(false)

        test("too short number"):
          //  intercept[IllegalArgumentException](ID("1234567T"))
          ID("1234567T") match
            case Left(error) => assert(error == "number 1234567 should contain 8 digits")
            case Right       => assert(false)

        test("too long number"):
          //  intercept[IllegalArgumentException](ID("123456789T"))
          ID("123456789T") match
            case Left(error) => assert(error == "number 123456789 should contain 8 digits")
            case Right       => assert(false)

        test("invalid number"):
          //  intercept[IllegalArgumentException](ID("1234567AT"))
          ID("1234567AT") match
            case Left(error) => assert(error == "number 1234567A should not contain letters")
            case Right       => assert(false)

        test("invalid controll letter"):
          //  intercept[IllegalArgumentException](ID("Y2345678Ñ"))
          ID("Y2345678Ñ") match
            case Left(error) => assert(error == "'Ñ' is not a valid ID letter")
            case Right       => assert(false)
          // assert(ID("Y2345678Ñ").isLeft)

      test("edge cases"):
        test("whitespace handling"):
          Seq(
            ("  12345678Z  ", "12345678-Z"),
            ("  X1234567L  ", "X-1234567-L")
          ).foreach { case (input, expected) =>
            val result = ID(input)
            result.foreach(id => assert(id.toString == expected))
          }

        test("dash handling"):
          Seq(
            ("12345678-Z", "12345678-Z"),
            ("X-1234567-L", "X-1234567-L")
          ).foreach { case (input, expected) =>
            val result = ID(input)
            result.foreach(id => assert(id.toString == expected))
          }

        test("lower case handling"):
          Seq(
            ("12345678z", "12345678-Z"),
            ("00000001r", "00000001-R"),
            ("99999999r", "99999999-R"),
            ("X0000001r", "X-0000001-R"),
            ("Y2345678z", "Y-2345678-Z")
          ).foreach { case (input, expected) =>
            val result = ID(input)
            result.foreach(id => assert(id.toString == expected))
          }
    }
  }
