package mailreader;

import com.growl.GrowlWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class WinnerProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        int counter = OrdeProcessingWithCamel.counter;
        String subject = exchange.getIn().getHeader("Subject").toString();
        String from = exchange.getIn().getHeader("From").toString();
        System.out.println("winner message received : (" + System.nanoTime() + ") - " + from + " : " + subject);

        sendGrowl(counter, from);

        OrdeProcessingWithCamel.counter++;
    }

    private void sendGrowl(int counter, String from) {
        String NOTIFICATION = "Looking For a Winner";
        GrowlWrapper gw = new GrowlWrapper("MyApp", "VLC", new String[]{NOTIFICATION}, new String[]{NOTIFICATION});
        if (counter == 5) {
            System.out.println("CONGRATULATIONS ! you are the winner - " + counter + " : " + from);
            gw.notify(NOTIFICATION, from, "CONGRATULATIONS !!! You are the WINNER!");
        } else {
            System.out.println("SORRY ! you are number - " + counter + " : " + from);
            gw.notify(NOTIFICATION, from, "NOT A WINNER! - " + counter);
        }
    }
}
