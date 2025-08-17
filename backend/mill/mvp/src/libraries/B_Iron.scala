package libraries

import domain.errors.*
import domain.ID
import domain.invariants.*
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

/**
 * Implement the logic based on this 2 pieces of reference:
 *
 * - [[https://iltotore.github.io/iron/docs/reference/iron-type.html Iron-Type]]
 *
 * - [[https://iltotore.github.io/iron/docs/reference/refinement.html Refinement-Methods]]
 *
 * Pay especial attention to:
 * {{{
 * val x: Int :| Greater[0] = 5
 * type Username = DescribedAs[Alphanumeric, "Username should be alphanumeric"]
 * }}}
 */
object B_Iron:

  // TODO implement the type validation and the error message
  private type ValidInput = DescribedAs[???, ""]
  private type ValidNumber = DescribedAs[???, ""]
  private type ValidDniNumber = DescribedAs[???, ""]
  private type ValidNieNumber = DescribedAs[???, ""]

  private[libraries] class DNI private (number: String :| ValidDniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$number-$letter"

  private[libraries] object DNI:
    // TODO implement with all the validations
    def either(input: String): Either[String, DNI] = ???

  private[libraries] class NIE private (nieLetter: NieLetter, number: String :| ValidNieNumber, letter: ControlLetter)
      extends ID:
    override def formatted: String = s"$nieLetter-$number-$letter"

  private[libraries] object NIE:
    // TODO implement with all the validations
    def either(input: String): Either[String, NIE] = ???

  object ID:
    // TODO implement it adding some additional requirements for ergonomics of users:
    // - Trim the input
    // - Replace dashes with empty char
    // - Capitalize the input
    // If the user add an ID with a dash, with lower case or adds an empty spaces, it will be handled
    def either(input: String): Either[String, ID] = ???
