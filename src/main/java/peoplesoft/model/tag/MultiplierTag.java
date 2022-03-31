package peoplesoft.model.tag;

import static peoplesoft.commons.util.AppUtil.checkArgument;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import peoplesoft.commons.util.JsonUtil;

/**
 * Represents a Tag with multiplier in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagMultiplier(double)}
 */
@JsonSerialize(using = Tag.TagSerializer.class)
@JsonDeserialize(using = Tag.TagDeserializer.class)
public class MultiplierTag extends Tag {
    public static final String MESSAGE_CONSTRAINTS = "Tag multiplier values should be non-negative";

    private final double multiplier;

    /**
     * Constructs a {@code MultiplierTag}.
     *
     * @param tagName A valid tag name.
     * @param multiplier A valid integer multiplier value.
     */
    public MultiplierTag(String tagName, double multiplier) {
        super(tagName);
        checkArgument(isValidTagMultiplier(multiplier), MESSAGE_CONSTRAINTS);
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MultiplierTag // instanceof handles nulls
                && super.tagName.equals(((MultiplierTag) other).tagName)
                && multiplier == ((MultiplierTag) other).multiplier);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagMultiplier(double multiplier) {
        return multiplier >= 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.tagName, multiplier);
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return '[' + super.tagName + ": x" + multiplier + ']';
    }

    protected static class MultiplierTagSerializer extends StdSerializer<MultiplierTag> {
        private MultiplierTagSerializer(Class<MultiplierTag> val) {
            super(val);
        }

        private MultiplierTagSerializer() {
            this(null);
        }

        @Override
        public void serialize(MultiplierTag val, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();

            gen.writeObjectField("name", val.tagName);
            gen.writeObjectField("multiplier", String.valueOf(val.multiplier));

            gen.writeEndObject();
        }
    }

    protected static class MultiplierTagDeserializer extends StdDeserializer<MultiplierTag> {
        private static final String MISSING_OR_INVALID_VALUE = "The tag value is invalid or missing!";

        private MultiplierTagDeserializer(Class<?> vc) {
            super(vc);
        }

        private MultiplierTagDeserializer() {
            this(null);
        }

        private static JsonNode getNonNullNode(ObjectNode node, String key, DeserializationContext ctx)
                throws JsonMappingException {
            JsonNode jsonNode = node.get(key);
            if (jsonNode == null) {
                throw JsonUtil.getWrappedIllegalValueException(
                        ctx, String.format(MISSING_OR_INVALID_VALUE, key));
            }

            return jsonNode;
        }

        @Override
        public MultiplierTag deserialize(JsonParser p, DeserializationContext ctx)
                throws IOException {
            JsonNode node = p.readValueAsTree();
            ObjectCodec codec = p.getCodec();

            if (!(node instanceof ObjectNode)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_VALUE);
            }

            ObjectNode multiplierTag = (ObjectNode) node;

            String name = codec.treeToValue(
                    getNonNullNode(multiplierTag, "name", ctx), String.class);

            double multiplier = codec.treeToValue(
                    getNonNullNode(multiplierTag, "multiplier", ctx), Double.class);

            if (!Tag.isValidTagName(name) || !MultiplierTag.isValidTagMultiplier(multiplier)) {
                throw JsonUtil.getWrappedIllegalValueException(ctx, Tag.MESSAGE_CONSTRAINTS);
            }

            return new MultiplierTag(name, multiplier);
        }

        @Override
        public MultiplierTag getNullValue(DeserializationContext ctx) throws JsonMappingException {
            throw JsonUtil.getWrappedIllegalValueException(ctx, MISSING_OR_INVALID_VALUE);
        }
    }
}
