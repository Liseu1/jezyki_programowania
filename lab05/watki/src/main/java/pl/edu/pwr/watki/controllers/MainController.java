package pl.edu.pwr.watki.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pl.edu.pwr.watki.model.*;
import pl.edu.pwr.watki.view.Cell;

public class MainController implements StationListener {

    @FXML
    private HBox entryRamp;
    @FXML
    private HBox exitRamp;
    @FXML
    private HBox feeder;

    public void initialize (int entryRampAmount, int exitRampAmount, int feederAmount) {

        setupRamp(entryRamp, entryRampAmount, false);
        setupRamp(exitRamp, exitRampAmount, false);
        setupRamp(feeder, exitRampAmount + 2 * (feederAmount - 1), false);

        Station station = new Station(entryRampAmount, exitRampAmount, feederAmount);
        station.setListener(this);

        Thread creator = new Thread(new CreatorThread(station));
        creator.setDaemon(true);
        creator.start();

        Thread anihilator = new Thread(new AnihilatorThread(station));
        anihilator.setDaemon(true);
        anihilator.start();

        for (int i = 0; i < feederAmount; i++) {
            FeederThread feederLogic = new FeederThread(station, i * 2);
            Thread feederThread = new Thread(feederLogic);
            feederThread.setDaemon(true);
            feederThread.start();
        }


    }


    public void setupRamp(HBox ramp, int count, boolean underlined) {
        for(int i = 0; i < count; i++) {
            Label label = new Label("");

            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(50);
            rectangle.setHeight(50);
            rectangle.setFill(Color.rgb(255, 255, 255));
            rectangle.setStroke(Color.rgb(0, 0, 0));

            Rectangle underline = new Rectangle();
            underline.setWidth(44);
            underline.setHeight(5);
            underline.setVisible(underlined);

            ramp.getChildren().add(new Cell(rectangle, label, underline));
        }
    }

    public void changeField(Ramp ramp, int index, Cargo cargo, boolean underline) {
        HBox selectedRamp = switch (ramp) {
            case Ramp.ENTRY_RAMP -> entryRamp;
            case Ramp.EXIT_RAMP -> exitRamp;
            case Ramp.FEEDER -> feeder;
        };

        if(index >= selectedRamp.getChildren().size() || index < 0) {
            throw new IndexOutOfBoundsException("Tried to modify out of bounds index of a ramp");
        }
        Cell cell = (Cell) selectedRamp.getChildren().get(index);
        String labelText;

        if(cargo != null) {
            labelText = Integer.toString(cargo.destination());

        } else {
            labelText = "";
        }

        Platform.runLater(() -> {
            cell.setLabel(labelText);
            cell.setUnderline(underline);
        });
    }
}
