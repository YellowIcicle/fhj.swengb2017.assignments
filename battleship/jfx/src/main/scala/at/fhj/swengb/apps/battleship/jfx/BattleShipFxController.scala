package at.fhj.swengb.apps.battleship.jfx

import java.io.{File, FilenameFilter}
import java.net.URL
import java.nio.file.{Files, Paths}
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Label, Slider, TextArea}
import javafx.scene.layout.GridPane
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter

import at.fhj.swengb.apps.battleship.BattleShipProtocol
import at.fhj.swengb.apps.battleship.model._

class BattleShipFxController extends Initializable {

  //Initialized in init
  private var game: BattleShipGame = _

  @FXML private var battleGroundGridPane: GridPane = _
  @FXML private var SliderState: Slider = _
  @FXML private var lbHeader: Label = _

  /**
    * A text area box to place the history of the game
    */
  @FXML private var log: TextArea = _

  //Creating a new game and resetting all states
  @FXML def newGame(): Unit = {
    appendLog("A new game has started")
    init(createNewGame(), List())
  }

  @FXML def saveGame(): Unit = {
      //Using FileChooser for accessing our files
      val FileChooser3000 = new FileChooser();
      //Filtering on our protobuf files with the ending .bin
      val ProtoFilter3000: FileChooser.ExtensionFilter = new ExtensionFilter("Protobuf files","*.bin")
      FileChooser3000.getExtensionFilters.add(ProtoFilter3000)
      //Converting and saving
      val FileSaver3000: File = FileChooser3000.showSaveDialog(BattleShipFxApp.rootStage)
      BattleShipProtocol.convert(game).writeTo(Files.newOutputStream(Paths.get(FileSaver3000.getAbsolutePath)))
      appendLog("Saved Game")
  }

  @FXML def loadGame(): Unit = {
      val FileChooser3000 = new FileChooser();
      val ProtoFilter3000: FileChooser.ExtensionFilter = new ExtensionFilter("Protobuf files","*.bin")
      FileChooser3000.getExtensionFilters.add(ProtoFilter3000)
      val FileLoader3000: File = FileChooser3000.showOpenDialog(BattleShipFxApp.rootStage)
      val (clickedPos, battleShipGame) = loadGame(FileLoader3000.getAbsolutePath)
      //Resetting log
      log.setText("")
      init(clickedPos, battleShipGame)
      appendLog("Loaded Game")
  }

  @FXML def onSliderChanged(): Unit = {
    val TimeOnSlider = SliderState.getValue.toInt
    var PastChecker: Boolean = true
    val PastState: List[BattlePos] = game.GameState.takeRight(TimeOnSlider)


    //Print some fancy output to user
    if (TimeOnSlider == SliderState.getMax.toInt) {
      appendLog("Backlog mode")
      lbHeader.setText("Battleship")
      PastChecker=false
      /*We are in the present now again, which means that the buttons get active again
        In this case we add the clicks to the list, that means we would have them tice.
        Remove them here now - they get inserted with simulateClicks anyway...
       */
      game.GameState = List()
    } else {
      appendLog("Backlog mode (" + PastState.size + ")")
      lbHeader.setText("Backlog")
      PastChecker=true
    }

    //If we are simulation mode, deactivate buttons
    battleGroundGridPane.getChildren.clear()
    for (c <- game.getCells()) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
      c.init()
      //Deactivate Buttons if we're in the history data!
      //History is not changeable, we just can learn from history!
      c.setDisable(PastChecker)
    }


    //Now simulate all already clicked positions
    game.simulateClicksOnClickedPositions(PastState)
  }

  override def initialize(url: URL, rb: ResourceBundle): Unit = newGame()

  private def getCellHeight(y: Int): Double =
    battleGroundGridPane.getRowConstraints.get(y).getPrefHeight

  private def getCellWidth(x: Int): Double =
    battleGroundGridPane.getColumnConstraints.get(x).getPrefWidth

  def appendLog(message: String): Unit = log.appendText(message + "\n")

  /**
    * Create a new game.
    *
    * This means
    *
    * - resetting all cells to 'empty' state
    * - placing your ships at random on the battleground
    *
    */
  def init(g: BattleShipGame, simulateClicks: List[BattlePos]): Unit = {
    //Initialize BattleShipGame
    //Required to save state!
    game = g

    battleGroundGridPane.getChildren.clear()
    for (c <- g.getCells()) {
      battleGroundGridPane.add(c, c.pos.x, c.pos.y)
    }
    g.getCells().foreach(c => c.init())

    //Reset all previous clicked positions
    //simulate all already clicked positions and update GUI-Slider!
    g.GameState = List()
    g.simulateClicksOnClickedPositions(simulateClicks)
    updateSlider(simulateClicks.size)
  }


  private def createNewGame(): BattleShipGame = {
    val field = BattleField(10, 10, Fleet(FleetConfig.Standard))
    val battleField: BattleField = BattleField.placeRandomly(field)
    val game = BattleShipGame(battleField, getCellWidth, getCellHeight, appendLog,updateSlider)
    game
  }

  private def loadGame(filePath: String): (BattleShipGame, List[BattlePos]) = {
    //Read Protobuf-Object
    val bsgIn =
      at.fhj.swengb.apps.battleship.BattleShipProtobuf.BattleShipGame
        .parseFrom(Files.newInputStream(Paths.get(filePath)))

    //Convert Protobuf-Object to a BattleShipGame Instance
    val loadedBattleShipGame: BattleShipGame =
      BattleShipProtocol.convert(bsgIn)

    //Create new game-Event based on loaded Data
    val battleShipGame = BattleShipGame(loadedBattleShipGame.battleField,
      getCellWidth,
      getCellHeight,
      appendLog,
      updateSlider)

    battleShipGame.GameState = List()

    (battleShipGame, loadedBattleShipGame.GameState)
  }

  def updateSlider(maxClicks: Int): Unit = {
    SliderState.setMax(maxClicks)
    SliderState.setValue(maxClicks)
  }


}