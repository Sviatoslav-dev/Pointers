package sample;

public class Pair {
    private final int first; //Перший елемент
    private final int second; //Другий елемент

    Pair(int first, int second) { //Конструктор
        this.first = first;
        this.second = second;
    }

    int getFirst() { //Отримати перший елемент
        return first;
    }

    int getSecond() { //Отримати другий елемент
        return second;
    }
}
