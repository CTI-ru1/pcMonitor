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
    /**
     * LOGGER.
     */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(CpuUsage.class);
    /**
     * Cpu usage.
     */
    private transient double usage;

    /**
     * Constructor.
     */
    public CpuUsage() {


        InputStreamReader stream = null;
        try {
            final Process runtime = Runtime.getRuntime().exec("mpstat");
            runtime.waitFor();
            stream = new InputStreamReader(runtime.getInputStream());
            final BufferedReader reader = new BufferedReader(stream);
            String line = reader.readLine();
            while (line != null) {
                if (line.contains("all")) {
                    final String[] parts = line.split("\\s+");
                    usage = Double.parseDouble(parts[3]);
                    break;
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

    public final Message.NodeReadings getReadings() {
        final Message.NodeReadings.Builder readings = Message.NodeReadings.newBuilder();


        final Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
        reading.setNode(PcMonitor.getPrefix() + PcMonitor.getHostname());
        final StringBuilder capability = new StringBuilder()
                .append(PcMonitor.getPrefix())
                .append(CAPABILITY_PREFIX)
                .append("cpu:usage");
        reading.setCapability(capability.toString());
        reading.setDoubleReading(usage);
        reading.setTimestamp(System.currentTimeMillis());
        readings.addReading(reading.build());

        return readings.build();
    }


    public static void main(final String[] args) {
        final CpuUsage item = new CpuUsage();
        LOGGER.info(item.getReadings());

    }


}
