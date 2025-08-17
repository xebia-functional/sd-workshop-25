# Use Case: Spanish IDs

The original source (in Spanish only) can be found [here](https://www.interior.gob.es/opencms/es/servicios-al-ciudadano/tramites-y-gestiones/dni/calculo-del-digito-de-control-del-nif-nie/)

## Breaking the use case into pieces in DDD style

### Nouns

1. **DNI** (Documento Nacional de Identidad):
    - Used by Spanish citizens
    - Composed of 8 numerical digits and 1 control letter

2. **NIE (Número de Identidad de Extranjero)**:
    - Used by foreign residents in Spain
    - Composed of 1 NIE letter (X, Y, or Z), 7 numerical digits, and 1 control letter

3. **Control Digit/Letter**:
    - Verifies the validity of both DNI and NIE numbers
    - Calculated using a specific algorithm

4. **Remainder**:
    - Result of dividing a core number by 23
    - Used to determine the control letter

5. **Control Letter**:
    - Corresponds to a remainder value mapped to a specific character (e.g., 0 → T, 1 → R, 2 → W, etc.)

6. **NIE Letter** (NIE-specific):
    - Represents the category of the resident:
        - X → 0, Y → 1, Z → 2 (substitution rules)

7. **Validation Algorithm Table**:
    - Mapping table to calculate the control letter:
        - Remainders (0-22) correspond to specific letters (e.g., 14 → Z)

8. **Input Data**:
    - DNI or NIE number provided by the user that requires validation

### Behaviors

1. **Accept Input**:
    - Allow the user to input a valid DNI or NIE string
    - Ensure the input follows proper formats (DNI: [8 digits + 1 letter], NIE: [1 letter + 7 digits + 1 letter])

2. **Normalize NIE Prefix**:
    - Substitute NIE letters X, Y, or Z with their respective numeric equivalents (0, 1, 2)

3. **Extract Control Letter**:
    - Extract the control letter from the input string

4. **Extract Core Number**:
    - For DNI: Extract the 8 digits
    - For NIE: Extract the transformed numeric NIE letter along with the 7 digits

5. **Perform Remainder Calculation**:
    - Divide the core number by 23 and calculate the remainder

6. **Lookup Control Letter in Table**:
    - Use the calculated remainder to find the expected control letter using the provided table mapping

7. **Validate Input**:
    - Compare the calculated control letter to the user-provided control letter
    - Determine whether the input is valid (matches) or invalid (does not match)

8. **Provide Feedback**:
    - If valid, indicate success (e.g., "Valid DNI/NIE")
    - If invalid, provide the user with an appropriate error message (e.g., "Invalid DNI/NIE. Control letter mismatch.")

### Rules

1. **DNI Validation**:
    - A DNI must consist of 8 numerical digits followed by 1 letter (e.g., 12345678Z)
    - The control letter is determined by dividing the 8 digits by 23, calculating the remainder, and referencing the mapping table

2. **NIE Validation**:
    - A NIE must consist of 1 NIE letter (X, Y, or Z), followed by 7 numerical digits, and 1 control letter (e.g., Y1234567L)
    - The NIE letter must be replaced by its numeric equivalent:
        - X → 0
        - Y → 1
        - Z → 2
    - The control letter is then determined using the same algorithm as the DNI

3. **Mapping Table for Control Letter**:
    - The remainder from the division of the core number (DNI: 8 digits; NIE: replaced NIE letter  + 7 digits) by 23 maps to a specific letter:
      ```
      Remainders and Corresponding Letters:
      0 → T, 1 → R, 2 → W, 3 → A, 4 → G, 5 → M, 6 → Y, 7 → F, 8 → P, 9 → D,
      10 → X, 11 → B, 12 → N, 13 → J, 14 → Z, 15 → S, 16 → Q, 17 → V, 18 → H,
      19 → L, 20 → C, 21 → K, 22 → E
      ```

4. **Valid Characters**:
    - **DNI**:
        - Digits: 0-9 for core number
        - Final character is a letter from the mapping table
    - **NIE**:
        - Prefix character: X, Y, or Z
        - Digits: 0-9 for core number
        - Final character is a letter from the mapping table

5. **Formatted Input**:
    - Reject improperly formatted strings (e.g., characters in place of digits, invalid letter, missing sections)

### Invariants

1. The remainder must always be an integer between 0 and 22, as it results from a modulo operation (`remainder = core_number % 23`)

2. The control letter must always match the mapping for the calculated remainder

3. NIE prefix substitutions must always follow these rules:
    - X → 0
    - Y → 1
    - Z → 2

4. Input strings that do not fit the structure of DNI or NIE are always invalid

5. The mapping table for the control letter is fixed and invariant. Here is the correspondence for reference:
   ```
   0 → T, 1 → R, 2 → W, 3 → A, 4 → G, 5 → M, 6 → Y, 7 → F, 8 → P, 9 → D,
   10 → X, 11 → B, 12 → N, 13 → J, 14 → Z, 15 → S, 16 → Q, 17 → V, 18 → H,
   19 → L, 20 → C, 21 → K, 22 → E
   ```

6. A DNI or NIE with a mismatched control letter is never valid

### Example Use Cases

1. **Valid DNI Validation**:
    - Input: `12345678Z`
    - Calculation: `12345678 % 23 = 14 → Z`
    - Result: Valid

2. **Invalid DNI Validation**:
    - Input: `12345678A`
    - Calculation: `12345678 % 23 = 14 → Z`
    - Result: Invalid

3. **Valid NIE Validation**:
    - Input: `Y1234567L`
    - Substitution: `Y → 1 → 11234567 % 23 = 19 → L`
    - Result: Valid

4. **Invalid NIE Validation**:
    - Input: `Z7654321A`
    - Substitution: `Z → 2 → 27654321 % 23 = 5 → M`
    - Result: Invalid
