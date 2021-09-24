package br.com.insurance.debeziumrelay.productcdc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class DebeziumConnectorConfig {
    @Value("${product.datasource.host}")
    private String productDBHost;

    @Value("${product.datasource.databasename}")
    private String productDBName;

    @Value("${product.datasource.port}")
    private String productDBPort;

    @Value("${product.datasource.username}")
    private String productDBUserName;

    @Value("${product.datasource.password}")
    private String productDBPassword;

    private String PRODUCT_TABLE_NAME = "public.product";

    //Debexium Configurtion Properties
    @Bean
    public Properties productConnector() {
        return io.debezium.config.Configuration.create()
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("offset.storage",  "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", "C:\\Users\\osnir\\OneDrive\\Área de Trabalho\\Arquitetura\\Estudo\\cdc\\productcdc\\product-offset.dat")
                .with("offset.flush.interval.ms", 60000)
                .with("transforms", "unwrap")
                .with("transforms.unwrap.type", "io.debezium.transforms.ExtractNewRecordState")
                .with("transforms.unwrap.add.source.fields","table,lsn")
                .with("transforms.unwrap.add.fields","op,table,lsn,source.ts_ms")
                .with("transforms.unwrap.add.headers","op,table,lsn,source.ts_ms")
                .with("delete.handling.mode","rewrite")
                .with("value.converter.schemas.enable",false)
                .with("SerializationFeature","FAIL_ON_EMPTY_BEANS")
                .with("name", "product-postgres-connector")
                .with("database.server.name", productDBHost+"-"+productDBName)
                .with("database.hostname", productDBHost)
                .with("database.port", productDBPort)
                .with("database.user", productDBUserName)
                .with("database.password", productDBPassword)
                .with("database.dbname", productDBName)
                .with("table.whitelist", PRODUCT_TABLE_NAME).build().asProperties();
    }

}
