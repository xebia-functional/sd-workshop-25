package backend.vanilla

import backend.common.*

/** =Type Aliases in Scala=
  *
  * Type aliases allow you to give alternative names to existing types. They are
  * declared using the 'type' keyword.
  *
  * Basic Syntax:
  * {{{
  *    type NewTypeName = ExistingType
  * }}}
  *
  * ==Pros of Type Aliases==
  *   - Improved Readability: Makes code more domain-specific and
  *     self-documenting by adding semantic meaning to primitive types
  *   - Reduced Verbosity: Shortens complex type signatures; especially useful
  *     for complex generic types
  *   - Maintenance Benefits: Centralizes type definitions and makes refactoring
  *     easier
  *
  * ==Cons of Type Aliases==
  *   - No Type Safety: it does not provide type checking. Hence, it can't
  *     prevent mixing of semantically different values of the same base type
  *   - Potential Confusion: May mislead developers into thinking they provide
  *     type safety; can make code more complex if overused
  */

object B_TypeAliases:

  type NieLetter = String
  type Number = Int
  type Letter = String

  sealed trait ID

  private final class DNI(number: Number, letter: Letter) extends ID:
    require(number > 0, s"'$number' is negative. It must be positive")
    require(number <= 99999999, s"'$number' is too big. Max number is 99999999")
    require(
      ControlLetter.values.map(_.toString).contains(letter),
      s"'$letter' is not a valid ID letter"
    )
    override def toString: String = s"$number-$letter"

  private final class NIE(nieLetter: NieLetter, number: Number, letter: Letter)
      extends ID:
    require(
      NieLetter.values.map(_.toString).contains(nieLetter),
      s"'$nieLetter' is not a valid NIE letter"
    )
    require(number > 0, s"'$number' is negative. It must be positive")
    require(number <= 99999999, s"'$number' is too big. Max number is 99999999")
    require(
      ControlLetter.values.map(_.toString).contains(letter),
      s"'$letter' is not a valid ID letter"
    )
    override def toString: String = s"$nieLetter-$number-$letter"

  object ID:
    def apply(input: String): ID =
      val trimmed = input.trim
      val withoutDash = trimmed.replace("-", "")
      if withoutDash.head.isDigit
      then
        val (number, letter) = withoutDash.splitAt(8)
        DNI(
          number = number.toInt,
          letter = letter
        )
      else
        val (number, letter) = withoutDash.tail.splitAt(7)
        NIE(
          nieLetter = withoutDash.head.toString,
          number = number.toInt,
          letter = letter
        )
