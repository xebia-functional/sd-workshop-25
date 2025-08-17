package mvp.basic

import mvp.domain.ID
import mvp.domain.invariants.*
import mvp.domain.rules.*

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
  *
  * ==Pros of Value Classes==
  *   - Type Safety: Provides compile-time type checking by preventing mixing up different types that share the same
  *     underlying representation
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

  //TODO: create 3 value classes:
  // - Number
  // - DniNumber
  // - NieNumber
  // Each containing internally its own validation
  
  private final class Number(val value: String) extends AnyVal
  private object Number:
    def apply(number: String): Number = ???

  private final class NieNumber(val value: String) extends AnyVal
  private object NieNumber:
    def apply(nieNumber: String): NieNumber = ???

  private final class DniNumber(val value: String) extends AnyVal
  private object DniNumber:
    def apply(dniNumber: String): DniNumber = ???

  private[basic] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"${dniNumber.value}-$letter"

  private[basic] object DNI:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): DNI = ???

  private[basic] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-${nieNumber.value}-$letter"

  private[basic] object NIE:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): NIE = ???
    
  object ID:
    // TODO implement it adding some additional requirements for ergonomics of users:
    // - Trim the input
    // - Replace dashes with empty char
    // - Capitalize the input
    // If the user add an ID with a dash, with lower case or adds an empty spaces, it will be handled
    def apply(input: String): ID = ???
