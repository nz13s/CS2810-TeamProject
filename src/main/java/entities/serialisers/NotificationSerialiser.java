package entities.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import entities.Notification;

import java.io.IOException;

public class NotificationSerialiser extends StdSerializer<Notification> {

    public NotificationSerialiser() {
        this(null);
    }

    public NotificationSerialiser(Class<Notification> notifications) {
        super(notifications);
    }

    @Override
    public void serialize(Notification notifications, JsonGenerator jsonGenerator,
                          SerializerProvider provider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("notifications", notifications.getMessage());
        jsonGenerator.writeEndObject();
    }
}
