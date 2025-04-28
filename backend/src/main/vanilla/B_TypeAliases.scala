package backend.vanilla

/** =Type Aliases in Scala=
  *
  * Type aliases allow you to give alternative names to existing types. They are declared using the 'type' keyword.
  *
  * Basic Syntax:
  * {{{
  *    type NewTypeName = ExistingType
  * }}}
  *
  * ==Pros of Type Aliases==
  *   - Improved Readability: Makes code more domain-specific and self-documenting by adding semantic meaning to
  *     primitive types
  *   - Reduced Verbosity: Shortens complex type signatures; especially useful for complex generic types
  *   - Maintenance Benefits: Centralizes type definitions and makes refactoring easier
  *
  * ==Cons of Type Aliases==
  *   - No Type Safety: it does not provide type checking. Hence, it can't prevent mixing of semantically different
  *     values of the same base type
  *   - Potential Confusion: May mislead developers into thinking they provide type safety; can make code more complex
  *     if overused
  */

object B_TypeAliases:

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

  private[vanilla] type DniNumber = String
  private[vanilla] object DniNumber:
    def apply(number: String): DniNumber =
      require(number.forall(_.isDigit), s"DNI number '$number' should not contain letters")
      require(number.length == 8, s"DNI number '$number' should contain 8 digits")
      number

  private[vanilla] type NieNumber = String
  private[vanilla] object NieNumber:
    def apply(number: String): NieNumber =
      require(number.forall(_.isDigit), s"NIE number '$number' should not contain letters")
      require(number.length == 7, s"NIE number '$number' should contain 7 digits")
      number

  sealed trait ID

  private[vanilla] final class DNI private (number: DniNumber, letter: ControlLetter) extends ID:
    val _number = number.toInt
    require(
      ControlLetter.fromOrdinal(_number % 23) == letter,
      s"DNI number '$number' does not match the control letter '$letter'"
    )
    override def toString: String = s"$number-$letter"

  private[vanilla] object DNI:
    def apply(input: String): DNI =
      val (number, letter) = input.splitAt(input.length - 1)
      val _number = DniNumber(number)
      val _letter = ControlLetter.valueOf(letter)
      // new DNI(number, _letter) // Compiles, passes tests... but it is actually wrong. Check it out!
      new DNI(_number, _letter)

  private[vanilla] final class NIE private (nieLetter: NieLetter, number: NieNumber, letter: ControlLetter) extends ID:
    val ordinalOfNIE = nieLetter.ordinal // Extracts the number representation of the NIE Letter
    val _number = s"$ordinalOfNIE$number".toInt // Appends the number representation from NIE Letter to the number
    require(
      ControlLetter.fromOrdinal(_number % 23) == letter,
      s"NIE number '$number' does not match the control letter '$letter'"
    )
    override def toString: String = s"$nieLetter-$number-$letter"

  private[vanilla] object NIE:
    def apply(input: String): NIE =
      val nieLetter = input.head.toString
      val (number, letter) = input.tail.splitAt(input.tail.length - 1)
      val _nieLetter = NieLetter.valueOf(nieLetter)
      val _number = NieNumber(number)
      val _letter = ControlLetter.valueOf(letter)
      // new NIE(_nieLetter, number, _letter) // Compiles, passes tests... but it is actually wrong. Check it out!
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
