package io.swagger.codegen.v3.generators.go;

import io.swagger.codegen.v3.CodegenOperation;

public class GoCodegenOperation extends CodegenOperation {

    public boolean hasQueryParams() {
        return !(this.queryParams == null || this.queryParams.isEmpty());
    }

}
