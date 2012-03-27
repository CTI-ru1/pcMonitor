package eu.uberdust.pcmonitor.jobs.unix;

import eu.uberdust.communication.protobuf.Message;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/26/12
 * Time: 11:44 PM
 */
public abstract class AbstractJob {
    public static final String CAPABILITY_PREFIX = "node:capability:";

    public abstract Message.NodeReadings getReadings();


}
