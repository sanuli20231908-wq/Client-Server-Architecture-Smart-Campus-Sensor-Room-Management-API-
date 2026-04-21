# Smart Campus Sensor & Room Management API

This project is a RESTful API developed using JAX-RS for the Smart Campus coursework. It manages rooms, sensors, and sensor readings in a campus environment.

## Technologies Used

The main technologies used in this project are:

- Java 17
- Maven
- JAX-RS (Jersey)
- Grizzly HTTP Server
- Jackson for JSON processing
- HashMap and ArrayList for in-memory storage

Java is used as the main programming language. Maven is used to manage dependencies and build the project. JAX-RS is used to create the REST API. Grizzly is used as the lightweight server to run the application. Jackson is used to convert Java objects into JSON responses and to read JSON request bodies.

## How to build and run

Before running the project, install Java and Maven to the system.

### Step 01: Open Terminal
Open the terminal and go to the project folder.

### Step 02: Compile the project
Run the following command:

```bash
mvn compile
```

### Step 03: Run the project
```bash
mvn exec:java
```

### Step 04: Open the localhost
```
http://localhost:8080/api/v1
```

## API Endpoints

The API contains three main areas: rooms, sensors and sensor readings.

### Step 01: Rooms

```
- `GET /api/v1/rooms` - get all rooms
- `POST /api/v1/rooms` - create a new room
- `GET /api/v1/rooms/{roomId}` - get one room by ID
- `DELETE /api/v1/rooms/{roomId}` - delete a room
```

### Step 02: Sensors

```
- `GET /api/v1/sensors` - get all sensors
- `POST /api/v1/sensors` - create a new sensor
- `GET /api/v1/sensors/{sensorId}` - get one sensor by ID
- `GET /api/v1/sensors?type=CO2` - filter sensors by type
```

### Step 03: Sensor Readings

```
- `GET /api/v1/sensors/{sensorId}/readings` - get all readings for a sensor
- `POST /api/v1/sensors/{sensorId}/readings` - add a new reading for a sensor
```

## Curl Commands

### Step 01: Create a room

This command creates a new room in the system.

```
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d "{\"id\":\"LIB-301\",\"name\":\"Library Quiet Study\",\"capacity\":50}"
```

- Expected response: 201 created with the new room details.

### Step 02: Create a sensor

This command creates a new sensor and links it to an existing room.

```
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d "{\"id\":\"CO2-001\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":400.0,\"roomId\":\"LIB-301\"}"
```

- Expected response: 201 created with the new sensor linked to LIB-301.

### Step 03: Get sensors filtered by type

This command returns only sensors that match the given type.

```
curl "http://localhost:8080/api/v1/sensors?type=CO2"
```

- Expected response: 200 OK with a filtered list of CO2 sensors only.

### Step 04: Add a reading

This command adds a new reading to a specific sensor and updates its current value.

```
curl -X POST http://localhost:8080/api/v1/sensors/CO2-001/readings \
-H "Content-Type: application/json" \
-d "{\"id\":\"R-001\",\"timestamp\":1713695400000,\"value\":420.5}"
```

- Expected response: 201 Created with the new reading added, sensor currentValue updated to 420.5.

### Step 05: Get readings

This command returns all historical readings for the selected sensor.

```
curl http://localhost:8080/api/v1/sensors/CO2-001/readings
```

- Expected response: 200 OK with a list of all readings for sensor CO2-001.

## Questions

### Part 1: Service Architecture & Setup 
#### Project & Application Configuration

##### Question 01:

In your report, explain the default lifecycle of a JAX-RS Resource class. Is a
new instance instantiated for every incoming request, or does the runtime treat it as a
singleton? Elaborate on how this architectural decision impacts the way you manage and
synchronize your in-memory data structures (maps/lists) to prevent data loss or race con-
ditions.

##### Answer:

In JAX-RS, the default lifecycle of a resource class is pre-request. This means a new instance of the resource class is created for each incoming HTTP request and discarded after after the response is sent.

Because of this behaviour, any data stored as instance variables inside the resource class will not persist between requests. To handle shared data in this project, a separate static `DataStore` class is used, where data is stored using collections such as `HashMap`.

However, since multiple requests can access these shared data structures at the same time, there is a risk of race conditions. To make this safer in a real world scenario, a thread-safe collection such as `ConcurrentHashMap` can be used instead of a regular `HashMap`, as it supports safe concurrent access.

#### The ”Discovery” Endpoint

##### Question 02:

Why is the provision of ”Hypermedia” (links and navigation within responses)
considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach
benefit client developers compared to static documentation?

##### Answer:

Hypermedia is considered a hallmark of advanced RESTful design because it allows clients to discover API links dynamically through the responses returned by the server. This approach, often referred to as HATEOAS, means that the API provides links to related resources and actions within its responses.

Instead of relying only on external documentation, the client can follow these links to navigate the API step by step. This makes it easier for client developers because they do not need to know all endpoints in advance. It also makes the API more flexible, as changes to URLs or structure can be handled by updating the links in the responses rather than modifying the client code.

### Part 2: Room Management
#### Room Resource Implementation

##### Question 03:

When returning a list of rooms, what are the implications of returning only
IDs versus returning the full room objects? Consider network bandwidth and client side
processing.

##### Answer:

Returning only IDs reduces the amount of data sent over the network, which improves performance. However, the client must make additional requests to get full details, increasing client-side processing.

Returning full room objects provides all information in one response, making it easier for the client to use, but it increases the response size. In this project, full room objects are returned to keep the interaction simple.

#### Room Deletion & Safety Logic

##### Question 04: 

Is the DELETE operation idempotent in your implementation? Provide a detailed
justification by describing what happens if a client mistakenly sends the exact same DELETE
request for a room multiple times.

##### Answer:

The DELETE operation is idempotent in this implementation. Idempotent means that repeating the same request does not change the system state after the first execution.

The first DELETE request removes the room and returns a success response. If the same request is sent again, the room no longer exists, so the API returns a 404 Not Found. Since the state does not change after the first deletion, the operation is considered idempotent.

If the room still has sensors assigned, the deletion is blocked and a 409 Conflict is returned.

### Part 3: Sensor Operations & Linking
#### Sensor Resource & Integrity

##### Question 05:

We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on
the POST method. Explain the technical consequences if a client attempts to send data in
a different format, such as text/plain or application/xml. How does JAX-RS handle this
mismatch?

##### Answer:

The @Consumes(MediaType.APPLICATION_JSON) annotation means the endpoint only accepts requests with a Content-Type of application/json. If a client sends data in another format such as text/plain or application/xml, JAX-RS will reject the request before it reaches the resource method.

In this case, the framework returns a 415 Unsupported Media Type response. This is handled automatically by JAX-RS and ensures that only valid JSON data is processed by the API.

#### Filtered Retrieval & Search

##### Question 06:

You implemented this filtering using @QueryParam. Contrast this with an alterna-
tive design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why
is the query parameter approach generally considered superior for filtering and searching
collections?

##### Answer:

Query parameters are better for filtering because they represent optional conditions on a collection, not a different resource. For example, GET /sensors?type=CO2 returns a filtered version of the same sensors collection, while still allowing all sensors to be retrieved when no filter is applied.

Using a path like /sensors/type/CO2 suggests a separate resource, which can be misleading. It is also harder to extend, since adding more filters would require more complex paths. Query parameters make filtering clearer and more flexible.

### Part 4: Deep Nesting with Sub - Resources
#### The Sub-Resource Locator Pattern

##### Question 07:

Discuss the architectural benefits of the Sub-Resource Locator pattern. How
does delegating logic to separate classes help manage complexity in large APIs compared
to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive con-
troller class?

##### Answer:

The Sub-Resource Locator pattern allows nested resources to be handled by separate classes. In this project, reading-related operations are handled by a separate SensorReadingResource instead of being placed inside SensorResource.

This improves separation of concerns, as each class is responsible for a specific resource. In large APIs, putting all nested paths in one controller would make it difficult to read and maintain. Splitting the logic into smaller classes makes the code easier to understand, test, and extend.

### Part 5: Advanced Error Handling, Exception Mapping & Logging
#### Dependency Validation

##### Question 08:

Why is HTTP 422 often considered more semantically accurate than a standard
404 when the issue is a missing reference inside a valid JSON payload?

##### Answer:

HTTP 404 Not Found is used when the requested resource at a specific URL does not exist. In this case, the endpoint itself exists and the request is valid, but the JSON payload contains a reference to a resource that does not exist, such as an invalid roomId.

HTTP 422 Unprocessable Entity is more accurate because the server understands the request but cannot process it due to invalid data in the payload. This helps the client understand that the issue is with the request content rather than the endpoint.

#### The Global Safety Net

##### Question 09:

From a cybersecurity standpoint, explain the risks associated with exposing
internal Java stack traces to external API consumers. What specific information could an
attacker gather from such a trace?

##### Answer:

Exposing Java stack traces is a security risk because they reveal internal details such as class names, method names, file paths, and line numbers. This gives attackers insight into the structure of the application and makes it easier to find weaknesses.

For example, attackers may identify specific libraries or frameworks and look for known vulnerabilities. To prevent this, the application uses a global exception mapper to return a generic 500 response instead of exposing internal details.

