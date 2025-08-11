package sandbox

import sandbox.Invariants.ControlLetter
import sandbox.Invariants.NieLetter

object Errors:

  /**
   * Come up with tailored error messages you want to give back to the user in the UI
   */
  object messages:
    def invalidInput(input: String): String = ???
    def invalidNumber(number: String): String = ???
    def invalidDniNumber(dniNumber: String): String = ???
    def invalidNieNumber(nieNumber: String): String = ???
    def invalidNieLetter(nieLetter: String): String = ???
    def invalidControlLetter(controlLetter: String): String = ???
    def invalidDni(dniNumber: String, letter: ControlLetter): String = ???
    def invalidNie(nieLetter: NieLetter, nieNumber: String, letter: ControlLetter): String = ???

  end messages

  /**
   * Codify those error messages into requirements
   */
  object requirements:

    import messages.*

    def requireValidInput(input: String): Unit = ???
    def requireValidNumber(number: String): Unit = ???
    def requireValidDniNumber(dniNumber: String): Unit = ???
    def requireValidNieNumber(nieNumber: String): Unit = ???
    def requireValidNieLetter(nieLetter: String): Unit = ???
    def requireValidControlLetter(controlLetter: String): Unit = ???
    def requireValidDni(dniNumber: String, letter: ControlLetter): Unit = ???
    def requireValidNie(nieLetter: NieLetter, nieNumber: String, letter: ControlLetter): Unit = ???

  end requirements

end Errors
