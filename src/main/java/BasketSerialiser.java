import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Class to serialise a {@link Basket} to pass to the Frontend
 */
public class BasketSerialiser extends StdSerializer<Basket> {

    public BasketSerialiser(){
        this(null);
    }

    public BasketSerialiser(Class<Basket> basket) {
        super(basket);
    }

    @Override
    public void serialize(
            Basket basket, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("items", basket.getBasket());
        jsonGenerator.writeEndObject();
    }
}