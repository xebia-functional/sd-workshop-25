package basic

import domain.ID
import domain.invariants.*
import domain.rules.*

/** =Type Aliases in Scala=
  *
  * Type aliases allow you to give alternative names to existing types. They are declared using the 'type' keyword.
  *
  * Basic Syntax:
  * {{{
  *    type NewTypeName = ExistingType
  * }}}
  *
  * ==Pros of Type Aliases==
  *   - Improved Readability: Makes code more domain-specific and self-documenting by adding semantic meaning to
  *     primitive types
  *   - Reduced Verbosity: Shortens complex type signatures; especially useful for complex generic types
  *   - Maintenance Benefits: Centralizes type definitions and makes refactoring easier
  *
  * ==Cons of Type Aliases==
  *   - No Type Safety: it does not provide type checking. Hence, it can't prevent mixing of semantically different
  *     values of the same base type
  *   - Potential Confusion: May mislead developers into thinking they provide type safety; can make code more complex
  *     if overused
  */

object B_TypeAliases:
  
  //TODO: create 3 type aliases:
  // - Number
  // - DniNumber
  // - NieNumber
  // Each containing internally its own validation

  private type Number = String
  private object Number:
    def apply(number: String): Number = ???

  private type DniNumber = String
  private object DniNumber:
    def apply(dniNumber: String): DniNumber = ???

  private type NieNumber = String
  private object NieNumber:
    def apply(nieNumber: String): NieNumber = ???

  private[basic] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$dniNumber-$letter"

  private[basic] object DNI:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): DNI = ???

  private[basic] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-$nieNumber-$letter"

  private[basic] object NIE:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): NIE = ???

  object ID:
    // TODO implement it adding some additional requirements for ergonomics of users:
    // - Trim the input
    // - Replace dashes with empty char
    // - Capitalize the input
    // If the user add an ID with a dash, with lower case or adds an empty spaces, it will be handled
    def apply(input: String): ID = ???
