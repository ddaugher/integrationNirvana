package mailreader;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class OrdeProcessingWithCamel {

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
                from("imaps://imap.gmail.com?username=integrationnirvana@gmail.com&password=Eiswein11&delete=false&unseen=true&consumer.delay=10000").
                        process(new OrderProcessor()).
                        to("jms:incomingOrders");

                // content-based router
                from("jms:incomingOrders")
                .choice()
                    .when(header("Subject").contains("winner"))
                        .to("jms:topic:winner")
                    .when(header("Subject").contains("new"))
                        .to("jms:topic:newCarOrders")
                    .otherwise()
                        .to("jms:topic:usedCarOrders");

                // durable topic consumer
                from("jms:topic:newCarOrders").to("jms:newCarOrder");
                from("jms:topic:usedCarOrders").to("jms:usedCarOrder");
                from("jms:topic:winner").to("jms:winner");

                // event based consumer -> route to Processor
                from("jms:newCarOrder").process(new NewCarOrderProcessor());
                from("jms:usedCarOrder").process(new UserCarOrderProcessor());
                from("jms:winner").process(new WinnerProcessor());

            }
        });

        // start the route
        context.start();
        Thread.sleep(3600000);

        // stop the CamelContext
        context.stop();
    }
}
