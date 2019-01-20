# Goose Game

My implementation of Goose Game contains all requirements apart from the optional one (Prank). I decided to skip it not to prolong delivering solution - but I'm pretty sure solution is easily extensible enough to do it smoothly.

### Requirements
Solution is implemented with Scala and requires sbt to be run.

### How to run game
You can run the game with `sbt run` in root directory of the project.

### How to run tests
You can run the tests with `sbt test` in root directory of the project.

### Assumptions:
- players should not be added once game started - for this I introduced additional command `start`, after this you can't add new players and you can start moving players
- game is finished once first player reaches last field

### Possible improvements:
- enforce players turns - in current state nothing stops one player to move without giving a chance to other players
- simplify models of movement - implemented model of player moves is redundant and overcomplicated, it should be done better
- more tests - tests of game engine are a good start, but there should be more
- interface for custom game configuration - implementation supports custom rules (board length, bridges and geese locations), but there's no interface to set it up other that in code. It could be done easily with use of Typesafe configuration files and a library like `pureconfig` to read such configuration file to case class `BoardConfiguration`.
