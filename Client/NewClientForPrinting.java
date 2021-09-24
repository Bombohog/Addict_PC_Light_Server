import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
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

                    byte[] lenghtOfByteArray = (inputStream.readNBytes(1));
                    String StringVersionOfn = new String(lenghtOfByteArray, StandardCharsets.UTF_8);
                    int n1 = Integer.parseInt(StringVersionOfn);
                    lenghtOfByteArray = (inputStream.readNBytes(2));
                    StringVersionOfn = new String(lenghtOfByteArray, StandardCharsets.UTF_8);
                    int n2 = Integer.parseInt(StringVersionOfn);

                    byte[] bytearray = (inputStream.readNBytes(n2));


                    String output = decrypt(bytearray);

                    System.out.println(output);


                    //System.out.println( decrypt(inputStream.readNBytes(16)));

                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static String decrypt(byte[] encrypted) {
        try {
            System.out.println("length" + encrypted.length);
            String encryptedString = "testtesttesttest";
            String key = "testtesttesttest";
            byte[] tempbytearray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
            IvParameterSpec iv = new IvParameterSpec(tempbytearray);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
