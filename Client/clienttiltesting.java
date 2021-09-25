import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class clienttiltesting {
    static String key = "sixteencharacter";
   static  String initVector = "jvHJ1XFt0IXBrxxx";

    static Socket socket;
    static DataInputStream inputStream;
    static DataOutputStream outputStream;

    static {
        try {
            socket = new Socket("10.0.0.165", 8001);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream= new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        //Socket socket = new Socket("10.0.0.165", 8001);
        //DataInputStream inputStream = new DataInputStream(socket.getInputStream());

        try {
            reciver();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static void reciver() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InterruptedException {

        outputStream.writeUTF("");

        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipherd = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipherd.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] inputfromserver = inputStream.readNBytes(24);
        String output = new String(inputfromserver);

        byte[] original = cipherd.doFinal(Base64.getUrlDecoder().decode(output));
        String decryptedResult = new String(original);
        System.out.println("Decrypted string: " + decryptedResult);

        //String value = decryptedResult.replaceAll("[^0-9]&&[^.]","");
        //    System.out.println("Tallet er " + value);

        String[] stringarray = decryptedResult.split("[,]");
        double humi = Double.parseDouble(stringarray[1]);
        double temp = Double.parseDouble(stringarray[0]);
        System.out.println("temp er " + temp + "    humi er " + humi);
        Thread.sleep(1000);
        reciver();

    }
}
