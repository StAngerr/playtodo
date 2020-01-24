package utils.collections.interfaces;


import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MyListInterface<T> extends Collection<T> {
    @Override
    boolean add(T t);
    public Array getAll();
    public Optional<T> getIf(Predicate<T> filter);
    public boolean addIf(Predicate<T> filter, T value);
    public boolean removeIf(Predicate<? super T> filter);
    public T find(Function<T, Boolean> comparator);
    public boolean updateByIndex(int idx, T data);
}
