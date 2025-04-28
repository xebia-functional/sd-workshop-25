package backend.vanilla

import scala.util.control.NoStackTrace

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

  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with number they represent
  enum NieLetter:
    case X // 0
    case Y // 1
    case Z // 2

  object NieLetter:
    def apply(letter: String): Either[InvalidNieLetter, NieLetter] =
      Either.cond(
        NieLetter.values.map(_.toString).contains(letter),
        NieLetter.valueOf(letter),
        InvalidNieLetter(letter)
      )  

  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with the remainder of number divided by 23
  enum ControlLetter:
    case T // 0
    case R // 1
    case W // 2
    case A // 3
    case G // 4
    case M // 5
    case Y // 6
    case F // 7
    case P // 8
    case D // 9
    case X // 10
    case B // 11
    case N // 12
    case J // 13
    case Z // 14
    case S // 15
    case Q // 16
    case V // 17
    case H // 18
    case L // 19
    case C // 20
    case K // 21
    case E // 22

  object ControlLetter:
    def apply(letter: String): Either[InvalidControlLetter, ControlLetter] =
      Either.cond(
        ControlLetter.values.map(_.toString).contains(letter),
        ControlLetter.valueOf(letter),
        InvalidControlLetter(letter)
      )  

  // All posible problems
  sealed trait FailedValidation(cause: String) extends Exception with NoStackTrace:
    override def toString: String = cause  
  case class InvalidInput(input: String) extends FailedValidation(s"'$input' should be AlphaNumeric and non empty")
  case class InvalidNieLetter(nieLetter: String) extends FailedValidation(s"'$nieLetter' is not a valid NIE letter")
  case class InvalidNaN(number: String) extends FailedValidation(s"'$number' should not contain letters")
  case class InvalidDniNumber(number: String) extends FailedValidation(s"DNI number '$number' should contain 8 digits")
  case class InvalidNieNumber(number: String) extends FailedValidation(s"NIE number '$number' should contain 7 digits")
  case class InvalidControlLetter(controlLetter: String) extends FailedValidation(s"'$controlLetter' is not a valid Control letter")
  case class InvalidID(number: String, letter: String) extends FailedValidation(s"ID number '$number' does not match the control letter '$letter'")  

  private[vanilla] final class NieNumber(val value: String) extends AnyVal
  private[vanilla] object NieNumber:
    def apply(number: String): Either[FailedValidation, NieNumber] =
      if !number.forall(_.isDigit) then Left(InvalidNaN(number))
      else if number.length != 7 then Left(InvalidNieNumber(number))
      else
        // These requires should pass if the previous logic is well implemented
        require(number.forall(_.isDigit), s"NIE number '$number' should not contain letters")
        require(number.length == 7, s"NIE number '$number' should contain 7 digits")
        Right(new NieNumber(number))

  private[vanilla] final class DniNumber(val value: String) extends AnyVal
  private[vanilla] object DniNumber:
    def apply(number: String): Either[FailedValidation, DniNumber] =
      if !number.forall(_.isDigit) then Left(InvalidNaN(number))
      else if number.length != 8 then Left(InvalidDniNumber(number))
      else
        // These requires should pass if the previous logic is well implemented
        require(number.forall(_.isDigit), s"DNI number '$number' should not contain letters")
        require(number.length == 8, s"DNI number '$number' should contain 8 digits")
        Right(new DniNumber(number))

  sealed trait ID

  private[vanilla] final class DNI private (number: DniNumber, letter: ControlLetter) extends ID:
    val _number = number.value.toInt
    require(
      ControlLetter.fromOrdinal(_number % 23) == letter,
      s"DNI number '${number.value}' does not match the control letter '$letter'"
    )
    override def toString: String = s"${number.value}-$letter"

  private[vanilla] object DNI:
    def apply(input: String): Either[FailedValidation, DNI] =
      val (number, letter) = input.splitAt(input.length - 1)
      for
        _number <- DniNumber(number)
        _letter <- ControlLetter(letter)
      yield new DNI(_number, _letter)

  private[vanilla] final class NIE private (nieLetter: NieLetter, number: NieNumber, letter: ControlLetter) extends ID:
    val ordinalOfNIE = nieLetter.ordinal // Extracts the number representation of the NIE Letter
    val _number =
      s"$ordinalOfNIE${number.value}".toInt // Appends the number representation from NIE Letter to the number
    require(
      ControlLetter.fromOrdinal(_number % 23) == letter,
      s"NIE number '${number.value}' does not match the control letter '$letter'"
    )
    override def toString: String = s"$nieLetter-${number.value}-$letter"

  private[vanilla] object NIE:
    def apply(input: String): Either[FailedValidation, NIE] =
      val nieLetter = input.head.toString
      val (number, letter) = input.tail.splitAt(input.tail.length - 1)
      for
        _nieLetter <- NieLetter(nieLetter)
        _number <- NieNumber(number)
        _letter <- ControlLetter(letter)
      yield new NIE(_nieLetter, _number, _letter)

  object ID:
    def apply(input: String): Either[FailedValidation, ID] = 
      
      // Preprocesing the input
      val _input = 
        input
          .trim              // Handeling empty spaces around
          .replace("-", "")  // Removing dashes
          .toUpperCase()     // Handling lower case 
      if _input.isEmpty || _input.forall(!_.isLetterOrDigit)
      then Left(InvalidInput(input))
      else
        // Validating the cleaned input
        require(!_input.isEmpty)
        require(_input.forall(_.isLetterOrDigit))
      
        // Selecting which type of ID base on initial character type - Letter or Digit
        if _input.head.isDigit // Splitting between DNI and NIE
        then DNI(_input)
        else NIE(_input)