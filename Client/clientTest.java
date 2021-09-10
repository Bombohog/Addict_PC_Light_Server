import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class clientTest {

    public static void main(String[] args) throws IOException {

        try {

            Socket socket = new Socket("10.0.0.226", 8001);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
         
            // Scanner sc = new Scanner(System.in);
            while (true) {
                 
              byte temp = inputStream.readByte();
              byte humi = inputStream.readByte();
               
              System.out.println("Temp " + temp);
              System.out.println("Humi " + humi);
                  
            }
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
