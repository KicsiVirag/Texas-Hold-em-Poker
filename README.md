# Texas Hold'em Poker – Java

A Java implementation of Texas Hold'em poker, currently focused on the core game logic and console-based testing. The project is designed as a foundation for a future multiplayer application with network communication and a graphical user interface (GUI).

## Current Features

- Standard 52-card deck creation and shuffling.
- Player management for up to 10 players.
- Hole cards and community cards (flop, turn and river).
- Player actions: check, call, raise, fold and basic all-in handling.
- Betting rounds, player balances and pot tracking using simulated chips.
- Recognition of the ten standard poker hand categories, from high card to royal flush.
- Basic winner determination and pot splitting based on hand category.
- Support for resetting the game state and starting a new hand.
- Console-based demonstrations and hand evaluation test scenarios.

## Project Structure

```text
src/poker/
├── Cards.java        # Card representation and deck management
├── Player_hand.java  # Player cards, balances and actions
├── Rules.java        # Poker hand category evaluation
├── GameManager.java  # Game state, betting rounds and pot management
├── Main.java         # Console-based demonstrations and tests
├── dealer.java       # Placeholder for dealer logic
├── PokerClient.java  # Placeholder for future networking
└── PokerServer.java  # Placeholder for future networking
```

## Technologies

- Java (the project is configured for OpenJDK 26)
- Java Standard Library
- IntelliJ IDEA

## Running the Project

Open the project in IntelliJ IDEA, configure the JDK and run `poker.Main`.

Alternatively, from the project root, compile and run using the command line:

```bash
javac -d out src/poker/*.java
java -cp out poker.Main
```

The current entry point runs predefined console test scenarios rather than a fully interactive game.

## Development Status and Roadmap

The project is still under development. **Network communication and the graphical user interface (GUI) are not implemented yet, but both are planned for future versions.**

- [ ] Complete hand comparison, including kickers and rank-based tie-breaking.
- [ ] Improve all-in handling, side pots and pot distribution edge cases.
- [ ] Implement dealer rotation and small/big blind handling.
- [ ] Implement client-server network communication.
- [ ] Add a graphical user interface (GUI).
- [ ] Integrate the game logic, networking and user interface.
- [ ] Expand automated testing and improve game stability.

## Purpose

The project aims to explore object-oriented programming, game state management and modular software design in Java. Its current scope is a local simulation using virtual chips, with no real-money functionality.
