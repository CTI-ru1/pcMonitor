package eu.uberdust.pcmonitor.task;

import eu.uberdust.pcmonitor.jobs.unix.BatteryCharge;
import eu.uberdust.pcmonitor.jobs.unix.CpuUsage;
import eu.uberdust.pcmonitor.jobs.unix.DiskFree;
import eu.uberdust.pcmonitor.jobs.unix.DiskTemp;
import eu.uberdust.pcmonitor.jobs.unix.HostAlive;
import eu.uberdust.pcmonitor.jobs.unix.KernelVersion;
import eu.uberdust.pcmonitor.jobs.unix.MemFree;
import eu.uberdust.pcmonitor.jobs.unix.ProcessesInfo;
import eu.uberdust.pcmonitor.jobs.unix.UpTime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/30/12
 * Time: 1:06 PM
 */
public class AbstractOsTask {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(AbstractOsTask.class);

    public static ArrayList<Class> getJobClasses(String os) {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Class> classes = new ArrayList<Class>();

//        try {
//            list = getClassNamesFromPackage("eu.uberdust.pcmonitor.jobs." + os);
//        } catch (IOException e) {
//            list = new ArrayList<String>();
//        }
//        String[] names = "BatteryCharge,CpuUsage,DiskFree,DiskTemp,HostAlive,KernelVersion,MemFree,UpTime".split(",");
//        for (int i = 0; i < names.length; i++) {
//            list.add(names[i]);
//        }
//
//        for (final String className : list) {
//            try {
//                classes.add(Class.forName("eu.uberdust.pcmonitor.jobs.unix." + className));
//            } catch (ClassNotFoundException e) {
//                LOGGER.error(e);
//            }
//        }

        classes.add(BatteryCharge.class);
        classes.add(CpuUsage.class);
        classes.add(DiskFree.class);
        classes.add(DiskTemp.class);
        classes.add(HostAlive.class);
        classes.add(KernelVersion.class);
        classes.add(MemFree.class);
        classes.add(UpTime.class);
        classes.add(ProcessesInfo.class);

        return classes;
    }

    public static ArrayList<String> getClassNamesFromPackage(String packageName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL packageURL;
        ArrayList<String> names = new ArrayList<String>();
        ;

        packageName = packageName.replace(".", "/");
        packageURL = classLoader.getResource(packageName);

        if (packageURL.getProtocol().equals("jar")) {
            String jarFileName;
            JarFile jf;
            Enumeration<JarEntry> jarEntries;
            String entryName;

            // build jar file name, then loop through zipped entries
            jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
            jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
            System.out.println(">" + jarFileName);
            jf = new JarFile(jarFileName);
            jarEntries = jf.entries();
            while (jarEntries.hasMoreElements()) {
                entryName = jarEntries.nextElement().getName();
                if (entryName.startsWith(packageName) && entryName.length() > packageName.length() + 5) {
                    entryName = entryName.substring(packageName.length(), entryName.lastIndexOf('.'));
                    names.add(entryName);
                }
            }

            // loop through files in classpath
        } else {
            File folder = new File(packageURL.getFile());
            File[] contenuti = folder.listFiles();
            String entryName;
            for (File actual : contenuti) {
                if (actual.getName().contains(".class")) {
                    entryName = actual.getName();
                    entryName = entryName.substring(0, entryName.lastIndexOf('.'));
                    names.add(entryName);
                }
            }
        }
        return names;
    }
}
