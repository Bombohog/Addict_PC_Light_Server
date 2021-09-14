import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.border.Border;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestAFRealTimeChart extends Application {
    static String numberForTemp = "";
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();

    final CategoryAxis xAxisForChart2 = new CategoryAxis();
    final NumberAxis yAxisForChart2 = new NumberAxis();

    final LineChart<String, Number> linechart = new LineChart<String, Number>(xAxis, yAxis);
    final LineChart<String, Number> linechart2 = new LineChart<String, Number>(xAxisForChart2, yAxisForChart2);
    XYChart.Series<String, Number> humidityDataSet = new XYChart.Series<String, Number>();
    XYChart.Series<String, Number> tempeturDataSet = new XYChart.Series<String, Number>();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");



    static boolean flippy2 = true;
    @Override
    public void start(Stage primaryStage) {
        new Thread(() -> {
            System.out.println("Thread is running " + Thread.currentThread().getName());
            var flippy = true;
            String textVariable;
            try {
                Socket socket = new Socket("10.0.0.226", 8001);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                while (true) {
                    byte[] lenghtbytearray = (inputStream.readNBytes(1));
                    String StringVersionOfn = new String(lenghtbytearray, StandardCharsets.UTF_8);
                    int n = Integer.parseInt(StringVersionOfn);
                    byte[] bytearray = (inputStream.readNBytes(n));
                    String string = new String(bytearray, StandardCharsets.UTF_8);
                    textVariable = flippy ? "Humidity " : "Tempetur ";
                    flippy = !flippy;
                    numberForTemp = string;
                    System.out.println(textVariable + string);
                    Platform.runLater(() ->{
                        Date now = new Date();
                        double temp = Double.parseDouble(string);

                        if (flippy2) {
                            humidityDataSet.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), temp));
                            flippy2=false;
                        }else{
                            tempeturDataSet.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), temp));
                            flippy2=true;
                        }


                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        System.out.println("Thread for UI" + Thread.currentThread().getName());
        setupUI();

        linechart.getData().add(humidityDataSet);
        linechart2.getData().add(tempeturDataSet);
        FlowPane pane = new FlowPane(linechart,linechart2);


        Scene scene = new Scene(pane, 900, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupUI() {
        xAxis.setLabel("Time/s");
        xAxis.setAnimated(true);
        yAxis.setLabel("Humidity/%");
        yAxis.setAnimated(true);
        yAxis.setForceZeroInRange(false);
        xAxisForChart2.setLabel("Time/s");
        xAxisForChart2.setAnimated(true);
        yAxisForChart2.setLabel("Tempetur/c");
        yAxisForChart2.setAnimated(true);
        yAxisForChart2.setForceZeroInRange(false);
        linechart.setTitle("Humidity");
        linechart.setAnimated(true);
        linechart.setPrefSize(400,700);
        humidityDataSet.setName("Humidity");
        tempeturDataSet.setName("Tempetur");
        linechart2.setTitle("Tempetur");
        linechart2.setAnimated(true);
        linechart2.setPrefSize(400,700);



    }


    public static void main(String[] args) {
        launch(args);

    }
}