package com.example.productcatalog.specs;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SearchOperation {
    LIKE("q", new String[]{"name", "description"}),
    GREATER_OR_EQUAL("min_price", new String[]{"price"}),
    LESS_OR_EQUAL("max_price", new String[]{"price"});

    private final String queryParam;
    private final String[] fields;

    SearchOperation(String queryParam, String[] fields) {
        this.queryParam = queryParam;
        this.fields = fields;
    }

    public String getQueryParam() { return this.queryParam; }

    public String[] getFields() { return this.fields; }

    public static List<String> getOperationParams() {
        return Stream.of(SearchOperation.values()).map(op -> op.getQueryParam()).collect(Collectors.toList());
    }

    public static SearchOperation getByParam(String param) {
        return Stream.of(SearchOperation.values()).filter(op -> op.getQueryParam().equals(param)).findFirst().get();
    }
}
