package backend.vanilla

import backend.common.*

/** =Value Classes in Scala=
  *
  * A value class in Scala is a mechanism to define a wrapper around a single value without the runtime overhead of
  * creating an actual instance of the wrapper class. For a regular class to become a value class, it must contain only
  * one parameter and extend `AnyVal`.
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
  *   - Type Safety: Provides compile-time type checking by preventing mixing up different types that share the same
  *     underlying representation
  *   - Zero-Cost Abstraction: Eliminates the wrapper class at runtime, resulting in no performance overhead compared to
  *     using the underlying type directly
  *   - Domain Modeling: Helps create more meaningful domain types and makes the code more readable and self-documenting
  *   - Encapsulation: Allows the addition of methods to primitive types without the need for inheritance and keeps
  *     related functionality together within the wrapper class
  *
  * ==Cons of Value Classes==
  *   - Limited Validation: Provides some enforcement of order but not much more. Cannot prevent invalid values at
  *     compile time
  *   - Restrictions: Can only have one parameter; cannot have auxiliary constructors; cannot extend other classes
  *     (except for universal traits)
  *   - Boxing Limitations: Performance benefits can be lost if boxing is needed, such as for collections or generic
  *     methods
  *   - Limited Inheritance: Cannot be extended by other classes and has limited support for traits
  */

object C_ValueClasses:

  private[vanilla] final class NIELetter(val value: String) extends AnyVal
  private[vanilla] final class NieNumber(val value: String) extends AnyVal
  private[vanilla] final class DniNumber(val value: String) extends AnyVal
  private[vanilla] final class Letter(val value: String) extends AnyVal

  sealed trait ID

  private[vanilla] final class DNI(number: DniNumber, letter: Letter) extends ID:
    require(number.value.forall(_.isDigit), s"number ${number.value} should not contain letters")
    require(number.value.length == 8, s"number $number should contain 8 digits")
    val _number: Int = number.value.toInt
    require(_number >= 0, s"'$number' is negative. It must be positive")
    require(_number <= 99999999, s"'$number' is too big. Max number is 99999999")
    require(
      ControlLetter.values.map(_.toString).contains(letter.value),
      s"'$letter' is not a valid ID letter"
    )
    require(
      ControlLetter.isValidId(number.value.toInt, ControlLetter.valueOf(letter.value)),
      "Number does not match correct control letter"
    )
    override def toString: String = s"${number.value}-${letter.value}"

  private[vanilla] final class NIE(nieLetter: NIELetter, number: NieNumber, letter: Letter) extends ID:
    require(number.value.forall(_.isDigit), s"number ${number.value} should not contain letters")
    require(number.value.length == 7, s"number ${number.value} should contain 7 digits")
    val _number: Int = number.value.toInt
    require(
      NieLetter.values.map(_.toString).contains(nieLetter.value),
      s"'$nieLetter' is not a valid NIE letter"
    )
    require(_number >= 0, s"'${number.value}' is negative. It must be positive")
    require(_number <= 99999999, s"'${number.value}' is too big. Max number is 99999999")
    require(
      ControlLetter.values.map(_.toString).contains(letter.value),
      s"'$letter' is not a valid ID letter"
    )
    require(
      ControlLetter.isValidId(
        s"${NieLetter.valueOf(nieLetter.value).ordinal}${number.value}".toInt,
        ControlLetter.valueOf(letter.value)
      ),
      "Number does not match correct control letter"
    )
    override def toString: String = s"${nieLetter.value}-${number.value}-${letter.value}"

  object ID:
    def apply(input: String): ID =
      val trimmed = input.trim
      val withoutDash = trimmed.replace("-", "")
      if withoutDash.head.isDigit
      then
        val (n, l) = withoutDash.splitAt(withoutDash.length - 1)
        val number = DniNumber(n)
        val letter = Letter(l.toUpperCase())
        DNI(number, letter)
      else
        val (n, l) = withoutDash.tail.splitAt(withoutDash.length - 2)
        val nieLetter = NIELetter(withoutDash.head.toString.toUpperCase())
        val number = NieNumber(n)
        val letter = Letter(l.toUpperCase())
        NIE(nieLetter, number, letter)
