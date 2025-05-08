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

  private[vanilla] final class Number(val value: String) extends AnyVal
  private[vanilla] object Number:
    def either(number: String): Either[InvalidNumber, Number] =
      Either.cond(
        number.forall(_.isDigit),
        new Number(number),
        InvalidNumber(number)
      )
 
  private[vanilla] final class NieNumber(val value: String) extends AnyVal
  private[vanilla] object NieNumber:
    def either(nieNumber: String): Either[FailedValidation, NieNumber] =
      Number.either(nieNumber).flatMap: number =>
        Either.cond(
          number.value.length == 7,
          new NieNumber(number.value),
          InvalidNieNumber(number.value)
        )

  private[vanilla] final class DniNumber(val value: String) extends AnyVal
  private[vanilla] object DniNumber:
    def either(dniNumber: String): Either[FailedValidation, DniNumber] =
      Number.either(dniNumber).flatMap: number =>
        Either.cond(
          number.value.length == 8,
          new DniNumber(number.value),
          InvalidDniNumber(number.value)
        )

  private[vanilla] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def pretty: String = s"${dniNumber.value}-$letter"

  private[vanilla] object DNI:
    def either(input: String): Either[FailedValidation, DNI] =
      val number = input.dropRight(1)
      val letter = input.last.toString
      for
        dniNumber <- DniNumber.either(number)
        _letter <- ControlLetter.either(letter)
        result <- Either.cond(
          dniNumber.value.toInt % 23 == _letter.ordinal,
          new DNI(dniNumber, _letter),
          InvalidDni(dniNumber.value, _letter)
        )
      yield result

  private[vanilla] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter) extends ID:
    override def pretty: String = s"$nieLetter-${nieNumber.value}-$letter"

  private[vanilla] object NIE:
    def either(input: String): Either[FailedValidation, NIE] =
      val nieLetter = input.head.toString
      val number = input.tail.dropRight(1)
      val letter = input.last.toString
      for
        _nieLetter <- NieLetter.either(nieLetter)
        nieNumber <- NieNumber.either(number)
        _letter <- ControlLetter.either(letter)
        result <- Either.cond(
           ((_nieLetter.ordinal * 10000000) + nieNumber.value.toInt) % 23 == _letter.ordinal,
           new NIE(_nieLetter, nieNumber, _letter),
           InvalidNie(_nieLetter, nieNumber.value.toString, _letter)
        )
      yield result

  object ID:
    def either(input: String): Either[FailedValidation, ID] = 
      
      // Preprocesing the input
      val _input = 
        input
          .trim              // Handeling empty spaces around
          .replace("-", "")  // Removing dashes
          .toUpperCase()     // Handling lower case 

      if _input.length != 9 || _input.forall(!_.isLetterOrDigit)
      then Left(InvalidInput(input))
      else
      
        // Selecting which type of ID base on initial character type - Letter or Digit
        if _input.head.isDigit // Splitting between DNI and NIE
        then DNI.either(_input)
        else NIE.either(_input)
        