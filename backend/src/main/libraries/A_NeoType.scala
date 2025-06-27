package backend.libraries

import backend.common.*
import neotype.*

object A_NeoType:

  // Validated Fields: NieNumber and DniNumber

  private type NieNumber = NieNumber.Type
  private object NieNumber extends Newtype[String]:
    override inline def validate(nieNumber: String): Boolean | String =
      if !nieNumber.forall(_.isDigit) then InvalidNumber(nieNumber).cause
      else if nieNumber.length != 7 then InvalidNieNumber(nieNumber).cause
      else true  
      
  private type DniNumber = DniNumber.Type
  private object DniNumber extends Newtype[String]:
    override inline def validate(dniNumber: String): Boolean | String =
      if !dniNumber.forall(_.isDigit) then InvalidNumber(dniNumber).cause
      else if dniNumber.length != 8 then InvalidDniNumber(dniNumber).cause
      else true  

  // Our model: DNI, NIE

  private[libraries] final class DNI private (number: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$number-$letter"

  private[libraries] object DNI:
    def either(input: String): Either[String, DNI] =
      val number = input.dropRight(1)
      val letter = input.last.toString
      for
        dniNumber <- DniNumber.make(number)
        controlLetter <- ControlLetter.either(letter).swap.map(_.cause).swap
        result <- Either.cond(
          dniNumber.unwrap.toInt % 23 == controlLetter.ordinal,
          new DNI(dniNumber, controlLetter),
          InvalidDni(dniNumber.unwrap, controlLetter).cause
        )
      yield result
      

  private[libraries] final class NIE private (nieLetter: NieLetter, number: NieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-$number-$letter"

  private[libraries] object NIE:
    def either(input: String): Either[String, NIE] =
      val nieLetter = input.head.toString
      val number = input.tail.dropRight(1)
      val letter = input.last.toString
      for
        _nieLetter <- NieLetter.either(nieLetter).swap.map(_.cause).swap
        nieNumber <- NieNumber.make(number)
        controlLetter <- ControlLetter.either(letter).swap.map(_.cause).swap
        result <- Either.cond(
          ((_nieLetter.ordinal * 10000000) + nieNumber.unwrap.toInt) % 23 == controlLetter.ordinal,
          new NIE(_nieLetter, nieNumber, controlLetter),
          InvalidNie(_nieLetter, nieNumber.unwrap, controlLetter).cause
        )
      yield result

  // Entry point: ID

  object ID:
    def either(input: String): Either[String, ID] = 
      
      // Preprocesing the input
      val _input = 
        input
          .trim              // Handeling empty spaces around
          .replace("-", "")  // Removing dashes
          .toUpperCase()     // Handling lower case 
      if _input.isEmpty || _input.forall(!_.isLetterOrDigit)
      then Left(InvalidInput(input).cause)
      else
        // Validating the cleaned input
        require(!_input.isEmpty)
        require(_input.forall(_.isLetterOrDigit))
      
        // Selecting which type of ID base on initial character type - Letter or Digit
        if _input.head.isDigit // Splitting between DNI and NIE
        then DNI.either(_input)
        else NIE.either(_input)
        