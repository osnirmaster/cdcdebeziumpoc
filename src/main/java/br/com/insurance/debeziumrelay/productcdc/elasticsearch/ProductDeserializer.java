package br.com.insurance.debeziumrelay.productcdc.elasticsearch;

import br.com.insurance.debeziumrelay.productcdc.elasticsearch.entity.Product;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import java.io.IOException;
import java.util.UUID;

public class ProductDeserializer extends StdDeserializer<Product> {


    public ProductDeserializer() {
        this(null);
    }

    public ProductDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Product deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String product_id = node.get("product_id").asText();
        String code = node.get("code").asText();
        String name = node.get("name").asText();
        String image = node.get("image").asText();
        String description = node.get("description").asText();
        String __op = node.get("__op").asText();;

        return new Product(
                product_id,
                code,
                name,
                image,
                description,
                __op
        );
    }
}
