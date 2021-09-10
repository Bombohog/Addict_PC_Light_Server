import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MyClient {

    public static void main(String[] args) throws IOException {

        try {

            Socket socket = new Socket("10.200.130.31", 8001);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            //DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // Scanner sc = new Scanner(System.in);
            while (true) {
                    /*outputStream.writeUTF(sc.nextLine());
                    outputStream.flush();*/

                //System.out.println(inputStream.readByte());
                ArrayList<Byte> data = new ArrayList<>();
                do {
                    data.add(inputStream.readByte());
                } while (inputStream.available()==0);

                System.out.println("\nNew data incoming");
                if (data.size() % 2 == 0) {System.out.println("Humidity: " + data.get(data.size() - 1));} else {System.out.println("Temperature: " + data.get(data.size() - 1));}

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
