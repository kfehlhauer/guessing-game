package com.kfehlhauer.guess

import zio.*
import zio.Console.*

object Main extends zio.ZIOAppDefault:

  val clearConsole = printLine("\u001b[2J")

  val selectGame: ZIO[Console, Throwable, GuessingGame] =
    for
      _ <- printLine(
        """|Which game do you you wish to play:
                                        |1. Guess Country
                                        |2. Guess City
                                        |Please enter a number as your choice.""".stripMargin
      )
      selection <- getStrLn
      thisGame <- GuessingGame(selection).catchAll { _ =>
        printLine("Please select a valid game type") *> selectGame
      }
    yield thisGame

  def makeGuess(gg: GuessingGame) =
    for
      _      <- hints(gg)
      _      <- printLine("Please enter your guess")
      guess  <- getStrLn
      result <- gg.checkGuess(guess)
    yield result

  def hints(gg: GuessingGame): ZIO[Console, Throwable, Unit] =
    printLine(
      s"""This ${gg.game.toString} contains ${gg.ttg.length} characters and begins with a "${gg
        .ttg(0)
        .toUpper}" and ends with a "${gg.ttg.last.toUpper}""""
    )

  def gameLoop(gg: GuessingGame): ZIO[Console, Throwable, Unit] =
    for
      guess <- makeGuess(gg)
      _ <-
        if !guess then clearConsole *> printLine("Please Try Again") *> gameLoop(gg)
        else printLine("Congratulations, you have Won!!!\n\n")
    yield ()

  def outerGameLoop: ZIO[Console, Throwable, Unit] =
    for
      gameChoice <- selectGame
      _          <- clearConsole *> gameLoop(gameChoice)
      _          <- printLine("Would you like to play again?")
      _          <- printLine("""Answer "y" or any other input to quit playing.""")
      replay     <- getStrLn
      _          <- clearConsole
      _ <-
        if replay.toUpperCase == "Y" then outerGameLoop
        else printLine("Thanks for playing! Goodbye.") *> ZIO.succeed(())
    yield ()

  def run = outerGameLoop
end Main
