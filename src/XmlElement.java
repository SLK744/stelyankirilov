import java.util.ArrayList; // Импортираме ArrayList, защото ще пазим списък с деца.
import java.util.HashMap; // Импортираме HashMap, защото ще пазим атрибутите като ключ и стойност.
import java.util.Map; // Импортираме Map, за да обхождаме атрибутите по-лесно.

/**
 * Класът XmlElement представя един XML елемент.
 * Пример: <person id="1">...</person>
 */
public class XmlElement {
    private String name; // Името на елемента, например person или name.
    private String id; // Уникалният идентификатор на елемента.
    private String text; // Текстът вътре в елемента, ако има такъв.
    private XmlElement parent; // Родителят на текущия елемент.
    private ArrayList<XmlElement> children; // Списък с вложени елементи.
    private HashMap<String, String> attributes; // Атрибутите на елемента.

    public XmlElement(String name) { // Конструктор, който създава нов XML елемент.
        this.name = name; // Записваме името на елемента.
        this.id = ""; // Първоначално няма id.
        this.text = ""; // Първоначално няма текст.
        this.parent = null; // Първоначално няма родител.
        this.children = new ArrayList<XmlElement>(); // Създаваме празен списък с деца.
        this.attributes = new HashMap<String, String>(); // Създаваме празна карта с атрибути.
    }

    public String getName() { // Метод, който връща името на елемента.
        return name; // Връщаме името.
    }

    public String getId() { // Метод, който връща id-то на елемента.
        return id; // Връщаме id-то.
    }

    public void setId(String id) { // Метод, който променя id-то на елемента.
        this.id = id; // Записваме новото id.
        this.attributes.put("id", id); // Записваме id и като XML атрибут.
    }

    public String getText() { // Метод, който връща текста на елемента.
        return text; // Връщаме текста.
    }

    public void setText(String text) { // Метод, който променя текста на елемента.
        this.text = text.trim(); // Записваме текста без излишни интервали в началото и края.
    }

    public XmlElement getParent() { // Метод, който връща родителя на елемента.
        return parent; // Връщаме родителя.
    }

    public void setParent(XmlElement parent) { // Метод, който задава родител.
        this.parent = parent; // Записваме подадения родител.
    }

    public ArrayList<XmlElement> getChildren() { // Метод, който връща децата.
        return children; // Връщаме списъка с деца.
    }

    public HashMap<String, String> getAttributes() { // Метод, който връща всички атрибути.
        return attributes; // Връщаме картата с атрибути.
    }

    public void addChild(XmlElement child) { // Метод, който добавя дете към елемента.
        child.setParent(this); // Казваме на детето кой е неговият родител.
        children.add(child); // Добавяме детето в списъка.
    }

    public void addAttribute(String key, String value) { // Метод, който добавя атрибут.
        attributes.put(key, value); // Записваме атрибута в HashMap.
        if (key.equals("id")) { // Проверяваме дали атрибутът е id.
            id = value; // Ако е id, записваме го и в полето id.
        }
    }

    public String getAttribute(String key) { // Метод, който връща стойност на атрибут.
        return attributes.get(key); // Връщаме стойността по ключ.
    }

    public void setAttribute(String key, String value) { // Метод, който променя или добавя атрибут.
        attributes.put(key, value); // Записваме новата стойност.
        if (key.equals("id")) { // Проверяваме дали променяме id.
            id = value; // Обновяваме и полето id.
        }
    }

    public void removeAttribute(String key) { // Метод, който изтрива атрибут.
        attributes.remove(key); // Премахваме атрибута от HashMap.
        if (key.equals("id")) { // Проверяваме дали е изтрито id.
            id = ""; // Зануляваме id-то.
        }
    }

    public XmlElement getChildByNumber(int number) { // Метод, който връща n-то дете.
        if (number < 0 || number >= children.size()) { // Проверяваме дали номерът е невалиден.
            return null; // Ако е невалиден, връщаме null.
        }
        return children.get(number); // Връщаме детето на тази позиция.
    }

    public void printAttributes() { // Метод, който отпечатва атрибутите на елемента.
        if (attributes.size() == 0) { // Проверяваме дали няма атрибути.
            System.out.println("No attributes."); // Извеждаме съобщение.
            return; // Спираме метода.
        }
        for (Map.Entry<String, String> entry : attributes.entrySet()) { // Обхождаме всички атрибути.
            System.out.println(entry.getKey() + " = " + entry.getValue()); // Отпечатваме ключ и стойност.
        }
    }
}
