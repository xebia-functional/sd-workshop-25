# Domain Driven Design

> _There is no sense talking about the solution before we agree on the problem, and no sense talking about the implementation steps before we agree on the solution._
>
> _Efrat Goldratt-Ashlag_

Software engineering is hard. The strategic aspect of DDD deals with answering the questions "what?" and "why?".
The tactical part is all about the "how?"

## 1. Premises & Core Concepts

### Strategic aspects of DDD
| Premise                             | Explanation                                                     |
|-------------------------------------|-----------------------------------------------------------------|
| **Domain as the heart of Software** | Business rules, not infrastructure, drive competitive advantage |
| **Talk to Domain Experts**          | Collaborative modeling uncovers deep business knowledge         |
| **Use Ubiquitous Language**         | Strict, shared terminology for code, docs, and conversation     |
| **Define Bounded Contexts**         | Each model exists within a well-defined boundary                |

### Tactical aspects of DDD
| Premise                  | Explanation                                                |
|--------------------------|------------------------------------------------------------|
| **Value Objects**        | Immutable objects, identified only by attributes           |
| **Entities**             | Objects with identity field than allow state changes       |
| **Aggregates**           | Consistency boundaries for enforcing invariants            |
| **Domain Events**        | Immutable facts (logs*) about meaningful domain changes    |
| **Domain Services**      | Domain operations not natural to entities or value objects |
| **Layered Architecture** | Keeps business logic isolated from UI/Infrastructure       |


## 2. Benefits of Making Illegal State Irrepresentable

There is plenty of literature about this topic. 
The main premise is that if you make illegal state irrepresentable, you do not have to write any defensive programing 
and additional testing.

Let's have a look at these 2 pieces:

- [Making Illegal States Unrepresentable](https://ybogomolov.me/making-illegal-states-unrepresentable) by Yuriy Bogomolov
- [Parse, don’t validate](https://lexi-lambda.github.io/blog/2019/11/05/parse-don-t-validate/) by Alex King

### Making Illegal Statements Unrepresentable
The blog introduces a design principle aimed at leveraging the type system to prevent runtime errors by enforcing 
correctness at compile time. It explores techniques like:
- **opaque types** for distinguishing primitive types
- **smart constructors** for validating input data
- **combinators**  to model precise updates or enforce stricter type constraints

The article emphasizes **type-level programming** to encode business logic, ensure data validity,
and eliminate invalid states, culminating in advanced concepts like **tagless final** patterns and **indexed monads**
for maintaining strict control over program flow and logic transitions.

### Parse, don't validate
The blog post advocates for **type-driven design**, emphasizing the mantra *"Parse, don’t validate"*. 
This approach leverages static type systems to make illegal states unrepresentable, enhancing code correctness and
reliability by embedding constraints directly into data types. 

Instead of relying on runtime validation checks, parsing transforms less-structured input into more-structured types 
upfront, reducing redundant checks, potential bugs, and performance costs. Examples like using `NonEmpty` lists in 
Haskell showcase how parsing ensures that data invariants are preserved at the language level, enabling programs to 
fail at compile-time rather than runtime. This approach promotes more robust, maintainable, and error-resistant code.

### What has Scala 3 to offer?

| Benefit                   | Scala Mechanism                                | Impact                          |
|---------------------------|------------------------------------------------|---------------------------------|
| **Compile-Time Safety**   | ADTs, Enums, Opaque/Refined Types              | Bugs prevented before runtime   |
| **Self-Documenting Code** | Types like `NonEmptyString`, `ValidDni`        | Faster onboarding, clearer code |
| **No Redundant Checks**   | Smart constructors enforce invariants          | Less boilerplate, safer code    |
| **Optimization**          | Trustworthy types enable compiler optimization | Performance gains               |
| **Easy Refactoring**      | Types force the compiler to catch all changes  | Safer evolution                 |


## 3. DDD Advantages in the Age of LLMs

The use of LLMs agent to generate code is something that is gaining more and more popularity.
One of the biggest pitfalls it that the models still hallucinate quite a lot.
In order to combat that, supporting technologies have been created - like retrieval-augmented generation (RAG).

Another technology is the Model Context Protocol (MCP), that allows to communicate with the model in an API fashion-like.
Metals - the Scala language server - has included support for MCP. We can check the following resources to activate 
MCP for Scala.

- [Metals MCP support](https://scalameta.org/metals/blog/2025/05/13/strontium/#mcp-support)
- [A Beginner's Guide to Using Scala Metals With its Model Context Protocol Server](https://softwaremill.com/a-beginners-guide-to-using-scala-metals-with-its-model-context-protocol-server/)

### How Scala improves the feedback loop of MCP?

- **Ubiquitous Language enables precise prompts:** Fewer LLM hallucinations, better code/text/test generation
- **Explainable outputs:** Well-modeled entities and value objects provide anchors for LLM-generated explanations
- **Automated, robust scaffolding:** LLMs can safely generate controllers, DTOs, migrations against well-typed models
- **Test synthesis:** Clear domain events and invariants let LLMs create smarter property tests
- **Prompt-driven prototyping in bounded contexts:** Avoids "big-ball-of-mud" issues with context isolation
- **Continuous, living documentation:** LLM bots can answer or summarize from DDD-annotated code
