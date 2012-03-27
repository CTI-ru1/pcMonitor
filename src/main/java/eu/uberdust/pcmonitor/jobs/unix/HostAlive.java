package eu.uberdust.pcmonitor.jobs.unix;

import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.pcmonitor.PcMonitor;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/26/12
 * Time: 9:22 PM
 */
public class HostAlive extends AbstractJob {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(HostAlive.class);

    public HostAlive() {
        LOGGER.debug("HostAlive");
    }

    public Message.NodeReadings getReadings() {

        final Message.NodeReadings.Builder readings = Message.NodeReadings.newBuilder();
        final Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
        reading.setNode(PcMonitor.getPrefix() + PcMonitor.getHostname());
        final StringBuilder capability = new StringBuilder()
                .append(PcMonitor.getPrefix())
                .append(CAPABILITY_PREFIX)
                .append("status");
        reading.setCapability(capability.toString());
        reading.setDoubleReading(1);
        reading.setTimestamp(System.currentTimeMillis());
        readings.addReading(reading.build());

        return readings.build();
    }


}
