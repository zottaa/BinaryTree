import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class BinaryTreeGUI extends Application {

    private BinaryTree binaryTree = new BinaryTree.Base();
    private final UserFactory userFactory = new UserFactory();
    private UserType builder;
    private BinaryTreeController controller;

    private TextField addValueField;

    private TextField deleteValueField;

    private TextField atValueField;
    private TextArea outputArea;
    private ComboBox<String> operationComboBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Binary Tree GUI");

        controller = new BinaryTreeController(binaryTree, this::updateOutput);

        ArrayList<String> items = userFactory.getTypeNameList();



        addValueField = new TextField();

        deleteValueField = new TextField();

        deleteValueField.setPromptText("Enter index");
        atValueField = new TextField();

        atValueField.setPromptText("Enter index");

        operationComboBox = new ComboBox<>();
        operationComboBox.getItems().addAll(items);
        operationComboBox.setValue(items.get(0));
        builder = userFactory.getBuilderByName(operationComboBox.getValue());

        addValueField.setPromptText("Enter value: example " + builder.create().toString());

        operationComboBox.setOnAction(e -> {
            String oldValue = builder.typeName();
            builder = userFactory.getBuilderByName(operationComboBox.getValue());
            addValueField.setPromptText("Enter value: example " + builder.create().toString());
            if (!oldValue.equals(builder.typeName())) {
                binaryTree.clear();
                updateOutput();
            }
        });

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> controller.onAddButtonClicked(builder, addValueField.getText()));


        outputArea = new TextArea();
        outputArea.setEditable(false);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> controller.onDeleteButtonClicked(deleteValueField.getText()));

        Button balanceButton = new Button("Balance");
        balanceButton.setOnAction(e -> controller.onBalanceButtonClicked());

        Button serializeButton = new Button("Serialize");
        serializeButton.setOnAction(e -> {
            controller.onSerializeButtonClicked(builder);
        });

        Button deserializeButton = new Button("Deserialize");
        deserializeButton.setOnAction(e -> {
            controller.onDeserializeButtonClicked();
            this.binaryTree = controller.binaryTree;
            if (!binaryTree.isEmpty()) {
                this.builder = userFactory.getBuilderByName(binaryTree.at(0).typeName());
                operationComboBox.setValue(this.builder.typeName());
            }
            controller.updateOutput();
        });

        Button atButton = new Button("At");
        atButton.setOnAction(e -> controller.onAtButtonClicked(atValueField.getText(), outputArea));

        Button showButton = new Button("Show");
        showButton.setOnAction(e -> controller.updateOutput());

        Button traverseOrderButton = new Button("Traverse Order");
        traverseOrderButton.setOnAction(e -> controller.onTraverseOrderButtonClicked(outputArea));

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> controller.onButtonClearClicked());


        HBox addBox = new HBox(20);
        addBox.setAlignment(Pos.CENTER);
        addBox.getChildren().addAll(addValueField, addButton);

        HBox deleteBox = new HBox(20);
        deleteBox.setAlignment(Pos.CENTER);
        deleteBox.getChildren().addAll(deleteValueField, deleteButton);

        HBox atBox = new HBox(20);
        atBox.setAlignment(Pos.CENTER);
        atBox.getChildren().addAll(atValueField, atButton);

        VBox operationBox = new VBox(20);
        operationBox.setAlignment(Pos.CENTER);
        operationBox.getChildren().addAll(addBox, deleteBox, atBox);

        VBox secondaryOperationsBox = new VBox(20);
        secondaryOperationsBox.setAlignment(Pos.CENTER);
        secondaryOperationsBox.getChildren().addAll(serializeButton, deserializeButton, traverseOrderButton);

        VBox otherOperations = new VBox(20);
        otherOperations.setAlignment(Pos.CENTER);
        otherOperations.getChildren().addAll(showButton, balanceButton, clearButton);

        HBox mainBox = new HBox(20, operationBox, secondaryOperationsBox, otherOperations, operationComboBox);
        mainBox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(outputArea);

        borderPane.setBottom(mainBox);

        Scene scene = new Scene(borderPane, 900, 900);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        controller.updateOutput();
    }
    private void updateOutput() {
        outputArea.setText(binaryTree.toString());
    }

    private static class BinaryTreeController {
        private BinaryTree binaryTree;
        private final Runnable updateOutputCallback;
        private final Serialize serialize;

        public BinaryTreeController(BinaryTree binaryTree, Runnable updateOutputCallback) {
            this.binaryTree = binaryTree;
            this.updateOutputCallback = updateOutputCallback;
            this.serialize = new Serialize.Base();
        }

        public void onAddButtonClicked(UserType builder,String input) {
            if (!input.isEmpty()) {
                try {
                    UserType value = (UserType) builder.parseValue(input);
                    if (value != null) {
                        binaryTree.add(value);
                        updateOutputCallback.run();
                    }
                } catch (IllegalArgumentException e) {
                    showAlert("Invalid input. Please enter a valid value.");
                }
            }
        }

        public void onDeleteButtonClicked(String input) {
            if (!input.isEmpty()) {
                try {
                    int index = Integer.parseInt(input);
                    binaryTree.delete(index);
                    updateOutputCallback.run();
                } catch (IllegalArgumentException e) {
                    showAlert("Invalid input. Please enter a valid value.");
                }
            }
        }

        public void updateOutput() {
            updateOutputCallback.run();
        }

        private void showAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

        public void onBalanceButtonClicked() {
            binaryTree.balance();
            updateOutputCallback.run();
        }

        public void onAtButtonClicked(String input, TextArea outputArea) {
            if (!input.isEmpty()) {
                try {
                    int index = Integer.parseInt(input);
                    String value = binaryTree.at(index).toString();
                    outputArea.setText(value);
                } catch (IllegalArgumentException e) {
                    showAlert("Invalid input. Please enter a valid value.");
                }
            }
        }

        public void onTraverseOrderButtonClicked(TextArea outputArea) {
            StringBuilder stringBuilder = new StringBuilder();
            binaryTree.forEach(v -> {
                stringBuilder.append(v.toString());
                stringBuilder.append(" ");
            });
            outputArea.setText(String.valueOf(stringBuilder));
        }

        public void onButtonClearClicked() {
            binaryTree.clear();
            updateOutputCallback.run();
        }

        public void onSerializeButtonClicked(UserType builder) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("Text Files", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            String userDirectory = System.getProperty("user.dir");
            File initialDirectory = new File(userDirectory);
            fileChooser.setInitialDirectory(initialDirectory);

            File selectedFile = fileChooser.showSaveDialog(new Stage());

            if (selectedFile != null) {
                serialize.serialize(binaryTree, selectedFile.getAbsolutePath(), builder.typeName());
            }
        }

        public void onDeserializeButtonClicked() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("Text Files", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            String userDirectory = System.getProperty("user.dir");
            File initialDirectory = new File(userDirectory);
            fileChooser.setInitialDirectory(initialDirectory);

            File selectedFile = fileChooser.showOpenDialog(new Stage());

            if (selectedFile != null) {
                this.binaryTree = serialize.deserialize(selectedFile.getAbsolutePath());
            }
        }
    }
}
