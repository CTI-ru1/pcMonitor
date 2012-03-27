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
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(UpTime.class);
    private Double users;
    private Double uptime;


    public UpTime() {
        uptime = (double) 0;

        try {
            Process runtime = Runtime.getRuntime().exec("uptime");
            runtime.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(runtime.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                for (String part : line.split(",")) {

                    if (part.contains("user")) {
                        users = Double.valueOf(part.split("\\s+")[1]);
                    } else if (part.contains("days")) {
                        String uptimeStr = part.substring(part.indexOf("up") + 2);
                        uptime += Double.valueOf(uptimeStr.split(" ")[1]) * 24 * 60;
                        LOGGER.info(uptimeStr);
                    } else if (part.contains("min")) {
                        String uptimeStr = part.substring(0, part.indexOf("min"));
                        uptime += Double.valueOf(uptimeStr);
                        LOGGER.info(uptimeStr);
                    } else if (part.contains(":")) {
                        String[] uptimeStr = part.split(":");
                        try {
                            uptime += Double.valueOf(uptimeStr[0]) * 60;
                            LOGGER.info("+" + Double.valueOf(uptimeStr[0]) * 60);
                            uptime += Double.valueOf(uptimeStr[1]);
                            LOGGER.info("+" + Double.valueOf(uptimeStr[1]));
                        } catch (NumberFormatException nfe) {

                        }
                    }
                }

            }
        } catch (IOException e) {
            LOGGER.fatal(e);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Message.NodeReadings getReadings() {
        final Message.NodeReadings.Builder readings = Message.NodeReadings.newBuilder();

        {
            Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
            reading.setNode(PcMonitor.prefix + PcMonitor.hostname);
            final StringBuilder capability = new StringBuilder()
                    .append(PcMonitor.prefix)
                    .append(CAPABILITY_PREFIX)
                    .append("users");
            reading.setCapability(capability.toString());
            reading.setDoubleReading(users);
            reading.setTimestamp(System.currentTimeMillis());
            readings.addReading(reading.build());
        }
        {
            Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
            reading.setNode(PcMonitor.prefix + PcMonitor.hostname);
            final StringBuilder capability = new StringBuilder()
                    .append(PcMonitor.prefix)
                    .append(CAPABILITY_PREFIX)
                    .append("uptime");
            reading.setCapability(capability.toString());
            reading.setDoubleReading(uptime);
            reading.setTimestamp(System.currentTimeMillis());
            readings.addReading(reading.build());
        }

        return readings.build();
    }

    public static void main(String[] args) {
        UpTime item = new UpTime();
        LOGGER.info(item.getReadings());

    }
}
