package advanced

import scala.util.control.NoStackTrace
import neotype.*
import domain.invariants.*
import domain.errors.*

/**
 * Get creative. You can declare new types made of tuples:
 * {{{
 *   object MyTuple extendsNewtype[(A, B, ..., Z)]
 * }}}
 */
object B_FullNeoType:

  // TODO you could create additional types for the invariants, but you dont have to.
  private object NieLetterNT extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = ???
  private object ControlLetterNT extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = ???

  // TODO newtypes for Validated Fields
  private type ValidInput = ValidInput.Type
  private object ValidInput extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = ???
  private type ValidNumber = ValidNumber.Type
  private object ValidNumber extends Newtype[String]:
    override inline def validate(input: String): Boolean | String = ???
  private type ValidDniNumber = ValidDniNumber.Type
  private object ValidDniNumber extends Newtype[ValidNumber]:
    override inline def validate(input: ValidNumber): Boolean | String = ???
  private type ValidNieNumber = ValidNieNumber.Type
  private object ValidNieNumber extends Newtype[ValidNumber]:
    override inline def validate(input: ValidNumber): Boolean | String = ???

  // TODO create neotypes for valid DNIs and NIEs. Maybe you can use tuples here
  private type ValidDNI = ValidDNI.Type
  private object ValidDNI extends Newtype[???]:
    override inline def validate(input: ???): Boolean | String = ???

  private type ValidNIE = ValidNIE.Type
  private object ValidNIE extends Newtype[???]:
    override inline def validate(input: NieTuple): Boolean | String = ???

  private[advanced] object DNI extends Newtype[String]:
    // TODO implement with all the validations
    override inline def validate(input: String): Boolean | String = ???

    extension (dni: DNI.Type)
      // TODO implement the extension so we can call `formatted` since we cannot extend ID
      def formatted: String = ???

  private[advanced] object NIE extends Newtype[String]:
    // TODO implement with all the validations
    override inline def validate(input: String): Boolean | String = ???
    extension (nie: NIE.Type)
      // TODO implement the extension so we can call `formatted` since we cannot extend ID
      def formatted: String = ???


  object ID extends Newtype[String]:
    // TODO implement it adding some additional requirements for ergonomics of users:
    // - Trim the input
    // - Replace dashes with empty char
    // - Capitalize the input
    // If the user add an ID with a dash, with lower case or adds an empty spaces, it will be handled
    override inline def validate(input: String): Boolean | String = ???
