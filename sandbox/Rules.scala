package sandbox

import sandbox.Invariants.ControlLetter
import sandbox.Invariants.NieLetter

object Rules:

  /**
   * Come up with tailored error messages you want to give back to the user in the UI
   */
  object messages:

    def invalidInput(input: String): String = s"Invalid Input: '$input' should be AlphaNumeric and have 9 characters"
    def invalidNumber(number: String): String = s"Invalid Number: '$number' should not contain letters"
    def invalidDniNumber(dniNumber: String): String = s"Invalid DNI Number: '$dniNumber' should contain 8 digits"
    def invalidNieNumber(nieNumber: String): String = s"Invalid NIE Number: '$nieNumber' should contain 7 digits"
    def invalidNieLetter(nieLetter: String): String = s"Invalid NIE Letter: '$nieLetter' is not a valid NIE letter"
    def invalidControlLetter(controlLetter: String): String = s"Invalid ControlLetter: '$controlLetter' is not a valid Control letter"
    def invalidDni(dniNumber: String, letter: ControlLetter): String = s"Invalid DNI: '$dniNumber' does not match the control letter '$letter'"
    def invalidNie(nieLetter: NieLetter, nieNumber: String, letter: ControlLetter): String = s"Invalid NIE: '$nieLetter-$nieNumber' does not match the control letter '$letter'"
  end messages

  /**
   * Codify those error messages into requirements
   */
  object requirements:

    import messages.*

    // All validations
    def requireValidInput(input: String): Unit =
      require(input.length == 9 && input.forall(_.isLetterOrDigit), invalidInput(input))
    def requireValidNumber(number: String): Unit =
      require(number.forall(_.isDigit), invalidNumber(number))
    def requireValidDniNumber(dniNumber: String): Unit =
      require(dniNumber.length == 8, invalidDniNumber(dniNumber))
    def requireValidNieNumber(nieNumber: String): Unit =
      require(nieNumber.length == 7, invalidNieNumber(nieNumber))
    def requireValidNieLetter(nieLetter: String): Unit =
      require(NieLetter.values.map(_.toString).contains(nieLetter), invalidNieLetter(nieLetter))
    def requireValidControlLetter(controlLetter: String): Unit =
      require(ControlLetter.values.map(_.toString).contains(controlLetter), invalidControlLetter(controlLetter))
    def requireValidDni(dniNumber: String, letter: ControlLetter): Unit =
      require(dniNumber.toInt % 23 == letter.ordinal, invalidDni(dniNumber, letter))
    def requireValidNie(nieLetter: NieLetter, nieNumber: String, letter: ControlLetter): Unit =
      require(
        s"${nieLetter.ordinal}$nieNumber".toInt % 23 == letter.ordinal,
        invalidNie(nieLetter, nieNumber, letter)
      )
      
  end requirements

end Rules
