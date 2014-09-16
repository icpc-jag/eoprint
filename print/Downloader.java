package print;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader {
    private static final Set<String> names;
    private static final ArrayBlockingQueue<String> logQueue = new ArrayBlockingQueue<>(10);
    private static final File logPath = new File("/var/tmp/eoprint-table");

    static {
        if (logPath.isFile()) {
            try (final ObjectInputStream is = new ObjectInputStream(
                    new BufferedInputStream(new FileInputStream(logPath)))) {
                names = (Set<String>) is.readObject();
                for (final String name : names)
                    logQueue.offer("Ignored:\t" + name);
            } catch (IOException e) {
                throw null;
            } catch (ClassNotFoundException e) {
                throw null;
            }
        } else
            names = new HashSet<>();
    }

    private static final String ps = Pattern.quote("<a href=\"") + "(.+\\.ps)" + Pattern.quote("\">")
            + "\\1" + Pattern.quote("</a>");
    private static final Pattern p = Pattern.compile(ps);
    private static Timer timer = new Timer(true);

    private static void print(final URL name) throws IOException {
        try (final BufferedOutputStream os = new BufferedOutputStream(
                Runtime.getRuntime().exec("lpr").getOutputStream());
             final BufferedInputStream is = new BufferedInputStream(
                     name.openStream())) {
            for (int b = is.read(); b >= 0; b = is.read())
                os.write(b);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void download(final String s) {
        if (s.isEmpty())
            return;
        logQueue.offer("Synchronizing:\t" + s);
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(new URL(s).openStream()))) {
            final StringBuilder sb = new StringBuilder();
            for (String line = br.readLine(); line != null; line = br.readLine())
                sb.append(line).append('\n');
            System.err.println(sb);
            final Matcher m = p.matcher(sb);
            while (m.find()) {
                final String name = m.group(1);
                synchronized (names) {
                    if (!names.contains(name)) {
                        logQueue.offer("Printing:\t" + name);
                        names.add(name);
                        try (final ObjectOutputStream os = new ObjectOutputStream(
                                new BufferedOutputStream(new FileOutputStream(logPath)))) {
                            os.writeObject(names);
                        } catch (IOException e) {
                        }

                        try {
                            print(new URL(s + '/' + name));
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            // some error window
        } catch (IOException e) {
            return;
        }
    }

    public static synchronized void submitAutoPrintTask(final String s, final int interval) {
        if (s.isEmpty())
            return;
        timer.cancel();
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                download(s);
            }
        }, 0L, interval);
    }

    public static synchronized void cancelAutoPrintTask() {
        timer.cancel();
    }

    public static synchronized void clear() {
        if (logPath.exists())
            logPath.delete();
        names.clear();
    }

    public static String poll() {
        return logQueue.poll();
    }
}