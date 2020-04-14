package entities.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import entities.Order;

import java.io.IOException;

/**
 * Class that serialises a list of cancelled objects and passes them to frontend.
 *
 * @author Jatin Khatra
 */

public class CancelledOrdersSerialiser extends StdSerializer<Order> {

  public CancelledOrdersSerialiser() {
    this(null);
  }

  public CancelledOrdersSerialiser(Class<Order> order) {
    super(order);
  }

  @Override
  public void serialize(Order order, JsonGenerator jsonGenerator, SerializerProvider serializer)
          throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeObjectField("cancelled_orders", order.getFoodItems());
    jsonGenerator.writeEndObject();
  }
}
