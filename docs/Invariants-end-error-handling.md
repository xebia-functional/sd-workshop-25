# Domain-Driven Design Analysis: Spanish ID Validation System

## Value Objects

### NieLetter and ControlLetter Enumerations
```scala
enum NieLetter:
  case X // 0
  case Y // 1  
  case Z // 2

enum ControlLetter:
  case T // 0
  case R // 1
  // ... rest of mapping
```

These enums represent Value Objects because they:
- **Encapsulate domain concepts**: Each represents a specific, well-defined concept in the Spanish ID domain
- **Are immutable**: Once created, they cannot be modified
- **Have no identity**: Two `NieLetter.X` instances are always equal and interchangeable
- **Encode business rules**: The ordinal values directly map to their numeric representations, making the domain logic explicit

## Invariants

The code enforces multiple layers of domain invariants. 
These enforce structural constraints that define what constitutes a valid ID format.

### Structural Invariants

```scala
def requireValidInput(input: String): Unit =
  require(input.length == 9 && input.forall(_.isLetterOrDigit), invalidInput(input))

def requireValidDniNumber(dniNumber: String): Unit =
  require(dniNumber.length == 8, invalidDniNumber(dniNumber))
```

### Business Rule Invariants

These enforce the mathematical invariants that are core to Spanish ID validation - the modulo 23 calculation that ensures data integrity.

```scala
def requireValidDni(dniNumber: String, letter: ControlLetter): Unit =
  require(dniNumber.toInt % 23 == letter.ordinal, invalidDni(dniNumber, letter))

def requireValidNie(nieLetter: NieLetter, nieNumber: String, letter: ControlLetter): Unit =
  require(s"${nieLetter.ordinal}$nieNumber".toInt % 23 == letter.ordinal,
    invalidNie(nieLetter, nieNumber, letter))
```

### Enum Ordering Invariants

The comments indicate critical invariants - the enum ordering directly maps to domain business rules and must remain stable.

```scala
// Do NOT change the order of the enumeration.
// The ordinal value of each letter corresponds with the remainder of number divided by 23
```

## DDD Relationship Analysis

### Invariant Preservation
The validation functions act as factory methods for Value Objects, ensuring:
- No invalid Value Objects can be constructed
- Domain invariants are enforced at creation time
- Fail-fast behavior prevents corrupt state propagation

### Domain Language Preservation
The code maintains ubiquitous language by:
- Using domain-specific terminology (`DNI`, `NIE`, `ControlLetter`)
- Encoding business rules directly in types
- Providing descriptive error messages that match domain vocabulary

This implementation demonstrates **DDD practices** by making domain concepts explicit, enforcing invariants 
through the type system, and providing comprehensive error handling that maintains domain integrity.
