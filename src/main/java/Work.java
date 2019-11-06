import yapi.datastructures.IntegerBuffer;
import yapi.datastructures.PrefixArray;
import yapi.graphics.Renderer;
import yapi.graphics.elements.EPlane;
import yapi.math.*;

import javax.swing.*;
import java.awt.*;

public class Work {

    public static void main(String[] args) {
        /*
        int times = 10;
        long time = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            NumberUtils.primeFactorization(1234567890123456789L);
        }
        System.out.println(((double)(System.currentTimeMillis() - time) / times) + "ms");

        time = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            NumberUtils.primeFactorization(Long.MAX_VALUE);
        }
        System.out.println(((double)(System.currentTimeMillis() - time) / times) + "ms");
        */

        /*
        List<Long> ls = NumberUtils.getPrimes(50);
        long l = NumberUtils.multiply(ls);
        System.out.println(l + " -> " + ls);

        System.out.println(NumberUtils.primeFactorization(l));
        List<Long> longs = NumberUtils.getDivisors(l);
        Collections.sort(longs);
        System.out.println(longs);

        //System.out.println(NumberUtils.primeFactorization(1307674368000L));
        //System.out.println(NumberUtils.getDivisors(1307674368000L));
        */

        /*
        long time = System.currentTimeMillis();
        System.out.println(NumberUtils.primeFactorization(3412613467L));
        System.out.println(System.currentTimeMillis() - time + "ms");
        */

        /*
        System.out.println(NumberUtils.getRange("0...20\\{0>..2, 7, 5...9\\{6, 7}}|-20...20#-10...10"));
        System.out.println(NumberUtils.simplifyRange("0...20\\{0>..2, 7, 5...9\\{6, 7}}|-20...20#-10...10"));

        System.out.println(NumberUtils.getRange("0...2758\\{619>.<621\\{620}}"));
        System.out.println(NumberUtils.simplifyRange("0...2758\\{619>.<621\\{620}}"));

        System.out.println(new PrefixArray<Long>("0...2758\\{619>.<621\\{620}}").foldLong('+'));
        System.out.println(NumberUtils.isPrime(3804661));
        System.out.println(NumberUtils.primeFactorization(3804661));
        System.out.println(NumberUtils.getDivisors(3804661));
        //System.out.println(NumberUtils.getRange("0...20\\{0>..2, 7, 5...9\\{6, 7}}"));
        //System.out.println(NumberUtils.simplifyRange("0...20\\{0>..2, 7, 5...9\\{6, 7}}"));
        */

        /*
        JFrame frame = new JFrame("Test");
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 4 * 3, Toolkit.getDefaultToolkit().getScreenSize().height / 4 * 3);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Renderer renderer = new Renderer();
        frame.setContentPane(renderer);
        frame.validate();

        EPlane ePlane = new EPlane(new Color(100, 0, 0), new Vector(100, 100, 100), new Vector(10, 20, 0), new Vector(50, 10, 20));
        renderer.addElement(ePlane);
        */

        //System.out.println(new Range("...10\\{10, 5...}").in(3));

        int x = 0;
        NumberRandom numberRandom = new NumberRandom();
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            int t = numberRandom.getInt();
            if (t == x) {
                System.out.println(i + " " + t);
            } else {
                x = t;
            }
            if (i % 100000000 == 0) {
                System.out.println(i);
            }
        }
    }

}
