import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchiver {
    public void zipFiles(String sourceFolderPath, String zipFilePath) throws IOException {
        File folder = new File(sourceFolderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Папка " + sourceFolderPath + " не существует или это не папка");
        }

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zipOut.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) >= 0) {
                            zipOut.write(buffer, 0, length);
                        }
                        zipOut.closeEntry();
                    }
                }
            }
        }

        System.out.println("Файлы успешно заархивированы в " + zipFilePath);
    }
}
