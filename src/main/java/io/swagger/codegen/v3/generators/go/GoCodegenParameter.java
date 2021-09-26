package io.swagger.codegen.v3.generators.go;

import io.swagger.codegen.v3.CodegenParameter;
import java.util.ArrayList;
import java.util.HashMap;

public class GoCodegenParameter extends CodegenParameter {

    public GoCodegenParameter(){};

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
        return this.dataType.equalsIgnoreCase("int64") || this.dataType.equalsIgnoreCase("integer");
    }
    public Boolean getIsInt32() {
        return this.dataType.equalsIgnoreCase("int32");
    }
    public Boolean getIsBoolean() {
        return this.dataType.equalsIgnoreCase("bool");
    }

    public Boolean getIsFile() {
        return this.dataType.equalsIgnoreCase("*os.File");
    }
    public Boolean getIsTime() {
        return this.dataType.equalsIgnoreCase("*os.File");
    }
    public Boolean getIsArray() {
        return this.dataType.startsWith("[]");
    }

    public GoCodegenParameter newInstance() {
        return new GoCodegenParameter();
    }

    public GoCodegenParameter copy() {
        GoCodegenParameter output = new GoCodegenParameter();
        output.secondaryParam = this.secondaryParam;
        output.baseName = this.baseName;
        output.paramName = this.paramName;
        output.dataType = this.dataType;
        output.datatypeWithEnum = this.datatypeWithEnum;
        output.enumName = this.enumName;
        output.dataFormat = this.dataFormat;
        output.collectionFormat = this.collectionFormat;
        output.description = this.description;
        output.unescapedDescription = this.unescapedDescription;
        output.baseType = this.baseType;
        output.nullable = this.nullable;
        output.required = this.required;
        output.maximum = this.maximum;
        output.exclusiveMaximum = this.exclusiveMaximum;
        output.minimum = this.minimum;
        output.exclusiveMinimum = this.exclusiveMinimum;
        output.maxLength = this.maxLength;
        output.minLength = this.minLength;
        output.pattern = this.pattern;
        output.maxItems = this.maxItems;
        output.minItems = this.minItems;
        output.uniqueItems = this.uniqueItems;
        output.multipleOf = this.multipleOf;
        output.jsonSchema = this.jsonSchema;
        output.defaultValue = this.defaultValue;
        output.example = this.example;
        output.testExample = this.testExample;
        if (this._enum != null) {
            output._enum = new ArrayList<String>(this._enum);
        }
        if (this.allowableValues != null) {
            output.allowableValues = new HashMap<String, Object>(this.allowableValues);
        }
        if (this.items != null) {
            output.items = this.items;
        }
        if(this.vendorExtensions != null){
            output.vendorExtensions = new HashMap<String, Object>(this.vendorExtensions);
        }

        return output;
    }
}
