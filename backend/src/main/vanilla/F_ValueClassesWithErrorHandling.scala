package backend.vanilla

import backend.common.*

/** =Value Classes with Error Handling in Scala=
  *
  * Value Classes provide a way to create type-safe wrappers around primitive
  * types while maintaining runtime efficiency. When combined with companion
  * objects and error handling, they offer a robust way to validate data at
  * creation time. They are particularly useful in domains where data validation
  * is crucial. This pattern helps catch errors early in the development cycle
  * and provides clear feedback about validation failures, making systems more
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
  *   - Performance Benefits: No runtime overhead at instantiation; avoids
  *     boxing/unboxing in most cases; memory efficient compared to regular
  *     classes
  *   - Safety Guarantees: Compile-time type safety; runtime validation
  *     guarantees; immutable by design
  *   - Developer Experience: Clear API boundaries; self-documenting code; easy
  *     to maintain and refactor
  *
  * ==Cons of Value Classes with Error Handling==
  *   - Implementation Complexity: Requires more initial setup code
  *   - Usage Restrictions: Cannot extend other classes; Limited to a single
  *     parameter; Some scenarios force boxing
  *   - Learning Curve: Requires understanding of Either type; Pattern matching
  *     knowledge needed; (Basic) Functional programming concepts required
  */

object F_ValueClassesWithErrorHandling:

  private class NIELetter private (val value: String) extends AnyVal
  private object NIELetter:
    def either(nieLetter: String): Either[String, NIELetter] =
      Either.cond(
        NieLetter.values.map(_.toString).contains(nieLetter),
        new NIELetter(nieLetter),
        s"'$nieLetter' is not a valid NIE letter"
      )

  private class Number private (val value: Int) extends AnyVal
  private object Number:
    def either(number: Int): Either[String, Number] =
      Either.cond(
        number >= 0 && number <= 99999999,
        new Number(number),
        if number <= 0 then s"'$number' is negative. It must be positive"
        else s"'$number' is too big. Max number is 99999999"
      )

  private class Letter private (val value: String) extends AnyVal
  private object Letter:
    def either(letter: String): Either[String, Letter] =
      Either.cond(
        ControlLetter.values.map(_.toString).contains(letter),
        new Letter(letter),
        s"'$letter' is not a valid ID letter"
      )

  sealed trait ID

  private final class DNI private (number: Number, letter: Letter) extends ID:
    require(number.value > 0, s"'${number.value}' is negative. It must be positive")
    require(number.value <= 99999999, s"'${number.value}' is too big. Max number is 99999999")
    require(
      ControlLetter.values.map(_.toString).contains(letter.value),
      s"'${letter.value}' is not a valid ID letter"
    )
    override def toString: String = s"${number.value}-${letter.value}"

  private object DNI:
    def either(
        number: Either[String, Number],
        letter: Either[String, Letter]
    ): Either[String, DNI] =
      for
        n <- number
        l <- letter
      yield new DNI(n, l)

  private final class NIE private (nieLetter: NIELetter, number: Number, letter: Letter) extends ID:
    require(
      NieLetter.values.map(_.toString).contains(nieLetter.value),
      s"'${nieLetter.value}' is not a valid NIE letter"
    )
    require(number.value > 0, s"'${number.value}' is negative. It must be positive")
    require(number.value <= 99999999, s"'${number.value}' is too big. Max number is 99999999")
    require(
      ControlLetter.values.map(_.toString).contains(letter.value),
      s"'${letter.value}' is not a valid ID letter"
    )
    override def toString: String =
      s"${nieLetter.value}-${number.value}-${letter.value}"

  private object NIE:
    def either(
        nieLetter: Either[String, NIELetter],
        number: Either[String, Number],
        letter: Either[String, Letter]
    ): Either[String, NIE] =
      for
        nl <- nieLetter
        n <- number
        l <- letter
      yield new NIE(nl, n, l)

  object ID:
   def either(input: String): Either[String, ID] =
     val trimmed = input.trim
     val withoutDash = trimmed.replace("-", "")
     if withoutDash.head.isDigit
     then
       val (number, letter) = withoutDash.splitAt(8)
       DNI.either(
         Number.either(number.toInt),
         Letter.either(letter)
       )
     else
       val (number, letter) = withoutDash.tail.splitAt(7)
       NIE.either(
         NIELetter.either(withoutDash.head.toString),
         Number.either(number.toInt),
         Letter.either(letter)
       )