package backend.vanilla

import backend.common.*

import scala.compiletime.constValue
import scala.compiletime.error
import scala.compiletime.ops.any.ToString
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.int.<=
import scala.compiletime.ops.int.>
import scala.compiletime.ops.int.<
import scala.compiletime.ops.string.Matches
import scala.compiletime.ops.string.CharAt
import scala.compiletime.ops.string.Length
import scala.compiletime.ops.string.Substring

/** =Opaque types with Validation in Scala=
  *
  * Opaque types allow for type abstractions without runtime overhead. They provide type safety by defining new,
  * distinct types from existing types and ensuring the correct use of these abstractions.
  *
  * Compared to type aliases, which are simply alternate names for existing types with no additional type safety, opaque
  * types enforce stricter type constraints and encapsulate the underlying type's operations and representation.
  *
  * Basic Syntax:
  * {{{
  * opaque type OpaqueType = UnderlyingType
  *
  * object OpaqueType:
  *   def apply(value: UnderlyingType): OpaqueType =
  *     require(boolean_condition, "error message")
  *     value
  * }}}
  *
  * '''Key Features'''
  *   - Creates new types, like classes, enums, etc.
  *   - They only exists at compile time
  *   - Can implement many of the features of classes and can also have a companion object
  *
  * ==Pros of Opaque Types with Validation==
  *   - Enhanced Type Safety: Encapsulates implementation details, ensuring that only valid operations are performed on
  *     the type.
  *   - Clearer Domain Modeling: Represents domain concepts more precisely by creating new types instead of using
  *     primitive ones.
  *   - Zero Overhead: Since opaque types are erased to their underlying types at runtime, they do not introduce
  *     performance penalties.
  *
  * ==Cons of Opaque Types with Validation==
  *   - Increased Complexity: May introduce additional complexity in the type system, which can be challenging for new
  *     developers.
  *   - Limited Interoperability: Sometimes difficult to work with libraries or frameworks expecting the underlying
  *     type.
  */

object F_OpaqueTypesRuntime:

  private[vanilla] opaque type NIELetter = String
  private[vanilla] object NIELetter:
    inline def apply(value: String): NIELetter =
      inline if constValue[Matches[value.type, "[XYZ]{1}"]]
      then value
      else error("'" + constValue[value.type] + "' is not a valid NIE letter")

  private[vanilla] opaque type NieNumber = String
  private[vanilla] object NieNumber:
    inline def apply(value: String): NieNumber =
      inline if constValue[Matches[value.type, "[0-9]{7}"]]
      then value
      else error("Number '" + constValue[value.type] + "' should contain 7 digits")

  private[vanilla] opaque type DniNumber = String
  private[vanilla] object DniNumber:
    inline def apply(value: String): DniNumber =
      inline if constValue[Matches[value.type, "[0-9]{8}"]]
      then value
      else error("Number '" + constValue[value.type] + "' should contain 8 digits")

  private[vanilla] opaque type Letter = String
  private[vanilla] object Letter:
    inline def apply(value: String): Letter =
      inline if constValue[Matches[value.type, "[ABCDEFGHJKLMNPQRSTVWXYZ]{1}"]]
      then value
      else error("'" + constValue[value.type] + "' is not a valid ID letter")

  sealed trait ID

  private[vanilla] final class DNI(number: DniNumber, letter: Letter) extends ID:
    require(
      ControlLetter.isValidId(number.toInt, ControlLetter.valueOf(letter)),
      "Number does not match correct control letter"
    )
    override def toString: String = s"$number-$letter"

  private[vanilla] final class NIE(nieLetter: NIELetter, number: NieNumber, letter: Letter) extends ID:
    require(
      ControlLetter.isValidId(s"${NieLetter.valueOf(nieLetter).ordinal}$number".toInt, ControlLetter.valueOf(letter)),
      "Number does not match correct control letter"
    )
    override def toString: String = s"$nieLetter-$number-$letter"

  object ID:
    inline def apply(input: String): ID =
      inline if constValue[>[Length[input.type], 9]] then
        error("ID can't have more than 9 characters. Do not use dashes")
      else if constValue[<[Length[input.type], 8]] then error("ID can't have less than 8 characters. Do not usedashes")
      else if constValue[Matches[ToString[CharAt[input.type, 0]], "[0-9]{1}"]]
      then
        inline val number = constValue[Substring[input.type, 0, 7]]
        inline val letter = constValue[Substring[input.type, 7, 8]]
        DNI(DniNumber(number), Letter(letter.toUpperCase))
      else
        inline val nieLetter = constValue[Substring[input.type, 0, 1]]
        inline val number = constValue[Substring[input.type, 1, 7]]
        inline val letter = constValue[Substring[input.type, 7, 8]]
        NIE(NIELetter(nieLetter), NieNumber(number), Letter(letter))
