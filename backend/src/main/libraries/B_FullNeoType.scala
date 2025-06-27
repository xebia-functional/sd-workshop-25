package backend.libraries

import scala.util.control.NoStackTrace
import backend.common.*
import neotype.*

object B_FullNeoType:

  // Define additional objects for NieLetter and ControlLetter.
  // Companion objects already exists for the enums at common.scala
  private object NieLetterNT extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = 
      if NieLetter.values.map(_.toString).contains(input)
      then true
      else InvalidNieLetter(input).cause

  private object ControlLetterNT extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = 
      if ControlLetter.values.map(_.toString).contains(input)
      then true
      else InvalidControlLetter(input).cause

  // Additional Invalidation
  private [libraries] case class InvalidID(number: String, letter: String) extends FailedValidation(s"ID number '$number' does not match the control letter '$letter'")    

  // Validated Fields
  private type ValidInput = ValidInput.Type
  private object ValidInput extends Newtype[String]:
    override def validate(input: String): Boolean | String = 
      if input.forall(_.isLetterOrDigit) && !input.isEmpty()
      then true
      else InvalidInput(input).cause

  private type ValidNumber = ValidNumber.Type
  private object ValidNumber extends Newtype[String]:
    override def validate(input: String): Boolean | String =
      if input.forall(_.isDigit)
      then true
      else InvalidNumber(input).cause  

  private type ValidDniNumber = ValidDniNumber.Type
  private object ValidDniNumber extends Newtype[ValidNumber]:
    override def validate(input: ValidNumber): Boolean | String = 
      if input.unwrap.length() == 8
      then true
      else InvalidDniNumber(input.unwrap).cause

  private type ValidNieNumber = ValidNieNumber.Type
  private object ValidNieNumber extends Newtype[ValidNumber]:
    override def validate(input: ValidNumber): Boolean | String =
      if input.unwrap.length() == 7
      then true
      else InvalidNieNumber(input.unwrap).cause

  private type ValidId = ValidId.Type
  private object ValidId extends Newtype[(ValidNumber, ControlLetter)]:
    override def validate(input: (ValidNumber, ControlLetter)): Boolean | String =
      if ControlLetter.fromOrdinal(input._1.unwrap.toInt % 23) == input._2
      then true
      else InvalidID(input._1.unwrap, input._2.toString).cause

  private type ValidDNI = ValidDNI.Type  
  private object ValidDNI extends Newtype[(ValidDniNumber, ControlLetter)]:
    override inline def validate(input: (ValidDniNumber, ControlLetter)): Boolean | String =
      val idCandidate = (input._1.unwrap, input._2)
      ValidId.make(idCandidate) match
        case Left(error) => error
        case Right(value) => true    

  private type ValidNIE = ValidNIE.Type
  private object ValidNIE extends Newtype[(NieLetter, ValidNieNumber, ControlLetter)]:
    override inline def validate(input: (NieLetter, ValidNieNumber, ControlLetter)): Boolean | String =
      val letterOrdinal = input._1.ordinal
      val validNumber = ValidNumber.unsafeMake(s"$letterOrdinal${input._2.unwrap}")
      val idCandidate = (validNumber, input._3)
      ValidId.make(idCandidate) match
        case Left(error) => error
        case Right(value) => true  

  // Our model
  private [libraries] object DNI extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      val validDNI = for
        validNumber <- ValidNumber.make(input.dropRight(1))
        validDniNumber <- ValidDniNumber.make(validNumber)
        validControlLetter <- ControlLetterNT.make(input.last.toString)
      yield ValidDNI.make((validDniNumber, ControlLetter.valueOf(validControlLetter.unwrap)))
      validDNI match
        case Left(value) => value
        case Right(value) => true

    extension (dni: DNI.Type) def formatted: String = {
      val (number, letter) = dni.unwrap.splitAt(8)
      s"$number-$letter"
    }    

  private [libraries] object NIE extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = 
      val validNIE = for
        validNieLetter <- NieLetterNT.make(input.head.toString)
        validNumber <- ValidNumber.make(input.tail.dropRight(1))
        validNieNumber <- ValidNieNumber.make(validNumber)
        validControlLetter <- ControlLetterNT.make(input.last.toString)
      yield ValidNIE.make(NieLetter.valueOf(validNieLetter.unwrap), validNieNumber, ControlLetter.valueOf(validControlLetter.unwrap))
      validNIE match
        case Left(value) => value
        case Right(_) => true
      
    extension (nie: NIE.Type) def formatted: String = {
      val (number, letter) = nie.unwrap.splitAt(8)
      s"${number.head}-${number.tail}-$letter"
    }   

  // Entry point  
  object ID extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      ValidInput.make(input).flatMap { vi => {
        val input = vi.unwrap
        if input.head.isDigit
        then DNI.make(input)
        else NIE.make(input)
      }} match
        case Left(error) => error
        case Right(_) => true

    extension (id: ID.Type) def formatted: String = {
      
      if id.unwrap.head.isLetter
      then {
        val (number, letter) = id.unwrap.splitAt(8)
        s"${number.head}-${number.tail}-$letter"
      }
      else {
        val (number, letter) = id.unwrap.splitAt(8)
        s"$number-$letter"
      }
    }
