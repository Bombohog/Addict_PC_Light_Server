import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClientForSensorDataGraph extends Application {
    final CategoryAxis xAxisForHumidity = new CategoryAxis();
    final NumberAxis yAxisForHumidity = new NumberAxis();
    final CategoryAxis xAxisForTemperature = new CategoryAxis();
    final NumberAxis yAxisForTemperature = new NumberAxis();
    ArrayList<Double> listOfHumidityValues = new ArrayList<>();
    ArrayList<Double> listOfTemperatureValues = new ArrayList<>();
    final LineChart<String, Number> linechartForHumity = new LineChart<>(xAxisForHumidity, yAxisForHumidity);
    final LineChart<String, Number> linechartForTempeture = new LineChart<>(xAxisForTemperature, yAxisForTemperature);
    XYChart.Series<String, Number> humidityDataSet = new XYChart.Series<>();
    XYChart.Series<String, Number> tempeturDataSet = new XYChart.Series<>();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    final int AMOUNTOFNOTES = 20;
    static boolean tempeturOrNot = false;

    Label minTemperatureValue = new Label();
    Label maxTemperatureValue = new Label();
    Label avgTemperatureValue = new Label();
    Label minHumidityValue = new Label();
    Label maxHumidityValue = new Label();
    Label avgHumidityValue = new Label();

    double maxForHumidity=0;
    double minForHumidity = 1000;
    double avgForHumidity;
    double maxForTemperature=0;
    double minForTemperature = 1000;
    double avgForTemperature;

    @Override
    public void start(Stage primaryStage) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("10.200.130.31", 8001);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                while (true) {
                    byte[] lenghtOfByteArray = (inputStream.readNBytes(1));
                    String StringVersionOfn = new String(lenghtOfByteArray, StandardCharsets.UTF_8);
                    int n = Integer.parseInt(StringVersionOfn);
                    byte[] bytearray = (inputStream.readNBytes(n));
                    String sensorData = new String(bytearray, StandardCharsets.UTF_8);
                    tempeturOrNot = !tempeturOrNot;
                    
                    Platform.runLater(() ->{
                        Date now = new Date();
                        double value = Double.parseDouble(sensorData);
                        if (humidityDataSet.getData().size() > AMOUNTOFNOTES)
                            humidityDataSet.getData().remove(0);
                        if (tempeturDataSet.getData().size() > AMOUNTOFNOTES)
                            tempeturDataSet.getData().remove(0);
                        if (tempeturOrNot) {
                            humidityDataSet.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), value));
                            listOfHumidityValues.add(value);
                        }else{
                            tempeturDataSet.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), value));
                            listOfTemperatureValues.add(value);
                        }
                        if (!tempeturOrNot){
                            monotoniforhold();
                            updateLabels();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        setupUI();

        Label minTemperature = new Label("Lavest temperatur ");
        Label maxTemperature = new Label("Højeste temperatur ");
        Label avgTemperature = new Label("Gennemsnitlig temperatur ");
        Label minHumidity = new Label("Lavest Luftfugtighed");
        Label maxHumidity = new Label("Højeste Luftfugtighed");
        Label avgHumidity = new Label("Gennemsnitlig Luftfugtighed");

        avgHumidity.setFont(Font.font("calibri", FontWeight.NORMAL, 15));
        maxHumidity.setFont(Font.font("calibri", FontWeight.NORMAL, 15));
        minHumidity.setFont(Font.font("calibri", FontWeight.NORMAL, 15));
        avgTemperature.setFont(Font.font("calibri", FontWeight.NORMAL, 15));
        minTemperature.setFont(Font.font("calibri", FontWeight.NORMAL, 15));
        maxTemperature.setFont(Font.font("calibri", FontWeight.NORMAL, 15));


        VBox labelsForTemperature = new VBox(minTemperature,maxTemperature,avgTemperature);
        VBox labelsForHumidity = new VBox(minHumidity,maxHumidity,avgHumidity);
        VBox valuesForTemperature = new VBox(minTemperatureValue,maxTemperatureValue,avgTemperatureValue);
        VBox valuesForHumidity = new VBox(minHumidityValue,maxHumidityValue,avgHumidityValue);
        HBox labelAndValuesTemperature = new HBox(labelsForTemperature, valuesForTemperature);
        HBox labelAndValuesHumidity = new HBox(labelsForHumidity, valuesForHumidity);
        labelAndValuesHumidity.setAlignment(Pos.CENTER);
        labelAndValuesTemperature.setAlignment(Pos.CENTER);
        VBox graphAndLabelContainerHumidity = new VBox(linechartForHumity,labelAndValuesHumidity);
        VBox graphAndLabelContainerTemperature = new VBox(linechartForTempeture,labelAndValuesTemperature );
        HBox hbox = new HBox(graphAndLabelContainerHumidity,graphAndLabelContainerTemperature);

        Scene scene = new Scene(hbox, 900, 500);
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
        linechartForHumity.setPrefSize(400,400);
        humidityDataSet.setName("Humidity");
        tempeturDataSet.setName("Tempetur");
        linechartForTempeture.setTitle("Tempetur");
        linechartForTempeture.setAnimated(true);
        linechartForTempeture.setPrefSize(400,400);
        linechartForHumity.getData().add(humidityDataSet);
        linechartForTempeture.getData().add(tempeturDataSet);

        maxTemperatureValue.setFont(Font.font("calibri", FontWeight.BOLD, 15 ));
        minTemperatureValue.setFont(Font.font("calibri", FontWeight.BOLD, 15 ));
        avgTemperatureValue.setFont(Font.font("calibri", FontWeight.BOLD, 15 ));
        minHumidityValue.setFont(Font.font("calibri", FontWeight.BOLD, 15 ));
        maxHumidityValue.setFont(Font.font("calibri", FontWeight.BOLD, 15 ));
        avgHumidityValue.setFont(Font.font("calibri", FontWeight.BOLD, 15 ));

        minHumidityValue.setPadding(new Insets(0,0,0,20));
        maxHumidityValue.setPadding(new Insets(0,0,0,20));
        avgHumidityValue.setPadding(new Insets(0,0,0,20));
        minTemperatureValue.setPadding(new Insets(0,0,0,20));
        maxTemperatureValue.setPadding(new Insets(0,0,0,20));
        avgTemperatureValue.setPadding(new Insets(0,0,0,20));
    }

    void updateLabels(){
        minHumidityValue.setText(String.format("%.2f",minForHumidity));
        maxHumidityValue.setText(String.format("%.2f",maxForHumidity));
        avgHumidityValue.setText(String.format("%.2f",avgForHumidity));
        minTemperatureValue.setText(String.format("%.2f",minForTemperature));
        maxTemperatureValue.setText(String.format("%.2f",maxForTemperature));
        avgTemperatureValue.setText(String.format("%.2f", avgForTemperature));
    }

    void monotoniforhold(){
        if (minForHumidity > listOfHumidityValues.get(listOfHumidityValues.size()-1)){
            minForHumidity = listOfHumidityValues.get(listOfHumidityValues.size()-1);
        }
        if (maxForHumidity < listOfHumidityValues.get(listOfHumidityValues.size()-1)){
            maxForHumidity = listOfHumidityValues.get(listOfHumidityValues.size()-1);

        }if (minForTemperature > listOfTemperatureValues.get(listOfTemperatureValues.size()-1)){
            minForTemperature = listOfTemperatureValues.get(listOfTemperatureValues.size()-1);

        }if (maxForTemperature < listOfTemperatureValues.get(listOfTemperatureValues.size()-1)){
            maxForTemperature = listOfTemperatureValues.get(listOfTemperatureValues.size()-1);
        }

        double avgForHumidityBeforeDevide = 0.0;
        double avgForTemperatureBeforeDevide = 0.0;

        for (int i = 0; i <listOfHumidityValues.size() ; i++) {
            avgForHumidityBeforeDevide += listOfHumidityValues.get(i);
        }
        for (int i = 0; i <listOfTemperatureValues.size() ; i++) {
            avgForTemperatureBeforeDevide += listOfTemperatureValues.get(i);
        }

        avgForHumidity =avgForHumidityBeforeDevide/listOfHumidityValues.size();
        avgForTemperature = avgForTemperatureBeforeDevide/listOfTemperatureValues.size();
    }

    public static void main(String[] args) {
        launch(args);
    }
}