package vn.edu.iuh.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Controller;
import vn.edu.iuh.client.KafkaClient;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ChatController implements Initializable {

    @FXML
    private Button btnSend;

    @FXML
    private TextArea txtMain;

    @FXML
    private TextArea txtMsg;

    private KafkaClient kafka = KafkaClient.getInstance();

    @FXML
    protected void handleSendMessage() {
        kafka.sendMessage(txtMsg.getText());
        txtMsg.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        KafkaClient kafka = KafkaClient.getInstance();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ConsumerRecords<String, String> records = kafka.pollRecords();
                    for (ConsumerRecord<String, String> record : records) {
                        if (record.key().trim().equals("chat-a")) {
                            txtMain.appendText("you > " + record.value() + "\n");
                        } else {
                            txtMain.appendText(record.key() + " > " + record.value() + "\n");
                        }
                    }
                    kafka.commitAsync();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
    }
}
