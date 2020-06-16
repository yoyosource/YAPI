package yapi.sorting;

import java.util.Comparator;

public interface Sort<T> {

    void sort(Comparator<T> comparator);

    void sortReversed(Comparator<T> comparator);

    void reverse();

    T[] getArray();

    void setArray(T... ts);

    void setHook(SortingHook<T> sortingHook);

}
