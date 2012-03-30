package eu.uberdust.pcmonitor.jobs.unix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/30/12
 * Time: 11:28 AM
 */
public class SceenSaverTest {
    public static void main(final String[] args) {
        try {
            while (true) {
                final Process runtime = Runtime.getRuntime().exec("gnome-screensaver-command --query");
                runtime.waitFor();
                InputStreamReader stream = new InputStreamReader(runtime.getInputStream());
                final BufferedReader reader = new BufferedReader(stream);
                String line = reader.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = reader.readLine();

                }

                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
