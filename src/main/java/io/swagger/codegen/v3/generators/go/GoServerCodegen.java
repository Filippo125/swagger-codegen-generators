package io.swagger.codegen.v3.generators.go;

import io.swagger.codegen.v3.CliOption;
import io.swagger.codegen.v3.CodegenConstants;
import io.swagger.codegen.v3.CodegenContent;
import io.swagger.codegen.v3.CodegenModel;
import io.swagger.codegen.v3.CodegenModelFactory;
import io.swagger.codegen.v3.CodegenModelType;
import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.codegen.v3.CodegenParameter;
import io.swagger.codegen.v3.CodegenType;
import io.swagger.codegen.v3.SupportingFile;
import io.swagger.v3.oas.models.media.Schema;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class GoServerCodegen extends AbstractGoCodegen {
    public static final String USE_LOGRUS = "useLogrus";
    public static final String MAIN_APPLICATION_FOLDER = "mainApplicationFolder";

    protected String apiVersion = "1.0.0";
    protected int serverPort = 8080;
    protected String projectName = "swagger-server";
    protected String apiPath = "go";
    protected String mainFolder = "application";
    protected TreeSet<String> classNamesDict = new TreeSet<>();
    protected List<Map<String,String>> commonImports = new ArrayList<>();

    public GoServerCodegen() {
        super();
        CodegenModelFactory.setTypeMapping(CodegenModelType.OPERATION,GoCodegenOperation.class);
        CodegenModelFactory.setTypeMapping(CodegenModelType.PARAMETER,GoCodegenParameter.class);
        // set the output folder here
        outputFolder = "generated-code/go";

        /*
         * Models.  You can write model files using the modelTemplateFiles map.
         * if you want to create one template for file, you can do so here.
         * for multiple files for model, just put another entry in the `modelTemplateFiles` with
         * a different extension
         */
        modelTemplateFiles.put(
                "model.mustache",
                ".go");

        /*
         * Api classes.  You can write classes for each Api file with the apiTemplateFiles map.
         * as with models, add multiple entries with different extensions for multiple files per
         * class
         */
        apiTemplateFiles.put(
                "controller-api.mustache",   // the template to use
                ".go");       // the extension for each file to write

        apiTemplateFiles.put(
            "controller.mustache",   // the template to use
            "_controller.go");       // the extension for each file to write

        /*
         * Reserved words.  Override this with reserved words specific to your language
         */
        setReservedWordsLowerCase(
            Arrays.asList(
                // data type
                "string", "bool", "uint", "uint8", "uint16", "uint32", "uint64",
                "int", "int8", "int16", "int32", "int64", "float32", "float64",
                "complex64", "complex128", "rune", "byte", "uintptr",

                "break", "default", "func", "interface", "select",
                "case", "defer", "go", "map", "struct",
                "chan", "else", "goto", "package", "switch",
                "const", "fallthrough", "if", "range", "type",
                "continue", "for", "import", "return", "var", "error", "nil")
                // Added "error" as it's used so frequently that it may as well be a keyword
        );

        additionalProperties.put("classNames",this.classNamesDict);
        cliOptions.add(CliOption.newBoolean(USE_LOGRUS, "Use sirupsen logrus library for logging instead of built-in library").defaultValue("false"));
        cliOptions.add(CliOption.newString(MAIN_APPLICATION_FOLDER, "Use to customize the main application folder"));
        //modelPackage = packageName; //"models";
        //apiPackage = packageName;
    }

    @Override
    public CodegenModel fromModel(String name, Schema schema, Map<String, Schema> allDefinitions) {
        CodegenModel codegenModel = super.fromModel(name,schema,allDefinitions);
        //addClassNames(codegenModel);
        return codegenModel;
    }

    @Override
    public String getDefaultTemplateDir() {
        return "go-server";
    }

    @Override
    public void processOpts() {
        super.processOpts();
        if (additionalProperties.containsKey(CodegenConstants.PACKAGE_NAME)) {
            setPackageName((String) additionalProperties.get(CodegenConstants.PACKAGE_NAME));
        }
        else {
            setPackageName("swagger");
        }

        if (additionalProperties.containsKey(CodegenConstants.API_PACKAGE)) {
            setApiPackage((String) additionalProperties.get(CodegenConstants.API_PACKAGE));
        }
        else {
            setApiPackage(packageName);
        }

        if (additionalProperties.containsKey(CodegenConstants.MODEL_PACKAGE)) {
            setModelPackage((String) additionalProperties.get(CodegenConstants.MODEL_PACKAGE));
        }
        else {
            setModelPackage(packageName);
        }


        boolean useLogrus = Boolean.parseBoolean((String)additionalProperties.get(USE_LOGRUS));
        if (useLogrus) {
            commonImports.add(createMapping("import","log  \"github.com/sirupsen/logrus\""));
        } else {
            commonImports.add(createMapping("import","log"));
        }

        if (additionalProperties.containsKey(MAIN_APPLICATION_FOLDER)) {
            mainFolder = (String) additionalProperties.get(MAIN_APPLICATION_FOLDER);
        }

        /*
         * Additional Properties.  These values can be passed to the templates and
         * are available in models, apis, and supporting files
         */
        additionalProperties.put("apiVersion", apiVersion);
        additionalProperties.put("serverPort", serverPort);
        additionalProperties.put("apiPath", apiPath);

        additionalProperties.put(CodegenConstants.PACKAGE_NAME, packageName);
        //additionalProperties.put("swaggerPackage", "sw "+'"'+ packageName + "/" + packageName + '"');
        additionalProperties.put("commonImports", commonImports);


        /*
         * Supporting Files.  You can write single files for the generator with the
         * entire object tree available.  If the input file has a suffix of `.mustache
         * it will be processed by the template engine.  Otherwise, it will be copied
         */
        supportingFiles.add(new SupportingFile("swagger.mustache", "api", "swagger.yaml"));
        supportingFiles.add(new SupportingFile("Dockerfile", "", "Dockerfile"));
        supportingFiles.add(new SupportingFile("main.mustache", mainFileFolder(), "main.go"));
        supportingFiles.add(new SupportingFile("routers.mustache", swaggerFileFolder(), "routers.go"));
        supportingFiles.add(new SupportingFile("logger.mustache", swaggerFileFolder(), "logger.go"));
        supportingFiles.add(new SupportingFile("goMod.mustache", apiPath, "go.mod"));
        writeOptional(outputFolder, new SupportingFile("README.mustache", apiPath, "README.md"));
    }


    /**
     * Configures the type of generator.
     *
     * @return the CodegenType for this generator
     * @see io.swagger.codegen.CodegenType
     */
    @Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    @Override
    protected void addAdditionPropertiesToCodeGenModel(CodegenModel codegenModel, Schema schema) {
        super.addAdditionPropertiesToCodeGenModel(codegenModel, schema);
        addVars(codegenModel, schema.getProperties(), schema.getRequired());
    }
    /**
     * Configures a friendly name for the generator.  This will be used by the generator
     * to select the library with the -l flag.
     *
     * @return the friendly name for the generator
     */
    @Override
    public String getName() {
        return "go-server";
    }

    /**
     * Returns human-friendly help for the generator.  Provide the consumer with help
     * tips, parameters here
     *
     * @return A string value for the help message
     */
    @Override
    public String getHelp() {
        return "Generates a Go server library using the swagger-tools project.  By default, " +
                "it will also generate service classes--which you can disable with the `-Dnoservice` environment variable.";
    }

    /**
     * Location to write api files.  You can use the apiPackage() as defined when the class is
     * instantiated
     */
    @Override
    public String apiFileFolder() {
        // to separate models in a specific package add it, but there is a template to be changed
        //  +   File.separator + packageName;
        if (this.apiHasOwnPackage()) {
            return basePackageFileFolder() + File.separator + apiPackage;
        }
        return basePackageFileFolder() + File.separator + modelPackage;

    }

    @Override
    public String modelFileFolder() {
        // to separate models in a specific package add it, but there is a template to be changed
        // +  File.separator + modelPackage;
        if (this.modelHasOwnPackage()) {
            return basePackageFileFolder() + File.separator + modelPackage;
        }
        return basePackageFileFolder() + File.separator + packageName;

    }

    public String mainFileFolder() {
        return baseSupportFileFolder() + File.separator + "cmd" + File.separator + mainFolder;
    }

    public String swaggerFileFolder() {
        return baseSupportFileFolder() + File.separator + packageName;
    }


    private String baseSupportFileFolder() {
        return packageName.replace('.', File.separatorChar);
    }

    private String basePackageFileFolder() {
        return outputFolder + File.separator + "pkg".replace('.', File.separatorChar);
    }

    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        super.postProcessOperations(objs);
        Map<String, Object> objectMap = (Map<String, Object>) objs.get("operations");
        Object className = objectMap.get("classname");
        if (className != null){
            addClassNames(className.toString());
        }
        List<Map<String, String>> imports = (List<Map<String, String>>) objs.get("imports");
        List<Map<String, String>> modelImports = new ArrayList<>();
        if (imports != null) {
            imports.clear();
            imports.addAll(commonImports);

            List<CodegenOperation> operations = (List<CodegenOperation>) objectMap.get("operation");
            boolean addedStrConvImport = false;
            boolean addedMuxImport = false;
            boolean addedOSImport = false;
            boolean addedModelImport = false;

            for (CodegenOperation operation : operations) {
                for (CodegenContent codegenContent : operation.getContents()) {
                    for (CodegenParameter xParam : codegenContent.getParameters()) {
                        GoCodegenParameter param = (GoCodegenParameter) xParam;
                        // import "os" if the operation uses files
                        if (!addedStrConvImport &&
                            (param.getIsInt32() || param.getIsInt64()
                                || param.getIsFloat32() || param.getIsFloat64()
                                || param.getIsBoolean()
                            ))
                        {
                            imports.add(createMapping("import", "strconv"));
                            addedStrConvImport = true;
                        }
                        if (!addedMuxImport && param.getIsPathParam()){
                            imports.add(createMapping("import","github.com/gorilla/mux"));
                            addedMuxImport = true;
                        }
                        if (!addedOSImport && param.getIsFile()){
                            imports.add(createMapping("import","os"));
                            modelImports.add(createMapping("import","os"));
                            addedOSImport = true;
                        }
                         if (this.modelHasOwnPackage()){
                            param.baseType = this.modelPackage() +  "." + param.baseType;
                            if (!addedModelImport) {
                                modelImports.add(createMapping("import",this.modelPackage()));
                                addedModelImport = true;
                            }
                        }
                    }
                }
            }
            objs.put("modelImports",modelImports);
        }
        return objs;
    }


    private void addClassNames(String classname) {
        this.classNamesDict.add(classname);
    }

    protected boolean apiHasOwnPackage() {
        return !this.packageName.equalsIgnoreCase(this.apiPackage);
    }

    protected boolean modelHasOwnPackage() {
        return !this.packageName.equalsIgnoreCase(this.modelPackage);
    }
}
