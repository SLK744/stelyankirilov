import java.util.Map; // Импортираме Map за обхождане на атрибути.

/**
 * Класът XmlPrinter превръща XML дървото обратно в красив XML текст.
 */
public class XmlPrinter {
    public String buildXml(XmlElement root) { // Метод, който връща XML текст от корена.
        StringBuilder result = new StringBuilder(); // Създаваме StringBuilder за резултата.
        buildElement(root, result, 0); // Започваме рекурсивно построяване.
        return result.toString(); // Връщаме готовия текст.
    }

    private void buildElement(XmlElement element, StringBuilder result, int level) { // Метод, който добавя един елемент към текста.
        addTabs(result, level); // Добавяме отстъп според нивото.
        result.append("<"); // Добавяме начало на таг.
        result.append(element.getName()); // Добавяме името на елемента.
        for (Map.Entry<String, String> entry : element.getAttributes().entrySet()) { // Обхождаме атрибутите.
            result.append(" "); // Добавяме интервал преди атрибута.
            result.append(entry.getKey()); // Добавяме името на атрибута.
            result.append("=\""); // Добавяме равно и отваряща кавичка.
            result.append(entry.getValue()); // Добавяме стойността на атрибута.
            result.append("\""); // Добавяме затваряща кавичка.
        }
        if (element.getChildren().size() == 0 && element.getText().equals("")) { // Проверяваме дали е празен елемент.
            result.append("/>"); // Затваряме го като самозатварящ се таг.
            result.append("\n"); // Добавяме нов ред.
            return; // Спираме метода.
        }
        result.append(">"); // Затваряме отварящия таг.
        if (!element.getText().equals("")) { // Проверяваме дали има текст.
            result.append(element.getText()); // Добавяме текста.
        }
        if (element.getChildren().size() > 0) { // Проверяваме дали има деца.
            result.append("\n"); // Добавяме нов ред преди децата.
            for (XmlElement child : element.getChildren()) { // Обхождаме децата.
                buildElement(child, result, level + 1); // Добавяме детето с по-голям отстъп.
            }
            addTabs(result, level); // Добавяме отстъп преди затварящия таг.
        }
        result.append("</"); // Добавяме начало на затварящ таг.
        result.append(element.getName()); // Добавяме името на елемента.
        result.append(">"); // Затваряме затварящия таг.
        result.append("\n"); // Добавяме нов ред.
    }

    private void addTabs(StringBuilder result, int level) { // Метод, който добавя отстъп.
        for (int i = 0; i < level; i++) { // Повтаряме според нивото.
            result.append("    "); // Добавяме 4 интервала.
        }
    }
}
