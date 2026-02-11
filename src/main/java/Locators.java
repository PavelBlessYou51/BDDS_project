/**
 * Класс с локаторами
 */
public class Locators {

    public static String MAIN_BLOCK = "//div[@class='CUSTOM_CODE-al CUSTOM_CODE-ap']";

    // Локаторы внутренних блоков
    public static String TITLE_MAIN_BLOCK = ".//h3";
    public static String FILE_BLOCKS = ".//tbody/tr";
    public static String FILE_NAME = ".//p[@class='text-semibold']";
    public static String OUTSIDE_DOWNLOAD_BUTTON = ".//button";

    // Локаторы всплывающего окна
    public static String INSIDE_DOWNLOAD_BUTTON = "//div[@aria-label='Download item?']//button[@data-testid='download-item-button']";

}
