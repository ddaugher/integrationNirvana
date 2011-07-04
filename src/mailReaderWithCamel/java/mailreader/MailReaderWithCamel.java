package mailreader;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class MailReaderWithCamel {

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

                // email from imap server into the queue (polling consumer)
                from("imaps://imap.gmail.com?username=integrationnirvana@gmail.com&password=Eiswein11&delete=false&unseen=true&consumer.delay=10000").
                        process(new EmailProcessor()).
                        to("jms:incomingEmails");

                // content-based router
                from("jms:incomingEmails")
                        .choice()
                        .when(header("Subject").contains("winner"))
                        .to("jms:topic:winnerEmails")
                        .when(header("Subject").contains("filtered"))
                        .to("jms:topic:filteredEmails")
                        .otherwise()
                        .to("jms:topic:nonFilteredEmails");

                // durable topic consumer
                from("jms:topic:nonFilteredEmails").to("jms:nonFiltered");
                from("jms:topic:filteredEmails").to("jms:filtered");
                from("jms:topic:winnerEmails").to("jms:winners");

                // event based consumer -> route to Processor
                from("jms:nonFiltered").process(new GrowlProcessor());
                from("jms:filtered").process(new FilteredProcessor());
                from("jms:winners").process(new WinnerProcessor());

            }
        });

        // start the route
        context.start();
        Thread.sleep(3600000);

        // stop the CamelContext
        context.stop();
    }
}

