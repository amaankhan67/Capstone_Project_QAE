# smart-city-management

## Project Overview

A backend system that integrates various smart city services like traffic
management, public utility monitoring, citizen complaint handling, and emergency
service coordination.

## Project Structure

smart-city-management/
│
├── pom.xml                        # Maven build and dependency configuration
├── testng.xml                     # TestNG suite configuration file
├── README.md                      # Project documentation (overview, usage, setup)
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── city/
│   │               ├── exception/          # Custom exception classes
│   │               │   ├── DuplicateEntityException.java
│   │               │   ├── EmergencyPriorityException.java
│   │               │   ├── InvalidInputException.java
│   │               │   └── ServiceUnavailableException.java
│   │               │
│   │               ├── model/              # Data models / POJOs representing domain entities
│   │               │   ├── Citizen.java
│   │               │   ├── Complaint.java
│   │               │   ├── EmergencyAlert.java
│   │               │   ├── PublicService.java
│   │               │   ├── TrafficSensor.java
│   │               │   └── UtilityMeter.java
│   │               │
│   │               ├── repository/         # Repository layer (data persistence or mock DB)
│   │               │   ├── CitizenRepository.java
│   │               │   ├── ComplaintRepository.java
│   │               │   ├── EmergencyRepository.java
│   │               │   ├── TrafficRepository.java
│   │               │   └── UtilityRepository.java
│   │               │
│   │               └── service/            # Business logic and core functionality
│   │                   ├── AnalyticsService.java
│   │                   ├── ComplaintService.java
│   │                   ├── EmergencyService.java
│   │                   ├── TrafficManagementService.java
│   │                   └── UtilityMonitoringService.java
│   │
│   └── resources/
│
├── src/
│   └── test/
│       └── java/
│           └── com/
│               └── city/
│                   └── service/            # Unit and integration tests
│                       ├── AnalyticsServiceTest.java
│                       ├── ComplaintServiceTest.java
│                       ├── EmergencyServiceTest.java
│                       ├── TrafficManagementServiceTest.java
│                       └── UtilityMonitoringServiceTest.java
## How to build

mvn clean test

## Key Modules & Files

- `pom.xml` — Maven build file and dependency management.
- `src/main/java` — main application code.
- `src/test/java` — test code and automation scripts (check for Selenium/Test frameworks).

## Dependencies (from pom.xml)

- org.testng:testng:7.11.0 
- org.apache.maven.plugins:maven-surefire-plugin:3.5.4 

Common Issues
Issue: java.lang.UnsupportedClassVersionError
• Solution: Ensure you're using Java 11 or higher
Issue: Maven dependencies not resolving
• Solution: Run mvn clean install
Issue: Tests failing
• Solution: Check Java version and run mvn clean test



