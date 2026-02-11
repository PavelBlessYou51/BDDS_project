import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс содержит вспомогательные методы для работы с файлами
 */
public final class FileUtils {


    private static final String TEMP_EXTENSIONS = ".part";

    /**
     * Метод возвращает абсолютный путь к файлу по относительному пути
     */
    public static String getAbsolutePathToFile(String file) {
        String absolutePath = Paths.get(file).toAbsolutePath().toString();
        return absolutePath;
    }

    /**
     * Метод возвращает список файлов в указанной директории
     */
    public static File[] getDirectoryFiles(String downloadDir) {
        File dir = new File(downloadDir);
        return dir.listFiles();
    }

    /**
     * Метод наличие временного файла в директории загрузки
     */
    public static boolean findDownloadedFile(File[] files) {
        List<File> tempFiles = new ArrayList<>();

        // Разделяем файлы на временные и завершенные
        for (File file : files) {
            if (isTempFile(file)) {
                tempFiles.add(file);
            }
        }

        // Если есть временные файлы - загрузка еще идет
        return !tempFiles.isEmpty();
    }

    /**
     * Метод проверяет, что файл является временным (загружается)
     */
    public static boolean isTempFile(File file) {
        return file.getName().toLowerCase().endsWith(TEMP_EXTENSIONS);
    }

    /**
     * Очистка временных файлов
     */
    public static void cleanupTempFiles() {
        File[] files = getDirectoryFiles(FileUtils.getAbsolutePathToFile("src/files"));

        if (files != null) {
            Arrays.stream(files)
                    .filter(FileUtils::isTempFile)
                    .forEach(file -> {
                        if (file.delete()) {
                            System.out.println("Удален временный файл: " + file.getName());
                        }
                    });
        }
    }


}
