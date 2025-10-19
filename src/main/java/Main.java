import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
  private final TextField inputField = new TextField();
  private final Label resultLabel = new Label();
  private final ComboBox<String> conversionType = new ComboBox<>();
  private double resultValue;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    inputField.setPromptText("Enter value");

    conversionType.getItems().addAll(
            "Celsius → Fahrenheit",
            "Fahrenheit → Celsius",
            "Kelvin → Celsius",
            "Celsius → Kelvin"
    );
    conversionType.setValue("Celsius → Fahrenheit");

    Button convertButton = new Button("Convert");
    convertButton.setOnAction(e -> convertTemperature());

    Button saveButton = new Button("Save to DB");
    saveButton.setOnAction(e -> {
      try {
        double input = Double.parseDouble(inputField.getText());
        Database.saveTemperature(input, resultValue, resultLabel);
      } catch (NumberFormatException ex) {
        resultLabel.setText("Invalid input!");
      }
    });

    VBox root = new VBox(10, inputField, conversionType, convertButton, resultLabel, saveButton);
    root.setStyle("-fx-padding: 15; -fx-alignment: center;");

    Scene scene = new Scene(root, 320, 220);
    stage.setTitle("Temperature Converter");
    stage.setScene(scene);
    stage.show();
  }

  private void convertTemperature() {
    try {
      double input = Double.parseDouble(inputField.getText());
      String type = conversionType.getValue();
      String resultText;

      switch (type) {
        case "Celsius → Fahrenheit":
          resultValue = (input * 9 / 5) + 32;
          resultText = String.format("Fahrenheit: %.2f°F", resultValue);
          break;

        case "Fahrenheit → Celsius":
          resultValue = (input - 32) * 5 / 9;
          resultText = String.format("Celsius: %.2f°C", resultValue);
          break;

        case "Kelvin → Celsius":
          resultValue = input - 273.15;
          resultText = String.format("Celsius: %.2f°C", resultValue);
          break;

        case "Celsius → Kelvin":
          resultValue = input + 273.15;
          resultText = String.format("Kelvin: %.2fK", resultValue);
          break;

        default:
          resultText = "Unknown conversion!";
      }

      resultLabel.setText(resultText);

    } catch (NumberFormatException ex) {
      resultLabel.setText("Invalid input!");
    }
  }
}