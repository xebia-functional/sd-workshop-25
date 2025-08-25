package advanced

import scala.util.control.NoStackTrace
import neotype.*
import domain.invariants.*
import domain.rules.*

object B_FullNeoType:

  // Define additional objects for NieLetter and ControlLetter.
  // Companion objects already exists for the enums at common.scala
  private object NieLetterNT extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      if NieLetter.values.map(_.toString).contains(input)
      then true
      else invalidNieLetter(input)

  private object ControlLetterNT extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      if ControlLetter.values.map(_.toString).contains(input)
      then true
      else invalidControlLetter(input)

  // Validated Fields
  private type ValidInput = ValidInput.Type
  private object ValidInput extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      if input.forall(_.isLetterOrDigit) && input.length >= 9
      then true
      else invalidInput(input)

  private type ValidNumber = ValidNumber.Type
  private object ValidNumber extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      if input.forall(_.isDigit)
      then true
      else invalidNumber(input)

  private type ValidDniNumber = ValidDniNumber.Type
  private object ValidDniNumber extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      ValidNumber.make(input) match {
        case Left(value) => value
        case Right(value) =>
          if value.unwrap.length() == 8 then true
          else invalidDniNumber(value.unwrap)
      }

  private type ValidNieNumber = ValidNieNumber.Type
  private object ValidNieNumber extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      ValidNumber.make(input) match {
        case Left(value) => value
        case Right(value) =>
          if value.unwrap.length() == 7 then true
          else invalidNieNumber(value.unwrap)
      }

  private type DniTuple = (number: ValidDniNumber, letter: ControlLetter)
  private type ValidDNI = ValidDNI.Type
  private object ValidDNI extends Newtype[DniTuple]:
    override inline def validate(input: DniTuple): Boolean | String =
      val (number, letter) = (input.number.unwrap.toInt, input.letter)
      if ControlLetter.fromOrdinal(number % 23) == letter
      then true
      else invalidDni(number.toString, letter)

  private type NieTuple = (nieLetter: NieLetter, number: ValidNieNumber, letter: ControlLetter)
  private type ValidNIE = ValidNIE.Type
  private object ValidNIE extends Newtype[NieTuple]:
    override inline def validate(input: NieTuple): Boolean | String =
      val (nieLetter, number, letter) = (input.nieLetter, input.number.unwrap, input.letter)
      val fullNumber = s"${nieLetter.ordinal}${number}".toInt
      if ControlLetter.fromOrdinal(fullNumber % 23) == letter
      then true
      else invalidNie(nieLetter, number, letter)

  private[advanced] object DNI extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      val (number, letter) = input.splitAt(input.length - 1)
      val either: Either[String, Boolean] = for
        dniNumber <- ValidDniNumber.make(number)
        controlLetterNT <- ControlLetterNT.make(letter)
        controlLetter = ControlLetter.valueOf(controlLetterNT.unwrap)
        result <- Either.cond(
          ControlLetter.fromOrdinal(dniNumber.unwrap.toInt % 23) == controlLetter,
          true,
          invalidDni(dniNumber.unwrap, controlLetter)
        )
      yield result
      // Transforms Either[String, Boolean] to the union type Boolean | String
      either match
        case Right(value) => value
        case Left(error)  => error

    extension (dni: DNI.Type)
      def formatted: String = {
        val (number, letter) = dni.unwrap.splitAt(8)
        s"$number-$letter"
      }

  private[advanced] object NIE extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      val (firstLetter, number, secondLetter) = input.head.toString *: input.tail.splitAt(input.length - 2)
      val either: Either[String, Boolean] = for
        nieLetterNT <- NieLetterNT.make(firstLetter)
        nieNumber <- ValidNieNumber.make(number)
        controlLetterNT <- ControlLetterNT.make(secondLetter)
        nieLetter = NieLetter.valueOf(nieLetterNT.unwrap)
        controlLetter = ControlLetter.valueOf(controlLetterNT.unwrap)
        fullNumber = s"${nieLetter.ordinal}$nieNumber".toInt
        result <- Either.cond(
          ControlLetter.fromOrdinal(fullNumber % 23) == controlLetter,
          true,
          invalidNie(nieLetter, nieNumber.unwrap, controlLetter)
        )
      yield result
      // Transforms Either[String, Boolean] to the union type Boolean | String
      either match
        case Right(value) => value
        case Left(error)  => error

    extension (nie: NIE.Type)
      def formatted: String = {
        val (number, letter) = nie.unwrap.splitAt(8)
        s"${number.head}-${number.tail}-$letter"
      }

  // Entry point
  object ID extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      ValidInput
        .make(input)
        .flatMap: validInput =>
          val input = validInput.unwrap
          if input.head.isDigit
          then DNI.make(input)
          else NIE.make(input)
      match
        case Left(error) => error
        case Right(_)    => true

    extension (id: ID.Type)
      def formatted: String =
        val (number, letter) = id.unwrap.splitAt(8)
        if number.head.isLetter
        // NIE case
        then s"${number.head}-${number.tail}-$letter"
        // DNI case
        else s"$number-$letter"
