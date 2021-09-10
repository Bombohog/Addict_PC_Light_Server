import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MyClient {

    public static void main(String[] args) throws IOException {

        try {

            Socket socket = new Socket("10.200.130.31", 8001);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
         
            // Scanner sc = new Scanner(System.in);
            while (true) {
                 
              String temp = inputStream.readByte(); 
              String humi = inputStream.readByte(); 
               
              System.out.println(temp)
              System.out.println(humi)
                  
            }
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
