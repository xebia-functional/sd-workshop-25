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

  private final class NIELetter(val value: String) extends AnyVal
  private final class Number(val value: Int) extends AnyVal
  private final class Letter(val value: String) extends AnyVal

  sealed trait ID

  private final class DNI(number: Number, letter: Letter) extends ID:
    require(
      number.value > 0,
      s"'${number.value}' is negative. It must be positive"
    )
    require(
      number.value <= 99999999,
      s"'${number.value}' is too big. Max number is 99999999"
    )
    require(
      ControlLetter.values.map(_.toString).contains(letter.value),
      s"'${letter.value}' is not a valid ID letter"
    )
    override def toString: String = s"${number.value}-${letter.value}"

  private final class NIE(nieLetter: NIELetter, number: Number, letter: Letter)
      extends ID:
    require(
      NieLetter.values.map(_.toString).contains(nieLetter.value),
      s"'${nieLetter.value}' is not a valid NIE letter"
    )
    require(
      number.value > 0,
      s"'${number.value}' is negative. It must be positive"
    )
    require(
      number.value <= 99999999,
      s"'${number.value}' is too big. Max number is 99999999"
    )
    require(
      ControlLetter.values.map(_.toString).contains(letter.value),
      s"'${letter.value}' is not a valid ID letter"
    )
    override def toString: String =
      s"${nieLetter.value}-${number.value}-${letter.value}"

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
