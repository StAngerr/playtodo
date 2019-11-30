package utils.collections;


import utils.collections.interfaces.MyListInterface;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MyList<T> implements MyListInterface<T> {
    private Object[] list;
    private final int arrayMaxValue = Integer.MAX_VALUE - 5;
    private int currentIndex;

    public MyList(int initialCapacity) {
        super();
        list = new Object[initialCapacity];
        currentIndex = 0;
    }

    public MyList() {
        super();
        list = new Object[10];
        currentIndex = 0;
    }

    @Override
    public int size() {
        return currentIndex;
    }

    @Override
    public boolean isEmpty() {
        return currentIndex == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i <= currentIndex; i++) {
            if (o == null && list[i] == null) {
                return true;
            } else if (o.equals(list[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MyIterator iterator() {
        return new MyIterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (Object item: list) {
            action.accept((T) item);
        }
    }

    @Override
    public Object[] toArray() {
        return list.clone();
    }

    public boolean add(T o) {
        list[currentIndex] = o;
        currentIndex += 1;
        if (list.length - 1 == currentIndex) {
            doubleCollection();
            // expandCollection();
        }
        return true;
    }

    @Override
    public Array getAll() {
        return null;
    }


    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            for (int i = index; i < list.length - 1; i++) {
                list[i] = list[i + 1];
            }
            return true;
        }
        return false;
    }

    public int indexOf(Object o) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null && list[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(item -> add(item));
        return true;
    }

    @Override
    public void clear() {
        list = new Object [list.length];
    }

    @Override
    public boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for(Object item: list) {
            sum += item.hashCode();
        }
        return sum + list.length;
    }

    @Override
    public boolean retainAll(Collection c) {
        Object[] result = (T[]) new Object[list.length];
        int index = 0;
        for (Object item : list) {
            if (this.contains(item)) {
                result[index++] = item;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(o -> {
            if(this.contains(o)) {
                remove(o);
            }
        });
        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        for (Object item : list) {
            if (!this.contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object[] toArray(Object[] a) {
        return list.clone();
    }

    @Override
    public Optional<T> getIf(Predicate<T> filter) {
        for (int i = 0; i <= currentIndex; i++) {
            if (filter.test((T) list[i])) {
                return Optional.of((T) list[i]);
            }
        }
        return Optional.empty();
    }


    @Override
    public boolean addIf(Predicate<T> filter, T value) {
        for (Object item : list) {
            if (filter.test((T) item)) {
                add(value);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        for (int i = 0; i < currentIndex; i++) {
            if (filter.test((T) list[i])) {
                this.remove(list[i]);
                currentIndex--;
            }
        }
        return true;
    }

    private void doubleCollection() {
        int currentLength = list.length;
        int newSize = currentLength * 2;
        Object[] extended;
        // if current array length is of max value
        if (currentLength == arrayMaxValue) {
            return;
        }
        // if new size exceeds max value and current array length is not max size
        else if (newSize > arrayMaxValue) {
            extended = new Object[arrayMaxValue];
        } else {
            extended = new Object[newSize];
        }
        for (int i = 0; i < list.length; i ++ ) {
            extended[i] = list[i];
        }
        list = extended;
    }

    private void expandCollection() {
        Object[] expanded =  new Object[list.length + 1];
        for (int i = 0; i < list.length; i++) {
            expanded[i] = list[i];
        }
        list = expanded;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        int i = 0;
        while(i != currentIndex) {
            res.append(" ").append(list[i]).append(" |");
            i++;
        }
        return res.toString();
    }

    private class MyIterator<T> implements Iterator<T> {
        private int currentElement = 0;
        public MyIterator() {}

        @Override
        public boolean hasNext() {
            return currentElement < currentIndex;
        }

        @Override
        public T next() {
            if (hasNext()) {
                return (T) list[currentElement++];
            }

            return null;
        }
    }
}
