# Smart Event Management System using Advanced Data Structures and Algorithms

## DSA-3 Project

### Team Members

|             Name             |    ID Numbers   | Role      |
|------------------------------|-----------------|-----------|
| CHERUKUMALLI SAI HIMA SEKHAR |    2520030554   | Developer |
| RAMGARI RAGHUVEER            |    2520030225   | Developer |
| PENDRI SAIVARSHITH REDDY     |    2520030598   | Developer |

### Supervisor

**VINAY KUMAR SRIPERAMBUDURI**

### Course

**Data Structures and Algorithms – DSA-3**

---

# Abstract

Colleges and organizations conduct numerous events throughout the year, making efficient event management an important requirement. Traditional manual event management can result in slow searching, registration delays, scheduling conflicts, duplicate records, and inefficient allocation of seats and volunteers.

The Smart Event Management System is designed to simplify event creation, registration, event searching, scheduling, volunteer management, and seat allocation.

The system is implemented using Java and File Handling, with advanced Data Structures and Algorithms forming the core of important operations.

The project incorporates String Matching algorithms such as KMP, Rabin–Karp, and Z Algorithm for efficient event searching. Dynamic Programming is used for scheduling optimization and conflict reduction. Network Flow and Bipartite Matching are used for optimized allocation of resources such as seats and volunteers.

The objective of the project is to demonstrate how Data Structures and Algorithms can be applied to solve real-world event management problems efficiently.

---

# Problem Statement

Managing events manually can lead to:

- Slow event searching
- Registration delays
- Duplicate records
- Scheduling conflicts
- Difficulty in assigning volunteers
- Inefficient seat allocation
- Difficulty handling a large number of events

The system aims to provide an efficient and scalable solution using appropriate Data Structures and Algorithms.

---

# Objectives

The main objectives of the project are:

1. Provide efficient event creation and management.
2. Allow users to register for events.
3. Provide fast keyword-based event searching.
4. Detect and reduce scheduling conflicts.
5. Optimize event scheduling.
6. Assign volunteers efficiently.
7. Allocate seats effectively.
8. Demonstrate practical applications of advanced DSA concepts.
9. Maintain event and registration data using file handling.
10. Provide reports and useful event statistics.

---

# Proposed Functionalities

## 1. User Login

The system provides role-based access for:

- Admin
- Organizer
- Attendee
- Volunteer

## 2. Event Creation & Management

Organizers can:

- Create events
- Edit events
- Categorize events
- Store event information
- Manage event metadata

## 3. Event Registration

Users can:

- Register for events
- Receive registration confirmation
- Join waitlists when required
- View registration information

## 4. Event Search

Users can search events using:

- Keywords
- Event names
- Categories
- Other event attributes

Advanced string matching algorithms are used to improve search efficiency.

## 5. Schedule Planner

The scheduling module handles:

- Event timings
- Venue availability
- Conflict detection
- Slot assignment
- Schedule optimization

## 6. Volunteer & Seat Management

The system manages:

- Volunteer roles
- Volunteer shifts
- Seat allocation
- Resource assignment

## 7. Reports & Analytics

The system can generate information related to:

- Attendance
- Event registrations
- Resource utilization
- Event performance

---

# Data Structures and Algorithms

The project focuses on the following algorithms:

### String Matching

- KMP Algorithm
- Rabin–Karp Algorithm
- Z Algorithm

These algorithms are used for efficient keyword and pattern searching.

### Dynamic Programming

Dynamic Programming is used for:

- Scheduling optimization
- Conflict reduction
- Finding efficient event schedules

### Network Flow

Network Flow techniques are used for:

- Resource allocation
- Capacity-based assignment
- Volunteer allocation

### Bipartite Matching

Bipartite Matching is used for:

- Volunteer assignment
- Seat/resource assignment
- Matching available resources with requirements

---

# Technology Stack

|       Component      |   Technology  |
|----------------------|---------------|
| Programming Language | Java          |
| Data Storage         | File Handling |
| IDE                  | VS Code       |
| Version Control      | Git           |
| Repository           | GitHub        |

---

# ⚙️ Setup and Installation

This section explains the software requirements, installation steps, project setup, and execution instructions required to run the Smart Event Management System.

---

## 1. Prerequisites

Before running the project, make sure the following software is installed on your computer:

|         Software        |     Requirement     |                 Purpose                |
|-------------------------|---------------------|----------------------------------------|
| Java JDK                | Version 17 or above | Compile and run the Java application   |
| Visual Studio Code      | Latest version      | Development and execution environment  |
| Git                     | Latest version      | Clone and manage the GitHub repository |
| Extension Pack for Java | VS Code Extension   | Java development and execution         |

> **Important:** Install the **JDK (Java Development Kit)**, not only the JRE, because the project requires the Java compiler (`javac`).

---

## 2. Install Java JDK

The project requires **Java JDK 17 or above**.

Download and install Java JDK from an official JDK provider.

After installation, open **Command Prompt**, **PowerShell**, or the **VS Code Terminal**.

### Verify Java Installation

Run:

```bash
java -version
```
# Project Architecture

The system is divided into multiple logical components:

```text
                    Smart Event Management System
                              |
              +---------------+---------------+
              |               |               |
           Users           Events          Resources
              |               |               |
        Authentication    Management     Volunteers/Seats
                              |
                    +---------+---------+
                    |                   |
                Searching           Scheduling
                    |                   |
             String Matching      Dynamic Programming
                    |                   |
                    +---------+---------+
                              |
                     Resource Allocation
                              |
                  Network Flow / Matching


                  
