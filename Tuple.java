package converter;

public class Tuple<T>  {
    private T[] values;
    public static <T> Tuple<T> of(T ... values){
        return new Tuple(values);
    }

    public Tuple(T[] values) {
        this.values = values;
    }

    private int size(){
        return values.length;
    }
    public T first(){
        return values[0];
    }

    public T last(){
        return values[size() - 1 ];
    }

}
