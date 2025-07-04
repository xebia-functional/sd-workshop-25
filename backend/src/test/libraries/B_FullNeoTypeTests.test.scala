package backend.libraries

import backend.libraries.B_FullNeoType.*

import utest.*
import neotype.*
import backend.common.*
import scala.compiletime.testing.typeCheckErrors

object B_FullNeoTypeTests extends TestSuite:

  val tests = Tests {

    test("DNI"):

      import DNI.formatted

      test("Runtime happy path"):
        Seq(
          ("12345678Z", "12345678-Z"),
          ("00000001R", "00000001-R"),
          ("99999999R", "99999999-R")
        ).foreach:
          case (input, expected) => DNI.make(input).foreach: 
            dni => assert(dni.formatted == expected) 

      test("Runtime unhappy path"):

        test("Invalid Dni Number: Too short"):
          DNI.make("1234567T") match
            case Left(error) => assert(error == InvalidDniNumber("1234567").cause)
            case Right(_) => assert(false)  

        test("Invalid Dni Number: Too long"):
          DNI.make("123456789T") match
            case Left(error) => assert(error == InvalidDniNumber("123456789").cause)
            case Right(_) => assert(false)  

        test("Invalid Number"):
          DNI.make("1234567AT") match
            case Left(error) => assert(error == InvalidNumber("1234567A").cause)
            case Right(_) => assert(false)

        test("Invalid ControlLetter"):
          DNI.make("12345678Ñ") match
            case Left(error) => assert(error == InvalidControlLetter("Ñ").cause)
            case Right(_) => assert(false)
  
        test("Invalid Dni"):
          DNI.make("00000001Z") match
            case Left(error) =>  assert(error.toString == InvalidDni("00000001", ControlLetter.Z).cause)
            case Right(_) => assert(false)
  
    test("NIE"):

      import NIE.formatted
      
      test("Runtime happy path"):
        Seq(
          ("X0000001R", "X-0000001-R"),
          ("Y2345678Z", "Y-2345678-Z")
        ).foreach:
          case (input, expected) => NIE.make(input).foreach: 
            nie => assert(nie.formatted == expected)

      test("Runtime unhappy path"):
          
        test("Invalid Nie Letter"):
          NIE.make("A1234567T") match
            case Left(error) => assert(error == InvalidNieLetter("A").cause)
            case Right(_) => assert(false)

        test("Invalid Nie Number: Too short"):
          NIE.make("Y234567T") match
            case Left(error) => assert(error == InvalidNieNumber("234567").cause)
            case Right(_) => assert(false)
  
        test("Invalid Nie Number: Too long"):
          NIE.make("Y23456789T") match
            case Left(error) => assert(error == InvalidNieNumber("23456789").cause)
            case Right(_) => assert(false)
  
        test("Invalid Number"):
          NIE.make("Y234567AT") match
            case Left(error) => assert(error == InvalidNumber("234567A").cause)
            case Right(_) => assert(false)
  
        test("Invalid Control Letter"):
          NIE.make("Y2345678Ñ") match
            case Left(error) => assert(error == InvalidControlLetter("Ñ").cause)
            case Right(_) => assert(false)
  
        test("Invalid Nie"):
          NIE.make("X0000001Z") match
            case Left(error) => assert(error == InvalidNie(NieLetter.X, "0000001", ControlLetter.Z).cause)
            case Right(_) => assert(false)
              

    test("IDs"):
      
      import ID.formatted

      test("Runtime happy path"):

        test("Valid input"):
          Seq(
            ("12345678Z", "12345678-Z"),
            ("00000001R", "00000001-R"),
            ("99999999R", "99999999-R"),
            ("X0000001R", "X-0000001-R"),
            ("Y2345678Z", "Y-2345678-Z")
          ).foreach:
            case (input, expected) => ID.make(input).foreach:
             id => id.formatted == expected

        test("Handling"):
          
          test("white spaces"):
            Seq(
              ("  12345678Z  ", "12345678-Z"),
              ("  X1234567L  ", "X-1234567-L")
            ).foreach:
              case (input, expected) => ID.make(input).foreach:
                id => assert(id.formatted == expected)

            test("Dash"):
              Seq(
                ("12345678-Z", "12345678-Z"),
                ("X-1234567-L", "X-1234567-L")
              ).foreach:
                case (input, expected) => ID.make(input).foreach:
                  id => assert(id.formatted == expected)    

            test("Lower case"):
              Seq(
                ("12345678z", "12345678-Z"),
                ("00000001r", "00000001-R"),
                ("99999999r", "99999999-R"),
                ("X0000001r", "X-0000001-R"),
                ("Y2345678z", "Y-2345678-Z")
              ).foreach:
                case (input, expected) => ID.make(input).foreach:
                  id => assert(id.formatted == expected)

      test("Runtime unhappy path"):
        
        test("InvalidInput: empty"):
          ID.make("         ") match
            case Left(error) => assert(error == InvalidInput("         ").cause)
            case Right(_) => assert(false)
  
        test("InvalidInput: invisible characters"):
          ID.make("\n\r\t\n\r\t\n\r\t") match
            case Left(error) => assert(error == InvalidInput("\n\r\t\n\r\t\n\r\t").cause)
            case Right(_) => assert(false)
          
        test("InvalidInput: symbols"):
          ID.make("@#¢∞¬÷“”≠") match
            case Left(error) => assert(error == InvalidInput("@#¢∞¬÷“”≠").cause)
            case Right(_) => assert(false)
  
        test("InvalidInput: too short"):
          ID.make("Y") match
            case Left(error) => assert(error == InvalidInput("Y").cause)
            case Right(_) => assert(false)
          
        test("InvalidInput: too long - number"):
          ID.make("123456789-Z") match
            case Left(error) => assert(error == InvalidInput("123456789-Z").cause)
            case Right(_) => assert(false)
          
        test("InvalidInput: too long - underscore"):
          ID.make("12345678_Z") match
            case Left(error) => assert(error == InvalidInput("12345678_Z").cause)
            case Right(_) => assert(false)
          
        test("InvalidInput: too long - dot"):
          ID.make("12345678.Z") match
            case Left(error) => assert(error == InvalidInput("12345678.Z").cause)
            case Right(_) => assert(false)  
  }
