# Trade-Offs Analysis Framework

By giving a score between 1 (Low) and 5 (High) to various factors, we can start to decide.

| Dimension                  | 1 (Low)              | 5 (High)                    |
|----------------------------|----------------------|-----------------------------|
| **Domain Accuracy**        | Minor errors are ok  | Must be perfect             |
| **Runtime Performance**    | Having latency is ok | Real-time or near real-time |
| **Developer Expertise**    | Junior or Mid        | Senior, Lead, Principal     |
| **Change Volatility**      | Stable rules         | Rapid innovation            |

#### Steps:
1. Score your requirement along all dimensions.
2. High scores → Favor type-level, compile-time guarantees (libraries for refining types).
3. Low expertise, low volatility, low performance → Favor vanilla DDD with runtime checks.
4. Document rationale and approach (keep ADRs).


## DDD Subdomain Implementation Strategy

### Core Domain

The **core domain** represents your organization's primary competitive advantage and most critical business logic.
This is where maximum investment in type safety, domain accuracy, and expert knowledge should be concentrated.

#### Investment Justification
- **Domain Accuracy**: **Score 5** - Business-critical logic demands perfect correctness
- **Developer Expertise**: **Score 4-5** - Requires senior/expert developers
- **Change Volatility**: **Score 3-4** - Core business rules tend to be a mix of stable and innovative rules

#### Recommended Scala 3 Techniques
- Type-Level Safety (Aggressive Approach)
- Advanced Pattern Matching & Union Types

#### Team Allocation
- **Senior/Lead Engineers (80%+)**: Domain modeling, complex business logic
- **Metaprogramming Specialists**: Custom derivations, type-level computations
- **Domain Experts**: Close collaboration for ubiquitous language definition

#### Trade-off Decisions
- ✅ **Accept higher compilation times** for stronger guarantees
- ✅ **Invest in complex type hierarchies** - ROI is highest here
- ✅ **Use experimental features** if they provide business value
- ❌ **Avoid runtime-only validation** - compile-time safety is paramount

### Generic Subdomain Analysis

Generic subdomains handle common, commodity functionality that doesn't differentiate your business. 
The focus should be on **pragmatism, maintainability, and team scalability**.

#### Investment Justification
- **Domain Accuracy**: **Score 2-3** - Important but not business-critical
- **Developer Expertise**: **Score 2-3** - Mid-level developers sufficient
- **Change Volatility**: **Score 1-2** - Standards and protocols are stable
- **Cross-Team Integration**: **Score 3-4** - Shared infrastructure component

#### Recommended Scala 3 Techniques
- Balanced Approach - Vanilla DDD
- Leverage Existing Libraries

#### Team Allocation
- **Mid-level Engineers (60-70%)**: Standard implementation patterns
- **Junior Engineers (20-30%)**: Simple CRUD operations, data mapping
- **Senior Engineers (10-20%)**: Architecture decisions, integration patterns

#### Trade-off Decisions
- ✅ **Favor runtime validation** over complex compile-time checks
- ✅ **Use conventional patterns** - minimize cognitive overhead
- ✅ **Leverage community libraries** - don't reinvent wheels
- ❌ **Avoid experimental features** - stability over cutting-edge

### Supporting Subdomain Analysis

Supporting subdomains are necessary infrastructure that enables core business functionality but doesn't provide
competitive advantage. **Simplicity, reliability, and operational ease** are key.

#### Investment Justification
- **Domain Accuracy**: **Score 2** - Functional correctness sufficient
- **Developer Expertise**: **Score 1-3** - Junior to mid-level appropriate
- **Change Volatility**: **Score 1-2** - Infrastructure patterns are stable
- **Cross-Team Integration**: **Score 2-3** - Internal service boundaries

#### Recommended Scala 3 Techniques
- Minimalistic Approach - Focus on Simplicity
- Prioritize Operational Concerns

#### Team Allocation
- **Junior Engineers (50-60%)**: Implementation of well-defined requirements
- **Mid-level Engineers (30-40%)**: Service integration, basic architecture
- **Senior Engineers (10%)**: Initial design, critical path review

#### Trade-off Decisions
- ✅ **Maximize readability** - optimize for maintenance
- ✅ **Use standard library features** - minimize dependencies
- ✅ **Focus on operational metrics** - monitoring over type safety
- ❌ **Avoid complex abstractions** - explicit over clever


### Migration Pathways

- From Supporting → Generic
    - Add basic domain modeling (ADTs, validation)
    - Introduce standard typeclass derivations
    - Maintain backward compatibility

- From Generic → Core
    - Invest in opaque types and refined validation
    - Add sophisticated business rule modeling
    - Implement advanced type-level guarantees

### Team Development Framework

| Subdomain       | Entry Level     | Growth Path                             | Advanced Skills                       |
|-----------------|-----------------|-----------------------------------------|---------------------------------------|
| **Supporting**  | Junior → Mid    | Standard patterns, basic FP             | Service design, integration           |
| **Generic**     | Mid → Senior    | Library ecosystem, typeclass design     | Advanced FP, performance optimization |
| **Core**        | Senior → Expert | Type-level programming, metaprogramming | Domain architecture, advanced types   |

### Decision Matrix

| Technique             | Core Domain              | Generic Subdomain       | Supporting Subdomain                      |
|-----------------------|--------------------------|-------------------------|-------------------------------------------|
| **Opaque Types**      | ✅ Aggressive             | ⚠️ Selective            | ❌ Use for external inputs, like configs   |
| **Union Types**       | ✅ Complex business logic | ⚠️ Simple unions        | ❌ Use enums or ADTs                       |
| **Refined Types**     | ✅ Critical validations   | ⚠️ Key boundaries       | ❌ Runtime checks                          |
| **Metaprogramming**   | ✅ Custom derivations     | ⚠️ Standard macros      | ❌ Library only - inline, opaque...        |
| **Type Classes**      | ✅ Custom + Advanced      | ✅ Standard ecosystem    | ⚠️ Simple instances                       |
| **Pattern Matching**  | ✅ Exhaustive, complex    | ✅ Standard patterns     | ⚠️ Basic matching                         |
