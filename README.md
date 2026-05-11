XML Parser 

Стартиране:
1. Отвори терминал в папка src.
2. Компилирай:
   javac *.java
3. Стартирай:
   java Main

Примерни команди:
open ../examples/people.xml
print
children auto_1
child auto_1 0
select 0 id
set 0 city Sofia
text auto_2
xpath auto_1 person/address
xpath auto_1 person[0]/name
xpath auto_1 person(@id)
xpath auto_1 person(address="USA")/name
save as ../examples/result.xml
close
exit

