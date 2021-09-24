package br.com.insurance.debeziumrelay.productcdc.elasticsearch.entity;


import br.com.insurance.debeziumrelay.productcdc.elasticsearch.ProductDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.UUID;


@Data
@Document(indexName = "productindex", createIndex = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize
public class Product {
    @Id
    @Field
    private String productId;

    @Field
    private String code;

    @Field
    private String name;

    @Field
    private String image;

    @Field
    private String description;

    private LocalDate created_date;

    @Transient
    private String __op;

    public Product(){}

    @JsonCreator
    public Product(
            @JsonProperty("product_id") String productId,
            @JsonProperty("code") String code,
            @JsonProperty("name") String name,
            @JsonProperty("image") String image,
            @JsonProperty("description") String description,
            @JsonProperty("__op") String __op) {
        this.productId = productId;
        this.code = code;
        this.name = name;
        this.image = image;
        this.description = description;
        this.__op = __op;
    }



    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_id() {
        return productId;
    }

    public void setProduct_id(String product_id) {
        this.productId = product_id;
    }

    public LocalDate getCreated_date() {
        return created_date;
    }

    public void setCreated_date(LocalDate created_date) {
        this.created_date = created_date;
    }

    public String get__op() {
        return __op;
    }

    public void set__op(String __op) {
        this.__op = __op;
    }
}


