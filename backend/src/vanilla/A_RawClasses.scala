package backend.vanilla

import backend.common.*

/** =Regular Classes in Scala=
  *
  * A regular class is defined using the 'class' keyword.
  *
  * Basic Syntax:
  * {{{
  *    class A (paramA: ParamAType, ..., paramN: ParamNType)
  * }}}
  *
  * '''Key Features of Regular Classes'''
  *   - Constructor parameters are defined directly in the class declaration
  *   - Classes can have methods, fields, and other members
  *   - Classes support method overriding using 'override' keyword
  *
  * ==Pros of Regular Classes==
  *   - Straightforward object-oriented programming
  *   - Full support for inheritance and polymorphism
  *   - Encapsulation of data and behavior
  *   - Flexibility in defining custom methods and fields
  *   - Support for constructor parameters with default values
  *
  * ==Cons of Regular Classes==
  *   - Each instance creates a new object in memory
  *   - Can't be used as type aliases (unlike case classes)
  *   - No built-in equals, hashCode, or toString methods (need manual
  *     implementation)
  *   - More verbose compared to case classes for data containers
  *   - No automatic pattern matching support
  */

object A_RawClasses:

  sealed trait ID

  private final class DNI(number: Int, letter: String) extends ID:
    require(number > 0, s"'$number' is negative. It must be positive")
    require(number <= 99999999, s"'$number' is too big. Max number is 99999999")
    require(
      ControlLetter.values.map(_.toString).contains(letter),
      s"'$letter' is not a valid ID letter"
    )
    override def toString: String = s"$number-$letter"

  private final class NIE(nieLetter: String, number: Int, letter: String)
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
