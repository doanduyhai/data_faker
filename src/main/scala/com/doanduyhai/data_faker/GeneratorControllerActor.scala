package com.doanduyhai.data_faker


import java.io.{BufferedWriter, File, FileWriter}

import akka.actor.{Actor, ActorLogging, ActorSystem}

import scala.io.Source

case class Done(actorId: String)

class GeneratorControllerActor(val system:ActorSystem, val actorCount: Int) extends Actor with ActorLogging {
  private var actorCounter = actorCount

  override def receive: Receive = {
    case Done(actorId) => maybeShutdown(actorId)
    case unknown @ _ => log.error(s"GeneratorControllerActor receiving unknown message $unknown")
  }

  def maybeShutdown(actorId: String): Unit = {
    actorCounter -= 1
    log.info(s"DataFakerActor $actorId has finished his job!")
    if(actorCounter == 0) {
      mergeFiles("users")
      mergeFiles("purchases")
      println("------------- Terminating the Fake Data Generator System ----------------")
      system.terminate
    }
  }

  def mergeFiles(fileNamePrefix: String): Unit = {
    log.info(s"Merging all $fileNamePrefix files together")
    val bw = new BufferedWriter(new FileWriter(s"$fileNamePrefix.txt"))
    try {
      (1 to actorCount).foreach(index => {

        Source.fromFile(fileNamePrefix + s"_$index")
          .getLines
          .foreach(line => bw.write(s"$line\n"))

        new File(fileNamePrefix + s"_$index").delete
      })
      bw.flush
    } finally {
      bw.close
    }

  }
}
