package backend.vanilla

import backend.common.*

/** =Opaque types with Validation in Scala=
  *
  * Opaque types allow for type abstractions without runtime overhead. They
  * provide type safety by defining new, distinct types from existing types and
  * ensuring the correct use of these abstractions.
  *
  * Compared to type aliases, which are simply alternate names for existing
  * types with no additional type safety, opaque types enforce stricter type
  * constraints and encapsulate the underlying type's operations and
  * representation.
  *
  * Basic Syntax:
  * {{{
  * opaque type OpaqueType = UnderlyingType
  *
  * object OpaqueType:
  *   def apply(value: UnderlyingType): OpaqueType =
  *     require(boolean_condition, "error message")
  *     value
  * }}}
  *
  * '''Key Features'''
  *   - Creates new types, like classes, enums, etc.
  *   - They only exists at compile time
  *   - Can implement many of the features of classes and can also have a
  *     companion object
  *
  * ==Pros of Opaque Types with Validation==
  *   - Enhanced Type Safety: Encapsulates implementation details, ensuring that
  *     only valid operations are performed on the type.
  *   - Clearer Domain Modeling: Represents domain concepts more precisely by
  *     creating new types instead of using primitive ones.
  *   - Zero Overhead: Since opaque types are erased to their underlying types
  *     at runtime, they do not introduce performance penalties.
  *
  * ==Cons of Opaque Types with Validation==
  *   - Increased Complexity: May introduce additional complexity in the type
  *     system, which can be challenging for new developers.
  *   - Limited Interoperability: Sometimes difficult to work with libraries or
  *     frameworks expecting the underlying type.
  */

object E_OpaqueTypesWithValidation:

  opaque type NIELetter = String
  private object NIELetter:
    def apply(input: String): NIELetter =
      require(NieLetter.values.map(_.toString).contains(input))
      input

  opaque type Number = Int
  private object Number:
    def apply(input: Int): Number =
      require(input > 0)
      require(input <= 99999999)
      input

  opaque type Letter = String
  private object Letter:
    def apply(input: String): Letter =
      require(ControlLetter.values.map(_.toString).contains(input))
      input

  sealed trait ID

  private final class DNI(number: Number, letter: Letter) extends ID:
    require(number > 0, s"'$number' is negative. It must be positive")
    require(number <= 99999999, s"'$number' is too big. Max number is 99999999")
    require(
      ControlLetter.values.map(_.toString).contains(letter),
      s"'$letter' is not a valid ID letter"
    )
    override def toString: String = s"$number-$letter"

  private final class NIE(nieLetter: NIELetter, number: Number, letter: Letter)
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
          number = Number(number.toInt),
          letter = Letter(letter)
        )
      else
        val (number, letter) = withoutDash.tail.splitAt(7)
        NIE(
          nieLetter = NIELetter(withoutDash.head.toString),
          number = Number(number.toInt),
          letter = Letter(letter)
        )
