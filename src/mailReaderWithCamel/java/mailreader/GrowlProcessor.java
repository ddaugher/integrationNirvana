package mailreader;

import com.growl.GrowlWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class GrowlProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        String subject = exchange.getIn().getHeader("Subject").toString();
        String from = exchange.getIn().getHeader("From").toString();
        System.out.println("growling message: " + from + " : " + subject);

        String NOTIFICATION = "Notificiation Received";
        GrowlWrapper gw = new GrowlWrapper("My App", "Finder", new String[]{NOTIFICATION}, new String[]{NOTIFICATION});
        gw.notify(NOTIFICATION, from, subject);
    }
}
