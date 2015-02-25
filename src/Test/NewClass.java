/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.util.ArrayList;
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

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Person p = new Person(i,j, "P" + i+""+j);
                lis.add(p);
            }
            list.add(lis);
            lis.clear();
        }
        for(List<Person> l1 : list){
            for (Person p : l1) {
                System.out.println("ListNO "+ p.listNO+ "  Id "+p.id + "  name "+p.name );
            }
        }
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
