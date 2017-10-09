package com.doanduyhai.data_faker


import akka.actor.{ActorLogging, ActorSystem, Props}
import org.slf4j.LoggerFactory



object AkkaFakeDataGenerator  {

  val log = LoggerFactory.getLogger(AkkaFakeDataGenerator.getClass.getCanonicalName)

  def main(input: Array[String]): Unit = {
    printBanner
    if(checkArguments(input)) {

      val parallelism = input(0).toInt
      val userCount = input(1).toInt
      val minTransactions = input(2).toInt
      val maxTransactions = input(3).toInt

      log.info(s"Generating fake data with parallelism $parallelism, user count $userCount, min transactions $minTransactions and max transactions $maxTransactions")

      val userCountPerActor = Math.floor(userCount/parallelism).toInt

      val system = ActorSystem("FakeDataGeneratorSystem")
      registerShutdownHook(system)

      val controller = system.actorOf(Props(new GeneratorControllerActor(system, parallelism)))
      (1 to parallelism).foreach(actorId => {
          val actor = system.actorOf(Props(new DataFakerActor(controller, actorId.toString)))
          actor ! GenerateData(userCountPerActor, minTransactions, maxTransactions)
      })
      }
  }


  def registerShutdownHook(system: ActorSystem): Unit = {
    system.registerOnTermination {
      println("------------- Ctrl+C detected, terminating the Fake Data Generator System ----------------")
    }

    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        system.terminate()
      }
    })
  }

  def printBanner: Unit = {
    println(
      """
        |
        |        ______    _            _____        _             _____                           _
        |       |  ____|  | |          |  __ \      | |           / ____|                         | |
        |       | |__ __ _| | _____    | |  | | __ _| |_ __ _    | |  __  ___ _ __   ___ _ __ __ _| |_ ___  _ __
        |       |  __/ _` | |/ / _ \   | |  | |/ _` | __/ _` |   | | |_ |/ _ \ '_ \ / _ \ '__/ _` | __/ _ \| '__|
        |       | | | (_| |   <  __/   | |__| | (_| | || (_| |   | |__| |  __/ | | |  __/ | | (_| | || (_) | |
        |       |_|  \__,_|_|\_\___|   |_____/ \__,_|\__\__,_|    \_____|\___|_| |_|\___|_|  \__,_|\__\___/|_|
        |
        |
      """.stripMargin)
  }

  def checkArguments(input: Array[String]): Boolean = {
    Option(input) match {
      case Some(input) =>
        if (input.size != 4) {
          displayUsage
          false
        } else true
      case None => {
        displayUsage
        false
      }
    }
  }

  def displayUsage:Unit = {
    println(
      """ Usage:
      |
      | java -jar fake-data-generator-<version>.jar <parallelism> <nb_of_users> <min_transactions> <max_transactions>
      """.stripMargin)
  }
}