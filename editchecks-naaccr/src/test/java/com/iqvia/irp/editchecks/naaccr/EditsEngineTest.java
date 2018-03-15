package com.iqvia.irp.editchecks.naaccr;

import java.io.BufferedReader;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Set;

import editck.naaccr.EditSetDataObject;
import editck.naaccr.EditsEngine;
import editck.naaccr.EditsEngineException;
import editck.naaccr.LayoutDataObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A functional test of the {@link EditsEngine}.
 *
 * @author Gopinathan Balaji
 *
 */
public class EditsEngineTest {

    private static final Logger LOG = LoggerFactory.getLogger(EditsEngineTest.class);

    /**
     * Run a couple of records against the engine.
     *
     * @throws EditsEngineException
     */
    @Test
    public void testEngineFunctional() throws EditsEngineException, FileNotFoundException {
        
        final long start = System.currentTimeMillis();
        final File smfFile = Paths.get("src", "main", "resources", "smf", "naaccr.smf").toAbsolutePath().toFile();
        LOG.info("SMF File '{}': {}.", smfFile.toString(), (smfFile.canRead() ? "canRead" : "cannotRead"));
        EditsEngine engine = null;
        try {
            System.load(Paths.get("src", "main", "resources", "native", "EDITS50.dll").toAbsolutePath().toString());
            engine = EditsEngine.initialize(smfFile.toString());
            LOG.info("engine version = '{}', metafile structure version = '{}'.", engine.getEngineVersion(),
                    engine.getMetafileStructureVersion());
            LOG.info("smf info = '{}'.", engine.getMetafileInformation());
            LOG.info("version comparison result = '{}'.", engine.getVersionComparisonResult());
            LOG.info("smf id = '{}'.", engine.getMetafileID());
            Set<EditSetDataObject> editSets = engine.getEditSetDataObjects();
            editSets.forEach((es) -> {
                LOG.info(es.toString());
            });
            Set<LayoutDataObject> layouts = engine.getLayoutDataObjects();
            layouts.forEach((l) -> {
                LOG.info(l.toString());
            });

//            final ArrayList<String> data = new ArrayList<>();
//
//            data.add("FAKE_LINE");
//            data.add("I1              1407232368635ABCDEFGHI11100000000ABCDEFGH                                     ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXAZ684264350245                 30427259613426  956076576218  04582423900ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUV2                                                                                                              0120100615  C5775840128032173445429840231540236  0067668576  80                                                                                                    ABCDEFGHIJFAC-0001                      ABCDEFGHI37ABC62805466  42578123  87473334  03168 3042270 400880000096251                                                                                                    18ABCAB30A2937ABCDEFGHIJKLMABABAA402ABCDABCDABCDABCDA1ABCDABCDABCDABCDA2AB02989689320009800999989911884157876686010351484075687136704485087086011081706213845503003163514852255812086752268678031204023                                                          846157765468      1841000ABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDEABCDE                                                                                                                                                                                                        76670702  03807815  42133367  56768352  72881406  08551480  07672412  40418673  76557375  28086307  75872573  36262464  32508484  1186583A8A50265899902990095041337326773043098874720AB347AA                                                                                                    56305256  ABAAABAAAAA81464861  ABAAABAAAAA66440273  ABAAABAAAAAA                                                                                                                         1710986932           490604ABCDEFGHIJ2  AB5164866352021213                20110131                                                                                                                             20100676  11038ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXFM132483105256A12023805  53ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXIL651071534ABCD800303144470872                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ");
//            data.add("I1              1507232368635ABCDEFGHI11100000000ABCDEFGH ");
//            data.add("I1              1501232368635ABCDEFGHI11100000000ABCDEFGH ");
//            data.add("I1              1607232368635ABCDEFGHI11100000000ABCDEFGH ");
//            engine.runEdits(data.stream());
            
            File largeDS = new File("C:\\Users\\Q845332\\codebase\\data\\naaccr-files\\NAACCR-RANDOM-1000.dat");
            BufferedReader reader = new BufferedReader(new FileReader(largeDS));

            final long startRunEdits = System.currentTimeMillis();
            engine.runEdits(reader.lines());
            final long durRunEdits = System.currentTimeMillis() - startRunEdits;
//            LOG.info("Total Time to run edits for {} records is {} ms. Average = {} ms per record.", data.size(), durRunEdits, (durRunEdits / (1.0f * data.size())));

            LOG.info("Total Time to run edits for {} records is {} ms. Average = {} ms per record.", 10000, durRunEdits, (durRunEdits / (1.0f * 10000)));

        } finally {
            if (engine != null) {
                engine.close();
            }
        }
        LOG.info("Test ended. Time taken = {} ms.", (System.currentTimeMillis() - start));
        assertTrue("No problems so far", true);
    }

}
