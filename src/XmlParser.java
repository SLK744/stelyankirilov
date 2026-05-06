import java.util.ArrayList; // Импортираме ArrayList за списъци от елементи.
import java.util.HashMap; // Импортираме HashMap за проверка на id-тата.

/**
 * Класът XmlParser чете XML текст и го превръща в дърво от XmlElement обекти.
 * Тук НЕ използваме готови XML библиотеки.
 */
public class XmlParser {
    private int position; // Позицията, до която сме стигнали в текста.
    private String xmlText; // Целият XML текст.
    private int automaticIdCounter; // Брояч за автоматично id.
    private HashMap<String, Integer> usedIds; // Карта с вече използвани id-та.

    public XmlParser() { // Конструктор за парсера.
        position = 0; // Започваме от позиция 0.
        xmlText = ""; // Първоначално няма XML текст.
        automaticIdCounter = 1; // Автоматичните id-та започват от 1.
        usedIds = new HashMap<String, Integer>(); // Създаваме празна карта за id-тата.
    }

    public XmlElement parse(String text) throws Exception { // Метод, който стартира парсването.
        xmlText = text; // Записваме текста в поле.
        position = 0; // Започваме от началото.
        automaticIdCounter = 1; // Нулираме брояча за автоматични id-та.
        usedIds.clear(); // Изчистваме старите id-та.
        skipSpaces(); // Прескачаме начални празни символи.
        if (!startsWith("<")) { // Проверяваме дали XML-ът започва с отваряща скоба.
            throw new Exception("XML файлът трябва да започва с отварящ таг."); // Даваме грешка.
        }
        XmlElement root = readElement(); // Четем кореновия елемент.
        makeIdsUnique(root); // Правим всички id-та уникални.
        return root; // Връщаме корена.
    }

    private XmlElement readElement() throws Exception { // Метод, който прочита един XML елемент.
        expect('<'); // Очакваме символ <.
        if (startsWith("/")) { // Проверяваме дали е затварящ таг.
            throw new Exception("Неочакван затварящ таг."); // Ако е затварящ, това е грешка.
        }
        String elementName = readName(); // Четем името на елемента.
        XmlElement element = new XmlElement(elementName); // Създаваме нов обект за елемента.
        readAttributes(element); // Четем атрибутите на елемента.
        skipSpaces(); // Прескачаме интервали.
        if (startsWith("/>")) { // Проверяваме дали тагът е самозатварящ се.
            position = position + 2; // Прескачаме />.
            return element; // Връщаме готовия елемент.
        }
        expect('>'); // Очакваме край на отварящия таг.
        StringBuilder textBuilder = new StringBuilder(); // Събираме текста вътре в елемента.
        while (position < xmlText.length()) { // Въртим, докато не стигнем края.
            if (startsWith("</")) { // Проверяваме дали започва затварящ таг.
                position = position + 2; // Прескачаме </.
                String closingName = readName(); // Четем името на затварящия таг.
                if (!closingName.equals(elementName)) { // Проверяваме дали имената съвпадат.
                    throw new Exception("Отварящият и затварящият таг не съвпадат."); // Даваме грешка.
                }
                skipSpaces(); // Прескачаме интервали.
                expect('>'); // Очакваме символ >.
                element.setText(textBuilder.toString()); // Записваме текста в елемента.
                return element; // Връщаме елемента.
            } else if (startsWith("<")) { // Проверяваме дали започва ново дете.
                XmlElement child = readElement(); // Четем детето рекурсивно.
                element.addChild(child); // Добавяме детето към текущия елемент.
            } else { // Ако не е таг, значи е текст.
                textBuilder.append(xmlText.charAt(position)); // Добавяме символа към текста.
                position++; // Преминаваме към следващия символ.
            }
        }
        throw new Exception("Липсва затварящ таг за: " + elementName); // Ако стигнем края, има грешка.
    }

    private void readAttributes(XmlElement element) throws Exception { // Метод, който чете атрибутите.
        while (position < xmlText.length()) { // Обхождаме символите.
            skipSpaces(); // Прескачаме празни символи.
            if (startsWith(">") || startsWith("/>")) { // Ако тагът приключва, спираме.
                return; // Връщаме се.
            }
            String key = readName(); // Четем името на атрибута.
            skipSpaces(); // Прескачаме интервали.
            expect('='); // Очакваме знак =.
            skipSpaces(); // Прескачаме интервали.
            String value = readAttributeValue(); // Четем стойността на атрибута.
            element.addAttribute(key, value); // Добавяме атрибута към елемента.
        }
    }

    private String readAttributeValue() throws Exception { // Метод, който чете стойност в кавички.
        char quote = xmlText.charAt(position); // Взимаме текущия символ.
        if (quote != '"' && quote != '\'') { // Проверяваме дали е кавичка.
            throw new Exception("Стойността на атрибут трябва да е в кавички."); // Даваме грешка.
        }
        position++; // Прескачаме отварящата кавичка.
        StringBuilder result = new StringBuilder(); // Създаваме StringBuilder за стойността.
        while (position < xmlText.length() && xmlText.charAt(position) != quote) { // Четем до затваряща кавичка.
            result.append(xmlText.charAt(position)); // Добавяме символ.
            position++; // Местим позицията.
        }
        expect(quote); // Очакваме затварящата кавичка.
        return result.toString(); // Връщаме стойността.
    }

    private String readName() { // Метод, който чете име на таг или атрибут.
        skipSpaces(); // Прескачаме празни символи.
        StringBuilder result = new StringBuilder(); // Създаваме StringBuilder за името.
        while (position < xmlText.length()) { // Обхождаме текста.
            char current = xmlText.charAt(position); // Взимаме текущия символ.
            if (Character.isLetterOrDigit(current) || current == '_' || current == '-') { // Проверяваме дали символът е позволен.
                result.append(current); // Добавяме символа към името.
                position++; // Местим позицията.
            } else { // Ако символът не е част от име.
                break; // Спираме цикъла.
            }
        }
        return result.toString(); // Връщаме прочетеното име.
    }

    private void skipSpaces() { // Метод, който прескача интервали и нови редове.
        while (position < xmlText.length() && Character.isWhitespace(xmlText.charAt(position))) { // Докато има празен символ.
            position++; // Местим позицията напред.
        }
    }

    private void expect(char symbol) throws Exception { // Метод, който проверява дали текущият символ е очакваният.
        if (position >= xmlText.length() || xmlText.charAt(position) != symbol) { // Проверяваме дали символът липсва.
            throw new Exception("Очакваше се символ: " + symbol); // Даваме грешка.
        }
        position++; // Прескачаме очаквания символ.
    }

    private boolean startsWith(String value) { // Метод, който проверява дали текстът започва с дадена стойност от текущата позиция.
        return xmlText.startsWith(value, position); // Връщаме резултата от проверката.
    }

    private void makeIdsUnique(XmlElement root) { // Метод, който прави id-тата уникални.
        ArrayList<XmlElement> allElements = new ArrayList<XmlElement>(); // Създаваме списък за всички елементи.
        collectElements(root, allElements); // Събираме всички елементи в списъка.
        for (XmlElement element : allElements) { // Обхождаме всички елементи.
            String currentId = element.getAttribute("id"); // Взимаме id атрибута.
            if (currentId == null || currentId.equals("")) { // Проверяваме дали няма id.
                element.setId(createAutomaticId()); // Даваме автоматично id.
            } else if (usedIds.containsKey(currentId)) { // Проверяваме дали id-то вече е използвано.
                int number = usedIds.get(currentId) + 1; // Увеличаваме брояча за това id.
                usedIds.put(currentId, number); // Записваме новия брой.
                element.setId(currentId + "_" + number); // Добавяме суфикс към id-то.
            } else { // Ако id-то е ново.
                usedIds.put(currentId, 0); // Записваме го като използвано.
                element.setId(currentId); // Оставяме id-то същото.
            }
        }
    }

    private String createAutomaticId() { // Метод, който създава автоматично id.
        String newId = "auto_" + automaticIdCounter; // Създаваме id с брояч.
        automaticIdCounter++; // Увеличаваме брояча.
        while (usedIds.containsKey(newId)) { // Проверяваме дали случайно вече съществува.
            newId = "auto_" + automaticIdCounter; // Правим ново id.
            automaticIdCounter++; // Увеличаваме брояча.
        }
        usedIds.put(newId, 0); // Записваме id-то като използвано.
        return newId; // Връщаме новото id.
    }

    private void collectElements(XmlElement element, ArrayList<XmlElement> list) { // Метод, който събира всички елементи.
        list.add(element); // Добавяме текущия елемент.
        for (XmlElement child : element.getChildren()) { // Обхождаме децата.
            collectElements(child, list); // Рекурсивно добавяме и техните деца.
        }
    }
}
