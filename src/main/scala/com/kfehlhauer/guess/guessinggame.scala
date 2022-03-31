package com.kfehlhauer.guess
import zio.*

enum Game:
  case Country, City, InvalidGame

final case class InvalidGameException() extends Exception

final case class GuessingGame(game: Game, ttg: String):
  def checkGuess(g: String): UIO[Boolean] = UIO.succeed(g.toUpperCase == this.ttg.toUpperCase)

object GuessingGame:
  def randomThing(lt: List[String]): String =
    val r               = scala.util.Random
    val randomSelection = r.nextInt(lt.length) - 1
    lt(randomSelection)

  def apply(game: String): Task[GuessingGame] =
    game match
      case "1" => Task(GuessingGame(Game.Country, randomThing(Countries.thingsToGuess)))
      case "2" => Task(GuessingGame(Game.City, randomThing(Cities.thingsToGuess)))
      case _   => Task(throw InvalidGameException())
