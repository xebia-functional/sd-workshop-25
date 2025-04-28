package backend.vanilla

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
  *   - Zero-Cost Abstraction
  *
  * ==Pros of Value Classes==
  *   - Type Safety: Provides compile-time type checking by preventing mixing up different types that share the same
  *     underlying representation
  *   - Zero-Cost Abstraction: Eliminates the wrapper class at runtime, resulting in no performance overhead compared to
  *     using the underlying type directly
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

  // Do NOT change the order of the enumeration.
  // The ordinal value of each letter corresponds with number they represent
  enum NieLetter:
    case X // 0
    case Y // 1
    case Z // 2

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

  private[vanilla] final class NieNumber(val value: String) extends AnyVal
  private[vanilla] object NieNumber:
    def apply(number: String): NieNumber =
      require(number.forall(_.isDigit), s"NIE number '$number' should not contain letters")
      require(number.length == 7, s"NIE number '$number' should contain 7 digits")
      new NieNumber(number)

  private[vanilla] final class DniNumber(val value: String) extends AnyVal
  private[vanilla] object DniNumber:
    def apply(number: String): DniNumber =
      require(number.forall(_.isDigit), s"DNI number '$number' should not contain letters")
      require(number.length == 8, s"DNI number '$number' should contain 8 digits")
      new DniNumber(number)

  sealed trait ID

  private[vanilla] final class DNI private (number: DniNumber, letter: ControlLetter) extends ID:
    val _number = number.value.toInt
    require(
      ControlLetter.fromOrdinal(_number % 23) == letter,
      s"DNI number '${number.value}' does not match the control letter '$letter'"
    )
    override def toString: String = s"${number.value}-$letter"

  private[vanilla] object DNI:
    def apply(input: String): DNI =
      val (number, letter) = input.splitAt(input.length - 1)
      val _number = DniNumber(number)
      val _letter = ControlLetter.valueOf(letter)
      new DNI(_number, _letter)

  private[vanilla] final class NIE private (nieLetter: NieLetter, number: NieNumber, letter: ControlLetter) extends ID:
    val _number = s"${nieLetter.ordinal}${number.value}".toInt
    require(
      ControlLetter.fromOrdinal(_number % 23) == letter,
      s"NIE number '${number.value}' does not match the control letter '$letter'"
    )
    override def toString: String = s"$nieLetter-${number.value}-$letter"

  private[vanilla] object NIE:
    def apply(input: String): NIE =
      val nieLetter = input.head.toString
      val (number, letter) = input.tail.splitAt(input.tail.length - 1)
      val _nieLetter = NieLetter.valueOf(nieLetter)
      val _number = NieNumber(number)
      val _letter = ControlLetter.valueOf(letter)
      new NIE(_nieLetter, _number, _letter)

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
