package backend.libraries

import scala.compiletime.constValue
import scala.compiletime.error
import scala.compiletime.ops.any.ToString
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.boolean.||
import scala.compiletime.ops.any.!=
import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.<=
import scala.compiletime.ops.int.>
import scala.compiletime.ops.int.<
import scala.compiletime.ops.int.-
import scala.compiletime.ops.string.Matches
import scala.compiletime.ops.string.CharAt
import scala.compiletime.ops.string.Length
import scala.compiletime.ops.string.Substring

import scala.util.control.NoStackTrace

import neotype.*

object B_FullNeoType:



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
      
  // All posible problems
  sealed trait FailedValidation(cause: String) extends Exception with NoStackTrace:
    override def toString: String = cause  
  case class InvalidInput(input: String) extends FailedValidation(s"'$input' should be AlphaNumeric and non empty")
  case class InvalidNieLetter(nieLetter: String) extends FailedValidation(s"'$nieLetter' is not a valid NIE letter")
  case class InvalidNaN(number: String) extends FailedValidation(s"'$number' should not contain letters")
  case class InvalidDniNumber(number: String) extends FailedValidation(s"DNI number '$number' should contain 8 digits")
  case class InvalidNieNumber(number: String) extends FailedValidation(s"NIE number '$number' should contain 7 digits")
  case class InvalidControlLetter(controlLetter: String) extends FailedValidation(s"'$controlLetter' is not a valid Control letter")
  case class InvalidID(number: String, letter: String) extends FailedValidation(s"ID number '$number' does not match the control letter '$letter'")

  // Validated Fields

  type ValidInput = ValidInput.Type
  object ValidInput extends Newtype[String]:
    override def validate(input: String): Boolean | String = 
      if input.forall(_.isLetterOrDigit) && !input.isEmpty()
      then true
      else InvalidInput(input).toString

  type ValidNieLetter = ValidNieLetter.Type
  object ValidNieLetter extends Newtype[String]:
    override def validate(input: String): Boolean | String =
      if NieLetter.values.map(_.toString).contains(input)
      then true
      else InvalidNieLetter(input).toString

  type ValidNumber = ValidNumber.Type
  object ValidNumber extends Newtype[String]:
    override def validate(input: String): Boolean | String =
      if input.forall(_.isDigit)
      then true
      else InvalidNaN(input).toString  

  type ValidDniNumber = ValidDniNumber.Type
  object ValidDniNumber extends Newtype[ValidNumber]:
    override def validate(input: ValidNumber): Boolean | String = 
      if input.unwrap.length() == 8
      then true
      else InvalidDniNumber(input.unwrap).toString

  type ValidNieNumber = ValidNieNumber.Type
  object ValidNieNumber extends Newtype[ValidNumber]:
    override def validate(input: ValidNumber): Boolean | String =
      if input.unwrap.length() == 7
      then true
      else InvalidNieNumber(input.unwrap).toString

  type ValidControlLetter = ValidControlLetter.Type
  object ValidControlLetter extends Newtype[String]:
    override def validate(input: String): Boolean | String =
      if ControlLetter.values.map(_.toString).contains(input)
      then true
      else InvalidControlLetter(input).toString

  type ValidId = ValidId.Type
  object ValidId extends Newtype[(ValidNumber, ValidControlLetter)]:
    override def validate(input: (ValidNumber, ValidControlLetter)): Boolean | String =
      if ControlLetter.fromOrdinal(input._1.unwrap.toInt % 23) == ControlLetter.valueOf(input._2.unwrap)
      then true
      else InvalidID(input._1.unwrap, input._2.unwrap).toString

  type ValidDNI = ValidDNI.Type  
  object ValidDNI extends Newtype[(ValidDniNumber, ValidControlLetter)]:
    override inline def validate(input: (ValidDniNumber, ValidControlLetter)): Boolean | String =
      val idCandidate = (input._1.unwrap, input._2)
      ValidId.make(idCandidate) match
        case Left(error) => error
        case Right(value) => true    

  extension(dni: ValidDNI)
    def asString: String =
      val tupple = dni.unwrap
      s"${tupple._1}-${tupple._2}"      

  type ValidNIE = ValidNIE.Type
  object ValidNIE extends Newtype[(ValidNieLetter, ValidNieNumber, ValidControlLetter)]:
    override inline def validate(input: (ValidNieLetter, ValidNieNumber, ValidControlLetter)): Boolean | String =
      val letterOrdinal = NieLetter.valueOf(input._1.unwrap).ordinal
      val validNumber = ValidNumber.unsafeMake(s"$letterOrdinal${input._2.unwrap}")
      val idCandidate = (validNumber, input._3)
      ValidId.make(idCandidate) match
        case Left(error) => error
        case Right(value) => true

  extension(nie: ValidNIE)
    def asString: String =
      val tupple = nie.unwrap
      s"${tupple._1}-${tupple._2}-${tupple._3}"      

  sealed trait ID

  class DNI private(dniNumber: String, controlLetter: String) extends ID:
    override def toString(): String = s"$dniNumber-$controlLetter"
  object DNI:
    def make(input: String): Either[String, DNI] =
      for
        validNumber <- ValidNumber.make(input.dropRight(1))
        validDniNumber <- ValidDniNumber.make(validNumber)
        validControlLetter <- ValidControlLetter.make(input.last.toString)
        validDNI <- ValidDNI.make((validDniNumber, validControlLetter))
      yield 
        val Array(number, letter): Array[String] = validDNI.asString.split("-")
        new DNI(number, letter)

  class NIE private(nieLetter: String, nieNumber: String, controlLetter: String) extends ID:
    override def toString(): String = s"$nieLetter-$nieNumber-$controlLetter"
  object NIE:
    def make(input: String): Either[String, NIE] =
      for
        validNieLetter <- ValidNieLetter.make(input.head.toString)
        validNumber <- ValidNumber.make(input.tail.dropRight(1))
        validNieNumber <- ValidNieNumber.make(validNumber)
        validControlLetter <- ValidControlLetter.make(input.last.toString)
        validNie <- ValidNIE.make((validNieLetter, validNieNumber, validControlLetter))
      yield 
        val Array(nieLetter, number, letter): Array[String] = validNie.asString.split("-")
        new NIE(nieLetter, number, letter)
     
  // Entry point  
  object ID:
    def make(value: String): Either[String, ID] = 
      // Preprocesing the input
      val _input = 
        value
          .trim              // Handeling empty spaces around
          .replace("-", "")  // Removing dashes
          .toUpperCase()     // Handling lower case

      val validInput = ValidInput.make(_input).map(_.unwrap)

      validInput.flatMap:
        vi =>
          if vi.head.isDigit
          then DNI.make(vi)
          else NIE.make(vi)
