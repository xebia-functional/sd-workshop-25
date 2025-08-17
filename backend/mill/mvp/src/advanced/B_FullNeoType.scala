package advanced

import scala.util.control.NoStackTrace
import neotype.*
import domain.invariants.*
import domain.errors.*

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

  // Validated Fields
  private type ValidInput = ValidInput.Type
  private object ValidInput extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      if input.forall(_.isLetterOrDigit) && input.nonEmpty && input.length >= 9
      then true
      else InvalidInput(input).cause

  private type ValidNumber = ValidNumber.Type
  private object ValidNumber extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      if input.forall(_.isDigit)
      then true
      else InvalidNumber(input).cause

  private type ValidDniNumber = ValidDniNumber.Type
  private object ValidDniNumber extends Newtype[ValidNumber]:
    override inline def validate(input: ValidNumber): Boolean | String =
      if input.unwrap.length() == 8
      then true
      else InvalidDniNumber(input.unwrap).cause

  private type ValidNieNumber = ValidNieNumber.Type
  private object ValidNieNumber extends Newtype[ValidNumber]:
    override inline def validate(input: ValidNumber): Boolean | String =
      if input.unwrap.length() == 7
      then true
      else InvalidNieNumber(input.unwrap).cause

  private type DniTuple = (number: ValidDniNumber, letter: ControlLetter)
  private type ValidDNI = ValidDNI.Type
  private object ValidDNI extends Newtype[DniTuple]:
    override inline def validate(input: DniTuple): Boolean | String =
      val (number, letter) = (input.number.unwrap.unwrap.toInt, input.letter)
      if ControlLetter.fromOrdinal(number % 23) == letter
      then true
      else InvalidDni(number.toString, letter).cause

  private type NieTuple = (nieLetter: NieLetter, number: ValidNieNumber, letter: ControlLetter)
  private type ValidNIE = ValidNIE.Type
  private object ValidNIE extends Newtype[NieTuple]:
    override inline def validate(input: NieTuple): Boolean | String =
      val (nieLetter, number, letter) = (input.nieLetter, input.number.unwrap.unwrap, input.letter)
      val fullNumber = s"${nieLetter.ordinal}${number}".toInt
      if ControlLetter.fromOrdinal(fullNumber % 23) == letter
      then true
      else InvalidNie(nieLetter, number, letter).cause

  private[advanced] object DNI extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      (
        for
          validNumber <- ValidNumber.make(input.dropRight(1))
          validDniNumber <- ValidDniNumber.make(validNumber)
          validControlLetter <- ControlLetterNT.make(input.last.toString)
          controlLetter = ControlLetter.valueOf(validControlLetter.unwrap)
          result <- Either.cond(
            ControlLetter.fromOrdinal(validDniNumber.unwrap.unwrap.toInt % 23) == controlLetter,
            true,
            InvalidDni(validDniNumber.unwrap.unwrap, controlLetter).cause
          )
        yield result
      ) match
        case Right(value) => value
        case Left(error)  => error

    extension (dni: DNI.Type)
      def formatted: String = {
        val (number, letter) = dni.unwrap.splitAt(8)
        s"$number-$letter"
      }

  private[advanced] object NIE extends Newtype[String]:
    override inline def validate(input: String): Boolean | String =
      (
        for
          validNieLetter <- NieLetterNT.make(input.head.toString)
          validNumber <- ValidNumber.make(input.tail.dropRight(1))
          validNieNumber <- ValidNieNumber.make(validNumber)
          validControlLetter <- ControlLetterNT.make(input.last.toString)
          nieLetter = NieLetter.valueOf(validNieLetter.unwrap)
          controlLetter = ControlLetter.valueOf(validControlLetter.unwrap)
          fullNumber = s"${nieLetter.ordinal}${validNieNumber}".toInt
          result <- Either.cond(
            ControlLetter.fromOrdinal(fullNumber % 23) == controlLetter,
            true,
            InvalidNie(nieLetter, validNieNumber.unwrap.unwrap, controlLetter).cause
          )
        yield result
      ) match
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
      ValidInput.make(input).flatMap { vi =>
        {
          val input = vi.unwrap
          if input.head.isDigit
          then DNI.make(input)
          else NIE.make(input)
        }
      } match
        case Left(error) => error
        case Right(_)    => true

    extension (id: ID.Type)
      def formatted: String = {

        if id.unwrap.head.isLetter
        then {
          val (number, letter) = id.unwrap.splitAt(8)
          s"${number.head}-${number.tail}-$letter"
        } else {
          val (number, letter) = id.unwrap.splitAt(8)
          s"$number-$letter"
        }
      }
