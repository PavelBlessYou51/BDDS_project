import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Класс для работы с браузером
 */
public class WebDriverManager {

    public WebDriver driver;
    public String downloadDir;
    public long timeoutSeconds;
    public long pollIntervalMillis;


    WebDriverManager() {
        this(FileUtils.getAbsolutePathToFile("src/files"), 600, 1000); // 600 сек timeout, проверка каждую секунду
    }

    WebDriverManager(String downloadDir, long timeoutSeconds, long pollIntervalMillis) {
        this.downloadDir = downloadDir;
        this.timeoutSeconds = timeoutSeconds;
        this.pollIntervalMillis = pollIntervalMillis;
    }


    /**
     * Инициализатор браузера. Масштабирует окно браузера на максимальный размер экрана
     */
    public void init() {
        // Настройки для загрузки файлов
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadDir);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);


        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.manager.closeWhenDone", true);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        profile.setPreference("browser.download.dir", downloadDir);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/zip, application/x-zip-compressed, application/x-zip, application/x-tar, application/tar, application/x-gtar, application/x-gzip, application/gzip, application/x-compressed-tar, pplication/octet-stream");
        profile.setPreference("permissions.default.dir", 1); // 1 - разрешить загрузку
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);

        // Отключить окно информационной панели
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");

        // Инициализация браузера
        driver = new FirefoxDriver(options);
        driver.get("https://publication-bdds.apps.epo.org/raw-data/products/public/product/32");
        driver.manage().window().maximize();
    }

    /**
     * Закрывает окно и сессию
     */
    public void quitClose() {
        driver.close();
        driver.quit();
    }

    /**
     * Метод явного ожидания присутствия веб-элемента на странице по локатору
     */
    public WebElement presenceOfElement(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(25)).until(ExpectedConditions.presenceOfElementLocated(locator));
    }


    /**
     * Метод явного ожидания присутствия вложенного веб-элемента
     */
    public WebElement presenceOfInnerElement(WebElement element, By innerLocator) {
        return new WebDriverWait(driver, Duration.ofSeconds(25)).until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, innerLocator));
    }

    /**
     * Метод явного ожидания присутствия списка элементов на странице
     */
    public List<WebElement> presenceOfElements(By locator) {
        List<WebElement> elements = new WebDriverWait(driver, Duration.ofSeconds(18)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return elements;
    }

    /**
     * Метод осуществляет ЛКМ по веб-элементу по локатору
     */
    public void click(By locator, boolean hasDelay) {
        for (int i = 1; i <= 10; i++) {
            try {
                WebElement element = presenceOfElement(locator);
                element.click();
                if (hasDelay) {
                    TimeUnit.MILLISECONDS.sleep(400);
                }
                return;
            } catch (ElementNotInteractableException exception) {
                System.out.println("Try to click, but get ElementNotInteractableException");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Метод осуществляет ЛКМ по внутреннему веб-элементу
     */
    public void innerClick(WebElement element, By innerLocator, boolean hasDelay) {
        for (int i = 1; i <= 10; i++) {
            try {
                WebElement innerElement = presenceOfInnerElement(element, innerLocator);
                innerElement.click();
                if (hasDelay) {
                    TimeUnit.MILLISECONDS.sleep(400);
                }
                return;
            } catch (ElementNotInteractableException exception) {
                System.out.println("Try to click, but get ElementNotInteractableException");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Метод загрузки файлов с ожиданием окончания загрузки
     */
    public void downloadFiles(int blockNumber) {
        FileUtils.cleanupTempFiles();
        List<WebElement> mainBlocks = presenceOfElements(By.xpath(Locators.MAIN_BLOCK));
        System.out.println("Всего блоков: " + mainBlocks.size());
        System.out.println("<---------------------------------------------------------->");
        for (int i = blockNumber - 1; i < mainBlocks.size(); i++) {
            WebElement block = mainBlocks.get(i);
            String blockName = presenceOfInnerElement(block, By.xpath(Locators.TITLE_MAIN_BLOCK)).getText();
            System.out.println("Блок " + blockName + " (" + (i + 1) + " из " + mainBlocks.size() + " )");
            System.out.println("Скачиваем файлы из блока");
            System.out.println();

            List<WebElement> fileBlocks = block.findElements(By.xpath(Locators.FILE_BLOCKS));

            System.out.println("Всего файлов в блоке: " + fileBlocks.size());
            filesDownloader(fileBlocks, i);
        }

    }

    /**
     * Метод загружает файлы из блока
     */
    private void filesDownloader(List<WebElement> fileBlocks, int i) {
        for (int k = 0; k < fileBlocks.size(); k++) {
            WebElement fileBlock = fileBlocks.get(k);
            String fileName = presenceOfInnerElement(fileBlock, By.xpath(Locators.FILE_NAME)).getText();
            System.out.println("Скачиваем файл №" + (i + 1) + " " + fileName);
            innerClick(fileBlock, By.xpath(Locators.OUTSIDE_DOWNLOAD_BUTTON), true);
            click(By.xpath(Locators.INSIDE_DOWNLOAD_BUTTON), true);
            waitForDownload();
        }
    }

    /**
     * Метод, обеспечивающий ожидание загрузки
     */
    public void waitForDownload() throws TimeoutException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (timeoutSeconds * 1000);

        System.out.println("Ожидание загрузки файла в: " + downloadDir);

        while (System.currentTimeMillis() < endTime) {
            // Получаем список файлов
            File[] files = FileUtils.getDirectoryFiles(downloadDir);

            if (files != null) {
                // Ищем файлы по критериям
                boolean result = FileUtils.findDownloadedFile(files);

                if (!result) {
                    break;
                }
            }

            // Пауза между проверками
            try {
                Thread.sleep(pollIntervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ожидание прервано", e);
            }
        }
        System.out.println("Файл загружен");
        System.out.println("<---------------------------------------------------------->");
    }


}
