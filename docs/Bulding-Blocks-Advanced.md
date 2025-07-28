# Implementing DDD Building Blocks with Scala Advanced Constructs

## Value Objects

**Value Objects** encapsulate domain data with rules, but without identity.

- **Opaque Types:**
    - Used to create types that wrap another type (like `String`, `Int`) at compile time but have no runtime overhead
    - Perfect for defining strongly-typed domain concepts
    - Enforces type safety and prevents mixing up values with the same underlying data type but different domain meanings

- **Union Types:**
    - Useful when a domain concept can take one of several forms (like `Email | PhoneNumber` for "ContactInfo")
    - Ensures all alternatives are considered in pattern matching, providing exhaustiveness/safety

**Benefits:**
- Clear domain boundaries
- Safer APIs
- Zero-cost abstractions at runtime

## Entities

**Entities** are distinguished by identity (not just their data).

- **Opaque Types for IDs:**
    - Entity IDs can be defined as opaque types, e.g., `UserId`
    - Enhances type safety by preventing accidental ID mix-ups

- **Intersection Types:**
    - Combine multiple traits/interfaces (e.g., `Timestamped & Auditable & Versioned`) to assemble an entity’s behavior
    - Promotes modular, reusable, and testable components for cross-cutting concerns such as auditing, version, tags, ownership

- **Class composition:**
    - Entities modeled as (case) classes that embed value objects for fields, and mix in traits for behaviors using intersection types

**Benefits:**
- Composable and granular behavioral traits
- Strong type guarantees
- Convenient extension and modification of entity responsibilities

## Aggregates

**Aggregates** are clusters of entities and value objects, often with rich behaviors and invariants, treated as a single consistent unit.

- **Intersection Types:**
    - Combine multiple traits and behaviors into a single aggregate root (e.g., an `Order` that is `Auditable & Taggable & Versioned & SoftDeletable`)
    - Suitable for enforcing multiple policies and behaviors at the boundary of the aggregate

- **Union Types:**
    - Represent domain events, status, or state that might evolve (e.g., `Draft | Submitted | Cancelled` for order statuses)
    - Ensures all possible states/events are covered in logic

**Benefits:**
- Rich, maintainable aggregates that satisfy all business rules
- Pattern-matching on states/events for robust state management

## The Role of `inline` in Opaque Type Factories

When defining factory methods for opaque types in Scala, `inline` serves several purposes:

- **Compile-time Expansion:**
    - The body of an `inline` method is substituted at the call site, eliminating method-call overhead
    - The compiler can optimize and type-check domain invariants at compile time, reducing boilerplate and runtime cost

- **Validation and Construction:**
    - Typical for validated factory methods of Value Objects
    - Inline ensures the enforcement of such invariants without incurring extra function calls at runtime

- **Unsafe Constructors:**
    - Sometimes provided as `inline def unsafe(value: A): OpaqueType`, to be used when invariants are established elsewhere

## Summary

| DDD Concept         | Scala Technique                                | Purpose                                                       |
|---------------------|------------------------------------------------|---------------------------------------------------------------|
| Value Object        | Opaque Type, Union Type                        | Type-safe, invariant-enforced data, exhaustiveness checking   |
| Entity              | Opaque Type for ID, Class + Intersection Types | Unique identity, composable behavior via intersection types   |
| Aggregate           | Intersection & Union Types                     | Composite invariants, state, and behaviors                    |
| Opaque Factories    | `inline def apply(...)`                        | Compile-time, zero-cost, validated construction               |

- **Opaque types** make Value Objects efficient and safe
- **Union types** model "this or that" (choice) domain concepts
- **Intersection types** compose behaviors for entities and aggregates
- The **inline** keyword in Scala’s opaque-type factories enforces invariants, ensures safety, and enables zero-overhead code
