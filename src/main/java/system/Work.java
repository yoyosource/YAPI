/**
 * Copyright 2019,2020 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package system;

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

        /*
        long x = 0;
        NumberRandom numberRandom = new NumberRandom();
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            long t = numberRandom.getInt();
            if (t == x) {
                System.out.println(i + " " + t);
            } else {
                x = t;
            }
            if (i % 1000000000 == 0) {
                System.out.println(i);
            }
        }*/

        /*
        //Brainfuck brainfuck = new Brainfuck("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.+++.");
        //Brainfuck brainfuck = new Brainfuck(">+++++>++++[<->-]<[<]>:", true);
        //Brainfuck brainfuck = new Brainfuck("+:>>+<<[[->+<]>>[-<+>]<:[-<+>>+<]<]", true);
        //Brainfuck brainfuck = new Brainfuck(">>>>>>>>-<<<<<<<<+[->+]-=", true);
        //Brainfuck brainfuck = new Brainfuck("#>#<[->>+<<]>[->+<]>:[-]<<", true);
        //Brainfuck brainfuck = new Brainfuck(">++++++[<++++++++>-][-]+++++++++++> Indexing , Input <<[->>->+<<<]>>> Removing Number 48 to check the number [-<<<+>>>]<< Resetting Number [->>+<[->>+>+<<<]>>>[-<<<+>>>]<<<<]>>[-<<+>>]", true);
        //Brainfuck brainfuck = new Brainfuck(">>>+[[-]>>[-]++>+>+++++++[<++++>>++<-]++>>+>+>+++++[>++>++++++<<-]+>>>,<++[[>[->>]<[>>]<<-]<[<]<+>>[>]>[<+>-[[<+>-]>]<[[[-]<]++<-[<+++++++++>[<->-]>>]>>]]<<]<]<[[<]>[[>]>>[>>]+[<<]<[<]<+>>-]>[>]+[->>]<<<<[[<<]<[<]+<<[+>+<<-[>-->+<<-[>+<[>>+<<-]]]>[<+>-]<]++>>-->[>]>>[>>]]<<[>>+<[[<]<]>[[<<]<[<]+[-<+>>-[<<+>++>-[<->[<<+>>-]]]<[>+<-]>]>[>]>]>[>>]>>]<<[>>+>>+>>]<<[->>>>>>>>]<<[>.>>>>>>>]<<[>->>>>>]<<[>,>>>]<<[>+>]<<[+<<]<]");
        //Brainfuck brainfuck = new Brainfuck(">>>+[,>+++++++[<------>-]<[->+>+<<]>>[-<<+>>]<->+<[>-<[-]]>[-<<[-]+++++>>>>>]<<[->+>+<<]>>[-<<+>>]<-->+<[>-<[-]]>[-<<[-]++++++++>>>>>]<<[->+>+<<]>>[-<<+>>]<--->+<[>-<[-]]>[-<<[-]++++++>>>>>]<<[->+>+<<]>>[-<<+>>]<---->+<[>-<[-]]>[-<<[-]+++++++>>>>>]<<[->+>+<<]>>[-<<+>>]++++++[<--->-]+<[>-<[-]]>[-<<[-]++++>>>>>]<<[->+>+<<]>>[-<<+>>]+++++[<---->-]+<[>-<[-]]>[-<<[-]+++>>>>>]<<[->+>+<<]>>[-<<+>>]+++++++[<------->-]+<[>-<[-]]>[-<<[-]+>>>>>]<<[->+>+<<]>>[-<<+>>]+++++++[<------->-]<-->+<[>-<[-]]>[-<<[-]++>>>>>]<++++[<---->-]<]<<<[<<<]>>>[-->+<[>-]>[>]<<++>[-<<<<<+[>-->+<[>-]>[-<<+>>>]<<+>+<[>-]>[-<<->>>]<<+<[-<<<+>>>]<<<]>>>>>]<->+<[>-]>[>]<<+>[-<->>>[>>>]>[->+>>+<<<]>[-<+>]>>[-[->>+<<]+>>]+>[->+<]>[-<+>>>[-]+<<]+>>[-<<->>]<<<<[->>[-<<+>>]<<<<]>>[-<<<<<+>>>>>]<<<<<<<[>>[-<<<+>>>]<<<<<]+>>[>-->+<[>-]>[-<<->>>]<<+>+<[>-]>[-<<+>>>]<<+<[->>>+<<<]>>>]<]<--->+<[>-]>[->>[>>>]>+<<<<[<<<]>>]<<->+<[>-]>[->>[>>>]>-<<<<[<<<]>>]<<->+<[>-]>[->>[>>>]>[->+>>+<<<]>[-<+>]>>[-[->>+<<]+>>]+>+<[-<<]<<<<<[<<<]>>]<<->+<[>-]>[->>[>>>]>[->+>>+<<<]>[-<+>]>>[-[->>+<<]+>>]+>-<[-<<]<<<<<[<<<]>>]<<->+<[>-]>[->>[>>>]>[->+>>+<<<]>[-<+>]>>[-[->>+<<]+>>]+>.<[-<<]<<<<<[<<<]>>]<<->+<[>-]>[->>[>>>]>[->+>>+<<<]>[-<+>]>>[-[->>+<<]+>>]+>,<[-<<]<<<<<[<<<]>>]<<++++++++>>>]");
        Brainfuck brainfuck = new Brainfuck(new File("/Users/jojo/IdeaProjects/YAPI/src/main/resources/test.b"));
        //brainfuck.setOutput(true);
        //brainfuck.setSlowdown(1);
        System.out.println(brainfuck.getCode());
        brainfuck.run();
        System.out.println("\n" + brainfuck.getExecutionTime() + "ms");
        */

        /*
        long longest = 0;
        long lenght = 0;

        long start = 10000000L * 16;
        start = 10000000L * 0;

        for (long i = start; i < start + 10000000; i++) {
            List<Long> longs = Collatz.computeFast(i);
            if (lenght < longs.size()) {
                lenght = longs.size();
                longest = i;
            }
        }

        System.out.println(longest + " " + lenght);*/

        /*
        List<Byte> bytes = Collatz.computeCompressed(989345275647L);
        System.out.println(bytes.size());
        System.out.println(bytes);*/

        /*
        LongTree longTree = Collatz.reverseCompute(8);
        System.out.println(longTree);*/

        /*
        JFrame frame = new JFrame("Test");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        YAPIWindow yapiWindow = new YAPIWindow(new File("/Users/jojo/IdeaProjects/YAPI/src/main/resources/Animation.uiconfig"));
        yapiWindow.addSelector("test", "Hello");
        yapiWindow.setUpdateLife(true);
        frame.setContentPane(yapiWindow);

        frame.setVisible(true);
        frame.invalidate();*/

        /*
        WorkerPool workerPool = new WorkerPool(20, 160);
        for (int i = 0; i < 320; i++) {
            workerPool.work(new Task() {
                @Override
                public void run() {
                        System.out.println("TEST  " + getTaskID());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            workerPool.work(new Task() {
                @Override
                public void run() {
                        System.out.println("HALLO " + getTaskID());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        workerPool.await();
        System.out.println(workerPool.toString());
        workerPool.close();
        System.out.println(Thread.activeCount());
        System.out.println(workerPool.getLog());*/

        /*Node.setCompact(false);

        Node node1 = new Node("Test1");
        Node node2 = new Node("Test2");
        Node node3 = new Node("Test3");
        Node node4 = new Node("Test4");
        Node node5 = new Node("Test5");
        node1.addConnection(node2);
        node1.addConnection(node3);
        node3.addConnection(node2);
        node2.addConnection(node3);
        node2.addConnection(node1);
        node1.addConnection(node1);
        node1.addConnection(node4);
        node3.addConnection(node4);
        node4.addConnection(node4);
        node4.addConnection(node2);
        node4.addConnection(node5);

        Graph graph = new Graph(node1, node2, node3, node4, node5);
        graph.setOutput(true);
        System.out.println(graph.ascii());

        System.out.println(node1);*/
    }

}