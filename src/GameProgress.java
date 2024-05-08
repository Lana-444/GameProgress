import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GameProgress implements Serializable {
    private static final long serialVersionUID = 1L;


    public static void main(String[] args) {

        GameProgress game1 = new GameProgress(1, 1, 1, 1);
        GameProgress game2 = new GameProgress(2, 2, 2, 2);
        GameProgress game3 = new GameProgress(3, 3, 3, 3);

        game1.saveGame();
        game2.saveGame();
        game3.saveGame();
        zipFiles();
        delFiles();
        openZip();

    }

    private int health;
    private int weapons;
    private int lvl;
    private double distance;

    public GameProgress(int health, int weapons, int lvl, double distance) {
        this.health = health;
        this.weapons = weapons;
        this.lvl = lvl;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GameProgress{" +
                "health=" + health +
                ", weapons=" + weapons +
                ", lvl=" + lvl +
                ", distance=" + distance +
                '}';
    }

    public static void saveGame() {

        String filePath = "C://Java//Games//savegames//gameProgress.txt";

        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(filePath);

            System.out.println("Объект успешно сериализован и записан в файл: " + filePath);
        } catch (IOException e) {
            System.out.println("Ошибка при записи сериализованных данных: " + e.getMessage());
        }
    }

    public static void zipFiles() {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("C://Java//Games//savegames//zip_gameProgress.zip"));
             BufferedInputStream bis = new BufferedInputStream(new FileInputStream("C://Java//Games//savegames//gameProgress.txt"))) {
            zos.putNextEntry(new ZipEntry("packed" + "gameProgress.txt"));
            byte[] buffer = new byte[bis.available()];
            bis.read(buffer);
            zos.write(buffer);
            zos.closeEntry();
            openProgress();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void delFiles() {
        File file = new File("C://Java//Games//savegames//gameProgress.txt");
        if (file.delete())
            System.out.println("Файл " + file + " удален");
    }

    public static void openZip() {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream("C://Java//Games//savegames//zip_gameProgress.zip"))) {
            ZipEntry zipEntry;
            String directoryPath = "C://Java//Games//savegames//gameProgress.txt";
            while ((zipEntry = zis.getNextEntry()) != null) {
                String entryName = zipEntry.getName();
                File file = new File(directoryPath, entryName);
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(directoryPath))) {
                    for (int i = zis.read(); i != -1; i = zis.read()) {
                        bos.write(i);
                    }
                }
                zis.closeEntry();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openProgress() {
        try (FileInputStream fis = new FileInputStream("C://Java//Games//savegames//gameProgress.txt");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object o = ois.readObject();
            if (o instanceof GameProgress gameProgress) {
                System.out.println(gameProgress);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}