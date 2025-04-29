package backend.vanilla

import backend.common.*

/** =Opaque types with Validation in Scala=
  *
  * Opaque types allow for type abstractions without runtime overhead. They provide type safety by defining new,
  * distinct types from existing types and ensuring the correct use of these abstractions.
  *
  * Compared to type aliases, which are simply alternate names for existing types with no additional type safety, opaque
  * types enforce stricter type constraints and encapsulate the underlying type's operations and representation.
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
  *   - Can implement many of the features of classes and can also have a companion object
  *
  * ==Pros of Opaque Types with Validation==
  *   - Enhanced Type Safety: Encapsulates implementation details, ensuring that only valid operations are performed on
  *     the type.
  *   - Clearer Domain Modeling: Represents domain concepts more precisely by creating new types instead of using
  *     primitive ones.
  *   - Zero Overhead: Since opaque types are erased to their underlying types at runtime, they do not introduce
  *     performance penalties.
  *
  * ==Cons of Opaque Types with Validation==
  *   - Increased Complexity: May introduce additional complexity in the type system, which can be challenging for new
  *     developers.
  *   - Limited Interoperability: Sometimes difficult to work with libraries or frameworks expecting the underlying
  *     type.
  */

object E_OpaqueTypes:

  private[vanilla] opaque type NIELetter = String
  private[vanilla] object NIELetter:
    def apply(value: String): NIELetter =
      require(
        NieLetter.values.map(_.toString).contains(value),
        s"'$value' is not a valid NIE letter"
      )
      value

  private[vanilla] opaque type NieNumber = String
  private[vanilla] object NieNumber:
    def apply(value: String): NieNumber =
      require(value.forall(_.isDigit), s"number $value should not contain letters")
      require(value.length == 7, s"number $value should contain 7 digits")
      require(value.toInt >= 0, s"'$value' is negative. It must be positive")
      require(value.toInt <= 9999999, s"'$value' is too big. Max number is 9999999")
      value

  private[vanilla] opaque type DniNumber = String
  private[vanilla] object DniNumber:
    def apply(value: String): DniNumber =
      require(value.forall(_.isDigit), s"number $value should not contain letters")
      require(value.length == 8, s"number $value should contain 8 digits")
      require(value.toInt >= 0, s"'$value' is negative. It must be positive")
      require(value.toInt <= 99999999, s"'$value' is too big. Max number is 99999999")
      value

  private[vanilla] opaque type Letter = String
  private[vanilla] object Letter:
    def apply(value: String): Letter =
      require(
        ControlLetter.values.map(_.toString).contains(value),
        s"'$value' is not a valid ID letter"
      )
      value

  sealed trait ID

  private[vanilla] final class DNI(number: DniNumber, letter: Letter) extends ID:
    require(
      ControlLetter.isValidId(number.toInt, ControlLetter.valueOf(letter)),
      "Number does not match correct control letter"
    )
    override def toString: String = s"$number-$letter"

  private[vanilla] final class NIE(nieLetter: NIELetter, number: NieNumber, letter: Letter) extends ID:
    require(
      ControlLetter.isValidId(s"${NieLetter.valueOf(nieLetter).ordinal}$number".toInt, ControlLetter.valueOf(letter)),
      "Number does not match correct control letter"
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
          number = DniNumber(number),
          letter = Letter(letter.toUpperCase)
        )
      else
        val (number, letter) = withoutDash.tail.splitAt(7)
        NIE(
          nieLetter = NIELetter(withoutDash.head.toString),
          number = NieNumber(number),
          letter = Letter(letter.toUpperCase)
        )
