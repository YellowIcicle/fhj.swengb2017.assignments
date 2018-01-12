package at.fhj.swengb.apps.battleship.jfx

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}

class BattleShipFxControllerCreateGame extends Initializable {

  override def initialize(url: URL, rb: ResourceBundle): Unit = {

  }

  @FXML def toWelcome(): Unit = BattleShipFxApp.ScenePresenter3000(BattleShipFxApp.SceneLoader3000("/at/fhj/swengb/apps/battleship/jfx/welcomescreen.fxml"),BattleShipFxApp.FirstStage3000)

  @FXML def Continue(): Unit = BattleShipFxApp.ScenePresenter3000(BattleShipFxApp.SceneLoader3000("/at/fhj/swengb/apps/battleship/jfx/battleshipfx.fxml"),BattleShipFxApp.FirstStage3000)


}