package soham.jspring.avro;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.SchemaCompatibility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class SchemaGenerator {
    private static final String AVRO_SCHEMA_FILE_EXTENSION = ".avsc";

    public static void main(String[] args) {
        Schema schema = SchemaBuilder.record("User")
                .namespace("soham.jspring.kafka")
                .fields().requiredString("firstname").requiredString("lastname")
                .endRecord();
        final String schemaPath = args[0];

        final Path dirPath = Path.of(schemaPath);
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        final String filePathString = schemaPath + "/" + schema.getName() + AVRO_SCHEMA_FILE_EXTENSION;
        Path filePath = Path.of(filePathString);
        try {
            Files.write(filePath, schema.toString(true).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    private static void validateSchemaCompatability(final Schema schema, final Schema oldSchema) {
        final List<SchemaCompatibility.Incompatibility> incompatibilities = SchemaCompatibility.checkReaderWriterCompatibility(oldSchema, schema).getResult().getIncompatibilities();
        if (!incompatibilities.isEmpty()) {
            throw new IllegalArgumentException("Incompatible schema's. \n" + incompatibilities.get(0).toString());
        }

    }
}
