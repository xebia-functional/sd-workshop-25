package basic

import domain.ID
import domain.invariants.*
import domain.rules.*

/** =Regular Classes in Scala=
  *
  * A regular class is defined using the 'class' keyword.
  *
  * Basic Syntax:
  * {{{
  *    class A (paramA: ParamAType, ..., paramN: ParamNType)
  * }}}
  *
  * '''Key Features of Regular Classes'''
  *   - Constructor parameters are defined directly in the class declaration
  *   - Classes can have methods, fields, and other members
  *   - Classes support method overriding using 'override' keyword
  *
  * ==Pros of Regular Classes==
  *   - Full support for inheritance and polymorphism
  *   - Encapsulation of data and behavior
  *   - Flexibility in defining custom methods and fields
  *   - Support for constructor parameters with default values
  *
  * ==Cons of Regular Classes==
  *   - Each instance creates a new object in memory
  *   - More verbose compared to case classes for data containers
  *   - No automatic pattern matching support
  */

object A_Classes:

  private[basic] final class DNI private (dniNumber: String, letter: ControlLetter) extends ID:
    override def formatted: String = s"$dniNumber-$letter"

  private[basic] object DNI:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): DNI = ???


  private[basic] final class NIE private (nieLetter: NieLetter, nieNumber: String, letter: ControlLetter) extends ID:
    override def formatted: String = s"$nieLetter-$nieNumber-$letter"

  private[basic] object NIE:
    // TODO implement it making sure all the requirements are met
    def apply(input: String): NIE = ???

  object ID:
    // TODO implement it adding some additional requirements for ergonomics of users:
    // - Trim the input
    // - Replace dashes with empty char
    // - Capitalize the input
    // If the user add an ID with a dash, with lower case or adds an empty spaces, it will be handled
    def apply(input: String): ID = ???
