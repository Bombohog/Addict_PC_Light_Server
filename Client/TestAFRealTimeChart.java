import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TestAFRealTimeChart extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time/s");
        xAxis.setAnimated(false);
        yAxis.setLabel("Temperatur");
        yAxis.setAnimated(false);

        final LineChart<String,Number> linechart = new LineChart<String, Number>(xAxis,yAxis);
        linechart.setTitle("Testing OF Realtime grath");
        linechart.setAnimated(false);


        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
        series.setName("Data For ");

        linechart.getData().add(series);

        Scene scene = new Scene(linechart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {

            Integer random = ThreadLocalRandom.current().nextInt(10);

            Platform.runLater(() -> {

                Date now = new Date();
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), random));

            });
        },0, 2, TimeUnit.SECONDS);
    }
}
