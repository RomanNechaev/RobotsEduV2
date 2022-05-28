package logic;

public class Pair<T> implements Comparable<T> {
    private final T x;
    private final T y;

    public Pair(T x, T y){
        this.x = x;
        this.y = y;
    }

    public T getX(){
        return this.x;
    }

    public T getY(){
        return this.y;
    }

    @Override
    public int compareTo(T o) {
        return 0;
    }
}
