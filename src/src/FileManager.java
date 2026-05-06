import java.io.BufferedReader; // Импортираме BufferedReader за четене на текст от файл.
import java.io.BufferedWriter; // Импортираме BufferedWriter за писане на текст във файл.
import java.io.FileReader; // Импортираме FileReader за отваряне на файл за четене.
import java.io.FileWriter; // Импортираме FileWriter за отваряне на файл за писане.
import java.io.IOException; // Импортираме IOException за грешки при файлове.

/**
 * Класът FileManager отговаря само за четене и записване на файлове.
 */
public class FileManager {
    public String readFile(String path) throws IOException { // Метод, който прочита файл и връща текста му.
        StringBuilder result = new StringBuilder(); // Създаваме StringBuilder за съдържанието.
        BufferedReader reader = new BufferedReader(new FileReader(path)); // Отваряме файла за четене.
        String line = reader.readLine(); // Прочитаме първия ред.
        while (line != null) { // Въртим, докато има редове.
            result.append(line); // Добавяме реда към резултата.
            result.append("\n"); // Добавяме нов ред.
            line = reader.readLine(); // Четем следващия ред.
        }
        reader.close(); // Затваряме файла.
        return result.toString(); // Връщаме целия текст.
    }

    public void writeFile(String path, String text) throws IOException { // Метод, който записва текст във файл.
        BufferedWriter writer = new BufferedWriter(new FileWriter(path)); // Отваряме файла за писане.
        writer.write(text); // Записваме текста.
        writer.close(); // Затваряме файла.
    }
}
