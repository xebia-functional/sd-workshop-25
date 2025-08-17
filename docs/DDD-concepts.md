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

## 2. Benefits of Making Illegal State Irrepresentable

There is plenty of literature about this topic. 
The main premise is that if you make illegal state irrepresentable, you do not have to write any defensive programing 
and additional testing.

### Making Illegal Statements Unrepresentable

[Making Illegal States Unrepresentable](https://ybogomolov.me/making-illegal-states-unrepresentable) by Yuriy Bogomolov

The blog introduces a design principle aimed at leveraging the type system to prevent runtime errors by enforcing 
correctness at compile time. It explores techniques like:
- **opaque types** for distinguishing primitive types
- **smart constructors** for validating input data
- **combinators**  to model precise updates or enforce stricter type constraints

The article emphasizes **type-level programming** to encode business logic, ensure data validity,
and eliminate invalid states, culminating in advanced concepts like **tagless final** patterns and **indexed monads**
for maintaining strict control over program flow and logic transitions.

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
