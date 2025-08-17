package errorHandling

import domain.errors.*
import domain.ID
import domain.invariants.*
import domain.rules.*

/** =Opaque Types with Error Handling=
  *
  * Opaque types are a Scala 3 feature that provides type abstraction without runtime overhead. It allows to create new
  * types that are light-weighted and can incorporate benefits of Value Classes and other structures.
  *
  * Basic syntax:
  * {{{
  * opaque type MyOpaqueType = UnderlyingType
  * object MyOpaqueType:
  *   def apply(underlyingValue: UnderlyingType): MyOpaqueType = underlyingValue
  *   def parse(input: UnderlyingType): Either[String, MyOpaqueType] =
  *     Either.cond(
  *       // boolean_condition
  *       MyOpaqueType(input),
  *       "Error message"
  *     )
  * }}}
  *
  * '''Key Features'''
  *   - Type safety
  *   - Validation control
  *   - Use of Either for error handling
  *
  * ==Pros of Opaque Types with Error Handling==
  *   - Performance Benefits: No runtime overhead. Opaque types do not exist during runtime.
  *   - Safety Guarantees: Compile-time type safety; runtime validation guarantees; immutable by design
  *   - Developer Experience: Clear API boundaries; self-documenting code; easy to maintain and refactor
  *   - Encapsulation: Prevent invalid states through controlled construction
  *
  * ==Cons of Value Classes with Error Handling==
  *   - Implementation Complexity: Opaque type's underlying representation is only visible in the companion object; can
  *     make debugging more challenging
  *   - Usage Restrictions: Cannot extend other classes; Limited to a single parameter; Some scenarios force boxing
  *   - Learning Curve: Requires understanding of Either type; Pattern matching knowledge needed; (Basic) Functional
  *     programming concepts required
  *   - Potential Overuse: Can lead to unnecessary abstraction if not used judiciously; might complicate simple code if
  *     used where not needed
  */

object B_OpaqueTypesWithErrorHandling:

  private[errorHandling] opaque type Number = String
  private[errorHandling] object Number:
    // TODO implement it making sure all the requirements are met
    def apply(number: String): Number = ???
    def either(number: String): Either[InvalidNumber, Number] = ???

  private[errorHandling] opaque type NieNumber = String
  // TODO implement it making sure all the requirements are met
  private[errorHandling] object NieNumber:
    def apply(nieNumber: String): Number = ???
    def either(nieNumber: String): Either[FailedValidation, NieNumber] = ???

  private[errorHandling] opaque type DniNumber = String
  private[errorHandling] object DniNumber:
    // TODO implement it making sure all the requirements are met
    def apply(dniNumber: String): Number = ???
    def either(dniNumber: String): Either[FailedValidation, DniNumber] = ???

  private[errorHandling] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$dniNumber-$letter"

  private[errorHandling] object DNI:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): DNI = ???
    def either(input: String): Either[FailedValidation, DNI] = ???

  private[errorHandling] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter)
      extends ID:
    override def formatted: String = s"$nieLetter-$nieNumber-$letter"

  private[errorHandling] object NIE:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): NIE = ???
    def either(input: String): Either[FailedValidation, NIE] = ???

  object ID:
    // TODO implement it adding some additional requirements for ergonomics of users:
    // - Trim the input
    // - Replace dashes with empty char
    // - Capitalize the input
    // If the user add an ID with a dash, with lower case or adds an empty spaces, it will be handled
    def apply(input: String): ID = ???
    def either(input: String): Either[FailedValidation, ID] = ???
