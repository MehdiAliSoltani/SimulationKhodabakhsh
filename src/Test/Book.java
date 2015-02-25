/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import java.util.Comparator;

/**
 *
 * @author Novin Pendar
 */
//public class Book implements Comparable<Book> {
//public class Book implements Comparator<Book> {
public class Book {
    int id;
    public String name, author, publisher;
    public Book(String name, int id, String author, String publisher) {
        this.name = name;
        this.id = id;
        this.author = author;
        this.publisher = publisher;
    }
    public String toString() {
        return ("(" + name + ", " + id + ", " + author + ", " + publisher + ")");
    }
}
//    
//    @Override
//    public int compareTo(Book o) {
//        // usually toString should not be used,
//        // instead one of the attributes or more in a comparator chain
//        
//        return toString().compareTo(o.toString());
//    }
/*
    @Override
    public int compare(Book o1, Book o2) {
        if(o1.id > o2.id ) 
            return 1;
        else if(o1.id < o2.id )
            return -1;
        else return 0;
    }
}*/
/*
@Test
public void sortBooks() {
    Book[] books = {
            new Book("foo", "1", "author1", "pub1"),
            new Book("bar", "2", "author2", "pub2")
    };

    // 1. sort using Comaprable
    Arrays.sort(books);
    System.out.println(Arrays.asList(books));

    // 2. sort using comaprator: sort by id
    Arrays.sort(books, new Comparator<Book>() {
        @Override
        public int compare(Book o1, Book o2) {
            return o1.id.compareTo(o2.id);
        }
    });
    System.out.println(Arrays.asList(books));
}
*/