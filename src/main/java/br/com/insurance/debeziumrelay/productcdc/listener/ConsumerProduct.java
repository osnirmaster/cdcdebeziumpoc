package br.com.insurance.debeziumrelay.productcdc.listener;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import org.apache.kafka.clients.consumer.Consumer;

import java.util.List;

public class ConsumerProduct implements DebeziumEngine.ChangeConsumer<ChangeEvent<String, String>> {

    @Override
    public void handleBatch(List<ChangeEvent<String, String>> records,
                            DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> committer) throws InterruptedException {

    }

    @Override
    public boolean supportsTombstoneEvents() {
        return DebeziumEngine.ChangeConsumer.super.supportsTombstoneEvents();
    }
}
