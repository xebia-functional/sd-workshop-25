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

object G_OpaqueTypesWithErrorHandling:

  opaque type NIELetter = String
  private object NIELetter:
    private def apply(input: String): NIELetter = input
    def either(input: String): Either[String, NIELetter] =
      Either.cond(
        NieLetter.values.map(_.toString).contains(input),
        NIELetter(input),
        s"'$input' is not a valid NIE letter"
      )

  opaque type Number = Int
  private object Number:
    private def apply(number: Int): Number = number
    def either(number: Int): Either[String, Number] =
      Either.cond(
        number > 0 && number <= 99999999,
        Number(number),
        if number <= 0 then s"'$number' is negative. It must be positive"
        else s"'$number' is too big. Max number is 99999999"
      )

  opaque type Letter = String
  private object Letter:
    private def apply(input: String): Letter = input
    def either(input: String): Either[String, Letter] =
      Either.cond(
        ControlLetter.values.map(_.toString).contains(input),
        Letter(input),
        s"'$input' is not a valid DNI letter"
      )

  sealed trait ID

  private class DNI private (number: Number, letter: Letter) extends ID:
    require(0 < number)
    require(number <= 9999999)
    require(ControlLetter.values.map(_.toString).contains(letter))
    override def toString: String = s"$number-$letter"

  private object DNI:
    def either(
        number: Either[String, Number],
        letter: Either[String, Letter]
    ): Either[String, DNI] =
      for
        n <- number
        l <- letter
      yield new DNI(n, l)

  private class NIE private (nieLetter: NIELetter, number: Number, letter: Letter) extends ID:
    require(NieLetter.values.map(_.toString).contains(nieLetter))
    require(0 < number)
    require(number <= 9999999)
    require(ControlLetter.values.map(_.toString).contains(letter))
    override def toString: String = s"$nieLetter-$number-$letter"

  private object NIE:
    def either(
        nieLetter: Either[String, NIELetter],
        number: Either[String, Number],
        letter: Either[String, Letter]
    ): Either[String, NIE] =
      for
        nl <- nieLetter
        n <- number
        l <- letter
      yield new NIE(nl, n, l)

  object ID:
    def either(input: String): Either[String, ID] =
      val trimmed = input.trim
      val withoutDash = trimmed.replace("-", "")
      if withoutDash.head.isDigit
      then
        val (number, letter) = withoutDash.splitAt(8)
        DNI.either(
          number = Number.either(number.toInt),
          letter = Letter.either(letter)
        )
      else
        val (number, letter) = withoutDash.tail.splitAt(7)
        NIE.either(
          nieLetter = NIELetter.either(withoutDash.head.toString),
          number = Number.either(number.toInt),
          letter = Letter.either(letter)
        )
