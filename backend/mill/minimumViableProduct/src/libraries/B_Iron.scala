package libraries

import mvp.common.*
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

object B_Iron:

  private type ValidInput = DescribedAs[Alphanumeric & FixedLength[9], "Should be AlphaNumeric and have 9 characters"]
  private type ValidNumber = DescribedAs[ForAll[Digit], "Should not contain letters"]
  private type ValidDniNumber = DescribedAs[MaxLength[8] & ValidNumber, "Should contain 8 digits"]
  private type ValidNieNumber = DescribedAs[MaxLength[7] & ValidNumber, "Should contain 7 digits"]

  private[libraries] class DNI private (number: String :| ValidDniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$number-$letter"

  private[libraries] object DNI:
    def either(input: String): Either[String, DNI] = {
      for
        validInput <- input.refineEither[ValidInput]
        number <- validInput.dropRight(1).refineEither[ValidDniNumber]
        letter <- ControlLetter.either(validInput.last.toUpper.toString).swap.map(_.cause).swap
        result <- Either.cond(
          letter.ordinal == number.toInt % 23,
          new DNI(number, letter),
          InvalidDni(number, letter).cause
        )
      yield result
    }

  private[libraries] class NIE private (nieLetter: NieLetter, number: String :| ValidNieNumber, letter: ControlLetter)
      extends ID:
    override def formatted: String = s"$nieLetter-$number-$letter"

  private[libraries] object NIE:
    def either(input: String): Either[String, NIE] = {
      for
        validInput <- input.refineEither[ValidInput]
        nieLetter <- NieLetter.either(validInput.head.toUpper.toString).swap.map(_.cause).swap
        number <- validInput.tail.dropRight(1).refineEither[ValidNieNumber]
        letter <- ControlLetter.either(validInput.last.toUpper.toString).swap.map(_.cause).swap
        result <- Either.cond(
          letter.ordinal == s"${nieLetter.ordinal}$number".toInt % 23,
          new NIE(nieLetter, number, letter),
          InvalidNie(nieLetter, number, letter).cause
        )
      yield result
    }

  object ID:
    def either(input: String): Either[String, ID] =
      val _input = input.trim.replace("-", "").toUpperCase
      _input.refineEither[ValidInput].flatMap { validInput =>
        if _input.head.isDigit
        then DNI.either(validInput)
        else NIE.either(validInput)
      }
