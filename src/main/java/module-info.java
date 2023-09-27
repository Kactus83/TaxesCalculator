module com.myapp.taxescalculator {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.myapp.taxescalculator to javafx.fxml;
    exports com.myapp.taxescalculator;
}
