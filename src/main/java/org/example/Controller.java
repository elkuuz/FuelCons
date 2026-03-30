package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller {


    private ResourceBundle rb;

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
    private void initialize() {
        setLanguage(Locale.getDefault());
    }


    @FXML
    private void handleLanguageChange(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();

        if (selectedItem == menuEN) {
            setLanguage(Locale.of("en", "US"));
        } else if (selectedItem == menuFR) {
            setLanguage(Locale.of("fr", "FR"));
        } else if (selectedItem == menuJP) {
            setLanguage(Locale.of("ja", "JP"));
        } else if (selectedItem == menuIR) {
            setLanguage(Locale.of("fa", "IR"));
        }
    }

    private void setLanguage(Locale locale) {
        rb = ResourceBundle.getBundle("messages", locale);

        lblDistance.setText(rb.getString("label.distance"));
        lblConsumption.setText(rb.getString("label.fuel"));
        lblPrice.setText(rb.getString("price.label"));
        btnCalculate.setText(rb.getString("button.calculate"));
        lblTitle.setText(rb.getString("app.title"));
        lblResult.setText(rb.getString("result.label"));

        mbLanguage.setText(rb.getString("language.menu.title"));
        menuEN.setText(rb.getString("language.en"));
        menuFR.setText(rb.getString("language.fr"));
        menuJP.setText(rb.getString("language.ja"));
        menuIR.setText(rb.getString("language.fa"));
    }

    @FXML
    private void handleCalculateAction(ActionEvent event) {
        try {
            double distance = Double.parseDouble(txtDistance.getText());
            double consumption = Double.parseDouble(txtConsumption.getText());
            double price = Double.parseDouble(txtPrice.getText());

            if (distance < 0 || consumption < 0 || price < 0) {
                lblResult.setText(rb.getString("error.negative.input"));
                return;
            }

            double totalFuel = (consumption/100) * distance;
            double totalCost = totalFuel * price;

            lblResult.setText(MessageFormat.format(rb.getString("result.label"), totalCost));
        } catch (NumberFormatException e) {
            lblResult.setText(rb.getString("error.invalidInput"));
        }
    }
}
