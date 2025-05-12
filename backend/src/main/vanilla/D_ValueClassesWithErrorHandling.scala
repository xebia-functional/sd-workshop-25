package backend.vanilla

import backend.common.*

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

  private[vanilla] final class Number(val value: String) extends AnyVal
  
  private[vanilla] object Number:
    
    def apply(number: String): Number =
      requireValidNumber(number)
      new Number(number)
    
    def either(number: String): Either[InvalidNumber, Number] =
      Either.cond(
        number.forall(_.isDigit),
        new Number(number),
        InvalidNumber(number)
      )
 
  private[vanilla] final class NieNumber(val value: String) extends AnyVal
  
  private[vanilla] object NieNumber:

    def apply(nieNumber: String): NieNumber =
      val number = Number(nieNumber)
      requireValidNieNumber(number.value)
      new NieNumber(number.value)
    
    def either(nieNumber: String): Either[FailedValidation, NieNumber] =
      Number.either(nieNumber).flatMap: number =>
        Either.cond(
          number.value.length == 7,
          new NieNumber(number.value),
          InvalidNieNumber(number.value)
        )

  private[vanilla] final class DniNumber(val value: String) extends AnyVal
  
  private[vanilla] object DniNumber:

    def apply(dniNumber: String): DniNumber =
      val number = Number(dniNumber)
      requireValidDniNumber(number.value)
      new DniNumber(number.value)
    
    def either(dniNumber: String): Either[FailedValidation, DniNumber] =
      Number.either(dniNumber).flatMap: number =>
        Either.cond(
          number.value.length == 8,
          new DniNumber(number.value),
          InvalidDniNumber(number.value)
        )

  private[vanilla] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    
    override def toString(): String = s"${dniNumber.value}-$letter"

  private[vanilla] object DNI:

    def apply(input: String): DNI =
      val number = input.dropRight(1)
      val dniNumber = DniNumber(number)
      val letter = input.last.toString
      requireValidControlLetter(letter)
      val controlLetter = ControlLetter.valueOf(letter)
      requireValidDni(dniNumber.value, controlLetter)
      new DNI(dniNumber, controlLetter)
    
    def either(input: String): Either[FailedValidation, DNI] =
      val number = input.dropRight(1)
      val letter = input.last.toString
      for
        dniNumber <- DniNumber.either(number)
        controlLetter <- ControlLetter.either(letter)
        result <- Either.cond(
          dniNumber.value.toInt % 23 == controlLetter.ordinal,
          new DNI(dniNumber, controlLetter),
          InvalidDni(dniNumber.value, controlLetter)
        )
      yield result

  private[vanilla] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter) extends ID:
    
    override def toString(): String = s"$nieLetter-${nieNumber.value}-$letter"

  private[vanilla] object NIE:

    def apply(input: String): NIE =
      val nieLetter = input.head.toString
      requireValidNieLetter(nieLetter)
      val _nieLetter = NieLetter.valueOf(nieLetter)
      val number = input.tail.dropRight(1)
      val nieNumber = NieNumber(number)
      val letter = input.last.toString
      requireValidControlLetter(letter)
      val controlLetter = ControlLetter.valueOf(letter)
      requireValidNie(_nieLetter, nieNumber.value, controlLetter)
      new NIE(_nieLetter, nieNumber, controlLetter)
    
    def either(input: String): Either[FailedValidation, NIE] =
      val nieLetter = input.head.toString
      val number = input.tail.dropRight(1)
      val letter = input.last.toString
      for
        _nieLetter <- NieLetter.either(nieLetter)
        nieNumber <- NieNumber.either(number)
        controlLetter <- ControlLetter.either(letter)
        result <- Either.cond(
           ((_nieLetter.ordinal * 10000000) + nieNumber.value.toInt) % 23 == controlLetter.ordinal,
           new NIE(_nieLetter, nieNumber, controlLetter),
           InvalidNie(_nieLetter, nieNumber.value.toString, controlLetter)
        )
      yield result

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
        