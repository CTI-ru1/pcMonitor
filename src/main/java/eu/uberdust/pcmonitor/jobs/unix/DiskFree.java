package eu.uberdust.pcmonitor.jobs.unix;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.pcmonitor.PcMonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/26/12
 * Time: 9:22 PM
 */
public class DiskFree extends AbstractJob {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(DiskFree.class);
    Map<String, Double> disks;

    public DiskFree() {
        disks = new HashMap<String, Double>();

        try {
            Process runtime = Runtime.getRuntime().exec("df -T");
            runtime.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(runtime.getInputStream()));
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("\\s+");
                if (parts[1].equals("ext4")) {

                    disks.put(parts[0].substring(parts[0].lastIndexOf("/") + 1), Double.valueOf(parts[4].substring(0, parts[5].indexOf("%"))));
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


        for (final String disk : disks.keySet()) {
            Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
            reading.setNode(PcMonitor.prefix + PcMonitor.hostname);
            final StringBuilder capability = new StringBuilder()
                    .append(PcMonitor.prefix)
                    .append(CAPABILITY_PREFIX)
                    .append(disk).append(":usage");
            reading.setCapability(capability.toString());
            reading.setDoubleReading(disks.get(disk));
            reading.setTimestamp(System.currentTimeMillis());
            readings.addReading(reading.build());
        }
        return readings.build();
    }


}
