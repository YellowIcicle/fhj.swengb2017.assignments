package at.fhj.swengb.apps.battleship.model

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle;

/**
  * Represents one part of a vessel or one part of the ocean.
  */
case class BattleFxCell(pos: BattlePos,
                        width: Double,
                        height: Double,
                        log: String => Unit,
                        someVessel: Option[Vessel] = None,
                        fn: (Vessel, BattlePos) => Unit,
                        upClickedPos: BattlePos => Unit)
  extends Rectangle(width, height) {

  def init(): Unit = {
    setFill(Color.DARKBLUE)
  }

  setOnMouseClicked(e => {
    if(!isDisable)
      upClickedPos(pos)
    someVessel match {
      case None =>
        log(s"Commander, you missed the enemy fleet!")
        setFill(Color.MEDIUMAQUAMARINE)
      case Some(v) =>
        fn(v, pos)
        setFill(Color.RED)
    }
  })

  def Clicker3000() = {
    if(!isDisable)
      upClickedPos(pos)
    someVessel match {
      case None =>
        log(s"Commander, you missed the enemy fleet!")
        setFill(Color.MEDIUMAQUAMARINE)
      case Some(v) =>
        fn(v, pos)
        setFill(Color.RED)
    }
  }

}