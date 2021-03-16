package Lab2;

import java.util.Scanner;

public class Lab2 {

        public static void main(String[] args) {

                Point3d p1 = new Point3d(1, 2, 1);
                Point3d p2 = new Point3d();
                Point3d p3 = writePoint();

            if (p1.equals(p2) || p1.equals(p3) || p2.equals(p3)) {
                System.out.println("Some point are equals");
            } else {
                System.out.println("Area: " + computeArea(p1, p2, p3));
            }
        }

        public static double computeArea(Point3d p1, Point3d p2, Point3d p3) {

            double a = p1.distanceTo(p2);
            double b = p1.distanceTo(p3);
            double c = p2.distanceTo(p3);

            double p = (a + b + c) / 2;
            return Math.sqrt(p * (p - a) * (p - b) * (p - c));

        }

        private static Point3d writePoint() {
            Scanner input = new Scanner(System.in);

            System.out.println("Enter x:");
            double x = input.nextDouble();
            System.out.println("Enter y:");
            double y = input.nextDouble();
            System.out.println("Enter z:");
            double z = input.nextDouble();

            return new Point3d(x, y, z);
        }
    }
