// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.math.distributions;

import yapi.manager.log.Logger;
import yapi.manager.worker.Task;
import yapi.manager.worker.TaskParallelization;
import yapi.manager.worker.WorkerPool;
import yapi.math.NumberUtils;
import yapi.runtime.ThreadUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkCrawler {

    private static void test() {
        WorkerPool workerPool = new WorkerPool(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2);
        for (int i = 100000; i < 100050; i++) {
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            for (int j = 0; j < 10000; j += 500) {
                createTask(workerPool, i + j);
            }
        }

        Logger logger = new Logger();
        //workerPool.registerLogger(logger);
        while (!workerPool.isDone()) {
            if (!logger.hasLog()) {
                ThreadUtils.sleep(1000);
                continue;
            }
            System.out.println(logger.getLog());
        }

        ThreadUtils.sleep(60000);
        if (logger.hasLog()) {
            System.out.println(logger.getLog());
        }
        System.exit(0);
    }

    private static void createTask(WorkerPool workerPool, final long l) {
        workerPool.work(new Task() {
            @Override
            public void run() {
                NumberUtils.factorial(BigInteger.valueOf(l));
            }
        });
    }

    public static void main(String[] args) {
        test();
        try {
            // https://de.wikipedia.org/wiki/Elektroplax
            // https://de.wikipedia.org/wiki/University_of_Cambridge
            // https://de.wikipedia.org/wiki/Adolf_Hitler
            // https://de.wikipedia.org/wiki/Nationalsozialistische_Deutsche_Arbeiterpartei
            LinkCrawler linkCrawler = new LinkCrawler(new URI("https://de.wikipedia.org/wiki/Elektroplax"), new URI("https://de.wikipedia.org/wiki/University_of_Cambridge"));
            linkCrawler.start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private URI start;
    private URI finish;

    private Map<URI, Integer> urlIntegerMap = new HashMap<>();

    private long done = 0;

    public LinkCrawler(URI start, URI finish) {
        this.start = start;
        this.finish = finish;
    }

    public void start() {
        List<URI> toCheck = new ArrayList<>();
        toCheck.add(start);

        int depth = 0;

        WorkerPool workerPool = new WorkerPool(Runtime.getRuntime().availableProcessors() * 16);
        TaskParallelization<URI> parallelization = new TaskParallelization<>();
        while (true) {
            System.out.println((depth) + ": " + toCheck.size());
            done = 0;
            for (int i = 0; i < toCheck.size(); i++) {
                if (urlIntegerMap.containsKey(toCheck.get(i))) {
                    continue;
                }
                urlIntegerMap.put(toCheck.get(i), 1);

                int l = i;
                workerPool.work(new Task() {
                    @Override
                    public void run() {
                        String s = getContent(toCheck.get(l));
                        parallelization.addResult(getURLs(s));
                        increment(toCheck);
                    }
                });
            }
            ThreadUtils.sleep(1);
            workerPool.awaitWorker();
            depth++;

            List<URI> nextURLs = parallelization.merge();
            for (int i = 0; i < nextURLs.size(); i++) {
                if (nextURLs.get(i).toString().equals(finish.toString())) {
                    System.out.println(depth);
                    return;
                }
            }

            for (int i = nextURLs.size() - 1; i >= 0; i--) {
                if (urlIntegerMap.containsKey(nextURLs.get(i)) || nextURLs.get(i).toString().length() == 0 || nextURLs.get(i).toString().contains("wikimedia.org") || !nextURLs.get(i).toString().contains("wikipedia")) {
                    nextURLs.remove(i);
                }
            }

            toCheck.clear();
            toCheck.addAll(nextURLs);
            nextURLs.clear();
            parallelization.clear();
        }
    }

    private void increment(List<URI> uris) {
        done++;

        if (done % 100 == 0) {
            System.out.println("DONE: " + (done + "/" + uris.size()) + "   " + ((int)(done / (uris.size() + 0.0) * 1000) /10.0) + "%");
        }
    }


    private String getContent(URI uri) {
        try {
            try {
                uri.toURL();
            } catch (IllegalArgumentException e) {
                return "";
            }
            URL url = uri.toURL();
            if (uri.getHost() == null) {
                return "";
            }
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
                if (byteArrayOutputStream.size() > 4194304) {
                    System.out.println(uri.toString());
                }
            }
            return new String(byteArrayOutputStream.toByteArray(), encoding);
        } catch (IOException e) {
            return "";
        }
    }

    private List<URI> getURLs(String s) {
        List<URI> urls = new ArrayList<>();

        StringBuilder st = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (st.length() != 0) {
                if (s.charAt(i) == '"') {
                    try {
                        new URL(st.toString().substring(1));
                        urls.add(new URI(st.toString().substring(1)));
                    } catch (URISyntaxException | MalformedURLException e) {

                    }
                    st = new StringBuilder();
                } else {
                    st.append(s.charAt(i));
                }
                continue;
            }
            if (i < 6) {
                continue;
            }
            if (s.charAt(i) != '"') {
                continue;
            }
            if (s.charAt(i - 1) != '=') {
                continue;
            }
            if (s.charAt(i - 2) != 'f') {
                continue;
            }
            if (s.charAt(i - 3) != 'e') {
                continue;
            }
            if (s.charAt(i - 4) != 'r') {
                continue;
            }
            if (s.charAt(i - 5) != 'h') {
                continue;
            }
            st.append(s.charAt(i));
        }

        return urls;
    }

}