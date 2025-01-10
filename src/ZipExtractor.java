import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {
    public void unzip(String zipFilePath, String destFolderPath) throws IOException {
        File destFolder = new File(destFolderPath);
        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                File file = new File(destFolderPath, entry.getName());
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipIn.read(buffer)) >= 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                zipIn.closeEntry();
            }
        }

        System.out.println("Архив успешно извлечен в " + destFolderPath);
    }
}
