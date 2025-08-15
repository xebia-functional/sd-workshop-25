package implementations.libraries

import implementations.common.*
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

object C_Iron:

  private type ValidInput = ValidInput.T
  private object ValidInput
      extends RefinedType[
        String,
        DescribedAs[Alphanumeric & FixedLength[9], "Should be AlphaNumeric and have 9 characters"]
      ]

  private type ValidNumber = DescribedAs[ForAll[Digit], "Should not contain letters"]

  private type DniNumber = DniNumber.T
  private object DniNumber
      extends RefinedType[String, DescribedAs[MaxLength[8] & ValidNumber, "Should contain 8 digits"]]

  private type NieNumber = NieNumber.T
  private object NieNumber
      extends RefinedType[String, DescribedAs[MaxLength[7] & ValidNumber, "Should contain 7 digits"]]

  private[libraries] class DNI private (number: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$number-$letter"

  private[libraries] object DNI:
    def either(input: String): Either[String, DNI] = {
      for
        validInput <- ValidInput.either(input)
        number <- DniNumber.either(validInput.value.dropRight(1))
        letter <- ControlLetter.either(validInput.value.last.toUpper.toString).swap.map(_.cause).swap
        result <- Either.cond(
          letter.ordinal == number.value.toInt % 23,
          new DNI(number, letter),
          InvalidDni(number.value, letter).cause
        )
      yield result
    }

  private[libraries] class NIE private (nieLetter: NieLetter, number: NieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-$number-$letter"

  private[libraries] object NIE:
    def either(input: String): Either[String, NIE] = {
      for
        validInput <- ValidInput.either(input)
        nieLetter <- NieLetter.either(validInput.value.head.toUpper.toString).swap.map(_.cause).swap
        number <- NieNumber.either(validInput.value.tail.dropRight(1))
        letter <- ControlLetter.either(validInput.value.last.toUpper.toString).swap.map(_.cause).swap
        result <- Either.cond(
          letter.ordinal == s"${nieLetter.ordinal}$number".toInt % 23,
          new NIE(nieLetter, number, letter),
          InvalidNie(nieLetter, number.value, letter).cause
        )
      yield result
    }

  object ID:
    def either(input: String): Either[String, ID] =
      val _input = input.trim.replace("-", "").toUpperCase
      if _input.isEmpty
      then Left("Should be AlphaNumeric and have 9 characters")
      else if _input.head.isDigit
      then DNI.either(_input)
      else NIE.either(_input)
