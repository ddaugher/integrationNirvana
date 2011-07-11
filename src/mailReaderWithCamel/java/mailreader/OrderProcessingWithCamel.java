package mailreader;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class OrderProcessingWithCamel {

    public static int counter = 0;

    public static void main(String args[]) throws Exception {
        // create CamelContext
        CamelContext context = new DefaultCamelContext();

        // connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            public void configure() {

                // order from imap server into the queue (polling consumer)

                // content-based router

                // durable topic consumer

                // event based consumer -> route to Processor

            }
        });

        // start the route
        context.start();
        Thread.sleep(3600000);

        // stop the CamelContext
        context.stop();
    }
}
