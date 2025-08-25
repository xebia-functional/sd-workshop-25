package errorHandling

import domain.errors.*
import domain.ID
import domain.invariants.*
import domain.rules.*

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

object B_OpaqueTypesWithErrorHandling:

  private[errorHandling] opaque type Number = String
  private[errorHandling] object Number:
    def apply(number: String): Number =
      requireValidNumber(number)
      number

    def either(number: String): Either[InvalidNumber, Number] =
      Either.cond(
        number.forall(_.isDigit),
        Number(number),
        InvalidNumber(number)
      )

  private[errorHandling] opaque type NieNumber = String
  private[errorHandling] object NieNumber:
    def apply(nieNumber: String): NieNumber =
      requireValidNieNumber(Number(nieNumber))
      nieNumber

    def either(nieNumber: String): Either[FailedValidation, NieNumber] =
      Number
        .either(nieNumber)
        .flatMap: number =>
          Either.cond(
            number.length == 7,
            NieNumber(number),
            InvalidNieNumber(number)
          )

  private[errorHandling] opaque type DniNumber = String
  private[errorHandling] object DniNumber:
    def apply(dniNumber: String): DniNumber =
      requireValidDniNumber(Number(dniNumber))
      dniNumber

    def either(dniNumber: String): Either[FailedValidation, DniNumber] =
      Number
        .either(dniNumber)
        .flatMap: number =>
          Either.cond(
            number.length == 8,
            DniNumber(number),
            InvalidDniNumber(number)
          )

  private[errorHandling] final class DNI private (dniNumber: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$dniNumber-$letter"

  private[errorHandling] object DNI:
    def apply(input: String): DNI =
      requireValidInput(input)
      val (number, letter) = input.splitAt(8)
      requireValidControlLetter(letter)
      val (dniNumber, controlLetter) = (DniNumber(number), ControlLetter.valueOf(letter))
      requireValidDni(dniNumber, controlLetter)
      new DNI(dniNumber, controlLetter)

    def either(input: String): Either[FailedValidation, DNI] =
      val (number, letter) = input.splitAt(input.length - 1)
      for
        dniNumber <- DniNumber.either(number)
        controlLetter <- ControlLetter.either(letter)
        result <- Either.cond(
          dniNumber.toInt % 23 == controlLetter.ordinal,
          new DNI(dniNumber, controlLetter),
          InvalidDni(dniNumber, controlLetter)
        )
      yield result

  private[errorHandling] final class NIE private (nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter)
      extends ID:
    override def formatted: String = s"$nieLetter-$nieNumber-$letter"

  private[errorHandling] object NIE:
    def apply(input: String): NIE =
      requireValidInput(input)
      val (firstLetter, number, secondLetter) = input.head.toString *: input.tail.splitAt(7)
      requireValidNieLetter(firstLetter)
      requireValidControlLetter(secondLetter)
      val (nieLetter, nieNumber, controlLetter) =
        (NieLetter.valueOf(firstLetter), NieNumber(number), ControlLetter.valueOf(secondLetter))
      requireValidNie(nieLetter, nieNumber, controlLetter)
      new NIE(nieLetter, nieNumber, controlLetter)

    def either(input: String): Either[FailedValidation, NIE] =
      val (firstLetter, number, secondLetter) = input.head.toString *: input.tail.splitAt(input.length - 2)
      for
        _nieLetter <- NieLetter.either(firstLetter)
        nieNumber <- NieNumber.either(number)
        controlLetter <- ControlLetter.either(secondLetter)
        result <- Either.cond(
          ((_nieLetter.ordinal * 10000000) + nieNumber.toInt) % 23 == controlLetter.ordinal,
          new NIE(_nieLetter, nieNumber, controlLetter),
          InvalidNie(_nieLetter, nieNumber, controlLetter)
        )
      yield result

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

    def either(input: String): Either[FailedValidation, ID] =

      // Preprocessing the input
      val sanitizedInput =
        input.trim // Handling empty spaces around
          .replace("-", "") // Removing dashes
          .toUpperCase() // Handling lower case
      if !(sanitizedInput.length == 9 && sanitizedInput.forall(_.isLetterOrDigit))
      then Left(InvalidInput(input))
      else
        // Selecting which type of ID base on initial character type - Letter or Digit
        if sanitizedInput.head.isDigit // Splitting between DNI and NIE
        then DNI.either(sanitizedInput)
        else NIE.either(sanitizedInput)
