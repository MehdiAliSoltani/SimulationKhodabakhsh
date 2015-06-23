/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Novin Pendar
 */
public class NewClass3 {

    public static void main(String[] args) {
//        double value = 3.25;
//        double fractionalPart = value % 1;
//        double integralPart = value - fractionalPart;
//        System.out.println(integralPart);
//        System.out.println(fractionalPart);
////        Scanner s = new Scanner(System.in);
//        Scanner in = new Scanner(System.in);

//int i = in.nextInt();
//String s = in.next();
//        System.out.println(s);

        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println(r.nextInt(100));
        }
        
    }

}
