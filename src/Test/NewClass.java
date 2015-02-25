/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

/**
 *
 * @author Novin Pendar
 */
public class NewClass {

    public static void main(String[] args) {
        List<List<Person>> list = new ArrayList<List<Person>>();
        List<Person> lis = new ArrayList<Person>();
        List<Person> lis1 ;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Person p = new Person(i,j, "P" + i+""+j);
                lis.add(p);
            }
            lis1 = new ArrayList<Person>(lis);
            list.add(lis1);
            
            lis.clear();
        }
        System.out.println("");
        Iterator<List<Person>> itt = list.iterator();
        while(itt.hasNext()){
            List<Person> l1 = itt.next();
            Iterator<Person> it = l1.iterator();
            while(it.hasNext()){
                Person p = it.next();
                System.out.println("ListNO "+ p.listNO+ "  Id "+p.id + "  name "+p.name );
                it.remove();
            }
//            for (Person p : l1) {
//                System.out.println("ListNO "+ p.listNO+ "  Id "+p.id + "  name "+p.name );
//                l1.remove(p);
//            }
            itt.remove();
            System.out.println("-------------------");
        }
        System.out.println("");
    }

    static class Person {

        int id;
        int listNO;
        String name;

        public Person(int listNo,int id, String name) {
            this.id = id;
            this.listNO = listNo;
            this.name = name;
        }

    }
}
