package mailreader;

import com.growl.GrowlWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class WinnerProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        int counter = MailReaderWithCamel.counter;
        String subject = exchange.getIn().getHeader("Subject").toString();
        String from = exchange.getIn().getHeader("From").toString();
        System.out.println("winner message received : " + from + " : " + subject);

        String NOTIFICATION = "Looking For a Winner";
        String WINNER_NOTIFICATION = "Winner Found !";
        GrowlWrapper gw = new GrowlWrapper("MyApp", "Finder", new String[]{NOTIFICATION, WINNER_NOTIFICATION}, new String[]{WINNER_NOTIFICATION});
        if (counter != 2) {
            System.out.println("SORRY ! you are number - " + counter + " : " + from);
            gw.notify(NOTIFICATION, from, subject);
        } else {
            System.out.println("CONGRATULATIONS ! you are the winner - " + counter + " : " + from);
            gw.notify(WINNER_NOTIFICATION, from, subject + " : " + from);
        }

        MailReaderWithCamel.counter++;
    }
}
