package print;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    final Parent dialog;
    private final Timer loggingTimer = new Timer(true);
    @FXML
    ListView<String> listView;
    @FXML
    Button suspendButton;

    public Controller() throws IOException {
        loggingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                final String s = Downloader.poll();
                if (s != null)
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            listView.getItems().add(s);
                        }
                    });
            }
        }, 0L, 100L);
        dialog = FXMLLoader.load(getClass().getResource("dialog.fxml"));
    }

    @FXML
    protected void updateURL() {
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setResizable(false);
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setScene(new Scene(dialog));
        dialogStage.showAndWait();
    }

    @FXML
    protected void resetNameTableFile() {
        Downloader.clear();
    }
}
