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
      requireValidNieNumber(Number(nieNumber).value)
      new NieNumber(nieNumber)

  private final class DniNumber(val value: String) extends AnyVal
  private object DniNumber:
    def apply(dniNumber: String): DniNumber =
      requireValidDniNumber(Number(dniNumber).value)
      new DniNumber(dniNumber)

  private[basic] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"${dniNumber.value}-$letter"

  private[basic] object DNI:
    def apply(input: String): DNI =
      requireValidInput(input)
      val (number, letter) = input.splitAt(8)
      requireValidControlLetter(letter)
      val (dniNumber, controlLetter) = (DniNumber(number), ControlLetter.valueOf(letter))
      requireValidDni(dniNumber.value, controlLetter)
      new DNI(dniNumber, controlLetter)

  private[basic] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-${nieNumber.value}-$letter"

  private[basic] object NIE:
    def apply(input: String): NIE =
      requireValidInput(input)
      val (firstLetter, number, secondLetter) = input.head.toString *: input.tail.splitAt(7)
      requireValidNieLetter(firstLetter)
      requireValidControlLetter(secondLetter)
      val (nieLetter, nieNumber, controlLetter) =
        (NieLetter.valueOf(firstLetter), NieNumber(number), ControlLetter.valueOf(secondLetter))
      requireValidNie(nieLetter, nieNumber.value, controlLetter)
      new NIE(nieLetter, nieNumber, controlLetter)

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
