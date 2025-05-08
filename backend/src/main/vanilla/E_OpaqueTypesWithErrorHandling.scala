package backend.vanilla

import backend.common.*

/** =Opaque Types with Error Handling=
  *
  * Opaque types are a Scala 3 feature that provides type abstraction without runtime overhead. It allows to create new
  * types that are light-weighted and can incorporate benefits of Value Classes and other structures.
  *
  * Basic syntax:
  * {{{
  * opaque type MyOpaqueType = UnderlyingType
  * object MyOpaqueType:
  *   def apply(underlyingValue: UnderlyingType): MyOpaqueType = underlyingValue
  *   def parse(input: UnderlyingType): Either[String, MyOpaqueType] =
  *     Either.cond(
  *       // boolean_condition
  *       MyOpaqueType(input),
  *       "Error message"
  *     )
  * }}}
  *
  * '''Key Features'''
  *   - Type safety
  *   - Validation control
  *   - Use of Either for error handling
  *
  * ==Pros of Opaque Types with Error Handling==
  *   - Performance Benefits: No runtime overhead. Opaque types do not exist during runtime.
  *   - Safety Guarantees: Compile-time type safety; runtime validation guarantees; immutable by design
  *   - Developer Experience: Clear API boundaries; self-documenting code; easy to maintain and refactor
  *   - Encapsulation: Prevent invalid states through controlled construction
  *
  * ==Cons of Value Classes with Error Handling==
  *   - Implementation Complexity: Opaque type's underlying representation is only visible in the companion object; can
  *     make debugging more challenging
  *   - Usage Restrictions: Cannot extend other classes; Limited to a single parameter; Some scenarios force boxing
  *   - Learning Curve: Requires understanding of Either type; Pattern matching knowledge needed; (Basic) Functional
  *     programming concepts required
  *   - Potential Overuse: Can lead to unnecessary abstraction if not used judiciously; might complicate simple code if
  *     used where not needed
  */

object E_OpaqueTypesWithErrorHandling:

  private[vanilla] opaque type Number = String
  private[vanilla] object Number:
    def either(number: String): Either[InvalidNumber, Number] =
      Either.cond(
        number.forall(_.isDigit),
        new Number(number),
        InvalidNumber(number)
      )

  private[vanilla] opaque type NieNumber = String
  private[vanilla] object NieNumber:
    def either(nieNumber: String): Either[FailedValidation, NieNumber] =
      Number.either(nieNumber).flatMap: number =>
        Either.cond(
          number.toString.length == 7,
          new NieNumber(number.toString),
          InvalidNieNumber(number.toString)
        )

  private[vanilla] opaque type DniNumber = String
  private[vanilla] object DniNumber:
    def either(dniNumber: String): Either[FailedValidation, DniNumber] =
      Number.either(dniNumber).flatMap: number =>
        Either.cond(
          number.toString.length == 8,
          new DniNumber(number.toString),
          InvalidDniNumber(number.toString)
        )

  private[vanilla] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def pretty: String = s"$dniNumber-$letter"

  private[vanilla] object DNI:
    def either(input: String): Either[FailedValidation, DNI] =
      val number = input.dropRight(1)
      val letter = input.last.toString
      for
        _number <- DniNumber.either(number)
        _letter <- ControlLetter.either(letter)
        result <- Either.cond(
          _number.toString.toInt % 23 == _letter.ordinal,
          new DNI(_number, _letter),
          InvalidDni(_number.toString, _letter)
        )
      yield result

  private[vanilla] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter) extends ID:
    override def pretty: String = s"$nieLetter-$nieNumber-$letter"

  private[vanilla] object NIE:
    def either(input: String): Either[FailedValidation, NIE] =
      val nieLetter = input.head.toString
      val number = input.tail.dropRight(1)
      val letter = input.last.toString
      for
        _nieLetter <- NieLetter.either(nieLetter)
        _number <- NieNumber.either(number)
        _letter <- ControlLetter.either(letter)
        result <- Either.cond(
         ((_nieLetter.ordinal*10000000) + _number.toString.toInt) % 23 == _letter.ordinal,
          new NIE(_nieLetter, _number, _letter),
          InvalidNie(_nieLetter, _number.toString, _letter)
        )
      yield result

  object ID:
    def either(input: String): Either[FailedValidation, ID] = 
      
      // Preprocesing the input
      val _input = 
        input
          .trim              // Handeling empty spaces around
          .replace("-", "")  // Removing dashes
          .toUpperCase()     // Handling lower case 
      if !(_input.length == 9 && _input.forall(_.isLetterOrDigit))
      then Left(InvalidInput(input))
      else    
        // Selecting which type of ID base on initial character type - Letter or Digit
        if _input.head.isDigit // Splitting between DNI and NIE
        then DNI.either(_input)
        else NIE.either(_input)
        