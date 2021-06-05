package org.sunprojects.mqtry;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * A minimal and simple application for Point-to-point messaging.
 *
 * Application makes use of fixed literals, any customisations will require
 * re-compilation of this source file. Application assumes that the named queue
 * is empty prior to a run.
 *
 * Notes:
 *
 * API type: JMS API (v2.0, simplified domain)
 *
 * Messaging domain: Point-to-point
 *
 * Provider type: IBM MQ
 *
 * Connection mode: Client connection
 *
 * JNDI in use: No
 *
 */
public class JmsPut {

    // System exit status value (assume unset value to be 1)
    private static int status = 1;

//    // Create variables for the connection to MQ
//    private static final String HOST = "_YOUR_HOSTNAME_"; // Host name or IP address
//    private static final int PORT = 1414; // Listener port for your queue manager
//    private static final String CHANNEL = "DEV.APP.SVRCONN"; // Channel name
//    private static final String QMGR = "QM1"; // Queue manager name
//    private static final String APP_USER = "app"; // User name that application uses to connect to MQ
//    private static final String APP_PASSWORD = "_APP_PASSWORD_"; // Password that the application uses to connect to MQ
//    private static final String QUEUE_NAME = "DEV.QUEUE.1"; // Queue that the application uses to put and get messages to and from

    // Create variables for the connection to MQ
    private static final String HOST = "eowyn-a3.it.volvo.net"; // Host name or IP address
    private static final int PORT = 1437; // Listener port for your queue manager
    private static final String CHANNEL = "LISA.SRV01"; // Channel name
    private static final String QMGR = "EOWYN_A3"; // Queue manager name
    private static final String APP_USER = "cs-ws-s-LISA"; // User name that application uses to connect to MQ
    //private static final String APP_PASSWORD = "_APP_PASSWORD_"; // Password that the application uses to connect to MQ
    private static final String QUEUE_NAME = "LISA.APP.CONFIRMTRANSLATION.IN"; // Queue that the application uses to put and get messages to and from



    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {

        // Variables
        JMSContext context = null;
        Destination destination = null;
        JMSProducer producer = null;
        JMSConsumer consumer = null;



        try {
            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            // Set the properties
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
            cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
            cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
            cf.setStringProperty(WMQConstants.USERID, APP_USER);
            //cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
            //cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, "*TLS12");

            // Create JMS objects
            context = cf.createContext();
            destination = context.createQueue("queue:///" + QUEUE_NAME);

            long uniqueNumber = System.currentTimeMillis() % 1000;
            TextMessage message = context.createTextMessage("Your lucky number today is " + uniqueNumber);

            producer = context.createProducer();
            producer.send(destination, message);
            System.out.println("Sent message:\n" + message);

            recordSuccess();
        } catch (JMSException jmsex) {
            recordFailure(jmsex);
        }

        System.exit(status);

    } // end main()

    /**
     * Record this run as successful.
     */
    private static void recordSuccess() {
        System.out.println("SUCCESS");
        status = 0;
        return;
    }

    /**
     * Record this run as failure.
     *
     * @param ex
     */
    private static void recordFailure(Exception ex) {
        if (ex != null) {
            if (ex instanceof JMSException) {
                processJMSException((JMSException) ex);
            } else {
                System.out.println(ex);
            }
        }
        System.out.println("FAILURE");
        status = -1;
        return;
    }

    /**
     * Process a JMSException and any associated inner exceptions.
     *
     * @param jmsex
     */
    private static void processJMSException(JMSException jmsex) {
        System.out.println(jmsex);
        Throwable innerException = jmsex.getLinkedException();
        if (innerException != null) {
            System.out.println("Inner exception(s):");
        }
        while (innerException != null) {
            System.out.println(innerException);
            innerException = innerException.getCause();
        }
        return;
    }

}
