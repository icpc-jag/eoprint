package print;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class DialogController {
    @FXML
    TextField urlField;
    @FXML
    TextField userField;
    @FXML
    PasswordField passField;
    @FXML
    TextField intervalField;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;

    private static void setAuthentication(final String username, final String password) {
        Authenticator.setDefault(new Authenticator() {
            protected java.net.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }

            ;
        });
    }

    @FXML
    public void okAction(ActionEvent actionEvent) {
        Downloader.cancelAutoPrintTask();
        if (!userField.getText().isEmpty()) {
            setAuthentication(userField.getText(), passField.getText());
        }
        Downloader.submitAutoPrintTask(urlField.getText(), Integer.parseInt(intervalField.getText()));
        okButton.getScene().getWindow().hide();
    }

    @FXML
    public void cancelAction(ActionEvent actionEvent) {
        cancelButton.getScene().getWindow().hide();
    }
}
