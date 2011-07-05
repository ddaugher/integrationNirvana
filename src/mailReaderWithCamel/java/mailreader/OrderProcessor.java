package mailreader;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class OrderProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        String subject = exchange.getIn().getHeader("Subject").toString();
        String from = exchange.getIn().getHeader("From").toString();

        System.out.println("incoming order received : (" + System.nanoTime() + ") - " + from + " : " + subject);

        exchange.getIn().setBody(subject + " : " + from);
    }
}
