# 3D-Game
# JavaFX Game - Developed in IntelliJ IDEA

This game is a JavaFX-based project where the player navigates a map, avoiding enemies and collecting various coins and power-ups. The player is constantly pursued by ghosts and must balance energy and health while accumulating points.

---

## **Controls**
- **Movement:** `W`, `A`, `S`, `D` keys
- **Jump:** `Space` key

## **Game Mechanics**

- **Energy Management:**
  - Moving reduces energy by **3% per second**.
  - Standing still restores energy at **1% per second**.

- **Health & Enemies:**
  - The player is haunted by a **ghost** throughout the game.
  - **Hedgehogs** (3 in total) also wander the map, collecting coins and tokens.
  - **Player Damage:**
    - Contact with enemies (ghosts or hedgehogs) causes **HP loss**.
    - **Coins and Tokens** on the map drain **10% HP per second** on contact.

## **Objective**
Avoid enemies, navigate obstacles, and collect as many coins and tokens as possible.

---

## **Collectibles**

1. **Coins**  
   - **Yellow Coin**: +1 point
   - **Green Coin**: +3 points
   - **Blue Coin**: +5 points

2. **Power-Ups**
   - **Energy (Lightning Bolt)**: Restores energy for movement.
   - **Freeze (Snowflake)**: Freezes all enemies for **10 seconds**.
   - **Immunity**: Grants invincibility against damage for **10 seconds**.
   - **Health Token**: Restores **25%** of the player’s max HP.

3. **Joker Coin**  
   - When collected, triggers one of the following effects:
     - **40% chance**: Player receives a random score boost (1–10 points, each with equal 10% probability).
     - **20% chance**: Deducts **20% energy** and adds **20% health**.
     - **20% chance**: Deducts **20% health** and adds **20% energy**.
     - **10% chance**: Activates **Freeze bonus**.
     - **10% chance**: Activates **Immunity bonus**.

---

## **Game Over**
- When the player’s health reaches zero, the game ends.
- A summary displays the total points collected by the player.
---
## **Example of the game**
![image](https://github.com/user-attachments/assets/ae4ed791-b16b-4a70-abb5-48940a0c5e21)

