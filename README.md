# Event Management System — Z-Algorithm Search Engine

**KLH CSE 2025-26 | S2-14 | String Pattern Matching**

---

## Project Overview

This project implements an **Event Management System** that:
- Stores information about Indian festivals in `.txt` files
- Uses the **Z-Algorithm** for fast, efficient keyword search across all event files
- Recommends **related events** based on shared characters and connections

---

## Z-Algorithm

The Z-Algorithm finds all occurrences of a pattern in a text in **O(n + m)** time, where:
- `n` = length of the text
- `m` = length of the search pattern

It works by building a **Z-array** where `Z[i]` = length of the longest substring starting at position `i` that is also a prefix of the combined string `pattern$text`.

**Advantages over Brute Force (O(n*m))**:
- Linear time guarantee
- Single pass through the text
- No backtracking

---

## Project Structure

```
KLH_CSE_2025-26_S2_14_Event-Management-System/
|
+-- data/                          <- Event text files
|   +-- pongal.txt
|   +-- dasara.txt
|   +-- ramanavami.txt
|   +-- diwali.txt
|   +-- ugadi.txt
|   +-- holi.txt
|   +-- krishna_jayanti.txt
|   +-- makar_sankranti.txt
|
+-- src/                           <- Java source files
|   +-- ZAlgorithm.java            <- Core Z-Algorithm (O(n+m) pattern matching)
|   +-- EventLoader.java           <- Reads .txt files and parses metadata
|   +-- EventSearchEngine.java     <- Searches all files using Z-Algorithm
|   +-- RecommendationEngine.java  <- Suggests related events by shared characters
|   +-- Main.java                  <- Entry point / interactive console
|
+-- out/                           <- Compiled .class files (auto-generated)
+-- run.bat                        <- Windows batch file to compile and run
+-- README.md
```

---

## How to Run

### Method 1: Use the batch script (Windows)
```
run.bat
```

### Method 2: Manual compile and run
```bash
# Step 1: Create output folder
mkdir out

# Step 2: Compile all Java files
javac -encoding UTF-8 -d out src\ZAlgorithm.java src\EventLoader.java src\EventSearchEngine.java src\RecommendationEngine.java src\Main.java

# Step 3: Run the program
java -cp out Main
```

---

## Example Usage

```
Enter event name or keyword to search: ramanavami

=== EVENT FOUND: DASARA (dasara.txt) ===
    Keyword "ramanavami" found at 3 position(s): [87, 2299, 2331]
[Full content of dasara.txt printed here]

=== EVENT FOUND: SRI RAMANAVAMI (ramanavami.txt) ===
    Keyword "ramanavami" found at 4 position(s): [11, 148, 168, 1258]
[Full content of ramanavami.txt printed here]

*** RECOMMENDED RELATED EVENTS:
  1. Diwali (diwali.txt)
     Reason: Shares the character "Rama" with "Sri Ramanavami"
  2. Holi (holi.txt)
     Reason: Shares the character "Krishna" with "Diwali"
```

---

## Event Files Format

Each `.txt` file has metadata at the top:
```
EVENT: <Event Display Name>
CHARACTERS: <char1>, <char2>, <char3>
RELATED: <filename1>, <filename2>

=== ABOUT <EVENT> ===
[Full event description...]
```

---

## Events Covered

| File | Event | Key Characters |
|------|-------|---------------|
| pongal.txt | Pongal | Sun God, Surya, Indra, cattle |
| dasara.txt | Dasara | Rama, Ravana, Durga, Hanuman |
| ramanavami.txt | Sri Ramanavami | Rama, Sita, Lakshmana, Hanuman |
| diwali.txt | Diwali | Rama, Sita, Lakshmi, Krishna |
| ugadi.txt | Ugadi | Brahma, Vishnu, Shiva |
| holi.txt | Holi | Holika, Prahlada, Krishna, Radha |
| krishna_jayanti.txt | Krishna Jayanti | Krishna, Radha, Devaki, Arjuna |
| makar_sankranti.txt | Makar Sankranti | Sun God, Bhishma, Vishnu |

---

## Recommendation Logic

1. **Direct links**: Each event file has a `RELATED:` tag listing directly connected events
2. **Shared characters**: The engine scans all files for common characters
   - Example: Searching "Ramanavami" → finds Rama → recommends Dasara and Diwali (both mention Rama)

---

## Algorithm Files

| File | Algorithm | Purpose |
|------|-----------|---------|
| ZAlgorithm.java | Z-Algorithm | O(n+m) pattern matching |
| RecommendationEngine.java | Character graph traversal | Event recommendation |
| EventLoader.java | File I/O + parsing | Load .txt event files |

---

*KLH University | CSE Department | 2025-26 | Team 14*
