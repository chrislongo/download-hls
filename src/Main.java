import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by IntelliJ IDEA.
 * User: clongo
 * Date: Oct 1, 2010
 * Time: 12:22:32 PM
 */
public class Main
{
    private static String IV = "214502ba0fa3b3911603d80a06514f89";
    private static String KEY = "b3d3199b25c018bfcb1085e304b69cb8";

    private Cipher cipher;

    public static void main(String[] args)
    {
        Main main = new Main();

        byte[] iv = main.unpack(IV);
        byte[] key = main.unpack(KEY);
        SecretKey secretKey = new SecretKeySpec(key, "AES");

        main.initCrypto(iv, secretKey);

        try
        {
            FileInputStream in =
                    new FileInputStream("/Users/clongo/src/java/DecryptHLS/data/fileSequence0.ts");

            FileOutputStream out = 
                    new FileOutputStream("/Users/clongo/src/java/DecryptHLS/data/out.ts");

            main.decrypt(in, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initCrypto(byte[] iv, SecretKey secretKey)
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());    
        AlgorithmParameterSpec spec = new IvParameterSpec(iv);

        try
        {
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void decrypt(InputStream in, OutputStream out) throws IOException
    {
        InputStream cis = new CipherInputStream(in, cipher);
	    byte[] buffer = new byte[1024];

        int read;

        while ((read = cis.read(buffer)) >= 0)
        {
            out.write(buffer, 0, read);
        }

        out.close();
    }

    private byte[] unpack(String hexString)
    {
        byte[] array = new byte[16];

        BigInteger bigInt = new BigInteger(hexString, 16);
        BigInteger andValue = new BigInteger("ff", 16);

        for(int i = 0, index = 15; i < 16; i++, index--)
        {
            BigInteger shift = bigInt.shiftRight(i * 8);
            shift = shift.and(andValue);
            array[index] = shift.byteValue();
        }

        return array;
    }
}