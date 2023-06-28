package vn.edu.iuh;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockUIApplication {
    public static void main(String[] args) {
        Application.launch(KafkaChatApplication.class, args);
    }
}
