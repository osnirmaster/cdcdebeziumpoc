package br.com.insurance.debeziumrelay.productcdc.elasticsearch.service;

import br.com.insurance.debeziumrelay.productcdc.elasticsearch.entity.Product;
import br.com.insurance.debeziumrelay.productcdc.elasticsearch.repository.ProductRepository;
import br.com.insurance.debeziumrelay.productcdc.schema.Operation;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProductServiceElasticSearch {
    private final ProductRepository productRepository;

    public ProductServiceElasticSearch(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public void listenerEvent(Map<String, Object> message, Operation operation) {

        int count = 0;
        for (Object object: message.values()) {
            count++;
            System.out.println(count +" TesteMensagem: " + object.toString());
        }

        final ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            String json = mapper.writeValueAsString(message);
            //System.out.println("TesteJson: " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        final Product product = mapper.convertValue(message.values(), Product.class);

        System.out.println("Teste Rapd: " + product.getDescription());
        if(Operation.DELETE.name().equals(operation.name())){
            productRepository.deleteById(product.getProductId());
        }else{
            productRepository.save(product);
        }
    }

    public void loadDataEntity(String json){

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Product product = null;

        try {
            product = mapper.readValue(json, Product.class);

            if(product.get__op().equalsIgnoreCase("d")){
                productRepository.deleteByProductId(product.getProductId());
                System.out.println("Registro Deletado");
            }
            else{
                productRepository.save(product);
                System.out.println("Registro Atualizado");
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("Operation Name: " + product.get__op());

        log.info("Date Captured: {} ", json);
    }
}
