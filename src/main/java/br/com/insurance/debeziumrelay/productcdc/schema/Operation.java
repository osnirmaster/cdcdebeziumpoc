package br.com.insurance.debeziumrelay.productcdc.schema;

public enum Operation {

    READ("r"),
    CREATE("c"),
    UPDATE("u"),
    DELETE("d");

    private final String code;

   private Operation(String code) {
        this.code = code;
    }

    private String getCode(){
        return this.code;
    }

    public static Operation searchCodeOperation(String code){

        for (Operation operation: values()) {

            if(operation.getCode().equalsIgnoreCase(code)){
                return operation;
            }
        }
       return null;
    }
}
