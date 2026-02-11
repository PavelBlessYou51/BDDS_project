/**
 * Базовый класс
 */
public class DataScrubber
{
    public static void main( String[] args )
    {
        WebDriverManager manager = new WebDriverManager(); // инициализация экземпляра менеджера
        manager.init(); // инициализация бразера и его открытие
        manager.downloadFiles(14); // загрузка файлов
        manager.quitClose(); // закрытие и убийство браузера
    }
}
