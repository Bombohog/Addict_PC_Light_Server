import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientForSensorDataGraph extends Application {
    final CategoryAxis xAxisForHumidity = new CategoryAxis();
    final NumberAxis yAxisForHumidity = new NumberAxis();

    final CategoryAxis xAxisForTemperature = new CategoryAxis();
    final NumberAxis yAxisForTemperature = new NumberAxis();

    final LineChart<String, Number> linechartForHumity = new LineChart<String, Number>(xAxisForHumidity, yAxisForHumidity);
    final LineChart<String, Number> linechartForTempeture = new LineChart<String, Number>(xAxisForTemperature, yAxisForTemperature);
    XYChart.Series<String, Number> humidityDataSet = new XYChart.Series<String, Number>();
    XYChart.Series<String, Number> tempeturDataSet = new XYChart.Series<String, Number>();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    final int AMOUNTOFNOTES = 20;
    static boolean tempeturOrNot = false;
    @Override
    public void start(Stage primaryStage) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("10.0.0.226", 8001);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                while (true) {
                    byte[] lenghtbytearray = (inputStream.readNBytes(1));
                    String StringVersionOfn = new String(lenghtbytearray, StandardCharsets.UTF_8);
                    int n = Integer.parseInt(StringVersionOfn);
                    byte[] bytearray = (inputStream.readNBytes(n));
                    String sensorData = new String(bytearray, StandardCharsets.UTF_8);
                    tempeturOrNot = !tempeturOrNot;
                    
                    Platform.runLater(() ->{
                        Date now = new Date();
                        double temp = Double.parseDouble(sensorData);
                        if (humidityDataSet.getData().size() > AMOUNTOFNOTES)
                            humidityDataSet.getData().remove(0);
                        if (tempeturDataSet.getData().size() > AMOUNTOFNOTES)
                            tempeturDataSet.getData().remove(0);
                        if (tempeturOrNot) {
                            humidityDataSet.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), temp));
                        }else{
                            tempeturDataSet.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), temp));
                        }


                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        setupUI();

        FlowPane pane = new FlowPane(linechartForHumity, linechartForTempeture);


        Scene scene = new Scene(pane, 900, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupUI() {
        xAxisForHumidity.setLabel("Time/s");
        xAxisForHumidity.setAnimated(true);
        yAxisForHumidity.setLabel("Humidity/%");
        yAxisForHumidity.setAnimated(true);
        yAxisForHumidity.setForceZeroInRange(false);
        xAxisForTemperature.setLabel("Time/s");
        xAxisForTemperature.setAnimated(true);
        yAxisForTemperature.setLabel("Tempetur/c");
        yAxisForTemperature.setAnimated(true);
        yAxisForTemperature.setForceZeroInRange(false);
        linechartForHumity.setTitle("Humidity");
        linechartForHumity.setAnimated(true);
        linechartForHumity.setPrefSize(400,700);
        humidityDataSet.setName("Humidity");
        tempeturDataSet.setName("Tempetur");
        linechartForTempeture.setTitle("Tempetur");
        linechartForTempeture.setAnimated(true);
        linechartForTempeture.setPrefSize(400,700);
        linechartForHumity.getData().add(humidityDataSet);
        linechartForTempeture.getData().add(tempeturDataSet);



    }


    public static void main(String[] args) {
        launch(args);

    }
}