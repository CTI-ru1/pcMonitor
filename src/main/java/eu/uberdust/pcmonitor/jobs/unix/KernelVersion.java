package eu.uberdust.pcmonitor.jobs.unix;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.pcmonitor.PcMonitor;
import eu.uberdust.pcmonitor.jobs.AbstractJob;

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
    /**
     * Kernel Version.
     */
    private transient String kernel;

    /**
     * Constructor.
     */
    public KernelVersion() {
        kernel = System.getProperty("os.version");
        LOGGER.debug(kernel);
    }

    /**
     * Returns the readings generated by the job.
     *
     * @return the readings generated as Message.NodeReadings,
     */
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
