import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
public class ClientTest {
    public static void main(String[] args) {
        var flippy = true;
        String textVariable;
        try {
            Socket socket = new Socket("10.200.130.31", 8001);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                byte[] lenghtbytearray = (inputStream.readNBytes(1));
                String StringVersionOfn = new String(lenghtbytearray,StandardCharsets.UTF_8);
                int n = Integer.parseInt(StringVersionOfn);
                byte[] bytearray = (inputStream.readNBytes(n));
                String string = new String(bytearray,StandardCharsets.UTF_8);
                textVariable = flippy?"Humidity ":"Tempetur ";
                flippy = !flippy;
                System.out.println(textVariable + string);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
