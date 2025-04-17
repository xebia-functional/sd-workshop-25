package backend.vanilla

import backend.common.*

/** =Value Classes in Scala=
  *
  * A value class in Scala is a mechanism to define a wrapper around a single
  * value without the runtime overhead of creating an actual instance of the
  * wrapper class. For a regular class to become a value class, it must contain
  * only one parameter and extend `AnyVal`.
  *
  * Basic Syntax:
  * {{{
  * class MyValueClass(val value: UnderlyingType) extends AnyVal
  * }}}
  *
  * ==Key Features==
  *   - Can only wrap one value
  *   - Creates effectively a new type by masking the underlying type
  *   - Zero-Cost Abstraction
  *
  * ==Pros of Value Classes==
  *   - Type Safety: Provides compile-time type checking by preventing mixing up
  *     different types that share the same underlying representation
  *   - Zero-Cost Abstraction: Eliminates the wrapper class at runtime,
  *     resulting in no performance overhead compared to using the underlying
  *     type directly
  *   - Domain Modeling: Helps create more meaningful domain types and makes the
  *     code more readable and self-documenting
  *   - Encapsulation: Allows the addition of methods to primitive types without
  *     the need for inheritance and keeps related functionality together within
  *     the wrapper class
  *
  * ==Cons of Value Classes==
  *   - Limited Validation: Provides some enforcement of order but not much
  *     more. Cannot prevent invalid values at compile time
  *   - Restrictions: Can only have one parameter; cannot have auxiliary
  *     constructors; cannot extend other classes (except for universal traits)
  *   - Boxing Limitations: Performance benefits can be lost if boxing is
  *     needed, such as for collections or generic methods
  *   - Limited Inheritance: Cannot be extended by other classes and has limited
  *     support for traits
  */

object C_ValueClasses:

  private final class NIELetter private (val value: String) extends AnyVal
  private object NIELetter:
    def apply(value: String): NIELetter =
      require(
        NieLetter.values.map(_.toString).contains(value),
        s"'$value' is not a valid NIE letter"
      )
      NIELetter(value)

  private final class NieNumber private (val value: Int) extends AnyVal
  private object NieNumber:
    def apply(value: Int): NieNumber =
      require(value >= 0, s"'$value' is negative. It must be positive")
      require(value <= 9999999, s"'$value' is too big. Max number is 9999999")
      NieNumber(value)

  private final class DniNumber private (val value: Int) extends AnyVal
  private object DniNumber:
    def apply(value: Int): DniNumber =
      require(value >= 0, s"'$value' is negative. It must be positive")
      require(value <= 99999999, s"'$value' is too big. Max number is 99999999")
      DniNumber(value)

  private final class Letter private (val value: String) extends AnyVal
  private object Letter:
    def apply(value: String): Letter=
      require(
        ControlLetter.values.map(_.toString).contains(value),
        s"'$value' is not a valid ID letter"
      )
      Letter(value)


  sealed trait ID

  private final class DNI(number: DniNumber, letter: Letter) extends ID:
    require(
      ControlLetter.isValidId(number.value, ControlLetter.valueOf(letter.value)),
      "Number does not match correct control letter"
    )
    override def toString: String =
      val paddedNumber = number.value.toString.reverse.padTo(8, "0").mkString.reverse
      s"$paddedNumber-${letter.value}"

  private final class NIE(nieLetter: NIELetter, number: NieNumber, letter: Letter)
      extends ID:
    require(
      ControlLetter.isValidId(s"${NieLetter.valueOf(nieLetter.value).ordinal}$number".toInt, ControlLetter.valueOf(letter.value)),
      "Number does not match correct control letter"
    )
    override def toString: String = {
      val paddedNumber = number.value.toString.reverse.padTo(7, "0").mkString.reverse
      s"${nieLetter.value}-$paddedNumber-${letter.value}"
    }

  object ID:
    def apply(input: String): ID =
      val trimmed = input.trim
      val withoutDash = trimmed.replace("-", "")
      if withoutDash.head.isDigit
      then
        val (number, letter) = withoutDash.splitAt(withoutDash.length-1)
        require(number.length == 8, s"number $number should contain 8 digits")
        require(number.forall(_.isDigit), s"number $number should not contain letters")
        DNI(
          number = DniNumber(number.toInt),
          letter = Letter(letter.toUpperCase())
        )
      else
        val (number, letter) = withoutDash.tail.splitAt(withoutDash.length-2)
        require(number.length == 7, s"number $number should contain 7 digits")
        require(number.forall(_.isDigit), s"number $number should not contain letters")
        NIE(
          nieLetter = NIELetter(withoutDash.head.toString.toUpperCase()),
          number = NieNumber(number.toInt),
          letter = Letter(letter.toUpperCase())
        )
