import java.util.ArrayList; // Импортираме ArrayList за списък с резултати.
import java.util.Scanner; // Импортираме Scanner за четене от конзолата.

/**
 * Класът CommandProcessor чете и изпълнява командите от потребителя.
 */
public class CommandProcessor {
    private XmlElement root; // Тук пазим корена на заредения XML файл.
    private String currentFilePath; // Тук пазим пътя до текущия файл.
    private boolean fileIsOpen; // Тук пазим дали има отворен файл.
    private FileManager fileManager; // Обект за четене и запис на файлове.
    private XmlParser parser; // Обект за четене на XML текст.
    private XmlPrinter printer; // Обект за отпечатване на XML.
    private SimpleXPath simpleXPath; // Обект за прости XPath заявки.

    public CommandProcessor() { // Конструктор на класа.
        root = null; // В началото няма зареден XML.
        currentFilePath = ""; // В началото няма път до файл.
        fileIsOpen = false; // В началото няма отворен файл.
        fileManager = new FileManager(); // Създаваме FileManager.
        parser = new XmlParser(); // Създаваме XmlParser.
        printer = new XmlPrinter(); // Създаваме XmlPrinter.
        simpleXPath = new SimpleXPath(); // Създаваме SimpleXPath.
    }

    public void start() { // Метод, който стартира командния ред.
        Scanner scanner = new Scanner(System.in); // Създаваме Scanner за въвеждане от клавиатурата.
        System.out.println("XML Parser started. Type help for commands."); // Извеждаме начално съобщение.
        boolean running = true; // Променлива, която казва дали програмата работи.
        while (running) { // Цикъл за командите.
            System.out.print("> "); // Показваме знак за команда.
            String command = scanner.nextLine(); // Четем един ред от потребителя.
            running = executeCommand(command); // Изпълняваме командата и проверяваме дали да продължим.
        }
    }

    private boolean executeCommand(String command) { // Метод, който избира коя команда да се изпълни.
        command = command.trim(); // Премахваме излишни интервали.
        if (command.equals("")) { // Проверяваме дали командата е празна.
            return true; // Продължаваме програмата.
        }
        if (command.startsWith("open ")) { // Проверяваме дали командата е open.
            open(command.substring(5).trim()); // Извикваме метода open с пътя към файла.
        } else if (command.equals("close")) { // Проверяваме дали командата е close.
            close(); // Извикваме метода close.
        } else if (command.equals("save")) { // Проверяваме дали командата е save.
            save(); // Извикваме метода save.
        } else if (command.startsWith("save as ")) { // Проверяваме дали командата е save as.
            saveAs(command.substring(8).trim()); // Извикваме saveAs с нов път.
        } else if (command.equals("help")) { // Проверяваме дали командата е help.
            help(); // Извикваме метода help.
        } else if (command.equals("exit")) { // Проверяваме дали командата е exit.
            System.out.println("Exiting the program..."); // Извеждаме съобщение.
            return false; // Спираме програмата.
        } else if (command.equals("print")) { // Проверяваме дали командата е print.
            print(); // Извикваме метода print.
        } else if (command.startsWith("select ")) { // Проверяваме дали командата е select.
            select(command); // Изпълняваме select.
        } else if (command.startsWith("set ")) { // Проверяваме дали командата е set.
            set(command); // Изпълняваме set.
        } else if (command.startsWith("children ")) { // Проверяваме дали командата е children.
            children(command); // Изпълняваме children.
        } else if (command.startsWith("child ")) { // Проверяваме дали командата е child.
            child(command); // Изпълняваме child.
        } else if (command.startsWith("text ")) { // Проверяваме дали командата е text.
            text(command); // Изпълняваме text.
        } else if (command.startsWith("delete ")) { // Проверяваме дали командата е delete.
            delete(command); // Изпълняваме delete.
        } else if (command.startsWith("newchild ")) { // Проверяваме дали командата е newchild.
            newChild(command); // Изпълняваме newchild.
        } else if (command.startsWith("xpath ")) { // Проверяваме дали командата е xpath.
            xpath(command); // Изпълняваме xpath.
        } else { // Ако командата не е позната.
            System.out.println("Unknown command. Type help."); // Извеждаме съобщение за грешка.
        }
        return true; // Продължаваме програмата.
    }

    private void open(String path) { // Метод за отваряне на файл.
        try { // Започваме блок за възможна грешка.
            String text = fileManager.readFile(path); // Прочитаме съдържанието на файла.
            root = parser.parse(text); // Парсваме XML текста.
            currentFilePath = path; // Записваме пътя на файла.
            fileIsOpen = true; // Отбелязваме, че има отворен файл.
            System.out.println("Successfully opened " + path); // Извеждаме успех.
        } catch (Exception e) { // Хващаме грешка при четене или парсване.
            System.out.println("Error while opening file: " + e.getMessage()); // Извеждаме грешката.
        }
    }

    private void close() { // Метод за затваряне на текущия файл.
        if (!fileIsOpen) { // Проверяваме дали няма отворен файл.
            System.out.println("No file is currently open."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        root = null; // Изчистваме XML дървото.
        currentFilePath = ""; // Изчистваме пътя.
        fileIsOpen = false; // Казваме, че вече няма отворен файл.
        System.out.println("Successfully closed file."); // Извеждаме успех.
    }

    private void save() { // Метод за запис в същия файл.
        if (!checkOpenFile()) { // Проверяваме дали има отворен файл.
            return; // Ако няма, спираме.
        }
        saveAs(currentFilePath); // Записваме в текущия файл.
    }

    private void saveAs(String path) { // Метод за запис в избран файл.
        if (!checkOpenFile()) { // Проверяваме дали има отворен файл.
            return; // Ако няма, спираме.
        }
        try { // Започваме блок за възможна файлова грешка.
            String xml = printer.buildXml(root); // Превръщаме дървото в XML текст.
            fileManager.writeFile(path, xml); // Записваме текста във файл.
            System.out.println("Successfully saved " + path); // Извеждаме успех.
        } catch (Exception e) { // Хващаме възможна грешка.
            System.out.println("Error while saving file: " + e.getMessage()); // Извеждаме грешката.
        }
    }

    private void print() { // Метод за отпечатване на XML-а.
        if (!checkOpenFile()) { // Проверяваме дали има отворен файл.
            return; // Ако няма, спираме.
        }
        System.out.print(printer.buildXml(root)); // Отпечатваме XML текста.
    }

    private void select(String command) { // Команда select <id> <key>.
        if (!checkOpenFile()) { // Проверяваме дали има файл.
            return; // Ако няма, спираме.
        }
        String[] parts = command.split(" "); // Разделяме командата по интервали.
        if (parts.length < 3) { // Проверяваме дали има достатъчно части.
            System.out.println("Usage: select <id> <key>"); // Показваме правилна употреба.
            return; // Спираме метода.
        }
        XmlElement element = findById(parts[1]); // Намираме елемента по id.
        if (element == null) { // Проверяваме дали не е намерен.
            System.out.println("Element not found."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        String value = element.getAttribute(parts[2]); // Взимаме стойността на атрибута.
        System.out.println(value); // Отпечатваме стойността.
    }

    private void set(String command) { // Команда set <id> <key> <value>.
        if (!checkOpenFile()) { // Проверяваме дали има файл.
            return; // Ако няма, спираме.
        }
        String[] parts = command.split(" ", 4); // Разделяме на максимум 4 части.
        if (parts.length < 4) { // Проверяваме дали има всички аргументи.
            System.out.println("Usage: set <id> <key> <value>"); // Показваме правилна употреба.
            return; // Спираме метода.
        }
        XmlElement element = findById(parts[1]); // Намираме елемента.
        if (element == null) { // Проверяваме дали не е намерен.
            System.out.println("Element not found."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        element.setAttribute(parts[2], parts[3]); // Променяме атрибута.
        System.out.println("Attribute changed."); // Извеждаме успех.
    }

    private void children(String command) { // Команда children <id>.
        if (!checkOpenFile()) { // Проверяваме дали има файл.
            return; // Ако няма, спираме.
        }
        String[] parts = command.split(" "); // Разделяме командата.
        if (parts.length < 2) { // Проверяваме дали има id.
            System.out.println("Usage: children <id>"); // Показваме правилна употреба.
            return; // Спираме метода.
        }
        XmlElement element = findById(parts[1]); // Намираме елемента.
        if (element == null) { // Проверяваме дали не е намерен.
            System.out.println("Element not found."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        for (XmlElement child : element.getChildren()) { // Обхождаме децата.
            System.out.println(child.getName() + " id=" + child.getId()); // Отпечатваме име и id.
        }
    }

    private void child(String command) { // Команда child <id> <n>.
        if (!checkOpenFile()) { // Проверяваме дали има файл.
            return; // Ако няма, спираме.
        }
        String[] parts = command.split(" "); // Разделяме командата.
        if (parts.length < 3) { // Проверяваме дали има достатъчно аргументи.
            System.out.println("Usage: child <id> <n>"); // Показваме правилна употреба.
            return; // Спираме метода.
        }
        XmlElement element = findById(parts[1]); // Намираме елемента.
        if (element == null) { // Проверяваме дали не е намерен.
            System.out.println("Element not found."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        try { // Опитваме да прочетем число.
            int number = Integer.parseInt(parts[2]); // Превръщаме текста в число.
            XmlElement child = element.getChildByNumber(number); // Взимаме n-тото дете.
            if (child == null) { // Проверяваме дали има такова дете.
                System.out.println("No such child."); // Извеждаме съобщение.
            } else { // Ако има дете.
                System.out.println(child.getName() + " id=" + child.getId()); // Печатаме името и id.
            }
        } catch (NumberFormatException e) { // Ако n не е число.
            System.out.println("The child number must be a number."); // Извеждаме грешка.
        }
    }

    private void text(String command) { // Команда text <id>.
        if (!checkOpenFile()) { // Проверяваме дали има файл.
            return; // Ако няма, спираме.
        }
        String[] parts = command.split(" "); // Разделяме командата.
        if (parts.length < 2) { // Проверяваме дали има id.
            System.out.println("Usage: text <id>"); // Показваме правилна употреба.
            return; // Спираме метода.
        }
        XmlElement element = findById(parts[1]); // Намираме елемента.
        if (element == null) { // Проверяваме дали не е намерен.
            System.out.println("Element not found."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        System.out.println(element.getText()); // Отпечатваме текста на елемента.
    }

    private void delete(String command) { // Команда delete <id> <key>.
        if (!checkOpenFile()) { // Проверяваме дали има файл.
            return; // Ако няма, спираме.
        }
        String[] parts = command.split(" "); // Разделяме командата.
        if (parts.length < 3) { // Проверяваме дали има всички аргументи.
            System.out.println("Usage: delete <id> <key>"); // Показваме правилна употреба.
            return; // Спираме метода.
        }
        XmlElement element = findById(parts[1]); // Намираме елемента.
        if (element == null) { // Проверяваме дали не е намерен.
            System.out.println("Element not found."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        element.removeAttribute(parts[2]); // Изтриваме атрибута.
        System.out.println("Attribute deleted."); // Извеждаме успех.
    }

    private void newChild(String command) { // Команда newchild <id>.
        if (!checkOpenFile()) { // Проверяваме дали има файл.
            return; // Ако няма, спираме.
        }
        String[] parts = command.split(" "); // Разделяме командата.
        if (parts.length < 2) { // Проверяваме дали има id.
            System.out.println("Usage: newchild <id>"); // Показваме правилна употреба.
            return; // Спираме метода.
        }
        XmlElement element = findById(parts[1]); // Намираме родителя.
        if (element == null) { // Проверяваме дали не е намерен.
            System.out.println("Element not found."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        XmlElement newElement = new XmlElement("newChild"); // Създаваме нов елемент с име newChild.
        newElement.setId(createFreeId()); // Даваме му свободно id.
        element.addChild(newElement); // Добавяме го като дете.
        System.out.println("New child added with id=" + newElement.getId()); // Извеждаме успех.
    }

    private void xpath(String command) { // Команда xpath <id> <XPath>.
        if (!checkOpenFile()) { // Проверяваме дали има файл.
            return; // Ако няма, спираме.
        }
        String[] parts = command.split(" ", 3); // Разделяме на 3 части.
        if (parts.length < 3) { // Проверяваме дали има id и XPath.
            System.out.println("Usage: xpath <id> <XPath>"); // Показваме правилна употреба.
            return; // Спираме метода.
        }
        XmlElement start = findById(parts[1]); // Намираме стартовия елемент.
        if (start == null) { // Проверяваме дали не е намерен.
            System.out.println("Element not found."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        ArrayList<XmlElement> result = simpleXPath.search(start, parts[2]); // Изпълняваме XPath заявката.
        for (XmlElement element : result) { // Обхождаме намерените елементи.
            System.out.println(element.getName() + " id=" + element.getId() + " text=" + element.getText()); // Печатаме резултата.
        }
    }

    private XmlElement findById(String id) { // Метод, който търси елемент по id.
        return findByIdRecursive(root, id); // Стартираме рекурсивно търсене от корена.
    }

    private XmlElement findByIdRecursive(XmlElement element, String id) { // Рекурсивен метод за търсене.
        if (element == null) { // Проверяваме дали елементът е null.
            return null; // Ако е null, няма резултат.
        }
        if (element.getId().equals(id)) { // Проверяваме дали id-то съвпада.
            return element; // Връщаме намерения елемент.
        }
        for (XmlElement child : element.getChildren()) { // Обхождаме децата.
            XmlElement found = findByIdRecursive(child, id); // Търсим в детето.
            if (found != null) { // Проверяваме дали е намерено.
                return found; // Връщаме намерения елемент.
            }
        }
        return null; // Ако не е намерено, връщаме null.
    }

    private String createFreeId() { // Метод, който създава свободно id за ново дете.
        int number = 1; // Започваме от 1.
        String id = "new_" + number; // Създаваме първо предложение за id.
        while (findById(id) != null) { // Докато id-то вече съществува.
            number++; // Увеличаваме числото.
            id = "new_" + number; // Създаваме ново предложение.
        }
        return id; // Връщаме свободното id.
    }

    private boolean checkOpenFile() { // Метод, който проверява дали има отворен файл.
        if (!fileIsOpen) { // Проверяваме флага.
            System.out.println("No file is open. Use open <file> first."); // Извеждаме съобщение.
            return false; // Връщаме false.
        }
        return true; // Връщаме true.
    }

    private void help() { // Метод, който показва помощ.
        System.out.println("The following commands are supported:"); // Заглавие.
        System.out.println("open <file>              opens <file>"); // Команда open.
        System.out.println("close                    closes currently opened file"); // Команда close.
        System.out.println("save                     saves the currently open file"); // Команда save.
        System.out.println("save as <file>           saves the currently open file in <file>"); // Команда save as.
        System.out.println("print                    prints the XML file"); // Команда print.
        System.out.println("select <id> <key>        prints attribute value"); // Команда select.
        System.out.println("set <id> <key> <value>   changes attribute value"); // Команда set.
        System.out.println("children <id>            prints child elements"); // Команда children.
        System.out.println("child <id> <n>           prints n-th child element"); // Команда child.
        System.out.println("text <id>                prints element text"); // Команда text.
        System.out.println("delete <id> <key>        deletes attribute"); // Команда delete.
        System.out.println("newchild <id>            adds new child element"); // Команда newchild.
        System.out.println("xpath <id> <XPath>       executes simple XPath"); // Команда xpath.
        System.out.println("help                     prints this information"); // Команда help.
        System.out.println("exit                     exits the program"); // Команда exit.
    }
}
