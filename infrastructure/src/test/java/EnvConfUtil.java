/**
 * @author lh
 */
public class EnvConfUtil {
    public static void setProxy() {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "1080");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "1080");
    }

}
