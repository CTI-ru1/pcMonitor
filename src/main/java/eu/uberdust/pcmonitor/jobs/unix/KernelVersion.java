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
public class KernelVersion extends AbstractJob {
    /**
     * LOGGER.
     */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(KernelVersion.class);
    private transient String kernel;

    /**
     * Constructor.
     */
    public KernelVersion() {

        InputStreamReader stream = null;
        try {
            final Process runtime = Runtime.getRuntime().exec("uname -r");
            runtime.waitFor();
            stream = new InputStreamReader(runtime.getInputStream());
            final BufferedReader reader = new BufferedReader(stream);
            String line = reader.readLine();
            while (line != null) {
                final String[] parts = line.split("\\s+");
                kernel = parts[0];
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
                .append("kernel");
        reading.setCapability(capability.toString());
        reading.setStringReading(kernel);
        reading.setTimestamp(System.currentTimeMillis());
        readings.addReading(reading.build());

        return readings.build();
    }


}
