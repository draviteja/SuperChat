package info.supercode.superchat.functons;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

public class MockPaymentService implements Function<MockPaymentService.Transaction, MockPaymentService.Status> {

    private static final Logger log = LoggerFactory.getLogger(MockPaymentService.class);

    @JsonClassDescription("Get the Payment status of a transaction")
    public static record Transaction(@JsonProperty(required = true) String id) {
    }

    public static record Status(String name) {
    }

    private static final Map<Transaction, Status> DATASET = Map.of(
            new Transaction("001"), new Status("pending"),
            new Transaction("002"), new Status("approved"),
            new Transaction("003"), new Status("rejected")
    );

    @Override
    public Status apply(Transaction transaction) {
        log.info("Payment Request: {}", transaction);
        return DATASET.get(transaction);
    }
}
