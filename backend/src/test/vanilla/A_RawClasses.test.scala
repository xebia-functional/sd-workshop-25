//> using test.dep com.lihaoyi::utest:0.8.5
package backend.vanilla

import backend.common.*
import backend.vanilla.A_RawClasses.*

import utest.*

object IDTests extends TestSuite:

  val tests = Tests {

    test("ID Object"):
      test("DNI parsing") {
        test("valid formats"):
          Seq(
            ("12345678Z", "12345678-Z"),
            ("12345678z", "12345678-Z")
          ).foreach { case (input, expected) =>
            val result = ID(input)
            assert(result.toString == expected)
          }

        test("invalid formats"):
          intercept[IllegalArgumentException]:
            ID("1234567T") // too short number

          intercept[IllegalArgumentException]:
            ID("123456789T") // too long number

          intercept[IllegalArgumentException]:
            ID("1234567AT") // invalid number
      }

      test("NIE parsing") {
        test("valid formats"):
          Seq(
            ("X1234567L", "X-1234567-L"),
            ("x1234567l", "X-1234567-L"),
            ("Y1234567X", "Y-1234567-X"),
            ("y1234567x", "Y-1234567-X"),
            ("Z1234567R", "Z-1234567-R"),
            ("z1234567r", "Z-1234567-R")
          ).foreach { case (input, expected) =>
            val result = ID(input)
            assert(result.toString == expected)
          }

        test("invalid formats"):
          intercept[IllegalArgumentException]:
            ID("X123456T") // too short number

          intercept[IllegalArgumentException]:
            ID("X12345678T") // too long number

          intercept[IllegalArgumentException]:
            ID("X123456AT") // invalid number

          intercept[IllegalArgumentException]:
            ID("A1234567T") // invalid NIE letter
      }

      test("edge cases") {
        test("whitespace handling"):
          assert(ID("  12345678Z  ").toString == "12345678-Z")
          assert(ID("  X1234567L  ").toString == "X-1234567-L")

        test("dash handling"):
          assert(ID("12345678-Z").toString == "12345678-Z")
          assert(ID("X-1234567-L").toString == "X-1234567-L")
      }
  }

