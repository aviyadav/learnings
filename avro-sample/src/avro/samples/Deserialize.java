package avro.samples;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.File;
import java.io.IOException;

public class Deserialize {

    public static void main(String[] args) throws IOException {
        DatumReader<emp> empDatumReader = new SpecificDatumReader<>(emp.class);
        DataFileReader<emp> dataFileReader = new DataFileReader<emp>(new File("schema/emp.avro"), empDatumReader);

        emp em1 = null;

        while (dataFileReader.hasNext()) {
            em1 = dataFileReader.next(em1);

            System.out.println(em1);
        }
    }
}
