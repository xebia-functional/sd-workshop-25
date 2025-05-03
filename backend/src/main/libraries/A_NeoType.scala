package backend.libraries

import scala.util.control.NoStackTrace

import neotype.*

object A_NeoType:

  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with number they represent
  enum NieLetter:
    case X // 0
    case Y // 1
    case Z // 2

  type NieLetterNT = NieLetterNT.Type  
  object NieLetterNT extends Newtype[String]:
    override inline def validate(value: String): Boolean | String =
      if NieLetter.values.map(_.toString).contains(value)
      then true
      else InvalidNieLetter(value).toString

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

  type ControlLetterNT = ControlLetterNT.Type
  object ControlLetterNT extends Newtype[String]:
    override inline def validate(value: String): Boolean | String =
      if ControlLetter.values.map(_.toString).contains(value)
      then true
      else InvalidControlLetter(value).toString
      
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

  type NieNumber = NieNumber.Type
  object NieNumber extends Newtype[String]:
    override inline def validate(value: String): Boolean | String =
      if !value.forall(_.isDigit) then InvalidNaN(value).toString
      else if value.length != 7 then InvalidNieNumber(value).toString
      else true

  type DniNumber = DniNumber.Type
  object DniNumber extends Newtype[String]:
    override inline def validate(number: String): Boolean | String =
      if !number.forall(_.isDigit) then InvalidNaN(number).toString
      else if number.length != 8 then InvalidDniNumber(number).toString
      else true

  sealed trait ID

  final class DNI private (number: DniNumber, letter: ControlLetterNT) extends ID:
    val _number = number.toString.toInt
    require(
      ControlLetter.fromOrdinal(_number % 23) == ControlLetter.valueOf(letter.toString),
      s"DNI number '$number' does not match the control letter '$letter'"
    )
    override def toString: String = s"$number-$letter"

  object DNI:
    def apply(input: String): Either[String, DNI] =
      val (number, letter) = input.splitAt(input.length - 1)
      for
        _number <- DniNumber.make(number)
        _letter <- ControlLetterNT.make(letter)
      yield new DNI(_number, _letter)

  final class NIE private (nieLetter: NieLetterNT, number: NieNumber, letter: ControlLetterNT) extends ID:
    val ordinalOfNIE = NieLetter.valueOf(nieLetter.toString).ordinal // Extracts the number representation of the NIE Letter
    val _number =
      s"$ordinalOfNIE${number.toString}".toInt // Appends the number representation from NIE Letter to the number
    require(
      ControlLetter.fromOrdinal(_number % 23) == ControlLetter.valueOf(letter.toString),
      s"NIE number '$number' does not match the control letter '$letter'"
    )
    override def toString: String = s"$nieLetter-$number-$letter"

  object NIE:
    def apply(input: String): Either[String, NIE] =
      val nieLetter = input.head.toString
      val (number, letter) = input.tail.splitAt(input.tail.length - 1)
      for
        _nieLetter <- NieLetterNT.make(nieLetter)
        _number <- NieNumber.make(number)
        _letter <- ControlLetterNT.make(letter)
      yield new NIE(_nieLetter, _number, _letter)

  object ID:
    def apply(input: String): Either[String, ID] = 
      
      // Preprocesing the input
      val _input = 
        input
          .trim              // Handeling empty spaces around
          .replace("-", "")  // Removing dashes
          .toUpperCase()     // Handling lower case 
      if _input.isEmpty || _input.forall(!_.isLetterOrDigit)
      then Left(InvalidInput(input).toString)
      else
        // Validating the cleaned input
        require(!_input.isEmpty)
        require(_input.forall(_.isLetterOrDigit))
      
        // Selecting which type of ID base on initial character type - Letter or Digit
        if _input.head.isDigit // Splitting between DNI and NIE
        then DNI(_input)
        else NIE(_input)
        
