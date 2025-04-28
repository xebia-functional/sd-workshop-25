package backend

import scala.util.control.NoStackTrace

object common:


  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with the remainder of number divided by 23
  enum NieLetter:
    case X // 0
    case Y // 1
    case Z // 2

  object NieLetter:
    def parse(letter: String): Either[InvalidNieLetter, NieLetter] =
      Either.cond(
        NieLetter.values.map(_.toString).contains(letter),
        NieLetter.valueOf(letter),
        InvalidNieLetter(letter)
      )

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

  object ControlLetter:
    def parse(letter: String): Either[InvalidControlLetter, ControlLetter] =
      Either.cond(
        ControlLetter.values.map(_.toString).contains(letter),
        ControlLetter.valueOf(letter),
        InvalidControlLetter(letter)
      )

    def isValidId(number: Int, letter: ControlLetter): Boolean =
      ControlLetter.fromOrdinal(number % 23) == letter

  sealed trait FailedValidation(cause: String) extends Exception with NoStackTrace:
    override def toString: String = cause
  
  case class InvalidNieLetter(nieLetter: String) extends FailedValidation(s"'$nieLetter' is not a valid NIE letter")
  case class InvalidNaN(number: String) extends FailedValidation(s"'$number' should only contain digits")
  case class InvalidDniNumber(dniNumber: String) extends FailedValidation(s"'$dniNumber' should contains 8 digits.")
  case class InvalidNieNumber(nieNumber: String) extends FailedValidation(s"'$nieNumber' should contains 7 digits.")
  case class InvalidControlLetter(controlLetter: String) extends FailedValidation(s"'$controlLetter' is not a valid Control letter")
  case class InvalidID(id: String) extends FailedValidation(s"'$id' is invalid. Number does not match the associated control letter")
