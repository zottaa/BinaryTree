import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class BinaryTreeGUIKt : Application() {

    private var binaryTree: BinaryTreeKt = BinaryTreeKt.Base()
    private val userFactory = UserFactoryKt()
    private var builder: UserTypeKt? = null
    private lateinit var controller: BinaryTreeController

    private lateinit var addValueField: TextField

    private lateinit var deleteValueField: TextField

    private lateinit var atValueField: TextField
    private lateinit var outputArea: TextArea
    private lateinit var operationComboBox: ComboBox<String>

    override fun start(primaryStage: Stage) {
        primaryStage.title = "Binary Tree GUI"

        controller = BinaryTreeController(binaryTree) { updateOutput() }

        val items = userFactory.getTypeNameList()

        addValueField = TextField()

        deleteValueField = TextField()

        deleteValueField.promptText = "Enter index"
        atValueField = TextField()

        atValueField.promptText = "Enter index"

        operationComboBox = ComboBox()
        operationComboBox.items.addAll(items)
        operationComboBox.value = items[0]
        builder = userFactory.getBuilderByName(operationComboBox.value)

        addValueField.promptText = "Enter value: example " + builder?.create().toString()

        operationComboBox.setOnAction { e ->
            val oldValue = builder?.typeName()
            builder = userFactory.getBuilderByName(operationComboBox.value)
            addValueField.promptText = "Enter value: example " + builder?.create().toString()
            if (oldValue != builder?.typeName()) {
                binaryTree.clear()
                updateOutput()
            }
        }

        val addButton = Button("Add")
        addButton.setOnAction { e -> controller.onAddButtonClicked(builder, addValueField.text) }


        outputArea = TextArea()
        outputArea.isEditable = false

        val deleteButton = Button("Delete")
        deleteButton.setOnAction { e -> controller.onDeleteButtonClicked(deleteValueField.text) }

        val balanceButton = Button("Balance")
        balanceButton.setOnAction { e -> controller.onBalanceButtonClicked() }

        val serializeButton = Button("Serialize")
        serializeButton.setOnAction { e -> controller.onSerializeButtonClicked(builder) }

        val deserializeButton = Button("Deserialize")
        deserializeButton.setOnAction {
        controller.onDeserializeButtonClicked()
            this.binaryTree = controller.binaryTree
            if (!binaryTree.isEmpty()) {
                this.builder = userFactory.getBuilderByName(binaryTree.at(0)?.typeName() ?: "None")
                operationComboBox.value = this.builder?.typeName()
            }
            controller.updateOutput()
        }

        val atButton = Button("At")
        atButton.setOnAction { e -> controller.onAtButtonClicked(atValueField.text, outputArea) }

        val showButton = Button("Show")
        showButton.setOnAction { e -> controller.updateOutput() }

        val traverseOrderButton = Button("Traverse Order")
        traverseOrderButton.setOnAction { e -> controller.onTraverseOrderButtonClicked(outputArea) }

        val clearButton = Button("Clear")
        clearButton.setOnAction { e -> controller.onButtonClearClicked() }


        val addBox = HBox(20.0)
        addBox.alignment = Pos.CENTER
        addBox.children.addAll(addValueField, addButton)

        val deleteBox = HBox(20.0)
        deleteBox.alignment = Pos.CENTER
        deleteBox.children.addAll(deleteValueField, deleteButton)

        val atBox = HBox(20.0)
        atBox.alignment = Pos.CENTER
        atBox.children.addAll(atValueField, atButton)

        val operationBox = VBox(20.0)
        operationBox.alignment = Pos.CENTER
        operationBox.children.addAll(addBox, deleteBox, atBox)

        val secondaryOperationsBox = VBox(20.0)
        secondaryOperationsBox.alignment = Pos.CENTER
        secondaryOperationsBox.children.addAll(serializeButton, deserializeButton, traverseOrderButton)

        val otherOperations = VBox(20.0)
        otherOperations.alignment = Pos.CENTER
        otherOperations.children.addAll(showButton, balanceButton, clearButton)

        val mainBox = HBox(20.0, operationBox, secondaryOperationsBox, otherOperations, operationComboBox)
        mainBox.alignment = Pos.CENTER

        val borderPane = BorderPane()
        borderPane.center = outputArea

        borderPane.bottom = mainBox

        val scene = Scene(borderPane, 900.0, 900.0)
        scene.stylesheets.add(javaClass.getResource("css/style.css").toExternalForm())

        primaryStage.scene = scene
        primaryStage.show()

        controller.updateOutput()
    }

    private fun updateOutput() {
        outputArea.text = binaryTree.toString()
    }

    private class BinaryTreeController(
        var binaryTree: BinaryTreeKt,
        private val updateOutputCallback: Runnable
    ) {
        private val serialize: SerializeKt = SerializeKt.Base()

        fun onAddButtonClicked(builder: UserTypeKt?, input: String) {
            if (!input.isEmpty()) {
                try {
                    val value = builder?.parseValue(input) as UserTypeKt?
                    if (value != null) {
                        binaryTree.add(value)
                        updateOutputCallback.run()
                    }
                } catch (e: IllegalArgumentException) {
                    showAlert("Invalid input. Please enter a valid value.")
                }
            }
        }

        fun onDeleteButtonClicked(input: String) {
            if (!input.isEmpty()) {
                try {
                    val index = Integer.parseInt(input)
                    binaryTree.delete(index)
                    updateOutputCallback.run()
                } catch (e: IllegalArgumentException) {
                    showAlert("Invalid input. Please enter a valid value.")
                }
            }
        }

        fun updateOutput() {
            updateOutputCallback.run()
        }

        private fun showAlert(message: String) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Error"
            alert.headerText = null
            alert.contentText = message
            alert.showAndWait()
        }

        fun onBalanceButtonClicked() {
            binaryTree.balance()
            updateOutputCallback.run()
        }

        fun onAtButtonClicked(input: String, outputArea: TextArea) {
            if (!input.isEmpty()) {
                try {
                    val index = Integer.parseInt(input)
                    val value = binaryTree.at(index).toString()
                    outputArea.text = value
                } catch (e: IllegalArgumentException) {
                    showAlert("Invalid input. Please enter a valid value.")
                }
            }
        }

        fun onTraverseOrderButtonClicked(outputArea: TextArea) {
            val stringBuilder = StringBuilder()
            binaryTree.forEach(object : ElementProcessorKt<UserTypeKt> {
                override fun toDo(v: UserTypeKt) {
                    stringBuilder.append(v.toString())
                    stringBuilder.append(" ")
                }
            })
            outputArea.text = stringBuilder.toString()
        }

        fun onButtonClearClicked() {
            binaryTree.clear()
            updateOutputCallback.run()
        }

        fun onSerializeButtonClicked(builder: UserTypeKt?) {
            val fileChooser = FileChooser()
            fileChooser.title = "Select File"
            val extFilter = FileChooser.ExtensionFilter("Text Files", "*.txt")
            fileChooser.extensionFilters.add(extFilter)

            val userDirectory = System.getProperty("user.dir")
            val initialDirectory = File(userDirectory)
            fileChooser.initialDirectory = initialDirectory

            val selectedFile = fileChooser.showSaveDialog(Stage())

            if (selectedFile != null) {
                serialize.serialize(binaryTree, selectedFile.absolutePath, builder?.typeName())
            }
        }

        fun onDeserializeButtonClicked() {
            val fileChooser = FileChooser()
            fileChooser.title = "Select File"
            val extFilter = FileChooser.ExtensionFilter("Text Files", "*.txt")
            fileChooser.extensionFilters.add(extFilter)

            val userDirectory = System.getProperty("user.dir")
            val initialDirectory = File(userDirectory)
            fileChooser.initialDirectory = initialDirectory

            val selectedFile = fileChooser.showOpenDialog(Stage())

            if (selectedFile != null) {
                this.binaryTree = serialize.deserialize(selectedFile.absolutePath)!!
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(BinaryTreeGUIKt::class.java, *args)
        }
    }
}
