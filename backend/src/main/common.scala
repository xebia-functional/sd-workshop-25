package backend

import scala.util.control.NoStackTrace
import scala.compiletime.constValue
import scala.compiletime.error
import scala.compiletime.ops.string.Matches

object common:
  
  trait ID:
    def pretty: String

  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with number they represent
  enum NieLetter:
    case X // 0
    case Y // 1
    case Z // 2

  object NieLetter:
    
    inline def apply(letter: String): NieLetter =
      inline if constValue[Matches[letter.type, "[XYZ]{1}"]]
      then NieLetter.valueOf(letter)
      else error("'" + constValue[letter.type] + "' is not a valid NIE letter") 
    
    def either(letter: String): Either[InvalidNieLetter, NieLetter] =
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

    inline def apply(letter: String): ControlLetter =
      inline if constValue[Matches[letter.type, "[TRWAGMYFPDXBNJZSQVHLCKE]{1}"]]
      then ControlLetter.valueOf(letter)
      else error("'" + constValue[letter.type] + "' is not a valid Control letter")

    def either(letter: String): Either[InvalidControlLetter, ControlLetter] =
      Either.cond(
        ControlLetter.values.map(_.toString).contains(letter),
        ControlLetter.valueOf(letter),
        InvalidControlLetter(letter)
      )  

   // All posible problems
  sealed trait FailedValidation(cause: String) extends Exception with NoStackTrace:
    override def toString: String = cause  
  case class InvalidInput(input: String) extends FailedValidation(s"Invalid Input: '$input' should be AlphaNumeric and non empty")
  case class InvalidNaN(number: String) extends FailedValidation(s"Invalid Number: '$number' should not contain letters")
  case class InvalidDniNumber(dniNumber: String) extends FailedValidation(s"Invalid DNI Number: '$dniNumber' should contain 8 digits")
  case class InvalidNieNumber(nieNumber: String) extends FailedValidation(s"Invalid NIE Number: '$nieNumber' should contain 7 digits")
  case class InvalidNieLetter(nieLetter: String) extends FailedValidation(s"Invalid NIE Letter: '$nieLetter' is not a valid NIE letter")
  case class InvalidControlLetter(controlLetter: String) extends FailedValidation(s"Invalid ControlLetter: '$controlLetter' is not a valid Control letter")
  case class InvalidDni(dniNumber: String, letter: String) extends FailedValidation(s"Invalid DNI: '$dniNumber' does not match the control letter '$letter'")  
  case class InvalidNie(nieLetter: String, nieNumber: String, letter: String) extends FailedValidation(s"Invalid NIE: '$nieLetter-$nieNumber' does not match the control letter '$letter'")  
    