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
public class BatteryCharge extends AbstractJob {
    /**
     * LOGGER.
     */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(BatteryCharge.class);
    /**
     * the battery charge observed.
     */
    private transient double charge;

    /**
     * Constructor.
     */
    public BatteryCharge() {


        InputStreamReader stream = null;
        try {
            final Process runtime = Runtime.getRuntime().exec("df -T");
            runtime.waitFor();
            stream = new InputStreamReader(runtime.getInputStream());
            final BufferedReader reader = new BufferedReader(stream);
            String line = reader.readLine();
            while (line != null) {
                if (line.contains("Battery")) {
                    final String[] parts = line.split("\\s+");
                    charge = Double.parseDouble(parts[3].substring(0, parts[3].indexOf('%')));
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

    /**
     * Returns the readings generated by the job.
     *
     * @return the readings generated as Message.NodeReadings,
     */
    public final Message.NodeReadings getReadings() {
        final Message.NodeReadings.Builder readings = Message.NodeReadings.newBuilder();
        if (charge > 0) {
            final Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
            reading.setNode(PcMonitor.getPrefix() + PcMonitor.getHostname());
            final StringBuilder capability = new StringBuilder()
                    .append(PcMonitor.getPrefix())
                    .append(CAPABILITY_PREFIX)
                    .append("charge");
            reading.setCapability(capability.toString());
            reading.setDoubleReading(charge);
            reading.setTimestamp(System.currentTimeMillis());
            readings.addReading(reading.build());

        }
        return readings.build();

    }


}
