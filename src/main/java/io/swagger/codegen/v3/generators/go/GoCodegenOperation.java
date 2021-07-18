package io.swagger.codegen.v3.generators.go;

import com.google.common.collect.Lists;
import io.swagger.codegen.v3.CodegenOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class GoCodegenOperation extends CodegenOperation {

    public boolean hasQueryParams() {
        return !(this.queryParams == null || this.queryParams.isEmpty());
    }

}
