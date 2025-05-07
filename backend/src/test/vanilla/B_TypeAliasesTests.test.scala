package backend.vanilla

import backend.common.*
import backend.vanilla.B_TypeAliases.*

import utest.*

object B_TypeAliasesTests extends TestSuite:

  val tests = Tests {

    test("DNI") {

      test("Runtime positives"):
        Seq(
          ("12345678Z", "12345678-Z"),
          ("00000001R", "00000001-R"),
          ("99999999R", "99999999-R")
        ).foreach { case (input, expected) =>
          val result = DNI(input)
          assert(result.pretty == expected)
        }

      test("Runtime negatives"):

        test("Too short number"):
          intercept[IllegalArgumentException](DNI("1234567T"))

        test("too long number"):
          intercept[IllegalArgumentException](DNI("123456789T"))

        test("invalid number"):
          intercept[IllegalArgumentException](DNI("1234567AT"))

        test("invalid control letter"):
          intercept[IllegalArgumentException](DNI("12345678Ñ"))

        test("flipping arguments"):
          intercept[IllegalArgumentException](DNI("Z12345678"))
    }

    test("NIE") {
      test("Runtime positives"):
        Seq(
          ("X0000001R", "X-0000001-R"),
          ("Y2345678Z", "Y-2345678-Z")
        ).foreach { case (input, expected) =>
          val result = NIE(input)
          assert(result.pretty == expected)
        }

      test("Runtime negatives"):

        test("invalid nie letter"):
          intercept[IllegalArgumentException](NIE("A1234567T"))

        test("too short number"):
          intercept[IllegalArgumentException](NIE("Y234567T"))

        test("too long number"):
          intercept[IllegalArgumentException](NIE("Y23456789T"))

        test("invalid number"):
          intercept[IllegalArgumentException](NIE("Y234567AT"))

        test("invalid controll letter"):
          intercept[IllegalArgumentException](NIE("Y2345678Ñ"))

        test("flipping nie letter and control letter"):
          intercept[IllegalArgumentException](NIE("R0000001X"))

        test("flipping all arguments"):
          intercept[IllegalArgumentException](NIE("0000001RX"))
    }

    test("IDs") {

     test("Runtime positives"):
      test("whitespace handling"):
        Seq(
          ("  12345678Z  ", "12345678-Z"),
          ("  X1234567L  ", "X-1234567-L")
        ).foreach{ case (input, expected) =>
          val result = ID(input)
          assert(result.pretty == expected)  
        }

      test("dash handling"):
        Seq(
          ("12345678-Z", "12345678-Z"),
          ("X-1234567-L", "X-1234567-L")
        ).foreach{ case (input, expected) =>
          val result = ID(input)
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
          val result = ID(input)
          assert(result.pretty == expected)
        }

      test("Runtime negatives"):

        test("empty"):
          intercept[IllegalArgumentException](ID(""))

        test("invisible characters"):
          intercept[IllegalArgumentException](ID("\n\r\t"))
        
        test("symbol"):
          intercept[IllegalArgumentException](ID("@#¢∞¬÷“”≠"))

        test("absent number and control letter in NIE"):
          intercept[IllegalArgumentException](ID("Y"))

        test("invalid nie letter"):
          intercept[IllegalArgumentException](ID("A1234567T"))

        test("too short number"):
          intercept[IllegalArgumentException](ID("1234567T"))

        test("too long number"):
          intercept[IllegalArgumentException](ID("123456789T"))

        test("invalid number"):
          intercept[IllegalArgumentException](ID("1234567AT"))

        test(" invalid controll letter"):
          intercept[IllegalArgumentException](ID("Y2345678Ñ"))

    }
  }
