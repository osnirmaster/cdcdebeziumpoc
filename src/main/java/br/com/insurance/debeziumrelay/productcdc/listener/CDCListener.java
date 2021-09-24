package br.com.insurance.debeziumrelay.productcdc.listener;

import br.com.insurance.debeziumrelay.productcdc.elasticsearch.entity.Product;
import br.com.insurance.debeziumrelay.productcdc.elasticsearch.service.ProductServiceElasticSearch;
import br.com.insurance.debeziumrelay.productcdc.schema.Operation;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.debezium.embedded.Connect;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
public class CDCListener {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final DebeziumEngine<ChangeEvent<String, String>> engine;
    private final ProductServiceElasticSearch productServiceElasticSearch;
    private final Properties productConnector;


    private CDCListener(Properties productConnector, ProductServiceElasticSearch productServceElasticSearch ){
        this.engine = DebeziumEngine.create(Json.class)
                .using(productConnector)
                .notifying(this::changeEventJson)
                .build();
        this.productServiceElasticSearch = productServceElasticSearch;
        this.productConnector = productConnector;
    }

    private void changeEventJson(ChangeEvent<String, String> stringStringChangeEvent) {

        this.productServiceElasticSearch.loadDataEntity(stringStringChangeEvent.value());

    }


    private void changeEvent(ChangeEvent<SourceRecord, SourceRecord> sourceRecordSourceRecordChangeEvent) {
        Struct sourceRecordValue =  (Struct) sourceRecordSourceRecordChangeEvent.value().value();

        System.out.println("Source: " + sourceRecordSourceRecordChangeEvent.value().value());

        if(sourceRecordValue != null) {
            Operation operation = Operation.searchCodeOperation( (String) sourceRecordValue.get("op"));

            if(operation != Operation.READ) {

                Map<String, Object> message;
                String record = "after";

                if(operation == Operation.DELETE){
                    record = "before";
                }
                Struct struct = (Struct) sourceRecordValue;
              //  System.out.println("Strutura: " + struct.get("after"));
                message = struct.schema().fields().stream()
                        .map(Field::name)
                        .filter(fieldName -> struct.get(fieldName) != null )
                        .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
                        .collect(toMap(Pair::getKey, Pair::getValue));

               System.out.println("Strutura: " + struct.schema());

               log.info("Dados Capturados: {} com Operation: {}", message, operation);
               this.productServiceElasticSearch.listenerEvent(message, operation);


            }
        }
    }

    @PostConstruct
    private void start() {
        this.executor.execute(engine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.engine != null) {
            this.engine.close();
        }
    }

}
