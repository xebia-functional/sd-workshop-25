package backend.vanilla

import backend.common.*

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
  *   - Straightforward object-oriented programming
  *   - Full support for inheritance and polymorphism
  *   - Encapsulation of data and behavior
  *   - Flexibility in defining custom methods and fields
  *   - Support for constructor parameters with default values
  *
  * ==Cons of Regular Classes==
  *   - Each instance creates a new object in memory
  *   - Can't be used as type aliases (unlike case classes)
  *   - No built-in equals, hashCode, or toString methods (need manual implementation)
  *   - More verbose compared to case classes for data containers
  *   - No automatic pattern matching support
  */

object A_RawClasses:

  private[vanilla] final class DNI private (dniNumber: String, letter: ControlLetter) extends ID:
    
    override def formatted: String = s"$dniNumber-$letter"

  private[vanilla] object DNI:
    
    def apply(input: String): DNI =
      val number = input.dropRight(1)
      requireValidNumber(number)
      requireValidDniNumber(number)
      val letter = input.last.toString
      requireValidControlLetter(letter)
      val _letter = ControlLetter.valueOf(letter)
      requireValidDni(number, _letter)
      new DNI(number, _letter)

  private[vanilla] final class NIE private (nieLetter: NieLetter, nieNumber: String, letter: ControlLetter) extends ID:
    
    override def formatted: String = s"$nieLetter-$nieNumber-$letter"

  private[vanilla] object NIE:
    
    def apply(input: String): NIE =
      val nieLetter = input.head.toString
      requireValidNieLetter(nieLetter)
      val _nieLetter = NieLetter.valueOf(nieLetter)
      val number = input.tail.dropRight(1)
      requireValidNumber(number)
      requireValidNieNumber(number)
      val letter = input.last.toString
      requireValidControlLetter(letter)
      val _letter = ControlLetter.valueOf(letter)
      requireValidNie(_nieLetter, number, _letter)
      new NIE(_nieLetter, number, _letter)

  object ID:
    
    def apply(input: String): ID = 
      
      // Preprocesing the input
      val _input = 
        input
          .trim              // Handeling empty spaces around
          .replace("-", "")  // Removing dashes
          .toUpperCase()     // Handling lower case 
      
      // Validating the cleaned input
      requireValidInput(_input)
      
      // Selecting which type of ID base on initial character type - Letter or Digit
      if _input.head.isDigit // Splitting between DNI and NIE
      then DNI(_input)
      else NIE(_input)
