package br.com.insurance.debeziumrelay.productcdc.elasticsearch.repository;

import br.com.insurance.debeziumrelay.productcdc.elasticsearch.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    void deleteByProductId(String productId);
}
