package binaryTreeScala


import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control._
import javafx.scene.layout._
import javafx.stage.FileChooser
import javafx.stage.Stage

import java.io.File
import scala.collection.convert.ImplicitConversions.`list asScalaBuffer`
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters.CollectionHasAsScala

class BinaryTreeGUI extends Application {

  private var binaryTree: BinaryTree = new BinaryTree.Base()
  private val userFactory: UserFactory = new UserFactory()
  private var builder: UserType = _
  private var controller: BinaryTreeController = _

  private var addValueField: TextField = _
  private var deleteValueField: TextField = _
  private var atValueField: TextField = _
  private var outputArea: TextArea = _
  private var operationComboBox: ComboBox[String] = _


  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Binary Tree GUI")

    controller = new BinaryTreeController(binaryTree, () => updateOutput())

    val items: ArrayBuffer[String] = userFactory.getTypeNameList.asScala.to(ArrayBuffer)

    addValueField = new TextField
    deleteValueField = new TextField
    deleteValueField.setPromptText("Enter index")
    atValueField = new TextField
    atValueField.setPromptText("Enter index")

    operationComboBox = new ComboBox[String]()
    operationComboBox.getItems ++= items
    operationComboBox.setValue(items(0))
    builder = userFactory.getBuilderByName(operationComboBox.getValue)

    addValueField.setPromptText(s"Enter value: example ${builder.create}")

    operationComboBox.setOnAction(e => {
      val oldValue = builder.typeName
      builder = userFactory.getBuilderByName(operationComboBox.getValue)
      addValueField.setPromptText(s"Enter value: example ${builder.create}")
      if (!oldValue.equals(builder.typeName)) {
        binaryTree.clear()
        updateOutput()
      }
    })

    val addButton: Button = new Button("Add")
    addButton.setOnAction(e => controller.onAddButtonClicked(builder, addValueField.getText))

    val functionalAddButton: Button = new Button("Functional Add")
    functionalAddButton.setOnAction(e => {
      controller.onFunctionalAddButtonClicked(builder, addValueField.getText)
      binaryTree = controller.binaryTree
    })

    outputArea = new TextArea
    outputArea.setEditable(false)

    val deleteButton: Button = new Button("Delete")
    deleteButton.setOnAction(e => controller.onDeleteButtonClicked(deleteValueField.getText))

    val balanceButton: Button = new Button("Balance")
    balanceButton.setOnAction(e => controller.onBalanceButtonClicked())

    val serializeButton: Button = new Button("Serialize")
    serializeButton.setOnAction(e => {
      controller.onSerializeButtonClicked(builder)
    })

    val deserializeButton: Button = new Button("Deserialize")
    deserializeButton.setOnAction(e => {
      controller.onDeserializeButtonClicked()
      this.binaryTree = controller.binaryTree
      if (!binaryTree.isEmpty) {
        this.builder = userFactory.getBuilderByName(binaryTree.at(0).typeName)
        operationComboBox.setValue(this.builder.typeName)
      }
      controller.updateOutput()
    })

    val atButton: Button = new Button("At")
    atButton.setOnAction(e => controller.onAtButtonClicked(atValueField.getText, outputArea))

    val showButton: Button = new Button("Show")
    showButton.setOnAction(e => controller.updateOutput())

    val traverseOrderButton: Button = new Button("Traverse Order")
    traverseOrderButton.setOnAction(e => controller.onTraverseOrderButtonClicked(outputArea))

    val clearButton: Button = new Button("Clear")
    clearButton.setOnAction(e => controller.onButtonClearClicked())


    val addBox: HBox = new HBox(20)
    addBox.setAlignment(Pos.CENTER)
    addBox.getChildren.addAll(addValueField, addButton)

    val deleteBox: HBox = new HBox(20)
    deleteBox.setAlignment(Pos.CENTER)
    deleteBox.getChildren.addAll(deleteValueField, deleteButton)

    val atBox: HBox = new HBox(20)
    atBox.setAlignment(Pos.CENTER)
    atBox.getChildren.addAll(atValueField, atButton)

    val operationBox: VBox = new VBox(20)
    operationBox.setAlignment(Pos.CENTER)
    operationBox.getChildren.addAll(addBox, deleteBox, atBox)

    val secondaryOperationsBox: VBox = new VBox(20)
    secondaryOperationsBox.setAlignment(Pos.CENTER)
    secondaryOperationsBox.getChildren.addAll(serializeButton, deserializeButton, traverseOrderButton)

    val otherOperations: VBox = new VBox(20)
    otherOperations.setAlignment(Pos.CENTER)
    otherOperations.getChildren.addAll(showButton, balanceButton, clearButton)

    val mainBox: HBox = new HBox(20, operationBox, secondaryOperationsBox, otherOperations, operationComboBox, functionalAddButton)
    mainBox.setAlignment(Pos.CENTER)

    val borderPane: BorderPane = new BorderPane
    borderPane.setCenter(outputArea)

    borderPane.setBottom(mainBox)

    val scene: Scene = new Scene(borderPane, 900, 900)
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm())
    primaryStage.setScene(scene)
    primaryStage.show()

    controller.updateOutput()
  }

  private def updateOutput(): Unit = {
    outputArea.setText(binaryTree.toString)
  }

  private class BinaryTreeController(var binaryTree: BinaryTree, val updateOutputCallback: () => Unit) {



    private val serialize: Serialize = new Serialize.Base()

    def onAddButtonClicked(builder: UserType, input: String): Unit = {
      if (!input.isEmpty) {
        try {
          val value: UserType = builder.parseValue(input).asInstanceOf[UserType]
          if (value != null) {
            binaryTree.add(value)
            updateOutputCallback()
          }
        } catch {
          case e: IllegalArgumentException =>
            showAlert("Invalid input. Please enter a valid value.")
        }
      }
    }

    def onFunctionalAddButtonClicked(builder: UserType, input: String): Unit = {
      if (!input.isEmpty) {
        try {
          val value: UserType = builder.parseValue(input).asInstanceOf[UserType]
          if (value != null) {
            binaryTree = binaryTree.addFunctional(value)
            updateOutputCallback()
          }
        } catch {
          case e: IllegalArgumentException =>
            showAlert("Invalid input. Please enter a valid value.")
        }
      }
    }

    def onDeleteButtonClicked(input: String): Unit = {
      if (!input.isEmpty) {
        try {
          val index: Int = input.toInt
          binaryTree.delete(index)
          updateOutputCallback()
        } catch {
          case e: IllegalArgumentException =>
            showAlert("Invalid input. Please enter a valid value.")
        }
      }
    }

    def updateOutput(): Unit = {
      updateOutputCallback()
    }

    private def showAlert(message: String): Unit = {
      val alert: Alert = new Alert(Alert.AlertType.ERROR)
      alert.setTitle("Error")
      alert.setHeaderText(null)
      alert.setContentText(message)
      alert.showAndWait()
    }

    def onBalanceButtonClicked(): Unit = {
      binaryTree.balance()
      updateOutputCallback()
    }

    def onAtButtonClicked(input: String, outputArea: TextArea): Unit = {
      if (!input.isEmpty) {
        try {
          val index: Int = input.toInt
          val value: String = binaryTree.at(index).toString
          outputArea.setText(value)
        } catch {
          case e: IllegalArgumentException =>
            showAlert("Invalid input. Please enter a valid value.")
        }
      }
    }

    def onTraverseOrderButtonClicked(outputArea: TextArea): Unit = {
      val stringBuilder: StringBuilder = new StringBuilder()
      binaryTree.forEach(v => {
        stringBuilder.append(v.toString)
        stringBuilder.append(" ")
      })
      outputArea.setText(stringBuilder.toString())
    }

    def onButtonClearClicked(): Unit = {
      binaryTree.clear()
      updateOutputCallback()
    }

    def onSerializeButtonClicked(builder: UserType): Unit = {
      val fileChooser: FileChooser = new FileChooser
      fileChooser.setTitle("Select File")
      fileChooser.getExtensionFilters.add(new FileChooser.ExtensionFilter("Text Files", "*.txt"))

      val userDirectory: String = System.getProperty("user.dir")
      val initialDirectory: File = new File(userDirectory)
      fileChooser.setInitialDirectory(initialDirectory)

      val selectedFile: File = fileChooser.showSaveDialog(new Stage)

      if (selectedFile != null) {
        serialize.serialize(binaryTree, selectedFile.getAbsolutePath, builder.typeName)
      }
    }

    def onDeserializeButtonClicked(): Unit = {
      val fileChooser: FileChooser = new FileChooser
      fileChooser.setTitle("Select File")
      fileChooser.getExtensionFilters.add(new FileChooser.ExtensionFilter("Text Files", "*.txt"))

      val userDirectory: String = System.getProperty("user.dir")
      val initialDirectory: File = new File(userDirectory)
      fileChooser.setInitialDirectory(initialDirectory)

      val selectedFile: File = fileChooser.showOpenDialog(new Stage)

      if (selectedFile != null) {
        this.binaryTree = serialize.deserialize(selectedFile.getAbsolutePath)
      }
    }
  }
}

object BinaryTreeGUI {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[BinaryTreeGUI], args: _*)
  }
}
