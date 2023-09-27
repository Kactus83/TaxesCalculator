package com.myapp.taxescalculator;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Label;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class PrimaryController {

    @FXML
    private TextField rateTextField;
    
    @FXML
    private TextField initialValueTextField;  
    
    @FXML
    private Label detailsLabel;

    @FXML
    private Label finalAmountLabel;

    @FXML
    private ToggleButton modeToggle;

    private Timer rateDelayTimer = null;
    private Timer initialValueDelayTimer = null;

    @FXML
    public void initialize() {
        setupRateTextFieldListener();
        setupInitialValueTextFieldListener();
        modeToggle.selectedProperty().addListener((observable, oldValue, newValue) -> updateAmount());
    }

    private void setupRateTextFieldListener() {
        rateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            startDelayTimer(newValue, "rate");
        });
    }

    private void setupInitialValueTextFieldListener() {
        initialValueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            startDelayTimer(newValue, "initialValue");
        });
    }

    private void startDelayTimer(String newValue, String type) {
        Timer currentTimer = type.equals("rate") ? rateDelayTimer : initialValueDelayTimer;

        if (currentTimer != null) {
            currentTimer.cancel();
        }

        Timer newTimer = new Timer();
        newTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (type.equals("rate")) {
                    processRateChange(newValue);
                } else {
                    processInitialValueChange(newValue);
                }
            }
        }, 2000);

        if (type.equals("rate")) {
            rateDelayTimer = newTimer;
        } else {
            initialValueDelayTimer = newTimer;
        }
    }

    private void processRateChange(String rateValue) {
        Platform.runLater(() -> {
            try {
                double rate = Double.parseDouble(rateValue);
                validateRateAndShowAlert(rate);
                updateAmount();
            } catch (NumberFormatException e) {
                showAlert("Erreur d'entrée", "Veuillez entrer un taux valide.");
            }
        });
    }

    private void processInitialValueChange(String initialValue) {
        Platform.runLater(() -> {
            try {
                double value = Double.parseDouble(initialValue);
                validateInitialValueAndShowAlert(value);
                updateAmount();
            } catch (NumberFormatException e) {
                showAlert("Erreur d'entrée", "Veuillez entrer une valeur initiale valide.");
            }
        });
    }

    private void validateRateAndShowAlert(double rate) {
        if (rate < 0) {
            showAlert("Erreur de taux", "Le taux ne peut pas être négatif !");
        } else if (rate > 100) {
            showAlert("Erreur de taux", "Le taux ne peut pas dépasser 100% !");
        }
    }

    private void validateInitialValueAndShowAlert(double value) {
        if (value < 0) {
            showAlert("Erreur de valeur", "La valeur initiale ne peut pas être négative !");
        }
    }

    private void updateAmount() {
        try {
            double rate = Double.parseDouble(rateTextField.getText()) / 100; 
            double initialValue = Double.parseDouble(initialValueTextField.getText());
            double amountChange = initialValue * rate;
            double finalAmount;

            if (modeToggle.isSelected()) {
                finalAmount = initialValue - amountChange;
                detailsLabel.setText(String.format("Montant du discount: %.2f", amountChange));
            } else {
                finalAmount = initialValue + amountChange;
                detailsLabel.setText(String.format("Montant de la taxe: %.2f", amountChange));
            }
            finalAmountLabel.setText(String.format("Montant final: %.2f", finalAmount));

        } catch (NumberFormatException e) {
            // It's possible to get here if one of the fields is not yet filled or has an illegal value.
            // In that case, we don't update the result.
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
