package advanced

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import domain.ID
import domain.invariants.*
import domain.errors.*

// TODO Implement Iron refinement for ValidID (letter == number % 23)
// This is a copy of B_Iron
object C_FullIron:

  private[advanced] type ValidInput = ValidInput.T

  private[advanced] object ValidInput
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

  private[advanced] class DNI private (number: ValidDniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$number-$letter"

  private[advanced] object DNI:
    def either(validInput: ValidInput): Either[String, DNI] = {
      for
        number <- DniNumber.either(validInput.value.dropRight(1))
        letter <- ControlLetter.either(validInput.value.last.toUpper.toString).swap.map(_.cause).swap
        result <- Either.cond(
          letter.ordinal == number.value.toInt % 23,
          new DNI(number, letter),
          InvalidDni(number.value, letter).cause
        )
      yield result
    }

  private[advanced] class NIE private (nieLetter: NieLetter, number: ValidNieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-$number-$letter"

  private[advanced] object NIE:
    def either(validInput: ValidInput): Either[String, NIE] = {
      for
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
      ValidInput.either(_input).flatMap { validInput =>
        if validInput.value.head.isDigit
        then DNI.either(validInput)
        else NIE.either(validInput)
      }
