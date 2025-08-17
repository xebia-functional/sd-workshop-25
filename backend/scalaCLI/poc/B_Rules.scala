package poc

import poc.A_Invariants.*

object B_Rules:

  /** 
   * Come up with tailored error messages you want to give back to the user in the UI 
   */
  object messages:

    def invalidInput(input: ???): String = ???
    def invalidNumber(number: ???): String = ???
    def invalidDniNumber(dniNumber: ???): String = ???
    def invalidNieNumber(nieNumber: ???): String = ???
    def invalidNieLetter(nieLetter: ???): String = ???
    def invalidControlLetter(controlLetter: ???): String = ???
    def invalidDni(dniNumber: ???, letter: ???): String = ???
    def invalidNie(nieLetter: ???, nieNumber: ???, letter: ???): String = ???
    
  end messages

  /** 
   * Codify those error messages into requirements
   */
  object requirements:

    import messages.*

    def requireValidInput(input: ???): Unit = ???
    def requireValidNumber(number: ???): Unit = ???
    def requireValidDniNumber(dniNumber: ???): Unit = ???
    def requireValidNieNumber(nieNumber: String): Unit = ???
    def requireValidNieLetter(nieLetter: String): Unit = ???
    def requireValidControlLetter(controlLetter: String): Unit = ???
    def requireValidDni(dniNumber: String, letter: ControlLetter): Unit = ???
    def requireValidNie(nieLetter: ???, nieNumber: ???, letter: ???): Unit = ???

  end requirements

end B_Rules
