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
    // TODO implement it making sure all the requirements are met
    def apply(number: String): Number = ???
    def either(number: String): Either[InvalidNumber, Number] = ???

  private final class NieNumber(val value: String) extends AnyVal
  private object NieNumber:
    // TODO implement it making sure all the requirements are met
    def apply(nieNumber: String): NieNumber = ???
    def either(nieNumber: String): Either[FailedValidation, NieNumber] = ???

  private final class DniNumber(val value: String) extends AnyVal
  private object DniNumber:
    // TODO implement it making sure all the requirements are met
    def apply(dniNumber: String): DniNumber = ???
    def either(dniNumber: String): Either[FailedValidation, DniNumber] = ???

  private[errorHandling] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"${dniNumber.value}-$letter"

  private[errorHandling] object DNI:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): DNI = ???
    def either(input: String): Either[FailedValidation, DNI] = ???

  private[errorHandling] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter)
      extends ID:
    override def formatted: String = s"$nieLetter-${nieNumber.value}-$letter"

  private[errorHandling] object NIE:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): NIE = ???¡
    def either(input: String): Either[FailedValidation, NIE] = ???

  object ID:
    // TODO implement it adding some additional requirements for ergonomics of users:
    // - Trim the input
    // - Replace dashes with empty char
    // - Capitalize the input
    // If the user add an ID with a dash, with lower case or adds an empty spaces, it will be handled
    def apply(input: String): ID = ???
    def either(input: String): Either[FailedValidation, ID] = ???
