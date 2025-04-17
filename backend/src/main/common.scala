package backend

import scala.util.control.NoStackTrace

object common:

  val controlLetter: Map[Int, String] = Map(
    0 -> "T",
    1 -> "R",
    2 -> "W",
    3 -> "A",
    4 -> "G",
    5 -> "M",
    6 -> "Y",
    7 -> "F",
    8 -> "P",
    9 -> "D",
    10 -> "X",
    11 -> "B",
    12 -> "N",
    13 -> "J",
    14 -> "Z",
    15 -> "S",
    16 -> "Q",
    17 -> "V",
    18 -> "H",
    19 -> "L",
    20 -> "C",
    21 -> "K",
    22 -> "E"
  )

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
  case class InvalidNieLetter(wrongInput: String) extends FailedValidation(s"'$wrongInput' is not a valid NIE letter")
  case class InvalidIdLetter(wrongInput: String) extends FailedValidation(s"'$wrongInput' is not a valid ID letter")
  case class InvalidControlLetter(wrongInput: String)
      extends FailedValidation(
        s"'$wrongInput' does not match the associated remainder letter"
      )
  case class InvalidNumber(wrongInput: String) extends FailedValidation(s"'$wrongInput' should only contain digits")
  case class InvalidNegativeNumber(wrongInput: Int)
      extends FailedValidation(
        s"'$wrongInput' is negative. It must be positive"
      )
  case class InvalidTooBigNumber(wrongInput: Int)
      extends FailedValidation(
        s"'$wrongInput' is too big. Max number is 99999999"
      )
  case class InvalidIdTooLong(wrongInput: String)
      extends FailedValidation(
        s"'$wrongInput' is too long. Max amount of characters is 9"
      )
