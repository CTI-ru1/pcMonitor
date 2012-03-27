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
public class CpuUsage extends AbstractJob {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(CpuUsage.class);
    private double usage;

    public CpuUsage() {


        try {
            Process runtime = Runtime.getRuntime().exec("mpstat");
            runtime.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(runtime.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                if (line.contains("all")) {
                    String[] parts = line.split("\\s+");
                    usage = Double.parseDouble(parts[3]);
                    break;
                }
                line = reader.readLine();
            }

        } catch (IOException e) {
            LOGGER.fatal(e);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Message.NodeReadings getReadings() {
        final Message.NodeReadings.Builder readings = Message.NodeReadings.newBuilder();


        Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
        reading.setNode(PcMonitor.prefix + PcMonitor.hostname);
        final StringBuilder capability = new StringBuilder()
                .append(PcMonitor.prefix)
                .append(CAPABILITY_PREFIX)
                .append("cpu:usage");
        reading.setCapability(capability.toString());
        reading.setDoubleReading(usage);
        reading.setTimestamp(System.currentTimeMillis());
        readings.addReading(reading.build());

        return readings.build();
    }


}