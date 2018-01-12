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

  def ScenePresenter3000(scene: Scene, stage: Stage): Unit = {
    stage.setScene(scene)
    stage.show()
  }

  def SceneLoader3000(sceneString: String): Scene = {
    val triedScene = Try(FXMLLoader.load[Parent](getClass.getResource(sceneString)))
    triedScene match {
      case Success(root) =>
        val scene: Scene = new Scene(root)
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
    BattleShipFxApp.FirstStage3000 = stage
    BattleShipFxApp.ScenePresenter3000(BattleShipFxApp.SceneLoader3000("/at/fhj/swengb/apps/battleship/jfx/splashscreen.fxml"),stage)
  }
}

