package at.fhj.swengb.apps.battleship.model

import scala.util.Random

/**
  * Denotes the size of our region of interest
  */
case class BattleField(width: Int, height: Int, fleet: Fleet) {

  /**
    * Adds vessel at a random, free position in the battlefield. if no position could be found,
    * returns the current battlefield without vessel added.
    *
    * @param v vessel to add
    * @return
    */
  def addAtRandomPosition(v: Vessel): BattleField = {

    def Iterator3000(found: Boolean, pos: Set[BattlePos], currBf: BattleField): BattleField = {
      if (found) {
        println(s"Placed vessel of type ${v.getClass.getSimpleName} on battlefield ...")
        currBf
      } else if (pos.isEmpty) {
        println(s"Giving up on vessel of type ${v.getClass.getSimpleName}. No place left.")
        currBf
      } else {
        val p = pos.toSeq(Random.nextInt(pos.size))
        val vessel = v.copy(startPos = p)
        if (vessel.occupiedPos.subsetOf(availablePos)) {
          Iterator3000(true, pos - p, currBf.copy(fleet = currBf.fleet.copy(vessels = currBf.fleet.vessels + vessel)))
        } else {
          Iterator3000(false, pos - p, currBf)
        }
      }
    }
    Iterator3000(false, availablePos, this)

  }

  /**
    * All positions in this battlefield
    */
  val Positions: Set[BattlePos] = (for {x <- 0 until width
                                        y <- 0 until height} yield BattlePos(x, y)).toSet


  val availablePos: Set[BattlePos] = Positions -- fleet.occupiedPositions

  def randomFleet(): Fleet = {
    Fleet(Set[Vessel]())
  }


}

object BattleField {
  def RandomPlacer3000(BattleField3000: BattleField): BattleField = {
    def Placer3000(CurrentBattleField: BattleField, Vessels: Set[Vessel]): BattleField = {
      if (Vessels.isEmpty) CurrentBattleField
      else {
        Placer3000(CurrentBattleField.addAtRandomPosition(Vessels.head), Vessels.tail)
      }
    }
    Placer3000(BattleField3000.copy(fleet = BattleField3000.fleet.copy(vessels = Set())), BattleField3000.fleet.vessels)
  }
}