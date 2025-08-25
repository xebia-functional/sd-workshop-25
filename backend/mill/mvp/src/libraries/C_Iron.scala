package libraries

import domain.rules.*
import domain.ID
import domain.invariants.*
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

object C_Iron:

  private[libraries] type ValidInput = ValidInput.T
  private[libraries] object ValidInput
      extends RefinedType[
        String,
        DescribedAs[Alphanumeric & FixedLength[9], "Should be AlphaNumeric and have 9 characters"]
      ]

  private type ValidNumber = DescribedAs[ForAll[Digit], "Should not contain letters"]

  private type ValidDniNumber = DniNumber.T
  private object DniNumber
      extends RefinedType[String, DescribedAs[MaxLength[8] & ValidNumber, "Should contain 8 digits"]]

  private type ValidNieNumber = NieNumber.T
  private object NieNumber
      extends RefinedType[String, DescribedAs[MaxLength[7] & ValidNumber, "Should contain 7 digits"]]

  private[libraries] class DNI private (number: ValidDniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$number-$letter"

  private[libraries] object DNI:
    def either(validInput: ValidInput): Either[String, DNI] = {
      for
        number <- DniNumber.either(validInput.value.dropRight(1))
        letter <- ControlLetter.either(validInput.value.last.toUpper.toString).left.map(_.cause)
        result <- Either.cond(
          letter.ordinal == number.value.toInt % 23,
          new DNI(number, letter),
          invalidDni(number.value, letter)
        )
      yield result
    }

  private[libraries] class NIE private (nieLetter: NieLetter, number: ValidNieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-$number-$letter"

  private[libraries] object NIE:
    def either(validInput: ValidInput): Either[String, NIE] = {
      for
        nieLetter <- NieLetter.either(validInput.value.head.toUpper.toString).left.map(_.cause)
        number <- NieNumber.either(validInput.value.tail.dropRight(1))
        letter <- ControlLetter.either(validInput.value.last.toUpper.toString).left.map(_.cause)
        result <- Either.cond(
          letter.ordinal == s"${nieLetter.ordinal}$number".toInt % 23,
          new NIE(nieLetter, number, letter),
          invalidNie(nieLetter, number.value, letter)
        )
      yield result
    }

  object ID:
    def either(input: String): Either[String, ID] =
      ValidInput
        .either(input.trim.replace("-", "").toUpperCase)
        .flatMap: validInput =>
          if validInput.value.head.isDigit
          then DNI.either(validInput)
          else NIE.either(validInput)
