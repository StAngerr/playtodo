package utils.collections;


import utils.collections.interfaces.MyListInterface;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
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
        for (int i = 0; i < currentIndex; i++) {
            if (o == null && list[i] == null) {
                return true;
            } else if (o != null && o.equals(list[i])) {
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
        System.out.println("For each first " + this.size());
        for (int i = 0; i < currentIndex; i++) {
            action.accept((T)list[i]);
        }
    }

    @Override
    public T[] toArray() {
        Object[] asAr = new Object[currentIndex];
        for (int i = 0; i < currentIndex; i++) {
            asAr[i] = list[i];
        }
        return (T[]) asAr;
    }

    public synchronized boolean add(T o) {
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
        System.out.println("Remove ethods");
        int index = indexOf(o);
        if (index != -1) {
            synchronized(this) {
                if (indexOf(o) != -1) {
                    System.out.println("Removing");
                    for (int i = index; i < list.length - 1; i++) {
                        list[i] = list[i + 1];
                    }
                    currentIndex--;
                    return true;
                } else {
                    System.out.println("Remove inner else");
                    return false;
                }
            }
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
        System.out.println("Enter " + list.length + " " + this.size());
        c.forEach(item -> {
            add(item);
        });
        System.out.println("Exit" + list.length + " " + this.size());

        return true;
    }

    @Override
    public void clear() {
        list = new Object [list.length];
        currentIndex = 0;
    }

    @Override
    public boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for(int i = 0; i < currentIndex; i++) {
            sum += list[i].hashCode();
        }
        return sum + currentIndex;
    }

    @Override
    public synchronized boolean retainAll(Collection c) {
        Object[] result = (T[]) new Object[c.size()];
        int index = 0;
        for (Object item : c) {
            if (this.contains(item)) {
                result[index++] = item;
            }
        }
        list = result;
        currentIndex = index;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Collection col = c == this ? this.clone() : c;
        col.forEach(o -> {
            if(this.contains(o)) {
                // synchronized (this) {
                remove(o);
                // }
            }
        });
        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        for (Object item: c ) {
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
            }
        }
        return true;
    }

    @Override
    public T find(Function<T, Boolean> comparator) {
        for (int i = 0; i < currentIndex; i++) {
            if (comparator.apply((T) list[i])) {
                return (T) list[i];
            }
        }
        return null;
    }

    @Override
    public boolean updateByIndex(int idx, T data) {
        if (idx < currentIndex) {
            list[idx] = data;
            return true;
        }
        return false;
    }

    public MyList clone(MyList c) {
        MyList newCollection = new MyList();
        c.forEach(item -> newCollection.add(item));
        return newCollection;
    }

    public MyList clone() {
        MyList newCollection = new MyList();
        this.forEach(item -> newCollection.add(item));
        return newCollection;
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
    public synchronized String toString() {
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
