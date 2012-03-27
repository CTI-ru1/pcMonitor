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
public class MemFree extends AbstractJob {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(MemFree.class);
    private transient double memory;
    private transient double swap;


    public MemFree() {

        InputStreamReader stream = null;
        try {
            final Process runtime = Runtime.getRuntime().exec("cat /proc/meminfo");
            runtime.waitFor();
            stream = new InputStreamReader(runtime.getInputStream());
            final BufferedReader reader = new BufferedReader(stream);
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith("MemFree")) {
                    final String[] parts = line.split("\\s+");
                    memory = Double.parseDouble(parts[1]);
                } else if (line.startsWith("SwapFree")) {
                    final String[] parts = line.split("\\s+");
                    swap = Double.parseDouble(parts[1]);
                }
                line = reader.readLine();
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

    public Message.NodeReadings getReadings() {
        final Message.NodeReadings.Builder readings = Message.NodeReadings.newBuilder();

        {
            final Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
            reading.setNode(PcMonitor.getPrefix() + PcMonitor.getHostname());
            final StringBuilder capability = new StringBuilder()
                    .append(PcMonitor.getPrefix())
                    .append(CAPABILITY_PREFIX)
                    .append("memfree");
            reading.setCapability(capability.toString());
            reading.setDoubleReading(memory);
            reading.setTimestamp(System.currentTimeMillis());
            readings.addReading(reading.build());
        }
        {
            final Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
            reading.setNode(PcMonitor.getPrefix() + PcMonitor.getHostname());
            final StringBuilder capability = new StringBuilder()
                    .append(PcMonitor.getPrefix())
                    .append(CAPABILITY_PREFIX)
                    .append("swapfree");
            reading.setCapability(capability.toString());
            reading.setDoubleReading(swap);
            reading.setTimestamp(System.currentTimeMillis());
            readings.addReading(reading.build());
        }
        return readings.build();
    }


}
