package errorHandling

import domain.errors.*
import domain.ID
import domain.invariants.*
import domain.rules.*

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

object A_ValueClassesWithErrorHandling:

  private final class Number(val value: String) extends AnyVal
  private object Number:
    def apply(number: String): Number =
      requireValidNumber(number)
      new Number(number)

    def either(number: String): Either[InvalidNumber, Number] =
      Either.cond(
        number.forall(_.isDigit),
        new Number(number),
        InvalidNumber(number)
      )

  private final class NieNumber(val value: String) extends AnyVal
  private object NieNumber:
    def apply(nieNumber: String): NieNumber =
      requireValidNieNumber(Number(nieNumber).value)
      new NieNumber(nieNumber)

    def either(nieNumber: String): Either[FailedValidation, NieNumber] =
      Number
        .either(nieNumber)
        .flatMap: number =>
          Either.cond(
            number.value.length == 7,
            new NieNumber(number.value),
            InvalidNieNumber(number.value)
          )

  private final class DniNumber(val value: String) extends AnyVal
  private object DniNumber:
    def apply(dniNumber: String): DniNumber =
      requireValidDniNumber(Number(dniNumber).value)
      new DniNumber(dniNumber)

    def either(dniNumber: String): Either[FailedValidation, DniNumber] =
      Number
        .either(dniNumber)
        .flatMap: number =>
          Either.cond(
            number.value.length == 8,
            new DniNumber(number.value),
            InvalidDniNumber(number.value)
          )

  private[errorHandling] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"${dniNumber.value}-$letter"

  private[errorHandling] object DNI:
    def apply(input: String): DNI =
      requireValidInput(input)
      val (number, letter) = input.splitAt(8)
      requireValidControlLetter(letter)
      val (dniNumber, controlLetter) = (DniNumber(number), ControlLetter.valueOf(letter))
      requireValidDni(dniNumber.value, controlLetter)
      new DNI(dniNumber, controlLetter)

    def either(input: String): Either[FailedValidation, DNI] =
      val (number, letter) = input.splitAt(input.length - 1)
      for
        dniNumber <- DniNumber.either(number)
        controlLetter <- ControlLetter.either(letter)
        result <- Either.cond(
          dniNumber.value.toInt % 23 == controlLetter.ordinal,
          new DNI(dniNumber, controlLetter),
          InvalidDni(dniNumber.value, controlLetter)
        )
      yield result

  private[errorHandling] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter)
      extends ID:
    override def formatted: String = s"$nieLetter-${nieNumber.value}-$letter"

  private[errorHandling] object NIE:
    def apply(input: String): NIE =
      requireValidInput(input)
      val (firstLetter, number, secondLetter) = input.head.toString *: input.tail.splitAt(7)
      requireValidNieLetter(firstLetter)
      requireValidControlLetter(secondLetter)
      val (nieLetter, nieNumber, controlLetter) =
        (NieLetter.valueOf(firstLetter), NieNumber(number), ControlLetter.valueOf(secondLetter))
      requireValidNie(nieLetter, nieNumber.value, controlLetter)
      new NIE(nieLetter, nieNumber, controlLetter)

    def either(input: String): Either[FailedValidation, NIE] =
      val (firstLetter, number, secondLetter) = input.head.toString *: input.tail.splitAt(input.length - 2)
      for
        nieLetter <- NieLetter.either(firstLetter)
        nieNumber <- NieNumber.either(number)
        controlLetter <- ControlLetter.either(secondLetter)
        result <- Either.cond(
          ((nieLetter.ordinal * 10000000) + nieNumber.value.toInt) % 23 == controlLetter.ordinal,
          new NIE(nieLetter, nieNumber, controlLetter),
          InvalidNie(nieLetter, nieNumber.value.toString, controlLetter)
        )
      yield result

  object ID:
    def apply(input: String): ID =

      // Preprocessing the input
      val sanitizedInput =
        input.trim // Handling empty spaces around
          .replace("-", "") // Removing dashes
          .toUpperCase() // Handling lower case

      // Validating the cleaned input
      requireValidInput(sanitizedInput)

      // Selecting which type of ID base on initial character type - Letter or Digit
      if sanitizedInput.head.isDigit // Splitting between DNI and NIE
      then DNI(sanitizedInput)
      else NIE(sanitizedInput)

    def either(input: String): Either[FailedValidation, ID] =

      // Preprocessing the input
      val sanitizedInput =
        input.trim // Handling empty spaces around
          .replace("-", "") // Removing dashes
          .toUpperCase() // Handling lower case

      if !(sanitizedInput.length == 9 && sanitizedInput.forall(_.isLetterOrDigit))
      then Left(InvalidInput(input))
      else

        // Selecting which type of ID base on initial character type - Letter or Digit
        if sanitizedInput.head.isDigit // Splitting between DNI and NIE
        then DNI.either(sanitizedInput)
        else NIE.either(sanitizedInput)
