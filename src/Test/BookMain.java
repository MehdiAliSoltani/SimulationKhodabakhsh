/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Novin Pendar
 */
public class BookMain {
    public static void main(String[] args) {
            Book[] books = {
            new Book("foo", 22, "author1", "pub1"),
            new Book("foo", 14, "author1", "pub1"),
            new Book("foo", 6, "author1", "pub1"),
            new Book("foo", 1, "author1", "pub1"),
            new Book("foo", 20, "author1", "pub1"),
            new Book("bar", 11, "author2", "pub2")
    };

    // 1. sort using Comaprable
//    Arrays.sort(books);
//    System.out.println(Arrays.asList(books));

    // 2. sort using comaprator: sort by id
    Arrays.sort(books, new Comparator<Book>() {
        @Override
        public int compare(Book o1, Book o2) {
            if(o1.id > o2.id ) 
            return 1;
        else if(o1.id < o2.id )
            return -1;
        else return 0;
        }
    });
    System.out.println(Arrays.asList(books));

    }
}
