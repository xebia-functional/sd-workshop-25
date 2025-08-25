package basic

import domain.ID
import domain.invariants.*
import domain.rules.*

/** =Regular Classes in Scala=
  *
  * A regular class is defined using the 'class' keyword.
  *
  * Basic Syntax:
  * {{{
  *    class A (paramA: ParamAType, ..., paramN: ParamNType)
  * }}}
  *
  * '''Key Features of Regular Classes'''
  *   - Constructor parameters are defined directly in the class declaration
  *   - Classes can have methods, fields, and other members
  *   - Classes support method overriding using 'override' keyword
  *
  * ==Pros of Regular Classes==
  *   - Full support for inheritance and polymorphism
  *   - Encapsulation of data and behavior
  *   - Flexibility in defining custom methods and fields
  *   - Support for constructor parameters with default values
  *
  * ==Cons of Regular Classes==
  *   - Each instance creates a new object in memory
  *   - More verbose compared to case classes for data containers
  *   - No automatic pattern matching support
  */

object A_Classes:

  private[basic] final class DNI private (dniNumber: String, letter: ControlLetter) extends ID:
    override def formatted: String = s"$dniNumber-$letter"

  private[basic] object DNI:
    def apply(input: String): DNI =
      requireValidInput(input)
      val (number, letter) = input.splitAt(8)
      requireValidNumber(number)
      requireValidDniNumber(number)
      requireValidControlLetter(letter)
      val controlLetter = ControlLetter.valueOf(letter)
      requireValidDni(number, controlLetter)
      new DNI(number, controlLetter)

  private[basic] final class NIE private (nieLetter: NieLetter, nieNumber: String, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-$nieNumber-$letter"

  private[basic] object NIE:
    def apply(input: String): NIE =
      requireValidInput(input)
      val (firstLetter, number, secondLetter) = input.head.toString *: input.tail.splitAt(7)
      requireValidNieLetter(firstLetter)
      requireValidNumber(number)
      requireValidNieNumber(number)
      requireValidControlLetter(secondLetter)
      val (nieLetter, controlLetter) = (NieLetter.valueOf(firstLetter), ControlLetter.valueOf(secondLetter))
      requireValidNie(nieLetter, number, controlLetter)
      new NIE(nieLetter, number, controlLetter)

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
