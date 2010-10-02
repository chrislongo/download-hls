import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: clongo
 * Date: Oct 1, 2010
 * Time: 5:19:35 PM
 */
public class KeyInfo
{
    private String method;
    private URL keyUrl;
    private byte[] iv;
    private byte[] key;

    public KeyInfo(String text)
    {
        String regex =
            "METHOD=([A-Z0-9-]+)" +
            ",URI=\"(https?://([-\\w\\.]+)+(:\\d+)?(/([\\w/_~\\.]*(\\?\\S+)?)?)?)\"" +
            ",IV=0x([0-9a-f]+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while(matcher.find())
        {
            method = matcher.group(1);

            try
            {
                keyUrl = new URL(matcher.group(2));
                fetchKey();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            String ivString = matcher.group(8);
            iv = unpack(ivString);
        }
    }

    public String getMethod()
    {
        return method;
    }

    public URL getKeyUrl()
    {
        return keyUrl;
    }

    public byte[] getIV()
    {
        return iv;
    }

    public byte[] getKey()
    {
        return key;
    }

    private void fetchKey() throws IOException
    {
        this.key = new byte[16];

        URLConnection con = this.keyUrl.openConnection();
        InputStream in = new BufferedInputStream(con.getInputStream());

        int read = 0;
        int offset = 0;

        while(offset < 16)
        {
            if((read = in.read(this.key, offset, key.length - offset)) == -1)
                break;

            offset += read;
        }

        in.close();
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

    private String byteArrayToString(byte[] array)
    {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < array.length; i++)
        {
            byte b = array[i];
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    @Override
    public String toString()
    {
        return "KeyInfo{" +
                "method='" + method + '\'' +
                ", keyUrl=" + keyUrl +
                ", iv=" + byteArrayToString(iv) +
                ", key=" + byteArrayToString(key) +
                '}';
    }
}
