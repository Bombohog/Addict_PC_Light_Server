import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

public class NewClientForPrinting {
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

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("10.0.0.165", 8001);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                while (true) {

                   String encryptedString = new String(inputStream.readNBytes(16),StandardCharsets.UTF_8);

                    System.out.println(encryptedString);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    static int decrypt(String stringToDecrypt){

        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            byte[] key;
            String mykey = "testtesttesttest";
            key = mykey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
            return Integer.parseInt(new String(cipher.doFinal(Base64.getDecoder().decode(stringToDecrypt))));
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return 1;
    }
}


