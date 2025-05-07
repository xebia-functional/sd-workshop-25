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

  private[vanilla] final class DNI private (number: String, letter: ControlLetter) extends ID:
    require(number.forall(_.isDigit), s"DNI number '$number' should not contain letters")
    require(number.length == 8, s"DNI number '$number' should contain 8 digits")
    require(
      number.toInt % 23 == letter.ordinal,
      s"DNI number '$number' does not match the control letter '$letter'"
      )
    override def pretty: String = s"$number-$letter"

  private[vanilla] object DNI:
    def apply(input: String): DNI =
      val number = input.dropRight(1)
      val letter = input.last.toString
      val _letter = ControlLetter.valueOf(letter)
      new DNI(number, _letter)

  private[vanilla] final class NIE private (nieLetter: NieLetter, number: String, letter: ControlLetter) extends ID:
    require(number.forall(_.isDigit), s"NIE number '$number' should not contain letters")
    require(number.length == 7, s"NIE number '$number' should contain 7 digits")
    require(
      s"${nieLetter.ordinal}$number".toInt % 23 == letter.ordinal,
      s"NIE number '$number' does not match the control letter '$letter'"
    )
    override def pretty: String = s"$nieLetter-$number-$letter"

  private[vanilla] object NIE:
    def apply(input: String): NIE =
      val nieLetter = input.head.toString
      val _nieLetter = NieLetter.valueOf(nieLetter)
      val number = input.tail.dropRight(1)
      val letter = input.last.toString
      val _letter = ControlLetter.valueOf(letter)
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
      require(!_input.isEmpty)
      require(_input.forall(_.isLetterOrDigit))
      
      // Selecting which type of ID base on initial character type - Letter or Digit
      if _input.head.isDigit // Splitting between DNI and NIE
      then DNI(_input)
      else NIE(_input)
