package eu.uberdust.pcmonitor.task;

import eu.uberdust.network.NetworkManager;
import eu.uberdust.pcmonitor.jobs.unix.BatteryCharge;
import eu.uberdust.pcmonitor.jobs.unix.CpuUsage;
import eu.uberdust.pcmonitor.jobs.unix.DiskFree;
import eu.uberdust.pcmonitor.jobs.unix.DiskTemp;
import eu.uberdust.pcmonitor.jobs.unix.HostAlive;
import eu.uberdust.pcmonitor.jobs.unix.KernelVersion;
import eu.uberdust.pcmonitor.jobs.unix.MemFree;
import eu.uberdust.pcmonitor.jobs.unix.UpTime;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/29/12
 * Time: 5:34 PM
 */
public class UnixTask extends TimerTask {
    /**
     * LOGGER.
     */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(UnixTask.class);

    @Override
    public void run() {

        final DiskFree diskFree = new DiskFree();
        try {
            NetworkManager.getInstance().sendNodeReading(diskFree.getReadings());
        } catch (IOException e) {
            LOGGER.error(e);
        }
        final CpuUsage cpuUsage = new CpuUsage();
        try {
            NetworkManager.getInstance().sendNodeReading(cpuUsage.getReadings());
        } catch (IOException e) {
            LOGGER.error(e);
        }
        final BatteryCharge batteryCharge = new BatteryCharge();
        try {
            NetworkManager.getInstance().sendNodeReading(batteryCharge.getReadings());
        } catch (IOException e) {
            LOGGER.error(e);
        }
        final HostAlive hostAlive = new HostAlive();
        try {
            NetworkManager.getInstance().sendNodeReading(hostAlive.getReadings());
        } catch (IOException e) {
            LOGGER.error(e);
        }
        final KernelVersion kernelVersion = new KernelVersion();
        try {
            NetworkManager.getInstance().sendNodeReading(kernelVersion.getReadings());
        } catch (IOException e) {
            LOGGER.error(e);
        }
        final MemFree memFree = new MemFree();
        try {
            NetworkManager.getInstance().sendNodeReading(memFree.getReadings());
        } catch (IOException e) {
            LOGGER.error(e);
        }
        final UpTime uptime = new UpTime();
        try {
            NetworkManager.getInstance().sendNodeReading(uptime.getReadings());
        } catch (IOException e) {
            LOGGER.error(e);
        }
        final DiskTemp diskTemp = new DiskTemp();
        try {
            NetworkManager.getInstance().sendNodeReading(diskTemp.getReadings());
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
