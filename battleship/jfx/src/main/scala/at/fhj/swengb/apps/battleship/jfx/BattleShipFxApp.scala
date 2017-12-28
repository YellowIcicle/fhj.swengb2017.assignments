package at.fhj.swengb.apps.battleship.jfx

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import scala.util.{Failure, Success, Try}

object BattleShipFxApp {

  //Become initialized when GUI starts
  var rootStage: Stage = _; //If this stays null, some crazy shit is going on...

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[BattleShipFxApp], args: _*)
  }
}


class BattleShipFxApp extends Application {

  val fxml = "/at/fhj/swengb/apps/battleship/jfx/battleshipfx.fxml"
  val css = "/at/fhj/swengb/apps/battleship/jfx/battleshipfx.css"

  val triedRoot = Try(FXMLLoader.load[Parent](getClass.getResource(fxml)))
  def setSkin(stage: Stage, fxml: String, css: String): Boolean = {
    stage.setScene(new Scene(new FXMLLoader(getClass.getResource(fxml)).load[Parent]()))
    stage.getScene.getStylesheets.clear()
    stage.getScene.getStylesheets.add(css)
  }

  override def start(stage: Stage): Unit = {
    BattleShipFxApp.rootStage = stage
    triedRoot match {
      case Success(root) =>
        stage.setResizable(false)
        stage.setScene(new Scene(root))
        stage.setTitle("Battleship the Game")
        setSkin(stage,fxml,css)
        stage.show()
      case Failure(e) => e.printStackTrace()
    }
  }

}