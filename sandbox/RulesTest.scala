//> using target.scope test
//> using test.dep com.lihaoyi::utest:0.9.0

package sandbox

import utest.*
import sandbox.Rules.requirements.*
import scala.util.Try
import sandbox.Invariants.ControlLetter
import sandbox.Invariants.NieLetter

object RulesTest extends TestSuite:

  val tests = Tests {

    test("valid input"):
      test("happy path"):
        assert(Try(requireValidInput("a1b23c4d5")).isSuccess)
      test("unhappy path"):
        assertThrows[IllegalArgumentException](requireValidInput("_invalid_"))

    test("valid number"):
      test("happy path"):
        assert(Try(requireValidNumber("0")).isSuccess)
      test("unhappy path"):
        assertThrows[IllegalArgumentException](requireValidNumber("a"))

    test("valid DNI number"):
      test("happy path"):
        assert(Try(requireValidDniNumber("12345678")).isSuccess)
      test("unhappy path"):
        assertThrows[IllegalArgumentException](requireValidDniNumber("a"))

    test("valid NIE number"):
      test("happy path"):
        assert(Try(requireValidNieNumber("1234567")).isSuccess)
      test("unhappy path"):
        assertThrows[IllegalArgumentException](requireValidNieNumber("a"))

    test("valid NIE letter"):
      test("happy path"):
        assert(Try(requireValidNieLetter("Y")).isSuccess)
      test("unhappy path"):
        assertThrows[IllegalArgumentException](requireValidNieLetter("a"))

    test("valid Control letter"):
      test("happy path"):
        assert(Try(requireValidControlLetter("R")).isSuccess)
      test("unhappy path"):
        assertThrows[IllegalArgumentException](requireValidControlLetter("a"))

    test("valid DNI"):
      test("happy path"):
        assert(Try(requireValidDni("12345678", ControlLetter.Z)).isSuccess)
      test("unhappy path"):
        assertThrows[IllegalArgumentException](requireValidDni("12345678", ControlLetter.R))

    test("valid NIE"):
      test("happy path"):
        assert(Try(requireValidNie(NieLetter.X, "0000001", ControlLetter.R)).isSuccess)
      test("unhappy path"):
        assertThrows[IllegalArgumentException](requireValidNie(NieLetter.X, "0000001", ControlLetter.Z))

  }

end RulesTest
