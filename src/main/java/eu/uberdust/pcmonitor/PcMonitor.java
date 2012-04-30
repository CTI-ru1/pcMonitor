package eu.uberdust.pcmonitor;

import eu.uberdust.network.NetworkManager;
import eu.uberdust.pcmonitor.task.UnixTask;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Timer;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/26/12
 * Time: 9:06 PM
 */
public final class PcMonitor {
    /**
     * LOGGER.
     */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PcMonitor.class);
    /**
     * Sampling interval.
     */
    private static final long INTERVALL = 30 * 60 * 1000;
    /**
     * local Hostname.
     */
    private static String hostname;
    /**
     * Testbed prefix.
     */
    private static String prefix = "";
    /**
     * Testbed url.
     */
    private static String testbedServer = "";
    private static Timer timer;

    /**
     * Constructor.
     */
    public PcMonitor() {
        LOGGER.debug("PcMonitor");
    }

    /**
     * main class.
     *
     * @param args cmd line arguments
     */
    public static void main(final String[] args) {
        PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource("log4j.properties"));
        final Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(System.getProperty("user.home") + "/.pcmonitor"));

//            if (!properties.contains("testbed_server")) {
//                errorMessage("testbed_server");
//            }
            testbedServer = ((String) properties.get("testbed_server"));

//            if (!properties.contains("prefix")) {
//                errorMessage("prefix");
//            }
            prefix = (String) properties.get("prefix");
        } catch (IOException e) {
            errorMessage("");
            return;
        }

        LOGGER.info("starting");
        hostname = findHostname();

        NetworkManager.getInstance().start(testbedServer, 3);

        timer = new Timer();

        runAll(System.getProperty("os.name"));

    }

    /**
     * Based on the os Name runs all available scripts and reports data.
     *
     * @param osName the name of the OS.
     */
    private static void runAll(final String osName) {
        if ("Linux".equals(osName)) {
            new UnixTask();
        } else if ("Mac OS".equals(osName)) {
            LOGGER.error("unavailable for " + osName);
        } else if ("Mac OS X".equals(osName)) {
            LOGGER.error("unavailable for " + osName);
        } else if ("Windows".contains(osName)) {
            LOGGER.error("unavailable for " + osName);
        }
    }

    /**
     * Displays an error message to user.
     */
    private static void errorMessage(final String error) {
        if ("".equals(error)) {
            LOGGER.fatal("No Property file detected please create a .pcmonitor file in " + System.getProperty("user.home"));
        } else {
            LOGGER.fatal("The property file " + System.getProperty("user.home") + "/.pcmonitor should contain " + error);
        }
        System.exit(0);

    }

    /**
     * Gets the hostname from the machine.
     *
     * @return the hostname.
     */
    public static String findHostname() {
        InputStreamReader stream = null;
        try {
            final Process runtime = Runtime.getRuntime().exec("hostname");
            runtime.waitFor();
            stream = new InputStreamReader(runtime.getInputStream());
            final BufferedReader reader = new BufferedReader(stream);
            final String line = reader.readLine();
            while (line != null) {
                return line;
            }
        } catch (IOException e) {
            LOGGER.fatal(e);
        } catch (InterruptedException e) {
            LOGGER.fatal(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
        return "";
    }

    /**
     * Returns the generated hostname.
     *
     * @return the hostname of the machine.
     */
    public static String getHostname() {
        return hostname;
    }

    /**
     * Returns the prefix read from the property file.
     *
     * @return the testbed prefix.
     */
    public static String getPrefix() {
        return prefix;
    }

    /**
     * Returns the server url read from the property file.
     *
     * @return the server url.
     */
    public static String getTestbedServer() {
        return testbedServer;
    }
}
