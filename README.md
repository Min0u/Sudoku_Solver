# Sudoku Solver

This is a simple sudoku solver that uses 3 different deduction rules to solve the puzzle. The rules are:
- Naked Single
- Hidden Single
- Pointing Pair

## Table of Contents
- [Project Structure](#project-structure)
- [How to Use](#how-to-use)
   - [Prerequisites](#prerequisites)
   - [Compiling and Running the Project](#compiling-and-running-the-project)
   - [Input the Sudoku Puzzle](#input-the-sudoku-puzzle)

## Project Structure

The project is divided into 3 main packages:
- `sudoku`: Contains the main classes for the sudoku puzzle
- `solver`: Contains the classes for the solver
- `rules`: Contains the classes for the deduction rules

## How to Use

### Prerequisites

Before you begin, make sure you have the **Java Development Kit (JDK)** installed on your machine. You can download it from [Oracle's official website](https://www.oracle.com/java/technologies/javase-jdk15-downloads.html).

### Compiling and Running the Project

1. **Download the Project**

   Clone or download this repository to your machine.

   ```sh
   git clone <repository-url>
   cd <repository-directory>
    ```
2. **Compile the Project**

   Open a terminal or command prompt. Navigate to the root directory of the project and compile the Java source files.

   ```sh
   javac -d bin src/**/*.java
   ```
   
3. **Run the Project**

   Stay in the same directory where the compiled files (*.class) are located and execute the main program file.

   ```sh
   java -cp bin <MainClass>
    ```
   
### Input the Sudoku Puzzle

The program will ask you to input the sudoku puzzle. You can enter the grid in one of the following ways:  
- As a single line of 81 integers.
- Row by row, with 9 integers per row.
- From a file, with each grid on a separate line.

After inputting the puzzle, the program will attempt to solve it and display the solution. If the puzzle is 
unsolvable with the current rules, the program will prompt you to manually input numbers to aid in solving the puzzle.

## Author
KHAO Chloé - 22106244     
M1 Informatique - Université Côte d'Azur
