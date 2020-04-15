package entities.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import entities.Queue;
import java.io.IOException;

/**
 * Class that serialises the Queue objects.
 *
 * @author Bhavik Narang, Jatin Khatra, Cameron Jones
 */

public class QueueSerialiser extends StdSerializer<Queue> {

  public QueueSerialiser() {
    this(null);
  }

  public QueueSerialiser(Class<Queue> varName) {
    super(varName);
  }

  @Override
  public void serialize(Queue varName, JsonGenerator jsonGenerator, SerializerProvider serializer)
          throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeObjectField("orders", varName.getList());
    jsonGenerator.writeEndObject();
  }
}
