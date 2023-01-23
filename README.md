# Blackjack

<a href="https://commons.wikimedia.org/wiki/File:Winning_Blackjack_hand_%285857826966%29.jpg"><img alt="Blackjack hand" align="right" src="https://upload.wikimedia.org/wikipedia/commons/2/2f/Winning_Blackjack_hand_%285857826966%29.jpg" width=200></a>

Simulating a variation of Blackjack (no splits or doubles), in Java. The player is "Sam".

Gradle is required. Tested with OpenJDK 11 on Arch Linux.

## Build

    gradle build -x test

## Run tests

    gradle test

## Run

    gradle run

## Run with a random deck and verbose output

    gradle run --args=-v

## Run with `cards.txt` and don't shuffle the cards

    gradle run --args='-n cards.txt'

## Build a jar file

    gradle jar

## Run the jar file with verbose output enabled

    java -jar build/libs/blackjack-1.0.0.jar -v

## Run with `cards.txt` and don't shuffle the cards

    java -jar build/libs/blackjack-1.0.0.jar -n cards.txt

## Generate HTML documentation and open it with `xdg-open`

    gradle javadoc && xdg-open build/docs/javadoc/index-all.html

## Test the "always stay" strategy

    java -jar build/libs/blackjack-1.0.0.jar -t -s

This strategy doesn't perform too well. Sam wins around **38%** of the rounds.

## Test the "basic optimized" strategy

    java -jar build/libs/blackjack-1.0.0.jar -t -b

This strategy performs better. Sam wins around **76%** of the rounds.

## Command line help text

```
Usage: blackjack [ flags ] FILE

The BasicOptimized strategy is used by default for Sam.

Flags:
-b | --basicopt       Use the BasicOptimized strategy for Sam. (default)
-2 | --second         Use the SecondOptimized strategy for Sam.
-3 | --third          Use the Third strategy for Sam.
-a | --always-hit     Use a strategy where Sam always hits.
-s | --always-stay    Use a strategy where Sam always stays.
-t | --test           Quickly test the current strategy.
-n | --noshuffle      Don't shuffle the cards.
-o | --optimize       Optimize the parameters of the chosen strategy.
-r | --random         Randomize parameters when optimizing them.
-h | --help           Output this help.
-v | --verbose        Output detailed information about the games.
--version             Output the current version number.
```

## General info

* Version: 1.0.0
* License: BSD-3

<sup><sub>The Blackjack image is from [WikiMedia](https://commons.wikimedia.org/wiki/File:Winning_Blackjack_hand_%285857826966%29.jpg) and is licensed under a Creative Commons license.</sub></sup>
