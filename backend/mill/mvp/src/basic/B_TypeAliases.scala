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

  private type Number = String
  private object Number:
    def apply(number: String): Number =
      requireValidNumber(number)
      number

  private type DniNumber = String
  private object DniNumber:
    def apply(dniNumber: String): DniNumber =
      val number = Number.apply(dniNumber)
      requireValidDniNumber(number.toString)
      number

  private type NieNumber = String
  private object NieNumber:
    def apply(nieNumber: String): NieNumber =
      val number = Number(nieNumber)
      requireValidNieNumber(number.toString)
      number

  private[basic] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$dniNumber-$letter"

  private[basic] object DNI:
    def apply(input: String): DNI =
      val number = input.dropRight(1)
      val dniNumber = DniNumber(number)
      val letter = input.last.toString
      requireValidControlLetter(letter)
      val controlLetter = ControlLetter.valueOf(letter)
      requireValidDni(dniNumber, controlLetter)
      // new DNI(number, controlLetter) // Compiles, passes tests... but it is actually wrong. Check it out!
      new DNI(dniNumber, controlLetter)

  private[basic] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-$nieNumber-$letter"

  private[basic] object NIE:
    def apply(input: String): NIE =
      val nieLetter = input.head.toString
      requireValidNieLetter(nieLetter)
      val _nieLetter = NieLetter.valueOf(nieLetter)
      val number = input.tail.dropRight(1)
      val nieNumber = NieNumber(number)
      val letter = input.last.toString
      requireValidControlLetter(letter)
      val controlLetter = ControlLetter.valueOf(letter)
      requireValidNie(_nieLetter, nieNumber, controlLetter)
      // new NIE(_nieLetter, number, controlLetter) // Compiles, passes tests... but it is actually wrong. Check it out!
      new NIE(_nieLetter, nieNumber, controlLetter)

  object ID:
    def apply(input: String): ID =

      // Preprocesing the input
      val _input =
        input.trim // Handeling empty spaces around
          .replace("-", "") // Removing dashes
          .toUpperCase() // Handling lower case

      // Validating the cleaned input
      requireValidInput(_input)

      // Selecting which type of ID base on initial character type - Letter or Digit
      if _input.head.isDigit // Splitting between DNI and NIE
      then DNI(_input)
      else NIE(_input)
