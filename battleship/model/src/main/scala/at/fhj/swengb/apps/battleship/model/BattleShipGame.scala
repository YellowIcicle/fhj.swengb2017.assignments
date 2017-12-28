package at.fhj.swengb.apps.battleship.model

/**
  * Contains all information about a battleship game.
  */
case class BattleShipGame(battleField: BattleField,
                          log: String => Unit,
                          updateSlider: Int => Unit,
                          getCellWidth: Int => Double,
                          getCellHeight: Int => Double,
                          ) {

  /**
    * remembers which vessel was hit at which position
    * starts with the empty map, meaning that no vessel was hit yet.
    *
    **/
  var hits: Map[Vessel, Set[BattlePos]] = Map()

  /**
    * contains all vessels which are destroyed
    */
  var sunkShips: Set[Vessel] = Set()

  /**
    * Contains all already clicked positions.
    * Array keeps sorting...
    */
  var GameState: List[BattlePos] = List();

  /**
    * We don't ever change cells, they should be initialized only once.
    */
  private val cells: Seq[BattleFxCell] = for {
    x <- 0 until battleField.width
    y <- 0 until battleField.height
    pos = BattlePos(x, y)
  } yield {
    BattleFxCell(BattlePos(x, y),
      getCellWidth(x),
      getCellHeight(y),
      log,
      battleField.fleet.findByPos(pos),
      updateGameState,
      ClickHistory)
  }

  def CellReader3000(): Seq[BattleFxCell] = cells

  //Adds a new Position to clicked set
  def ClickHistory(pos: BattlePos): Unit = {
    //We keep already clicked positions awell!
    GameState = pos :: GameState

    //Update Slider aswell
    updateSlider(GameState.size)
  }

  //Simulates click for all positions in list
  def RebuildGame(pos: List[BattlePos]): Unit = {

    /*
    We have to iterate to get the correct sequence.
    We are not allowed to do this:
        val relevantCells: Seq[BattleFxCell] = cells.filter(c => pos.contains(c.pos))
        relevantCells.map(e => e.handleMouseClick())
    because filter is unsorted and would destroy the sequence
     */
    for (p <- pos) {
      //There is just one, we take the risc
      val fxCell: BattleFxCell = cells.filter(e => e.pos.equals(p)).head
      fxCell.Clicker3000()
    }
  }

  def updateGameState(vessel: Vessel, pos: BattlePos): Unit = {
    log(vessel.name.value + "was hit!")

    if (hits.contains(vessel)) {
      // this code is executed if vessel was already hit at least once

      // pos
      // vessel
      // map (hits)

      // we want to update the hits map
      // the map should be updated if
      // we hit a vessel which is already contained
      // in the 'hits' map, and it's values (
      // the set of BattlePos) should be added
      // the current pos
      val oldPos: Set[BattlePos] = hits(vessel)

      hits = hits.updated(vessel, oldPos + pos)

      hits(vessel).foreach(p => log(p.toString))

      if (oldPos.contains(pos)) {
        log("Commander, you already bombed this area!")
      }

      if (vessel.occupiedPos == hits(vessel)) {
        log(s"${vessel.name.value} annihilated!")
        sunkShips = sunkShips + vessel

        if (battleField.fleet.vessels == sunkShips) {
          log("GAME OVER!")
        }
      }

    } else {
      // vessel is not part of the map
      // but vessel was hit!
      // it was hit the first time ever!
      hits = hits.updated(vessel, Set(pos))
    }

  }

}