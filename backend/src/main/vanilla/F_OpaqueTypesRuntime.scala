package backend.vanilla

import backend.common.*

import scala.compiletime.constValue
import scala.compiletime.error
import scala.compiletime.ops.any.ToString
import scala.compiletime.ops.boolean.!
import scala.compiletime.ops.boolean.||
import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.<=
import scala.compiletime.ops.int.>
import scala.compiletime.ops.int.+
import scala.compiletime.ops.int.%
import scala.compiletime.ops.string.Matches

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

  private[vanilla] opaque type DNI = (Int, String)

  extension(dni: DNI)
    def pretty: String = s"${s"%0${8}d".format(dni._1)}-${dni._2}"

  private[vanilla] object DNI:

    private inline def isValidDNI(number: Int, letter: String, expected: String): DNI =
      inline if letter == expected 
      then new DNI(number, letter)
      else error("DNI number '" + constValue[ToString[number.type]] + "' does not match the control letter '" + letter + "', expected '" + expected + "'")  

    inline def apply(dniNumber: Int, controlLetter: String): DNI =
      inline if dniNumber <= 0 || dniNumber > 99999999
      then error("Invalid DNI number '" + constValue[ToString[dniNumber.type]] + "' should be positive and contain 8 digits") 
      else inline if constValue[![Matches[controlLetter.type, "[[TRWAGMYFPDXBNJZSQVHLCKE]{1}]"]]]
      then error("Invalid ControlLetter: '" + controlLetter + "' is not a valid Control letter")
      else inline (dniNumber % 23) match
        case 0 => isValidDNI(dniNumber, controlLetter, "T")
        case 1 => isValidDNI(dniNumber, controlLetter, "R")
        case 2 => isValidDNI(dniNumber, controlLetter, "W")
        case 3 => isValidDNI(dniNumber, controlLetter, "A")
        case 4 => isValidDNI(dniNumber, controlLetter, "G")
        case 5 => isValidDNI(dniNumber, controlLetter, "M")
        case 6 => isValidDNI(dniNumber, controlLetter, "Y")
        case 7 => isValidDNI(dniNumber, controlLetter, "F")
        case 8 => isValidDNI(dniNumber, controlLetter, "P")
        case 9 => isValidDNI(dniNumber, controlLetter, "D")
        case 10 => isValidDNI(dniNumber, controlLetter, "X")
        case 11 => isValidDNI(dniNumber, controlLetter, "B")
        case 12 => isValidDNI(dniNumber, controlLetter, "N")
        case 13 => isValidDNI(dniNumber, controlLetter, "J")
        case 14 => isValidDNI(dniNumber, controlLetter, "Z")
        case 15 => isValidDNI(dniNumber, controlLetter, "S")
        case 16 => isValidDNI(dniNumber, controlLetter, "Q")
        case 17 => isValidDNI(dniNumber, controlLetter, "V")
        case 18 => isValidDNI(dniNumber, controlLetter, "H")
        case 19 => isValidDNI(dniNumber, controlLetter, "L")
        case 20 => isValidDNI(dniNumber, controlLetter, "C")
        case 21 => isValidDNI(dniNumber, controlLetter, "K")
        case 22 => isValidDNI(dniNumber, controlLetter, "E")
    
  private[vanilla] opaque type NIE = (String, Int, String)

  extension(nie: NIE)
    def pretty: String = s"${nie._1}-${s"%0${7}d".format(nie._2)}-${nie._3}"

  private[vanilla] object NIE:

    private inline def isValidNIE(nieLetter: String, nieNumber: Int, controlLetter: String, expectedLetter: String, fullNumber: Int): NIE =
      inline if controlLetter == expectedLetter
      then new NIE(nieLetter, nieNumber, controlLetter)
      else error("NIE number '" + constValue[ToString[fullNumber.type]] + "' does not match the control letter '" + controlLetter + "', expected '" + expectedLetter + "'")  


    inline def apply(nieLetter: String, nieNumber: Int, controlLetter: String): NIE =
      inline if constValue[![Matches[nieLetter.type, "[XYZ]{1}"]]]
      then error("Invalid NIE Letter: '" + nieLetter + "' is not a valid NIE letter")
      else inline if nieNumber <= 0 || nieNumber > 9999999
      then error("Invalid NIE Number: '" + constValue[ToString[nieNumber.type]] + "' should be positive and contain 7 digits")
      else inline if constValue[![Matches[controlLetter.type, "[TRWAGMYFPDXBNJZSQVHLCKE]{1}"]]]
      then error("Invalid ControlLetter: '" + controlLetter + "' is not a valid Control letter")
      else inline nieLetter match
        case "X" => inline (nieNumber % 23) match
          case 0 => isValidNIE(nieLetter, nieNumber, controlLetter, "T", nieNumber)
          case 1 => isValidNIE(nieLetter, nieNumber, controlLetter, "R", nieNumber)
          case 2 => isValidNIE(nieLetter, nieNumber, controlLetter, "W", nieNumber)
          case 3 => isValidNIE(nieLetter, nieNumber, controlLetter, "A", nieNumber)
          case 4 => isValidNIE(nieLetter, nieNumber, controlLetter, "G", nieNumber)
          case 5 => isValidNIE(nieLetter, nieNumber, controlLetter, "M", nieNumber)
          case 6 => isValidNIE(nieLetter, nieNumber, controlLetter, "Y", nieNumber)
          case 7 => isValidNIE(nieLetter, nieNumber, controlLetter, "F", nieNumber)
          case 8 => isValidNIE(nieLetter, nieNumber, controlLetter, "P", nieNumber)
          case 9 => isValidNIE(nieLetter, nieNumber, controlLetter, "D", nieNumber)
          case 10 => isValidNIE(nieLetter, nieNumber, controlLetter, "X", nieNumber)
          case 11 => isValidNIE(nieLetter, nieNumber, controlLetter, "B", nieNumber)
          case 12 => isValidNIE(nieLetter, nieNumber, controlLetter, "N", nieNumber)
          case 13 => isValidNIE(nieLetter, nieNumber, controlLetter, "J", nieNumber)
          case 14 => isValidNIE(nieLetter, nieNumber, controlLetter, "Z", nieNumber)
          case 15 => isValidNIE(nieLetter, nieNumber, controlLetter, "S", nieNumber)
          case 16 => isValidNIE(nieLetter, nieNumber, controlLetter, "Q", nieNumber)
          case 17 => isValidNIE(nieLetter, nieNumber, controlLetter, "V", nieNumber)
          case 18 => isValidNIE(nieLetter, nieNumber, controlLetter, "H", nieNumber)
          case 19 => isValidNIE(nieLetter, nieNumber, controlLetter, "L", nieNumber)
          case 20 => isValidNIE(nieLetter, nieNumber, controlLetter, "C", nieNumber)
          case 21 => isValidNIE(nieLetter, nieNumber, controlLetter, "K", nieNumber)
          case 22 => isValidNIE(nieLetter, nieNumber, controlLetter, "E", nieNumber)
  
        case "Y" => inline ((10000000 + nieNumber) % 23)  match
          case 0 => isValidNIE(nieLetter, nieNumber, controlLetter, "T", 10000000 + nieNumber)
          case 1 => isValidNIE(nieLetter, nieNumber, controlLetter, "R", 10000000 + nieNumber)
          case 2 => isValidNIE(nieLetter, nieNumber, controlLetter, "W", 10000000 + nieNumber)
          case 3 => isValidNIE(nieLetter, nieNumber, controlLetter, "A", 10000000 + nieNumber)
          case 4 => isValidNIE(nieLetter, nieNumber, controlLetter, "G", 10000000 + nieNumber)
          case 5 => isValidNIE(nieLetter, nieNumber, controlLetter, "M", 10000000 + nieNumber)
          case 6 => isValidNIE(nieLetter, nieNumber, controlLetter, "Y", 10000000 + nieNumber)
          case 7 => isValidNIE(nieLetter, nieNumber, controlLetter, "F", 10000000 + nieNumber)
          case 8 => isValidNIE(nieLetter, nieNumber, controlLetter, "P", 10000000 + nieNumber)
          case 9 => isValidNIE(nieLetter, nieNumber, controlLetter, "D", 10000000 + nieNumber)
          case 10 => isValidNIE(nieLetter, nieNumber, controlLetter, "X", 10000000 + nieNumber)
          case 11 => isValidNIE(nieLetter, nieNumber, controlLetter, "B", 10000000 + nieNumber)
          case 12 => isValidNIE(nieLetter, nieNumber, controlLetter, "N", 10000000 + nieNumber)
          case 13 => isValidNIE(nieLetter, nieNumber, controlLetter, "J", 10000000 + nieNumber)
          case 14 => isValidNIE(nieLetter, nieNumber, controlLetter, "Z", 10000000 + nieNumber)
          case 15 => isValidNIE(nieLetter, nieNumber, controlLetter, "S", 10000000 + nieNumber)
          case 16 => isValidNIE(nieLetter, nieNumber, controlLetter, "Q", 10000000 + nieNumber)
          case 17 => isValidNIE(nieLetter, nieNumber, controlLetter, "V", 10000000 + nieNumber)
          case 18 => isValidNIE(nieLetter, nieNumber, controlLetter, "H", 10000000 + nieNumber)
          case 19 => isValidNIE(nieLetter, nieNumber, controlLetter, "L", 10000000 + nieNumber)
          case 20 => isValidNIE(nieLetter, nieNumber, controlLetter, "C", 10000000 + nieNumber)
          case 21 => isValidNIE(nieLetter, nieNumber, controlLetter, "K", 10000000 + nieNumber)
          case 22 => isValidNIE(nieLetter, nieNumber, controlLetter, "E", 10000000 + nieNumber)

        case "Z" => inline ((20000000 + nieNumber) % 23)  match
          case 0 => isValidNIE(nieLetter, nieNumber, controlLetter, "T", 20000000 + nieNumber)
          case 1 => isValidNIE(nieLetter, nieNumber, controlLetter, "R", 20000000 + nieNumber)
          case 2 => isValidNIE(nieLetter, nieNumber, controlLetter, "W", 20000000 + nieNumber)
          case 3 => isValidNIE(nieLetter, nieNumber, controlLetter, "A", 20000000 + nieNumber)
          case 4 => isValidNIE(nieLetter, nieNumber, controlLetter, "G", 20000000 + nieNumber)
          case 5 => isValidNIE(nieLetter, nieNumber, controlLetter, "M", 20000000 + nieNumber)
          case 6 => isValidNIE(nieLetter, nieNumber, controlLetter, "Y", 20000000 + nieNumber)
          case 7 => isValidNIE(nieLetter, nieNumber, controlLetter, "F", 20000000 + nieNumber)
          case 8 => isValidNIE(nieLetter, nieNumber, controlLetter, "P", 20000000 + nieNumber)
          case 9 => isValidNIE(nieLetter, nieNumber, controlLetter, "D", 20000000 + nieNumber)
          case 10 => isValidNIE(nieLetter, nieNumber, controlLetter, "X", 20000000 + nieNumber)
          case 11 => isValidNIE(nieLetter, nieNumber, controlLetter, "B", 20000000 + nieNumber)
          case 12 => isValidNIE(nieLetter, nieNumber, controlLetter, "N", 20000000 + nieNumber)
          case 13 => isValidNIE(nieLetter, nieNumber, controlLetter, "J", 20000000 + nieNumber)
          case 14 => isValidNIE(nieLetter, nieNumber, controlLetter, "Z", 20000000 + nieNumber)
          case 15 => isValidNIE(nieLetter, nieNumber, controlLetter, "S", 20000000 + nieNumber)
          case 16 => isValidNIE(nieLetter, nieNumber, controlLetter, "Q", 20000000 + nieNumber)
          case 17 => isValidNIE(nieLetter, nieNumber, controlLetter, "V", 20000000 + nieNumber)
          case 18 => isValidNIE(nieLetter, nieNumber, controlLetter, "H", 20000000 + nieNumber)
          case 19 => isValidNIE(nieLetter, nieNumber, controlLetter, "L", 20000000 + nieNumber)
          case 20 => isValidNIE(nieLetter, nieNumber, controlLetter, "C", 20000000 + nieNumber)
          case 21 => isValidNIE(nieLetter, nieNumber, controlLetter, "K", 20000000 + nieNumber)
          case 22 => isValidNIE(nieLetter, nieNumber, controlLetter, "E", 20000000 + nieNumber)
