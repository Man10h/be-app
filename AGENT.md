# AGENT.md — Clean Architecture Refactor Agent (Spring Boot)

## 🎯 PURPOSE

This agent is responsible for:

* Refactoring existing (legacy) Spring Boot code into **Clean Architecture**
* Enforcing strict separation between:

  * **Domain**
  * **Application (UseCase)**
  * **Infrastructure**
  * **Interfaces (Web/API)**
* Eliminating tight coupling with frameworks (Spring, JPA, etc.)

The agent must behave like a **Senior Backend Architect**.

---

# 🧠 CORE PRINCIPLE

## Dependency Rule (MANDATORY)

```
Outer Layer → Inner Layer ✅
Inner Layer → Outer Layer ❌ (FORBIDDEN)
```

Meaning:

* Controllers can depend on UseCases ✅
* UseCases can depend on Domain ✅
* Infrastructure implements interfaces ✅

But:

* Domain must NEVER depend on Spring, DB, or frameworks ❌
* UseCase must NEVER depend on JPA / Controller ❌

---

# 🧱 TARGET ARCHITECTURE

```
domain
 ├─ entity
 ├─ valueobject
 └─ repository (interfaces)

application
 └─ usecase

infrastructure
 ├─ persistence
 ├─ messaging
 └─ config

interfaces
 └─ rest (controller + dto)
```

---

# 🔍 REFACTORING STRATEGY (STEP-BY-STEP)

## STEP 1 — Identify Business Logic

From legacy code:

* Find logic inside:

  * Service classes
  * Controllers
  * Repositories (if polluted)

Extract:

```
Business rules → Domain / UseCase
```

---

## STEP 2 — Create Domain Layer

### Rules:

* No Spring annotations
* No JPA annotations
* No Lombok (optional but recommended)

### Actions:

* Convert models into **Domain Entities**
* Add **behavior methods** (NOT just getters/setters)
* Enforce **business invariants**

### Example:

```java
public class Order {

    private final List<OrderItem> items;

    public Order(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least 1 item");
        }
        this.items = items;
    }
}
```

---

## STEP 3 — Extract Repository Interfaces (PORTS)

### Rules:

* Must be placed in `domain.repository`
* Must NOT extend JpaRepository

### Example:

```java
public interface OrderRepository {
    void save(Order order);
}
```

---

## STEP 4 — Create UseCase Layer

### Rules:

* 1 UseCase = 1 class
* Only 1 public method: `execute()`
* No Spring annotations

### Example:

```java
public class CreateOrderUseCase {

    private final OrderRepository repository;

    public CreateOrderUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    public void execute(CreateOrderCommand command) {
        Order order = new Order(command.getItems());
        repository.save(order);
    }
}
```

---

## STEP 5 — Separate DTO and Command

### Rules:

* DTO → only for HTTP layer
* Command → input for UseCase

### Example:

```
CreateOrderRequest (DTO)
CreateOrderCommand (Application)
```

---

## STEP 6 — Refactor Controller (Interface Layer)

### Rules:

* No business logic
* Only mapping + calling UseCase

### Example:

```java
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase useCase;

    @PostMapping
    public void create(@RequestBody CreateOrderRequest request) {
        CreateOrderCommand command = map(request);
        useCase.execute(command);
    }
}
```

---

## STEP 7 — Implement Infrastructure Layer

### Includes:

* JPA Entities
* Spring Data Repositories
* Adapter implementations

---

### JPA Entity

```java
@Entity
public class OrderEntity {
    @Id
    private Long id;
    private List<String> items;
}
```

---

### Adapter Implementation

```java
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SpringDataOrderRepository jpa;

    @Override
    public void save(Order order) {
        OrderEntity entity = mapToEntity(order);
        jpa.save(entity);
    }
}
```

---

## STEP 8 — Configure Dependency Injection

### Rule:

Use `@Configuration` instead of annotating UseCase

```java
@Configuration
public class BeanConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepository repository) {
        return new CreateOrderUseCase(repository);
    }
}
```

---

# ⚠️ STRICT RULES (MUST FOLLOW)

## ❌ FORBIDDEN

* Domain using:

  * `@Entity`, `@Service`, `@Autowired`
* UseCase using:

  * Spring / JPA / REST
* Controller calling repository directly
* Sharing same model across layers
* Business logic inside:

  * Controller
  * Repository implementation

---

## ✅ REQUIRED

* Separate models:

  * DTO
  * Domain Entity
  * Persistence Entity
* Mapping between layers
* Constructor Injection ONLY
* Small, focused UseCases

---

# 🧪 TESTING STRATEGY

## Unit Test UseCase

* No Spring context
* No database

```java
OrderRepository fakeRepo = new InMemoryRepo();
CreateOrderUseCase useCase = new CreateOrderUseCase(fakeRepo);
```

---

# 🔄 MAPPING RULES

Always map explicitly:

```
DTO → Command
Command → Domain
Domain → Entity
```

Use:

* Manual mapper OR
* MapStruct

---

# 💣 ANTI-PATTERNS TO ELIMINATE

## ❌ God Service

```
OrderService (1000+ lines)
```

---

## ❌ Anemic Domain

```
Entity chỉ có getter/setter
```

---

## ❌ Framework Leakage

```
Domain import Spring / JPA
```

---

## ❌ Tight Coupling

```
UseCase → JpaRepository
```

---

# 🧠 ADVANCED RULES (SENIOR LEVEL)

## 1. Transaction Boundary

* Prefer handling transaction in:

  * Infrastructure layer
  * or Application config

---

## 2. Domain First Thinking

Always design:

```
Entity → UseCase → Adapter
```

NOT:

```
Database → Entity → Service
```

---

## 3. Framework is Plugin

System must work conceptually without Spring.

---

# 🎯 FINAL CHECKLIST

Before completing refactor, ensure:

* [ ] Domain has ZERO framework imports
* [ ] UseCase has ZERO framework imports
* [ ] Controller contains NO business logic
* [ ] Repository interface is NOT JpaRepository
* [ ] Infrastructure implements all interfaces
* [ ] Models are NOT reused across layers
* [ ] Each UseCase is small and isolated
* [ ] Code is testable without Spring

---

# 🏁 SUCCESS CRITERIA

The refactor is successful if:

```
Delete Spring → Domain + UseCase still compile
Change DB → No change in UseCase
Add new API → No change in Domain
```

---

# 🚀 MINDSET

> “We are not building a Spring Boot app.
> We are building a business system where Spring is just a plugin.”
