import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
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

    static {
        try {
            socket = new Socket("10.0.0.165", 8001);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static DataInputStream inputStream;

    static {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

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

        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        //  String encryptedString = new String(inputStream.readNBytes(16),StandardCharsets.UTF_8);
        byte[] inputfromserver = inputStream.readAllBytes();

        String output = new String(inputfromserver);



        Cipher cipherd = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipherd.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipherd.doFinal(Base64.getUrlDecoder().decode(output));
        String decryptedResult = new String(original);
        System.out.println("Decrypted string: " + decryptedResult);

        System.out.println(output);
        Thread.sleep(1000);
        reciver();

    }
}
