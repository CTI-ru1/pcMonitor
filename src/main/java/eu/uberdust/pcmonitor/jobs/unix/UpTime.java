package eu.uberdust.pcmonitor.jobs.unix;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.pcmonitor.PcMonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/26/12
 * Time: 9:22 PM
 */
public class UpTime extends AbstractJob {
    /**
     * LOGGER.
     */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(UpTime.class);
    /**
     * Logged in users.
     */
    private transient double users;
    /**
     * Uptime.
     */
    private transient double time;

    /**
     * Constructor.
     */
    public UpTime() {
        time = 0;

        InputStreamReader stream = null;
        try {
            final Process runtime = Runtime.getRuntime().exec("uptime");
            runtime.waitFor();
            stream = new InputStreamReader(runtime.getInputStream());
            final BufferedReader reader = new BufferedReader(stream);
            final String line = reader.readLine();
            if (line != null) {
                for (final String part : line.split(",")) {

                    if (part.contains("user")) {
                        users = Double.valueOf(part.split("\\s+")[1]);
                    } else if (part.contains("days")) {
                        final String uptimeStr = part.substring(part.indexOf("up") + 2);
                        time += Double.valueOf(uptimeStr.split(" ")[1]) * 24 * 60;
                        LOGGER.info(uptimeStr);
                    } else if (part.contains("min")) {
                        final String uptimeStr = part.substring(0, part.indexOf("min"));
                        time += Double.valueOf(uptimeStr);
                        LOGGER.info(uptimeStr);
                    } else if (part.contains(":")) {
                        final String[] uptimeStr = part.split(":");
                        try {
                            time += Double.valueOf(uptimeStr[0]) * 60;
                            LOGGER.info("+" + Double.valueOf(uptimeStr[0]) * 60);
                            time += Double.valueOf(uptimeStr[1]);
                            LOGGER.info("+" + Double.valueOf(uptimeStr[1]));
                        } catch (final NumberFormatException nfe) {
                            LOGGER.error(nfe);
                        }
                    }
                }

            }
        } catch (IOException e) {
            LOGGER.fatal(e);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }

    public final Message.NodeReadings getReadings() {
        final Message.NodeReadings.Builder readings = Message.NodeReadings.newBuilder();

        final Message.NodeReadings.Reading.Builder reading1 = Message.NodeReadings.Reading.newBuilder();
        reading1.setNode(PcMonitor.getPrefix() + PcMonitor.getHostname());
        final StringBuilder capability1 = new StringBuilder()
                .append(PcMonitor.getPrefix())
                .append(CAPABILITY_PREFIX)
                .append("users");
        reading1.setCapability(capability1.toString());
        reading1.setDoubleReading(users);
        reading1.setTimestamp(System.currentTimeMillis());
        readings.addReading(reading1.build());

        final Message.NodeReadings.Reading.Builder reading2 = Message.NodeReadings.Reading.newBuilder();
        reading2.setNode(PcMonitor.getPrefix() + PcMonitor.getHostname());
        final StringBuilder capability2 = new StringBuilder()
                .append(PcMonitor.getPrefix())
                .append(CAPABILITY_PREFIX)
                .append("uptime");
        reading2.setCapability(capability2.toString());
        reading2.setDoubleReading(time);
        reading2.setTimestamp(System.currentTimeMillis());
        readings.addReading(reading2.build());

        return readings.build();
    }

    public static void main(final String[] args) {
        final UpTime item = new UpTime();
        LOGGER.info(item.getReadings());

    }
}
