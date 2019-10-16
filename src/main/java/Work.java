import yapi.datastructures.PrefixArray;

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

        /*long time = System.currentTimeMillis();
        System.out.println(NumberUtils.primeFactorization(3412613467L));
        System.out.println(System.currentTimeMillis() - time + "ms");
         */


        System.out.println(new PrefixArray<Integer>("[0 10]").foldInteger('+'));
    }

}
