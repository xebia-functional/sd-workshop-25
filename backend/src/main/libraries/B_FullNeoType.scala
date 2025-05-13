package backend.libraries

import scala.util.control.NoStackTrace

import neotype.*

object B_FullNeoType:

  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with number they represent
  private[libraries] enum NieLetter:
    case X // 0
    case Y // 1
    case Z // 2
  private[libraries] object NieLetter extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = 
      if NieLetter.values.map(_.toString).contains(input)
      then true
      else InvalidNieLetter(input).toString

  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with the remainder of number divided by 23
  private[libraries] enum ControlLetter:
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
  private[libraries] object ControlLetter extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = 
      if ControlLetter.values.map(_.toString).contains(input)
      then true
      else InvalidControlLetter(input).toString
      
  // All posible problems
  private[libraries] sealed trait FailedValidation(cause: String) extends Exception with NoStackTrace:
    override def toString: String = cause  
  private[libraries] case class InvalidInput(input: String) extends FailedValidation(s"'$input' should be AlphaNumeric and non empty")
  private[libraries] case class InvalidNieLetter(nieLetter: String) extends FailedValidation(s"'$nieLetter' is not a valid NIE letter")
  private[libraries] case class InvalidNaN(number: String) extends FailedValidation(s"'$number' should not contain letters")
  private[libraries] case class InvalidDniNumber(number: String) extends FailedValidation(s"DNI number '$number' should contain 8 digits")
  private[libraries] case class InvalidNieNumber(number: String) extends FailedValidation(s"NIE number '$number' should contain 7 digits")
  private[libraries] case class InvalidControlLetter(controlLetter: String) extends FailedValidation(s"'$controlLetter' is not a valid Control letter")
  private[libraries] case class InvalidID(number: String, letter: String) extends FailedValidation(s"ID number '$number' does not match the control letter '$letter'")

  // Validated Fields

  private[libraries] type ValidInput = ValidInput.Type
  private[libraries] object ValidInput extends Newtype[String]:
    override def validate(input: String): Boolean | String = 
      if input.forall(_.isLetterOrDigit) && !input.isEmpty()
      then true
      else InvalidInput(input).toString

  private[libraries] type ValidNumber = ValidNumber.Type
  private[libraries] object ValidNumber extends Newtype[String]:
    override def validate(input: String): Boolean | String =
      if input.forall(_.isDigit)
      then true
      else InvalidNaN(input).toString  

  private[libraries] type ValidDniNumber = ValidDniNumber.Type
  private[libraries] object ValidDniNumber extends Newtype[ValidNumber]:
    override def validate(input: ValidNumber): Boolean | String = 
      if input.unwrap.length() == 8
      then true
      else InvalidDniNumber(input.unwrap).toString

  private[libraries] type ValidNieNumber = ValidNieNumber.Type
  private[libraries] object ValidNieNumber extends Newtype[ValidNumber]:
    override def validate(input: ValidNumber): Boolean | String =
      if input.unwrap.length() == 7
      then true
      else InvalidNieNumber(input.unwrap).toString

  private[libraries] type ValidId = ValidId.Type
  private[libraries] object ValidId extends Newtype[(ValidNumber, ControlLetter)]:
    override def validate(input: (ValidNumber, ControlLetter)): Boolean | String =
      if ControlLetter.fromOrdinal(input._1.unwrap.toInt % 23) == input._2
      then true
      else InvalidID(input._1.unwrap, input._2.toString).toString

  private[libraries] type ValidDNI = ValidDNI.Type  
  private[libraries] object ValidDNI extends Newtype[(ValidDniNumber, ControlLetter)]:
    override inline def validate(input: (ValidDniNumber, ControlLetter)): Boolean | String =
      val idCandidate = (input._1.unwrap, input._2)
      ValidId.make(idCandidate) match
        case Left(error) => error
        case Right(value) => true    

  private[libraries] type ValidNIE = ValidNIE.Type
  private[libraries] object ValidNIE extends Newtype[(NieLetter, ValidNieNumber, ControlLetter)]:
    override inline def validate(input: (NieLetter, ValidNieNumber, ControlLetter)): Boolean | String =
      val letterOrdinal = input._1.ordinal
      val validNumber = ValidNumber.unsafeMake(s"$letterOrdinal${input._2.unwrap}")
      val idCandidate = (validNumber, input._3)
      ValidId.make(idCandidate) match
        case Left(error) => error
        case Right(value) => true  

  private[libraries] final class DNI private(dniNumber: String, controlLetter: String):
    override def toString(): String = s"$dniNumber-$controlLetter"
  private[libraries] object DNI extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      val validDNI = for
        validNumber <- ValidNumber.make(input.dropRight(1))
        validDniNumber <- ValidDniNumber.make(validNumber)
        validControlLetter <- ControlLetter.make(input.last.toString)
      yield ValidDNI.make((validDniNumber, ControlLetter.valueOf(validControlLetter.unwrap)))
      validDNI match
        case Left(value) => value
        case Right(value) => true
        
  private[libraries] final class NIE private(nieLetter: String, nieNumber: String, controlLetter: String):
    override def toString(): String = s"$nieLetter-$nieNumber-$controlLetter"
  private[libraries] object NIE extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = 
      val validNIE = for
        validNieLetter <- NieLetter.make(input.head.toString)
        validNumber <- ValidNumber.make(input.tail.dropRight(1))
        validNieNumber <- ValidNieNumber.make(validNumber)
        validControlLetter <- ControlLetter.make(input.last.toString)
      yield ValidNIE.make(NieLetter.valueOf(validNieLetter.unwrap), validNieNumber, ControlLetter.valueOf(validControlLetter.unwrap))
      validNIE match
        case Left(value) => value
        case Right(_) => true
      
  // Entry point  
  private[libraries] object ID extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      // Preprocesing the input
      val _input = 
        input
          .trim              // Handeling empty spaces around
          .replace("-", "")  // Removing dashes
          .toUpperCase()     // Handling lower case

      // Here we lose the track of the original input for the error handling trace    
      val validInput = ValidInput.make(_input)
      
      validInput.flatMap{ vi => {
        val input = vi.unwrap
        if input.head.isDigit
        then DNI.make(input)
        else NIE.make(input)
      }} match
        case Left(error) => error.toString
        case Right(_) => true      
