package backend.vanilla

import backend.common.*

/** =Value Classes with Error Handling in Scala=
  *
  * Value Classes provide a way to create type-safe wrappers around primitive types while maintaining runtime
  * efficiency. When combined with companion objects and error handling, they offer a robust way to validate data at
  * creation time. They are particularly useful in domains where data validation is crucial. This pattern helps catch
  * errors early in the development cycle and provides clear feedback about validation failures, making systems more
  * maintainable and reliable.
  *
  * Basic Syntax:
  * {{{
  * class ValueClass private (val value: Type) extends AnyVal
  *
  * object ValueClass:
  *   def apply(value: Type): Either[Error, ValueClassType] =
  *     Either.cond(
  *       boolean_condition,
  *       new ValueClass(value),
  *       Error("Error message")
  *     )
  * }}}
  *
  * '''Key Features'''
  *   - Type safety
  *   - Validation control
  *   - Use of Either for error handling
  *
  * ==Pros of Value Classes with Error Handling==
  *   - Performance Benefits: No runtime overhead at instantiation; avoids boxing/unboxing in most cases; memory
  *     efficient compared to regular classes
  *   - Safety Guarantees: Compile-time type safety; runtime validation guarantees; immutable by design
  *   - Developer Experience: Clear API boundaries; self-documenting code; easy to maintain and refactor
  *
  * ==Cons of Value Classes with Error Handling==
  *   - Implementation Complexity: Requires more initial setup code
  *   - Usage Restrictions: Cannot extend other classes; Limited to a single parameter; Some scenarios force boxing
  *   - Learning Curve: Requires understanding of Either type; Pattern matching knowledge needed; (Basic) Functional
  *     programming concepts required
  */

object D_ValueClassesWithErrorHandling:

  private[vanilla] final class NIELetter(val value: String) extends AnyVal
  private[vanilla] object NIELetter:
    def apply(value: String): Either[String, NIELetter] =
      Either.cond(
        NieLetter.values.map(_.toString).contains(value),
        new NIELetter(value),
        s"'$value' is not a valid NIE letter"
      )

  private[vanilla] final class NieNumber(val value: String) extends AnyVal
  private[vanilla] object NieNumber:
    def apply(value: String): Either[String, NieNumber] =
      if !value.forall(_.isDigit) then Left(s"number $value should not contain letters")
      else if value.length != 7 then Left(s"number $value should contain 7 digits")
      else if value.toInt < 0 then Left(s"'$value' is negative. It must be positive")
      else if value.toInt > 9999999 then Left(s"'$value' is too big. Max number is 9999999")
      else Right(new NieNumber(value))

  private[vanilla] final class DniNumber(val value: String) extends AnyVal
  private[vanilla] object DniNumber:
    def apply(value: String): Either[String, DniNumber] =
      if !value.forall(_.isDigit) then Left(s"number $value should not contain letters")
      else if value.length != 8 then Left(s"number $value should contain 8 digits")
      else if value.toInt < 0 then Left(s"'$value' is negative. It must be positive")
      else if value.toInt > 99999999 then Left(s"'$value' is too big. Max number is 99999999")
      else Right(new DniNumber(value))

  private[vanilla] final class Letter(val value: String) extends AnyVal
  private[vanilla] object Letter:
    def apply(value: String): Either[String, Letter] =
      Either.cond(
        ControlLetter.values.map(_.toString).contains(value),
        new Letter(value),
        s"'$value' is not a valid ID letter"
      )

  sealed trait ID

  private[vanilla] final class DNI(number: DniNumber, letter: Letter) extends ID:
    require(
      ControlLetter.isValidId(number.value.toInt, ControlLetter.valueOf(letter.value)),
      "Number does not match correct control letter"
    )
    override def toString: String = s"${number.value}-${letter.value}"

  private[vanilla] final class NIE(nieLetter: NIELetter, number: NieNumber, letter: Letter) extends ID:
    require(
      ControlLetter.isValidId(
        s"${NieLetter.valueOf(nieLetter.value).ordinal}${number.value}".toInt,
        ControlLetter.valueOf(letter.value)
      ),
      "Number does not match correct control letter"
    )
    override def toString: String = s"${nieLetter.value}-${number.value}-${letter.value}"

  object ID:
    def apply(input: String): Either[String, ID] =
      val trimmed = input.trim
      val withoutDash = trimmed.replace("-", "")
      if withoutDash.head.isDigit
      then
        val (n, l) = withoutDash.splitAt(withoutDash.length - 1)
        for
          number <- DniNumber(n)
          letter <- Letter(l.toUpperCase)
        yield DNI(number, letter)
      else
        val (n, l) = withoutDash.tail.splitAt(withoutDash.length - 2)
        for
          nieLetter <- NIELetter(withoutDash.head.toString.toUpperCase)
          number <- NieNumber(n)
          letter <- Letter(l.toUpperCase)
        yield NIE(nieLetter, number, letter)
