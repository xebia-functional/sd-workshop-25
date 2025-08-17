package advanced

import scala.compiletime.ops.any.{==, ToString}
import scala.compiletime.ops.boolean.{!, ||}
import scala.compiletime.ops.int.{%, +, <=, >}
import scala.compiletime.ops.string.Matches
import scala.compiletime.{constValue, error}

/** =Advance usage of Opaque types in Scala=
 *
 * Read:
 *
 * - [[https://docs.scala-lang.org/scala3/reference/metaprogramming/compiletime-ops.html Compile-Time-Ops]]
 *
 * - [[https://xebia.com/blog/crafting-concise-constructors-opaque-types-scala-3/]]
 *
 * Hint: You can use opaque types for tuples. If you want to get spicy, you can try `named tuples`
 *
 */

object A_OpaqueTypesRuntime:

  private[advanced] opaque type DNI = ???
  extension (dni: DNI) def formatted: String = ???
  private[advanced] object DNI:
    private inline def isValidDNI(number: Int, letter: String, expected: String): DNI = ???

    inline def apply(dniNumber: Int, controlLetter: String): DNI = ???

  private[advanced] opaque type NIE = ???
  extension (nie: NIE) def formatted: String = ???
  private[advanced] object NIE:
    private inline def isValidNIE(
        nieLetter: String,
        nieNumber: Int,
        controlLetter: String,
        expectedLetter: String,
        fullNumber: Int
    ): NIE = ???