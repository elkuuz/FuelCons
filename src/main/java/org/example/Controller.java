package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.db.CalculationService;
import org.example.db.LocalizationService;


public class Controller {

    private LocalizationService locService = new LocalizationService();
    private CalculationService calcService = new CalculationService();
    private String currentLang = "EN"; // Default language

    @FXML
    private Label lblDistance;

    @FXML
    private Label lblConsumption;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblResult;

    @FXML
    private TextField txtDistance;

    @FXML
    private TextField txtConsumption;

    @FXML
    private TextField txtPrice;

    @FXML
    private Button btnCalculate;

    @FXML
    private Label lblTitle;

    @FXML
    private MenuButton mbLanguage;

    @FXML
    private MenuItem menuEN;

    @FXML
    private MenuItem menuFR;

    @FXML
    private MenuItem menuJP;

    @FXML
    private MenuItem menuIR;

    @FXML
    private VBox rootContainer;



    @FXML
    private void initialize() {
        setLanguage("EN"); // Default language
    }

    @FXML
    private void handleLanguageChange(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();

        if (selectedItem == menuEN) {
            setLanguage("EN");
        } else if (selectedItem == menuFR) {
            setLanguage("FR");
        } else if (selectedItem == menuJP) {
            setLanguage("JP");
        } else if (selectedItem == menuIR) {
            setLanguage("IR");
        }
    }

    private void setLanguage(String langCode) {
        this.currentLang = langCode;
        locService.loadStrings(langCode); // Fetch from DB
        applyLayoutDirection(langCode);

        // Update UI labels from DB
        if (lblTitle != null) {
            lblTitle.setText(locService.getString("title"));
        }
        if (lblDistance != null) {
            lblDistance.setText(locService.getString("lbl_distance"));
        }
        if (lblConsumption != null) {
            lblConsumption.setText(locService.getString("lbl_consumption"));
        }
        if (lblPrice != null) {
            lblPrice.setText(locService.getString("lbl_price"));
        }
        if (btnCalculate != null) {
            btnCalculate.setText(locService.getString("btn_calculate"));
        }

        // Result and Menu text
        if (lblResult != null) {
            lblResult.setText(locService.getString("res_total_fuel"));
        }
        if (mbLanguage != null) {
            mbLanguage.setText(langCode);
        }
    }

    private void applyLayoutDirection(String langCode) {
        if (rootContainer != null) {
            rootContainer.setNodeOrientation(isRtlLanguage(langCode)
                    ? NodeOrientation.RIGHT_TO_LEFT
                    : NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    private boolean isRtlLanguage(String langCode) {
        return "IR".equalsIgnoreCase(langCode);
    }

    @FXML
    private void handleCalculateAction(ActionEvent event) {
        try {
            double distance = Double.parseDouble(txtDistance.getText());
            double consumption = Double.parseDouble(txtConsumption.getText());
            double price = Double.parseDouble(txtPrice.getText());

            // Validate positive numbers
            if (distance < 0 || consumption < 0 || price < 0) {
                lblResult.setText(locService.getString("msg_error_input"));
                return;
            }

            // Calculation logic
            double totalFuel = (consumption / 100) * distance;
            double totalCost = totalFuel * price;

            // Display results
            String result = String.format("%s %.2f | %s %.2f",
                    locService.getString("res_total_fuel"), totalFuel,
                    locService.getString("res_total_cost"), totalCost);
            lblResult.setText(result);

            // SAVE TO DATABASE
            calcService.saveCalculation(distance, consumption, price, totalFuel, totalCost, currentLang);

        } catch (NumberFormatException e) {
            lblResult.setText(locService.getString("msg_error_input"));
        }
    }
}
