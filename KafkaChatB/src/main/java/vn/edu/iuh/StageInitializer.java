package vn.edu.iuh;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import vn.edu.iuh.KafkaChatApplication.StageReadyEvent;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {
    @Value("classpath:/chat-view.fxml")
    private Resource chartResource;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(chartResource.getURL());
            fxmlLoader.setControllerFactory(KafkaChatApplication.getApplicationContext()::getBean);
            Parent parent = fxmlLoader.load();
            Stage stage = event.getStage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Kafka chat B");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
