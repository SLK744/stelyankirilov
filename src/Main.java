/**
 * Главен клас на програмата.
 * Оттук стартира XML Parser проектът.
 */
public class Main {
    public static void main(String[] args) { // Главният метод, който Java изпълнява първи.
        CommandProcessor processor = new CommandProcessor(); // Създаваме обект, който обработва командите.
        processor.start(); // Стартираме командния ред на програмата.
    }
}
