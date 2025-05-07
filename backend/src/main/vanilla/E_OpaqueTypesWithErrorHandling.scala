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

  private[vanilla] opaque type NieNumber = String
  private[vanilla] object NieNumber:
    def either(number: String): Either[FailedValidation, NieNumber] =
      if !number.forall(_.isDigit) then Left(InvalidNaN(number))
      else if number.length != 7 then Left(InvalidNieNumber(number))
      else Right(new NieNumber(number))

  private[vanilla] opaque type DniNumber = String
  private[vanilla] object DniNumber:
    def either(number: String): Either[FailedValidation, DniNumber] =
      if !number.forall(_.isDigit) then Left(InvalidNaN(number))
      else if number.length != 8 then Left(InvalidDniNumber(number))
      else Right(new DniNumber(number))

  private[vanilla] final class DNI private (number: DniNumber, letter: ControlLetter) extends ID:
    override def pretty: String = s"$number-$letter"

  private[vanilla] object DNI:
    def either(input: String): Either[FailedValidation, DNI] =
      val number = input.dropRight(1)
      val letter = input.last.toString
      for
        _number <- DniNumber.either(number)
        _letter <- ControlLetter.either(letter)
        result <- Either.cond(
          number.toInt % 23 == _letter.ordinal,
          new DNI(_number, _letter),
          InvalidDni(number, letter)
        )
      yield result

  private[vanilla] final class NIE private (nieLetter: NieLetter, number: NieNumber, letter: ControlLetter) extends ID:
    override def pretty: String = s"$nieLetter-$number-$letter"

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
          s"${_nieLetter.ordinal}$number".toInt % 23 == _letter.ordinal,
          new NIE(_nieLetter, _number, _letter),
          InvalidNie(nieLetter, number, letter)
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
      if _input.isEmpty || _input.forall(!_.isLetterOrDigit)
      then Left(InvalidInput(input))
      else
        // Validating the cleaned input
        require(!_input.isEmpty)
        require(_input.forall(_.isLetterOrDigit))
      
        // Selecting which type of ID base on initial character type - Letter or Digit
        if _input.head.isDigit // Splitting between DNI and NIE
        then DNI.either(_input)
        else NIE.either(_input)
        