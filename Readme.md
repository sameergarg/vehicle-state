## Vehicle State Information

A service using Akka streams that reads from two sources:

One which contains battery data

```json
{
"batteryCapacity": 60,
"stateOfCharge": 44.5
}
```

and its location

```json
{
"longitude": 0.332342,
"latitude": 51.32342342
}
```

It construct a graph that output the (combined) current state of the vehicle for each incoming event.

Use flows for the following:
- Battery data comes in as kW, output state must include battery percentage
- Direction in which the vehicle is heading should be included in the output state
- Using slick and h2, to read a config and then determine a "red", "yellow", "green" status of the vehicle battery

| Status | Lower | Upper |
|--------|-------|-------|
| Red    | 0     | 33    |
| Yellow | 34    | 66    |
| Green  | 67    | 100   |

## Running the app
    Sample data is part of `batteryData.json` and `locationData.json` in `resources` folder

### Prerequisite
- sbt 1.7.1 
- Azul Systems, Inc. Java 11.0.14

### Packaging and execution 

#### Commandline
- `VehicleApp` is the main class to run the application
- Use `sbt assembly` to package the application
  - assembled application can be found in `target/scala-2.13` folder
- use `java - jar [assembely_jar_application.jar]` to run

#### IntelliJ
This project can be imported in IntelliJ and run `VehicleApp`