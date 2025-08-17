package domain

import scala.util.control.NoStackTrace

trait ID:
  def formatted: String

object invariants:
  
  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with number they represent
  enum NieLetter:
    case X // 0
    case Y // 1
    case Z // 2

  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with the remainder of number divided by 23
  enum ControlLetter:
    case T // 0
    case R // 1
    case W // 2
    case A // 3
    case G // 4
    case M // 5
    case Y // 6
    case F // 7
    case P // 8
    case D // 9
    case X // 10
    case B // 11
    case N // 12
    case J // 13
    case Z // 14
    case S // 15
    case Q // 16
    case V // 17
    case H // 18
    case L // 19
    case C // 20
    case K // 21
    case E // 22
  
end invariants

object rules:

  import invariants.*
  // All error messages as functions
  def invalidInput(input: String): String =
    s"Invalid Input: '$input' should be AlphaNumeric and have 9 characters"
  def invalidNumber(number: String): String = s"Invalid Number: '$number' should not contain letters"
  def invalidDniNumber(dniNumber: String): String = s"Invalid DNI Number: '$dniNumber' should contain 8 digits"
  def invalidNieNumber(nieNumber: String): String = s"Invalid NIE Number: '$nieNumber' should contain 7 digits"
  def invalidNieLetter(nieLetter: String): String =
    s"Invalid NIE Letter: '$nieLetter' is not a valid NIE letter"
  def invalidControlLetter(controlLetter: String): String =
    s"Invalid ControlLetter: '$controlLetter' is not a valid Control letter"
  def invalidDni(dniNumber: String, letter: ControlLetter): String =
    s"Invalid DNI: '$dniNumber' does not match the control letter '$letter'"
  def invalidNie(nieLetter: NieLetter, nieNumber: String, letter: ControlLetter): String =
    s"Invalid NIE: '$nieLetter-$nieNumber' does not match the control letter '$letter'"

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

end rules
