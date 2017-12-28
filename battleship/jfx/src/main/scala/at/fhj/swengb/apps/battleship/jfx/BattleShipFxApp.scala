package at.fhj.swengb.apps.battleship.jfx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import scala.util.{Failure, Success, Try}

object BattleShipFxApp {

  var rootStage: Stage = _;

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[BattleShipFxApp], args: _*)
  }
}


class BattleShipFxApp extends Application {

  val BodyFile = "/at/fhj/swengb/apps/battleship/jfx/battleshipfx.fxml"
  val triedRoot = Try(FXMLLoader.load[Parent](getClass.getResource(BodyFile)))

  override def start(stage: Stage): Unit = {
    BattleShipFxApp.rootStage = stage
    triedRoot match {
      case Success(root) =>
        stage.setResizable(false)
        stage.setScene(new Scene(root))
        stage.setTitle("Battleship the Game")
        stage.show()
      case Failure(e) => e.printStackTrace()
    }
  }

}