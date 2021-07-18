package io.swagger.codegen.v3.generators.go;

import io.swagger.codegen.v3.CodegenParameter;

public class GoCodegenParameter extends CodegenParameter {


    public Boolean getIsString() {
        return this.dataType.equalsIgnoreCase("string");
    }
    public Boolean getIsDateTime() {
        return this.dataType.equalsIgnoreCase("time.Time");
    }
    public Boolean getIsFloat32() {
        return this.dataType.equalsIgnoreCase("float32");
    }
    public Boolean getIsFloat64() {
        return this.dataType.equalsIgnoreCase("float64");
    }

    public Boolean getIsInt64() {
        return this.dataType.equalsIgnoreCase("int64");
    }
    public Boolean getIsInt32() {
        return this.dataType.equalsIgnoreCase("int32");
    }
    public Boolean getIsBoolean() {
        return this.dataType.equalsIgnoreCase("bool");
    }

}
