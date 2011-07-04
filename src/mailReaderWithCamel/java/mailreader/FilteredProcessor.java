package mailreader;

import com.growl.GrowlWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class FilteredProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        String subject = exchange.getIn().getHeader("Subject").toString();
        String from = exchange.getIn().getHeader("From").toString();
        System.out.println("filtered message received : " + from + " : " + subject);

        String NOTIFICATION = "Filtered Message Received";
        GrowlWrapper gw = new GrowlWrapper("MyApp", "Spirited Away", new String[]{NOTIFICATION}, new String[]{NOTIFICATION});
            gw.notify(NOTIFICATION, from, subject);
    }
}
