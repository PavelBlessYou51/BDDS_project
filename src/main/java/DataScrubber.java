/**
 * Базовый класс
 */
public class DataScrubber
{
    public static void main( String[] args )
    {
        WebDriverManager manager = new WebDriverManager();
        manager.init();
        manager.downloadFiles(6);
        manager.quitClose();
    }
}
