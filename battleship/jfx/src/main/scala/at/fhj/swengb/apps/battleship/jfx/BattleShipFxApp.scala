package at.fhj.swengb.apps.battleship.jfx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import scala.util.{Failure, Success, Try}

object BattleShipFxApp {

  var FirstStage3000: Stage = _;
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[BattleShipFxApp], args: _*)
  }

  def showScene(scene: Scene, stage: Stage): Unit = {
    stage.setScene(scene)
    stage.show()
  }

  def parseScene(fxml: String): Scene = {
    val triedScene = Try(FXMLLoader.load[Parent](getClass.getResource(fxml)))
    triedScene match {
      case Success(root) =>
        val scene: Scene = new Scene(root)
        scene.getStylesheets.clear()
        scene.getStylesheets.add("/at/fhj/swengb/apps/battleship/jfx/battleshipfx.css")
        scene
      case Failure(e) => {
        e.printStackTrace()
        null
      }
    }
  }
}




class BattleShipFxApp extends Application {

  override def start(stage: Stage): Unit = {
    stage.setTitle("Battleship the Game")
    stage.setResizable(false)
    BattleShipFxApp.FirstStage3000 = stage
    BattleShipFxApp.showScene(BattleShipFxApp.parseScene("/at/fhj/swengb/apps/battleship/jfx/battleshipfx.fxml"),stage)
  }
}

