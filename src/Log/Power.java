/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Log;

/**
 *
 * @author Novin Pendar
 */
public class Power {

    public static void main(String[] args) {
        double[] utilization = {0.713700262,
            0.642527201,
            0.78847226,
            0.530423866,
            0.982872534,
            0.663999776,
            0.532588543,
            0.640931722,
            0.563066965,
            0.598157515,
            0.597125128,
            0.721193907,
            0.923026215,
            0.948674174,
            0.657965405,
            0.532065268,
            0.70344705,
            0.666177244,
            0.446223677,
            0.65602957,
            0.563889741,
            0.909870692,
            0.755680481,
            0.648588375

        };
//        double[] utilization = {0.356850131,
//            0.305965334,
//            0.384620615,
//            0.279170456,
//            0.674479869,
//            0.35319137,
//            0.284806707,
//            0.337332485,
//            0.304360522,
//            0.295678455,
//            0.284345299,
//            0.329312286,
//            0.594329539,
//            0.481560494,
//            0.349981598,
//            0.266032634,
//            0.348241114,
//            0.336453154,
//            0.212994595,
//            0.30944791,
//            0.283361679,
//            0.440615347,
//            0.375960438,
//            0.324294188
//        };
        int[] util = new int[utilization.length];
        for (int i = 0; i < utilization.length; i++) {
            util[i] = (int) (utilization[i] * 100);
        }
        double[] pow = new double[utilization.length];
        double[] p = {52.7, 80.5, 90.3, 100, 110, 120, 131, 143, 161, 178, 203};
        double[] powers = new double[100];
        for (int i = 0; i < 100; i++) {

        }

        for (int i = 0; i < utilization.length; i++) {
            int u = (int) (utilization[i] * 100);
            if ((u >= 0) && (u < 10)) {
                pow[i] = getPower(p[0], p[1], u);
            } else if ((u >= 10) && (u < 20)) {
                pow[i] = getPower(p[1], p[2], u);
            } else if ((u >= 20) && (u < 30)) {
                pow[i] = getPower(p[2], p[3], u);
            } else if ((u >= 30) && (u < 40)) {
                pow[i] = getPower(p[3], p[4], u);
            } else if ((u >= 40) && (u < 50)) {
                pow[i] = getPower(p[4], p[5], u);
            } else if ((u >= 50) && (u < 60)) {
                pow[i] = getPower(p[5], p[6], u);
            } else if ((u >= 60) && (u < 70)) {
                pow[i] = getPower(p[6], p[7], u);
            } else if ((u >= 70) && (u < 80)) {
                pow[i] = getPower(p[7], p[8], u);
            } else if ((u >= 80) && (u < 90)) {
                pow[i] = getPower(p[8], p[9], u);
            } else if ((u >= 90) && (u < 100)) {
                pow[i] = getPower(p[9], p[10], u);
            }

        }

        for (int i = 0; i < pow.length; i++) {
            System.out.println(pow[i]);
        }
//        System.out.println((int)(utilization[7]*100));
    }

    private static double getPower(double p, double q, int util) {
        double diff = (q - p) / 10.0;
        int u = util % 10;
        return p + u * diff;
    }

}
