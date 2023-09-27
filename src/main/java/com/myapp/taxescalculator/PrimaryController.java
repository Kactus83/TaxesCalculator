package com.myapp.taxescalculator;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class PrimaryController {

    @FXML
    private TextField rateTextField;

    // Cette variable stockera le Timer qui est utilisé pour retarder l'alerte.
    private Timer delayTimer = null;


    @FXML
    public void initialize() {
        rateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (delayTimer != null) {
                delayTimer.cancel();
            }

            delayTimer = new Timer();
            delayTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {  
                        double rate;
                        try {
                            rate = Double.parseDouble(newValue);
                            validateRateAndShowAlert(rate);
                        } catch (NumberFormatException e) {
                            showAlert("Erreur d'entrée", "Veuillez entrer un taux valide.");
                        }
                    });
                }
            }, 2000);
        });
    }

    private void validateRateAndShowAlert(double rate) {
        if (rate < 0) {
            showAlert("Erreur de taux", "Le taux ne peut pas être négatif !");
        } else if (rate > 100) {
            showAlert("Erreur de taux", "Le taux ne peut pas dépasser 100% !");
        } else {
            showAlert("Modification du taux", "Le taux a été modifié en : " + rate + "%");
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
