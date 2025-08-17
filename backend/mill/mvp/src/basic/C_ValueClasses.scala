package basic

import domain.ID
import domain.invariants.*
import domain.rules.*

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

  private final class Number(val value: String) extends AnyVal
  private object Number:
    def apply(number: String): Number =
      requireValidNumber(number)
      new Number(number)

  private final class NieNumber(val value: String) extends AnyVal
  private object NieNumber:
    def apply(nieNumber: String): NieNumber =
      val number = Number(nieNumber)
      requireValidNieNumber(number.value)
      new NieNumber(number.value)

  private final class DniNumber(val value: String) extends AnyVal
  private object DniNumber:
    def apply(dniNumber: String): DniNumber =
      val number = Number(dniNumber)
      requireValidDniNumber(number.value)
      new DniNumber(number.value)

  private[basic] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"${dniNumber.value}-$letter"

  private[basic] object DNI:
    def apply(input: String): DNI =
      val number = input.dropRight(1)
      val dniNumber = DniNumber(number)
      val letter = input.last.toString
      requireValidControlLetter(letter)
      val controlLetter = ControlLetter.valueOf(letter)
      requireValidDni(dniNumber.value, controlLetter)
      new DNI(dniNumber, controlLetter)

  private[basic] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-${nieNumber.value}-$letter"

  private[basic] object NIE:
    def apply(input: String): NIE =
      val nieLetter = input.head.toString
      requireValidNieLetter(nieLetter)
      val _nieLetter = NieLetter.valueOf(nieLetter)
      val number = input.tail.dropRight(1)
      val nieNumber = NieNumber(number)
      val letter = input.last.toString
      requireValidControlLetter(letter)
      val controlLetter = ControlLetter.valueOf(letter)
      requireValidNie(_nieLetter, nieNumber.value, controlLetter)
      new NIE(_nieLetter, nieNumber, controlLetter)

  object ID:
    def apply(input: String): ID =

      // Preprocesing the input
      val _input =
        input.trim // Handeling empty spaces around
          .replace("-", "") // Removing dashes
          .toUpperCase() // Handling lower case

      // Validating the cleaned input
      requireValidInput(_input)

      // Selecting which type of ID base on initial character type - Letter or Digit
      if _input.head.isDigit // Splitting between DNI and NIE
      then DNI(_input)
      else NIE(_input)
