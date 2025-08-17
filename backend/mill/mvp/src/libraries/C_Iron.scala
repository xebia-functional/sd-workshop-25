package libraries

import domain.errors.*
import domain.ID
import domain.invariants.*
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/**
 * Implement the logic based on this reference:
 *
 * - [[https://iltotore.github.io/iron/docs/reference/newtypes.html Creating-New-Types]]
 *
 * Pay especial attention to:
 * {{{
*  type Temperature = Temperature.T
*  object Temperature extends RefinedType[Double, Positive]
 * }}}
 */
object C_Iron:

  // TODO implement the type validation and the error message
  private[libraries] type ValidInput = ValidInput.T
  private[libraries] object ValidInput extends RefinedType[???, ???]
  private type ValidNumber = DescribedAs[???, ""]
  private type ValidDniNumber = DniNumber.T
  private object DniNumber extends RefinedType[???, ???]
  private type ValidNieNumber = NieNumber.T
  private object NieNumber extends RefinedType[???, ???]

  private[libraries] class DNI private (number: ValidDniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$number-$letter"

  private[libraries] object DNI:
    // TODO implement with all the validations
    def either(validInput: ValidInput): Either[String, DNI] = ???

  private[libraries] class NIE private (nieLetter: NieLetter, number: ValidNieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-$number-$letter"

  private[libraries] object NIE:
    // TODO implement with all the validations
    def either(validInput: ValidInput): Either[String, NIE] = ???

  object ID:
    // TODO implement it adding some additional requirements for ergonomics of users:
    // - Trim the input
    // - Replace dashes with empty char
    // - Capitalize the input
    // If the user add an ID with a dash, with lower case or adds an empty spaces, it will be handled

    def either(input: String): Either[String, ID] = ???
