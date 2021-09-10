import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientTest {

    public static void main(String[] args) throws IOException {

        try {

            Socket socket = new Socket("10.200.130.31", 8001);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
         
            // Scanner sc = new Scanner(System.in);
            while (true) {
                 
              byte temp = inputStream.readByte();
              byte humi = inputStream.readByte();
              
              String tempUTF = new String(temp, StandardCharsets.US_ASCII);
              String humiUTF = new String(humi, StandardCharsets.US_ASCII);
               
              System.out.println(tempUTF);
              System.out.println(humiUTF);
                  
            }
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
