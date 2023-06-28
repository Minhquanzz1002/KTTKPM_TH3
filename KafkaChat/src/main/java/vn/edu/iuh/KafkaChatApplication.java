package vn.edu.iuh;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import vn.edu.iuh.client.KafkaClient;

public class KafkaChatApplication extends Application {
    private static ConfigurableApplicationContext applicationContext;

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(StockUIApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    static class StageReadyEvent extends ApplicationEvent {

        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return (Stage) getSource();
        }
    }
}
