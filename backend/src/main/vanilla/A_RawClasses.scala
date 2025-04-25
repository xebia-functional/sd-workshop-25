package backend.vanilla

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

  sealed trait ID

  private[vanilla] final class DNI private(number: String, letter: ControlLetter) extends ID:
    require(number.forall(_.isDigit), s"DNI number '$number' should not contain letters")
    require(number.length == 8, s"DNI number '$number' should contain 8 digits")
    val _number = number.toInt
    require(
      ControlLetter.fromOrdinal(_number % 23) == letter,
      s"DNI number '$number' does not match the control letter '$letter'"
    )
    override def toString: String = s"$number-$letter"

  private[vanilla] object DNI:
    def apply(number: String, letter: String): DNI =
      val _letter = ControlLetter.valueOf(letter)
      new DNI(number, _letter)

  private[vanilla] final class NIE private(nieLetter: NieLetter, number: String, letter: ControlLetter) extends ID:
    require(number.forall(_.isDigit), s"NIE number '$number' should not contain letters")
    require(number.length == 7, s"NIE number '$number' should contain 7 digits")
    val ordinalOfNIE = nieLetter.ordinal // Extracts the number representation of the NIE Letter
    val _number = s"$ordinalOfNIE$number".toInt // Appends the number representation from NIE Letter to the number
    require(
      ControlLetter.fromOrdinal(_number % 23) == letter,
      s"NIE number '$number' does not match the control letter '$letter'"
    )
    override def toString: String = s"$nieLetter-$number-$letter"

  private [vanilla] object NIE:
    def apply(nieLetter: String, number: String, letter: String): NIE =
      val _nieLetter = NieLetter.valueOf(nieLetter)
      val _letter = ControlLetter.valueOf(letter)
      new NIE(_nieLetter, number, _letter)  

  object ID:
    def apply(input: String): ID =
      val trimmed = input.trim // Handeling empty spaces around
      val withoutDash = trimmed.replace("-", "") // Removing dashes
      if withoutDash.head.isDigit // Splitting between DNI and NIE
      then
        val (number, letter) = withoutDash.splitAt(withoutDash.length - 1)
        val _letter = letter.toUpperCase // Handeling lowerCase
        DNI(number, _letter) 
      else
        val (number, letter) = withoutDash.tail.splitAt(withoutDash.length - 2)
        val nieLetter = withoutDash.head.toString.toUpperCase // Handling lowerCase
        val _letter = letter.toUpperCase // Handling lowerCase
        NIE(nieLetter, number, _letter)
