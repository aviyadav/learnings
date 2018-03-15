package main;

import editck.naaccr.EditSetDataObject;
import editck.naaccr.EditsEngine;
import editck.naaccr.EditsEngineException;
import editck.naaccr.LayoutDataObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditsThread implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(EditsThread.class);

    @Override
    public void run() {
        try {
            final long start = System.currentTimeMillis();
            final File smfFile = Paths.get("src", "main", "resources", "smf", "naaccr.smf").toAbsolutePath().toFile();
            LOG.info("SMF File '{0}': {1}.", smfFile.toString(), smfFile.canRead() ? "canRead" : "cannotRead");
            EditsEngine engine = null;
            try {
                System.load(Paths.get("src", "main", "resources", "native", "EDITS50.dll").toAbsolutePath().toString());
                engine = EditsEngine.initialize(smfFile.toString());
                LOG.info("engine version = '{ }', metafile structure version = '{ }'.", engine.getEngineVersion(), engine.getMetafileStructureVersion());
                LOG.info("smf info = '{}'.", engine.getMetafileInformation());
                LOG.info("version comparison result = '{}'.", engine.getVersionComparisonResult());
                LOG.info("smf id = '{}'.", engine.getMetafileID());
                Set<EditSetDataObject> editSets = engine.getEditSetDataObjects();
                editSets.forEach((EditSetDataObject es) -> {
                    LOG.info(es.toString());
                });
                Set<LayoutDataObject> layouts = engine.getLayoutDataObjects();
                layouts.forEach((LayoutDataObject l) -> {
                    LOG.info(l.toString());
                });

                File largeDS = new File("C:\\Users\\Q845332\\codebase\\data\\naaccr-files\\NAACCR-RANDOM-1000.dat");
                BufferedReader reader = new BufferedReader(new FileReader(largeDS));

                final long startRunEdits = System.currentTimeMillis();
                engine.runEdits(reader.lines());
                final long durRunEdits = System.currentTimeMillis() - startRunEdits;
//            LOG.info("Total Time to run edits for {} records is {} ms. Average = {} ms per record.", data.size(), durRunEdits, (durRunEdits / (1.0f * data.size())));

                LOG.info("Total Time to run edits for { } records is { } ms. Average = { } ms per record.", 10000, durRunEdits, (durRunEdits / (1.0f * 10000)));

            } finally {
                if (engine != null) {
                    engine.close();
                }
            }
            LOG.info("Test ended. Time taken = {} ms.", (System.currentTimeMillis() - start));
        } catch (EditsEngineException | FileNotFoundException ex) {
            LOG.error(ex.getMessage());
        }
    }
}
