package libraries

import domain.errors.*
import domain.ID
import domain.invariants.*

import neotype.*

/**
 * Implement the logic based on this 3 pieces of reference:
 *
 * - [[https://www.youtube.com/watch?v=6AxSX_WX7ek&themeRefresh=1 5-Minute-Video]]
 *
 * - [[https://github.com/kitlangton/neotype/blob/main/examples/src/main/scala/examples/Main.scala Main]]
 *
 * - [[https://github.com/kitlangton/neotype/blob/main/examples/src/main/scala/examples/types/Newtypes.scala New-Types]]
 *
 */
object A_NeoType:

  // TODO implement with all the validations
  private type NieNumber = NieNumber.Type
  private object NieNumber extends Newtype[String]:
    override inline def validate(nieNumber: String): Boolean | String = ???
  private type DniNumber = DniNumber.Type
  private object DniNumber extends Newtype[String]:
    override inline def validate(dniNumber: String): Boolean | String = ???

  private[libraries] final class DNI private (number: DniNumber, letter: ControlLetter) extends ID:
    override def formatted: String = s"$number-$letter"

  private[libraries] object DNI:
    // TODO implement with all the validations
    def either(input: String): Either[String, DNI] = ???

  private[libraries] final class NIE private (nieLetter: NieLetter, number: NieNumber, letter: ControlLetter)
      extends ID:
    override def formatted: String = s"$nieLetter-$number-$letter"

  private[libraries] object NIE:
    // TODO implement with all the validations
    def either(input: String): Either[String, NIE] = ???

  object ID:
    // TODO implement it adding some additional requirements for ergonomics of users:
    // - Trim the input
    // - Replace dashes with empty char
    // - Capitalize the input
    // If the user add an ID with a dash, with lower case or adds an empty spaces, it will be handled
    def either(input: String): Either[String, ID] = ???