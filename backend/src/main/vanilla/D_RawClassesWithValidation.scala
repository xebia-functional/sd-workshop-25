package backend.vanilla

import backend.common.*

/** =Regular Classes with Validation in Scala=
  *
  * Basic Syntax:
  * {{{
  *    class MyClass(val value: Int):
  *      require(boolean_condition, "Error Message")
  *      require(boolean_condition, "Error Message") // Multiple require statements can be defined
  * }}}
  *
  * '''Key Language Features Used'''
  *   - Constructor validation using 'require'
  *   - Immutable class design
  *
  * ==Pros of Class-based Validation==
  *   - Strong encapsulation of validation logic
  *   - Immutability by design
  *   - Clear separation of concerns
  *   - Can have multiple parameters
  *   - Full inheritance support
  *   - More flexible than value classes
  *
  * ==Cons of Class-based Validation==
  *   - Runtime overhead (object allocation)
  *   - Memory footprint larger than value classes
  *   - Potential performance impact with many instances
  *   - GC pressure with large numbers of objects
  */

object D_RawClassesWithValidation:

  private class NIELetter(val value: String) extends AnyVal
  private class Number(val value: Int) extends AnyVal
  private class Letter(val value: String) extends AnyVal

  sealed trait ID

  private final class DNI(number: Number, letter: Letter) extends ID:
    require(
      number.value > 0,
      s"'${number.value}' is negative. It must be positive"
    )
    require(
      number.value <= 99999999,
      s"'${number.value}' is too big. Max number is 99999999"
    )
    require(
      ControlLetter.values.map(_.toString).contains(letter.value),
      s"'${letter.value}' is not a valid ID letter"
    )
    override def toString: String = s"${number.value}-${letter.value}"

  private final class NIE(nieLetter: NIELetter, number: Number, letter: Letter)
      extends ID:
    require(
      NieLetter.values.map(_.toString).contains(nieLetter.value),
      s"'${nieLetter.value}' is not a valid NIE letter"
    )
    require(
      number.value > 0,
      s"'${number.value}' is negative. It must be positive"
    )
    require(
      number.value <= 99999999,
      s"'${number.value}' is too big. Max number is 99999999"
    )
    require(
      ControlLetter.values.map(_.toString).contains(letter.value),
      s"'${letter.value}' is not a valid ID letter"
    )
    override def toString: String =
      s"${nieLetter.value}-${number.value}-${letter.value}"

  object ID:
    def apply(input: String): ID =
      val trimmed = input.trim
      val withoutDash = trimmed.replace("-", "")
      if withoutDash.head.isDigit
      then
        val (number, letter) = withoutDash.splitAt(8)
        DNI(
          number = Number(number.toInt),
          letter = Letter(letter)
        )
      else
        val (number, letter) = withoutDash.tail.splitAt(7)
        NIE(
          nieLetter = NIELetter(withoutDash.head.toString),
          number = Number(number.toInt),
          letter = Letter(letter)
        )
