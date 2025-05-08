package backend.vanilla

import backend.common.*

import scala.compiletime.constValue
import scala.compiletime.error
import scala.compiletime.ops.any.ToString
import scala.compiletime.ops.boolean.&&
import scala.compiletime.ops.boolean.||
import scala.compiletime.ops.any.!=
import scala.compiletime.ops.any.==
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

  private[vanilla] opaque type NieNumber = String
  private[vanilla] object NieNumber:
    inline def apply(number: String): NieNumber =
      inline if constValue[Matches[number.type, "[\\d]*"]]
      then 
        if constValue[Matches[number.type, "[0-9]{7}"]]
        then number 
        else error("NIE number '" + constValue[number.type] + "' should contain 7 digits")
      else error("'" + constValue[number.type] + "' should not contain letters") 

  private[vanilla] opaque type DniNumber = String
  private[vanilla] object DniNumber:
    inline def apply(number: String): DniNumber =
      inline if constValue[Matches[number.type, "[0-9]{8}"]]
      then number
      else error("Number '" + constValue[number.type] + "' should contain 8 digits")

  private[vanilla] final class DNI(dniNumber: DniNumber, letter: ControlLetter) extends ID:
    require(
      dniNumber.toString.toInt % 23 == letter.ordinal,
      s"DNI number '$dniNumber' does not match the control letter '$letter'"
    )
    override def pretty: String = s"$dniNumber-$letter"

  private[vanilla] object DNI:
    inline def apply(input: String): DNI =
      inline if constValue[!=[Length[input.type], 9]]
      then error("'" + constValue[input.type] + "' must have lenght of 9")
      else
        val number = constValue[ToString[Substring[input.type, 0, 8]]]
        val letter = constValue[ToString[Substring[input.type, 8, 9]]]
        val _number = DniNumber(number)
        val _letter = ControlLetter(letter)
        new DNI(_number, _letter)


  private[vanilla] final class NIE(nieLetter: NieLetter, nieNumber: NieNumber, letter: ControlLetter) extends ID:
    require(
      s"${nieLetter.ordinal}$nieNumber".toInt % 23 == letter.ordinal,
      s"NIE number '$nieNumber' does not match the control letter '$letter'"
    )
    override def pretty: String = s"$nieLetter-$nieNumber-$letter"

  private[vanilla] object NIE:
    inline def apply(input: String): NIE =
      inline if constValue[!=[Length[input.type], 9]]
      then error("'" + constValue[input.type] + "' must have lenght of 9")
      else
        val nieLetter = constValue[ToString[Substring[input.type, 0, 1]]]
        val number = constValue[ToString[Substring[input.type, 1, 8]]]
        val letter = constValue[ToString[Substring[input.type, 8, 9]]]
        new NIE(NieLetter(nieLetter), NieNumber(number), ControlLetter(letter))

  object ID:
    inline def apply(input: String): ID = 
      
      // Preprocesing the input
      //val _input = 
      //  input
      //    .trim              // Handeling empty spaces around
      //    .replace("-", "")  // Removing dashes
      //    .toUpperCase()     // Handling lower case 
      inline if constValue[Matches[input.type, "[\\d\\w]{9}"]]
      then
        // Selecting which type of ID base on initial character type - Letter or Digit
        inline if constValue[Matches[ToString[Substring[input.type, 0, 1]], "[0-9]{1}"]] // Splitting between DNI and NIE
        then DNI(input)
        else NIE(input)
      else error("'" + constValue[input.type] + "' should be AlphaNumeric and contains 9 characters")  

