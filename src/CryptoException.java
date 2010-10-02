/**
 * User: chris
 * Date: Oct 2, 2010
 * Time: 3:47:41 PM
 */
public class CryptoException extends RuntimeException
{
    public CryptoException()
    {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public CryptoException(String message)
    {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public CryptoException(String message, Throwable cause)
    {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public CryptoException(Throwable cause)
    {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
