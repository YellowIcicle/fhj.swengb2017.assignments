package at.fhj.swengb.apps.battleship

import at.fhj.swengb.apps.battleship.BattleShipProtobuf.BattleShipGame.{
  Position,
  Vessel,
  VesselDirection
}
import at.fhj.swengb.apps.battleship.model._
import scala.collection.JavaConverters._

object BattleShipProtocol {

  def convert(g: BattleShipGame): BattleShipProtobuf.BattleShipGame = {

    val protoBattleField = BattleShipProtobuf.BattleShipGame
      .newBuilder()
      .setFieldWidth(g.battleField.width)
      .setFieldHeight(g.battleField.height)

    //Convert vessesls to protobuf-Vessels and add it
    val fleetProtobuf: Set[Vessel] =
      g.battleField.fleet.vessels.map(e => convert(e))
    fleetProtobuf.foreach(e => protoBattleField.addVessels(e))

    //Convert set of BattleBos to Protobuf clicked positions add add it
    val clickedPos =
      g.GameState.map(e => convert(e))
    clickedPos.foreach(e => protoBattleField.addClickedPositions(e))

    //Build battlefield and write to file
    protoBattleField.build()
  }

  def convert(g: BattleShipProtobuf.BattleShipGame): BattleShipGame = {
    //Create data for BattleField
    val fleet: Fleet = Fleet(
      g.getVesselsList.asScala.map(e => convert(e)).toSet)

    val battleField = BattleField(g.getFieldWidth, g.getFieldHeight, fleet)

    //Create set of alread clicked positions
    val clickedPos: List[BattlePos] =
      g.getClickedPositionsList.asScala.map(e => convert(e)).toList

    //Create BattleshipGame and set aready clicked positions
    val game = BattleShipGame(battleField,
      e => (),
      e => (),
      e => e.toDouble,
      e => e.toDouble)
    game.GameState = clickedPos

    //return game
    game
  }

  /**
    * Convertes a given Battleship-Game Vessel to an Protobuf Vessel to store
    *
    * @param vessel
    * @return
    */
  // Previous convertVesseltoProtobufVessel
  def convert(vessel: at.fhj.swengb.apps.battleship.model.Vessel): Vessel = {
    //Create new protobuf Vessel
    Vessel
      .newBuilder()
      .setOrientation(vessel.direction match {
        case Vertical   => VesselDirection.Vertical;
        case Horizontal => VesselDirection.Horizontal;
        case _ => VesselDirection.Vertical
      })
      .setName(vessel.name.value)
      .setSize(vessel.size)
      .setStartPos(
        Position
          .newBuilder()
          .setX(vessel.InitPosition.x)
          .setY(vessel.InitPosition.y)
          .build())
      .build()
  }

  /**
    * Convert a Protobuf Vessel to a BattleShipGame Vessel
    *
    * @param vessel
    * @return
    */
  //Previous convertProtobufVesseltoVessel
  def convert(vessel: Vessel): at.fhj.swengb.apps.battleship.model.Vessel = {

    val name: NonEmptyString = NonEmptyString(vessel.getName)
    val startPos: BattlePos =
      BattlePos(vessel.getStartPos.getX, vessel.getStartPos.getY)
    val size = vessel.getSize
    val direction: Direction = vessel.getOrientation match {
      case VesselDirection.Vertical   => Vertical
      case VesselDirection.Horizontal => Horizontal
      case _ => Vertical
    }

    //Create Battleship Vessel and return it.
    at.fhj.swengb.apps.battleship.model.Vessel(name, startPos, direction, size)
  }

  /**
    * Generates a Protobuf Position from given BattlePos position
    *
    * @param battlePos
    * @return
    */
  //Previous convertBattlePosToProtobufPosition
  private def convert(battlePos: BattlePos): Position = {
    Position
      .newBuilder()
      .setX(battlePos.x)
      .setY(battlePos.y)
      .build()
  }

  /** Converts a Protobuf Position to a BattleShipGame BattlePos
    *
    * @param position
    * @return
    */
  //previous convertProtoBufPositionToBattlePos
  private def convert(position: Position): BattlePos = {
    BattlePos(position.getX, position.getY)
  }

}