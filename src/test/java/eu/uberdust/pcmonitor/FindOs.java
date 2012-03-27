package eu.uberdust.pcmonitor;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/28/12
 * Time: 12:54 AM
 */
public class FindOs {
    public static void main(final String[] args) {
        final String osName = System.getProperty("os.name");
        System.out.println(osName);
        final String osArch = System.getProperty("os.arch");
        System.out.println(osArch);
        final String osVersion = System.getProperty("os.version");
        System.out.println(osVersion);

    }
}
