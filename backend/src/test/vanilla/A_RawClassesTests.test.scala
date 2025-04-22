//> using test.dep com.lihaoyi::utest:0.8.5

package backend.vanilla

import backend.common.*
import backend.vanilla.A_RawClasses.*

import utest.*

object A_RawClassesTests extends TestSuite:

  val tests = Tests {

    test("DNI") {

      test("Compile positives"):
        Seq(
          (("12345678", "Z"), "12345678-Z"),
          (("00000001", "R"), "00000001-R"),
          (("99999999", "R"), "99999999-R")
        ).foreach { case (input, expected) =>
          val result = DNI(input._1, input._2)
          assert(result.toString == expected)
        }

      test("Compile false positives"):

        test("Too short number"):
          intercept[IllegalArgumentException](DNI("1234567", "T"))

        test("too long number"):
          intercept[IllegalArgumentException](DNI("123456789", "T"))

        test("invalid number"):
          intercept[IllegalArgumentException](DNI("1234567A", "T"))

        test("invalid control letter"):
          intercept[IllegalArgumentException](DNI("12345678", "Ñ"))

        test("flipping arguments"):
          intercept[IllegalArgumentException](DNI("Z", "12345678"))
    }

    test("NIE") {
      test("Compile positives"):
        Seq(
          (("X", "0000001", "R"), "X-0000001-R"),
          (("Y", "2345678", "Z"), "Y-2345678-Z")
        ).foreach { case (input, expected) =>
          val result = NIE(input._1, input._2, input._3)
          assert(result.toString == expected)
        }

      test("Compile false positives"):

        test("invalid nie letter"):
          intercept[IllegalArgumentException](NIE("A", "1234567", "T"))

        test("too short number"):
          intercept[IllegalArgumentException](NIE("Y", "234567", "T"))

        test("too long number"):
          intercept[IllegalArgumentException](NIE("Y", "23456789", "T"))

        test("invalid number"):
          intercept[IllegalArgumentException](NIE("Y", "234567A", "T"))

        test("invalid controll letter"):
          intercept[IllegalArgumentException](NIE("Y", "2345678", "Ñ"))

        test("flipping nie letter and control letter"):
          intercept[IllegalArgumentException](NIE("R", "0000001", "X"))

        test("flipping all arguments"):
          intercept[IllegalArgumentException](NIE("0000001", "R", "X"))
    }

    test("IDs") {
      test("Compile false positives"):

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

      test("edge cases"):
        test("whitespace handling"):
          assert(ID("  12345678Z  ").toString == "12345678-Z")
          assert(ID("  X1234567L  ").toString == "X-1234567-L")

        test("dash handling"):
          assert(ID("12345678-Z").toString == "12345678-Z")
          assert(ID("X-1234567-L").toString == "X-1234567-L")

        test("lower case handling"):
          Seq(
            ("12345678z", "12345678-Z"),
            ("00000001r", "00000001-R"),
            ("99999999r", "99999999-R"),
            ("X0000001r", "X-0000001-R"),
            ("Y2345678z", "Y-2345678-Z")
          ).foreach { case (input, expected) =>
            val result = ID(input)
            assert(result.toString == expected)
          }
    }
  }
