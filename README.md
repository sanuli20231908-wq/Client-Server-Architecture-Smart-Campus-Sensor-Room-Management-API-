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

###### Question 01:

In your report, explain the default lifecycle of a JAX-RS Resource class. Is a
new instance instantiated for every incoming request, or does the runtime treat it as a
singleton? Elaborate on how this architectural decision impacts the way you manage and
synchronize your in-memory data structures (maps/lists) to prevent data loss or race con-
ditions.

##### Answer:

In JAX-RS, the default lifecycle of a resource class is pre-request. This means a new instance of the resource class is created for each incoming HTTP request and discarded after after the response is sent.

Because of this behaviour, any data stored as instance variables inside the resource class will not persist between requests. To handle shared data in this project, a separate static `DataStore` class is used, where data is stored using collections such as `HashMap`.

However, since multiple requests can access these shared data structures at the same time, there is a risk of race conditions. To make this safer in a real world scenario, a thread-safe collection such as `ConcurrentHashMap` can be used instead of a regular `HashMap`, as it supports safe concurrent access.


