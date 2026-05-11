import java.util.ArrayList; // Импортираме ArrayList за резултатите от XPath.

/**
 * Класът SimpleXPath изпълнява малка част от XPath заявките от условието.
 * Поддържа /, [], @ и сравнение атрибут="стойност".
 */
public class SimpleXPath {
    public ArrayList<XmlElement> search(XmlElement start, String path) { // Метод, който търси елементи по път.
        ArrayList<XmlElement> currentElements = new ArrayList<XmlElement>(); // Списък с текущите елементи.
        currentElements.add(start); // Започваме от подадения елемент.
        String[] parts = path.split("/"); // Разделяме пътя по символ /.
        for (String part : parts) { // Обхождаме всяка част от пътя.
            if (part.trim().equals("")) { // Пропускаме празни части.
                continue; // Продължаваме със следващата част.
            }
            currentElements = searchOneLevel(currentElements, part.trim()); // Търсим следващото ниво.
        }
        return currentElements; // Връщаме намерените елементи.
    }

    private ArrayList<XmlElement> searchOneLevel(ArrayList<XmlElement> parents, String part) { // Търси една част от XPath.
        ArrayList<XmlElement> found = new ArrayList<XmlElement>(); // Списък с намерени елементи.
        String elementName = getElementName(part); // Взимаме името на елемента от частта.
        Integer index = getIndex(part); // Проверяваме дали има индекс в квадратни скоби.
        String wantedAttribute = getWantedAttribute(part); // Проверяваме дали се търси атрибут чрез @.
        String filterKey = getFilterKey(part); // Проверяваме дали има филтър по атрибут.
        String filterValue = getFilterValue(part); // Взимаме стойността за филтъра.

        for (XmlElement parent : parents) { // Обхождаме всички текущи родители.
            ArrayList<XmlElement> sameName = new ArrayList<XmlElement>(); // Тук пазим децата с правилно име.
            for (XmlElement child : parent.getChildren()) { // Обхождаме децата на родителя.
                if (child.getName().equals(elementName)) { // Проверяваме дали името съвпада.
                    if (filterKey == null || filterValue.equals(child.getAttribute(filterKey))) { // Проверяваме филтъра.
                        sameName.add(child); // Добавяме детето като подходящо.
                    }
                }
            }
            if (index != null) { // Проверяваме дали има индекс.
                if (index >= 0 && index < sameName.size()) { // Проверяваме дали индексът е валиден.
                    found.add(sameName.get(index)); // Добавяме само елемента с този индекс.
                }
            } else { // Ако няма индекс.
                found.addAll(sameName); // Добавяме всички подходящи елементи.
            }
        }

        if (wantedAttribute != null) { // Проверяваме дали се търси атрибут.
            printAttributesFromResult(found, wantedAttribute); // Отпечатваме атрибутите.
        }
        return found; // Връщаме намерените елементи.
    }

    private String getElementName(String part) { // Метод, който взима името на елемента.
        int bracketIndex = part.indexOf("["); // Търсим квадратна скоба.
        int parenthesisIndex = part.indexOf("("); // Търсим кръгла скоба.
        int end = part.length(); // Първоначално краят е краят на текста.
        if (bracketIndex != -1 && bracketIndex < end) { // Проверяваме квадратната скоба.
            end = bracketIndex; // Краят става преди скобата.
        }
        if (parenthesisIndex != -1 && parenthesisIndex < end) { // Проверяваме кръглата скоба.
            end = parenthesisIndex; // Краят става преди скобата.
        }
        return part.substring(0, end); // Връщаме името.
    }

    private Integer getIndex(String part) { // Метод, който взима индекс от [n].
        int start = part.indexOf("["); // Търсим начало на индекс.
        int end = part.indexOf("]"); // Търсим край на индекс.
        if (start == -1 || end == -1) { // Ако няма скоби.
            return null; // Няма индекс.
        }
        try { // Опитваме да превърнем текста в число.
            return Integer.parseInt(part.substring(start + 1, end)); // Връщаме числото.
        } catch (NumberFormatException e) { // Ако не е число.
            return null; // Няма валиден индекс.
        }
    }

    private String getWantedAttribute(String part) { // Метод, който намира @атрибут.
        int atIndex = part.indexOf("@"); // Търсим символ @.
        int endIndex = part.indexOf(")"); // Търсим затваряща скоба.
        if (atIndex == -1 || endIndex == -1) { // Проверяваме дали липсват.
            return null; // Няма заявка за атрибут.
        }
        return part.substring(atIndex + 1, endIndex); // Връщаме името на атрибута.
    }

    private String getFilterKey(String part) { // Метод, който взима ключ от условие attr="value".
        int start = part.indexOf("("); // Търсим начало на условието.
        int equals = part.indexOf("="); // Търсим знак равно.
        if (start == -1 || equals == -1) { // Ако няма условие.
            return null; // Няма филтър.
        }
        if (part.indexOf("@") != -1) { // Ако условието е само @id.
            return null; // Това не е филтър, а заявка за атрибут.
        }
        return part.substring(start + 1, equals).trim(); // Връщаме името на атрибута.
    }

    private String getFilterValue(String part) { // Метод, който взима стойност от условие attr="value".
        int equals = part.indexOf("="); // Търсим знак равно.
        int end = part.indexOf(")"); // Търсим край на условието.
        if (equals == -1 || end == -1) { // Ако липсва част от условието.
            return null; // Няма стойност.
        }
        String value = part.substring(equals + 1, end).trim(); // Взимаме текста след равното.
        value = value.replace("\"", ""); // Премахваме двойните кавички.
        value = value.replace("'", ""); // Премахваме единичните кавички.
        return value; // Връщаме стойността.
    }

    private void printAttributesFromResult(ArrayList<XmlElement> elements, String attribute) { // Метод, който печата атрибутите от резултата.
        for (XmlElement element : elements) { // Обхождаме намерените елементи.
            String value = element.getAttribute(attribute); // Взимаме стойността на атрибута.
            System.out.println(element.getId() + ": " + attribute + " = " + value); // Печатаме резултата.
        }
    }
}
