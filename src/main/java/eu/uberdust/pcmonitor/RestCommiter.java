package eu.uberdust.pcmonitor;

import eu.uberdust.communication.protobuf.Message;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 12/4/11
 * Time: 2:15 PM
 */
public class RestCommiter {

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(RestCommiter.class);


    /**
     * Add a new nodeReading using the rest interfaces.
     *
     * @param nodeReadings the node readings to add
     */
    public RestCommiter(final Message.NodeReadings nodeReadings) {
        if (nodeReadings != null) {
            for (Message.NodeReadings.Reading reading : nodeReadings.getReadingList()) {


                if (reading.hasDoubleReading()) {

                    final StringBuilder urlBuilder = new StringBuilder(PcMonitor.testbed_server);
                    //node/urn:testbed2:test/capability/testingcap/insert/timestamp/11111111000/reading/27.2/
                    urlBuilder.append("node/").append(reading.getNode())
                            .append("/capability/").append(reading.getCapability())
                            .append("/insert/timestamp/").append(reading.getTimestamp())
                            .append("/reading/").append(reading.getDoubleReading()).append("/");

                    LOGGER.info(urlBuilder.toString());
                    callUrl(urlBuilder.toString());
                } else if (reading.hasStringReading()) {

                    final StringBuilder urlBuilder = new StringBuilder(PcMonitor.testbed_server);
                    //node/urn:testbed2:test/capability/testingcap/insert/timestamp/11111111000/reading/27.2/
                    urlBuilder.append("node/").append(reading.getNode())
                            .append("/capability/").append(reading.getCapability())
                            .append("/insert/timestamp/").append(reading.getTimestamp())
                            .append("/stringreading/").append(reading.getStringReading()).append("/");

                    LOGGER.info(urlBuilder.toString());
                    callUrl(urlBuilder.toString());
                }
            }
        }
    }
//
//    /**
//     * Adds a new link reading using the rest interfaces.
//     *
//     * @param linkReading the link reading to add
//     */
//    public RestCommiter(final Message.LinkReadings.Reading linkReading) {
//        final StringBuilder urlBuilder = new StringBuilder(TESTBED_SERVER);
//        /**
//         * TODO: Implementation is Missing.
//         */
//        LOGGER.fatal("Not Implemented...");
//        throw new RuntimeException("Not Implemented...");
///*
//
//        urlBuilder.append(linkReading.toRestString());
//        final String insertReadingUrl = urlBuilder.toString();
//        callUrl(insertReadingUrl);
//*/
//
//    }

    /**
     * Opens a connection over the Rest Interfaces to the server and adds the event.
     *
     * @param urlString the string url that describes the event
     */
    private void callUrl(final String urlString) {
        HttpURLConnection httpURLConnection = null;

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            LOGGER.error(e);
            return;
        }

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                LOGGER.debug("Added " + urlString);
            } else {
                final StringBuilder errorBuilder = new StringBuilder("Problem ");
                errorBuilder.append("with ").append(urlString);
                errorBuilder.append(" Response: ").append(httpURLConnection.getResponseCode());
                LOGGER.error(errorBuilder.toString());
            }
            httpURLConnection.disconnect();
        } catch (IOException e) {
            LOGGER.error(e);
        }


    }
}
