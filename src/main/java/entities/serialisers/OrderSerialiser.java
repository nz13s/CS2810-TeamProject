package entities.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import entities.Order;

import java.io.IOException;

/**
 * Class to serialise a {@link Order} to pass to the Frontend.
 *
 * @author Cameron Jones
 */
public class OrderSerialiser extends StdSerializer<Order> {

  public OrderSerialiser() {
    this(null);
  }

  public OrderSerialiser(Class<Order> order) {
    super(order);
  }

  @Override
  public void serialize(
          Order order, JsonGenerator jsonGenerator, SerializerProvider serializer)
          throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeObjectField("items", order.getFoodItems());
    jsonGenerator.writeEndObject();
  }
}