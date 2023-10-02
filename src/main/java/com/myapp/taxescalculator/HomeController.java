/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.taxescalculator;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void openTaxesCalculator() throws IOException {
        App.setRoot("primary");
    }
}

