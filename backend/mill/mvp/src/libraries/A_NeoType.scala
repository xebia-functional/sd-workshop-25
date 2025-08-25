package libraries

import domain.rules.*
import domain.ID
import domain.invariants.*

import neotype.*

object A_NeoType:

  private type NieNumber = NieNumber.Type
  private object NieNumber extends Newtype[String]:
    override inline def validate(nieNumber: String): Boolean | String =
      if !nieNumber.forall(_.isDigit) then invalidNumber(nieNumber)
      else if nieNumber.length != 7 then invalidNieNumber(nieNumber)
      else true

  private type DniNumber = DniNumber.Type
  private object DniNumber extends Newtype[String]:
    override inline def validate(dniNumber: String): Boolean | String =
      if !dniNumber.forall(_.isDigit) then invalidNumber(dniNumber)
      else if dniNumber.length != 8 then invalidDniNumber(dniNumber)
      else true

  private[libraries] final class DNI private (number: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$number-$letter"

  private[libraries] object DNI:
    def either(input: String): Either[String, DNI] =
      val (number, letter) = input.splitAt(input.length - 1)
      for
        dniNumber <- DniNumber.make(number)
        controlLetter <- ControlLetter.either(letter).left.map(_.cause)
        result <- Either.cond(
          dniNumber.unwrap.toInt % 23 == controlLetter.ordinal,
          new DNI(dniNumber, controlLetter),
          invalidDni(dniNumber.unwrap, controlLetter)
        )
      yield result

  private[libraries] final class NIE private (nieLetter: NieLetter, number: NieNumber, letter: ControlLetter)
      extends ID:
    override def formatted: String = s"$nieLetter-$number-$letter"

  private[libraries] object NIE:
    def either(input: String): Either[String, NIE] =
      val (firstLetter, number, secondLetter) = input.head.toString *: input.tail.splitAt(input.length - 2)
      for
        nieLetter <- NieLetter.either(firstLetter).left.map(_.cause)
        nieNumber <- NieNumber.make(number)
        controlLetter <- ControlLetter.either(secondLetter).left.map(_.cause)
        result <- Either.cond(
          ((nieLetter.ordinal * 10000000) + nieNumber.unwrap.toInt) % 23 == controlLetter.ordinal,
          new NIE(nieLetter, nieNumber, controlLetter),
          invalidNie(nieLetter, nieNumber.unwrap, controlLetter)
        )
      yield result

  // Entry point: ID
  object ID:
    def either(input: String): Either[String, ID] =

      // Preprocessing the input
      val sanitizedInput =
        input.trim // Handling empty spaces around
          .replace("-", "") // Removing dashes
          .toUpperCase() // Handling lower case
      // Validating the cleaned input
      if sanitizedInput.isEmpty || !sanitizedInput.forall(_.isLetterOrDigit)
      then Left(invalidInput(input))
      else if sanitizedInput.head.isDigit
      then DNI.either(sanitizedInput)
      else NIE.either(sanitizedInput)
