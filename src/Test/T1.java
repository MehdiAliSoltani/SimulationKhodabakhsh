/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.cloudbus.cloudsim.distributions.ExponentialDistr;
import org.cloudbus.cloudsim.distributions.ParetoDistr;
import org.cloudbus.cloudsim.distributions.UniformDistr;

/**
 *
 * @author Novin Pendar
 */
public class T1 {

    public static void st(){
        System.out.println("000000");
    }
    
    static Map< Integer, El[]> l = new LinkedHashMap<Integer, El[]>();

    public static void main(String[] args) {
        El[] el = new El[3];
        for (int i = 0; i < el.length; i++) {
            el[i] = new El(i, i + 3);
//                    el[i].setI(i);
//                    el[i].setJ(i+3);
        }

        l.put(100, el);
        El[] ell = l.get(100);
        for (int i = 0; i < ell.length; i++) {
            System.out.println(ell[i].getI() + "   " + ell[i].getJ());
        }
        
        ell[1].setI(9);
        l.replace(100, ell);
        for (int i = 0; i < ell.length; i++) {
            System.out.println(ell[i].getI() + "   " + ell[i].getJ());
        }

        int even=0, odd=0;
        Random r = new Random();
        for (int i = 0; i < 100000; i++) {
            if(r.nextInt(100)% 2 == 0)
                even++;
            else
                odd++;
          
        }
        System.out.println("even: "+even + "  "+" odd: "+odd);
        int seed = 50000;
        ExponentialDistr ex = new ExponentialDistr( seed);
//        ParetoDistr pr = new ParetoDistr( 1, 100000);
//        UniformDistr ud = new UniformDistr(2000, 10000000);
/*        
        double avg = 0;
        int no = 100;
        for (int i = 0; i < no; i++) {
            double ran = ex.sample();
            avg +=ran;
            System.out.println(ran);
        }
        System.out.println("------");
        System.out.println(avg/no);
        */
        System.out.println(log(1.333, 2));
        System.out.println(log2(1.333));
        System.out.println(log2(4.0/3.0));
        System.out.println(fact(5));
    }
 static    double log2(double x) {
     return Math.log(x)/Math.log(2.0d);
}

 static  double fact(int n){
        double f=1;
        for (int i = n; i >0 ; i--) {
            f = f* i;
        }
        return f;
    }
    static class El {

        int i;
        int j;

        public El(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getJ() {
            return j;
        }

        public void setJ(int j) {
            this.j = j;
        }

    }
    static double log(double x, int base)
{
    return (double) (Math.log(x) / Math.log(base));
}
}
