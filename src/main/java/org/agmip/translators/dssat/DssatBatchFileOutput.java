package org.agmip.translators.dssat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.agmip.util.MapUtil.*;

/**
 * DSSAT Batch File I/O API Class
 *
 * @author Meng Zhang
 * @version 1.0
 */
public class DssatBatchFileOutput extends DssatCommonOutput {

    /**
     * DSSAT Batch File Output method
     *
     * @param arg0 file output path
     * @param results array of data holder object
     */
    public void writeFile(String arg0, ArrayList<LinkedHashMap> results) {

        // Initial variables
        LinkedHashMap result;                       // Data holder for summary data
        BufferedWriter bwB;                         // output object
        StringBuilder sbData = new StringBuilder(); // construct the data info in the output

        try {

            // Set default value for missing data
            setDefVal();

            // Get Data from input holder
            if (results.isEmpty()) {
                return;
            }
            result = results.get(0);

            // Initial BufferedWriter

            arg0 = revisePath(arg0);
            outputFile = new File(arg0 + "DSSBatch.v45");
            bwB = new BufferedWriter(new FileWriter(outputFile));

            // Output Batch File
            // Titel Section
            String crop = getCropName(result);
            String dssatPath = "C:\\DSSAT45\\";
            String exFileName = getExName(result);
            int dssatVersion;
            int expNo = results.size();
            // Get file name
            try {
                exFileName = exFileName.substring(0, exFileName.length() - 2) + "." + exFileName.substring(exFileName.length() - 2) + "X";
            } catch (Exception e) {
                exFileName = "TEMP.XXX";
            }
            // Get version number
            try {
                String cropVersion = getObjectOr(result, "crop_model_version", "").replaceAll("\\D", "");
                dssatVersion = Integer.parseInt(cropVersion);
            } catch (Exception e) {
                dssatVersion = 45;
            }

            // Write title section
            sbData.append("$BATCH(").append(crop.toUpperCase()).append(")\r\n!\r\n");
            sbData.append(String.format("! Command Line : %1$sDSCSM%2$03d.EXE B DSSBatch.v%2$02d\r\n", dssatPath, dssatVersion));
            sbData.append("! Crop         : ").append(crop).append("\r\n");
            sbData.append("! Experiment   : ").append(exFileName).append("\r\n");
            sbData.append("! ExpNo        : ").append(expNo).append("\r\n");
            sbData.append(String.format("! Debug        : %1$sDSCSM%2$03d.EXE \" B DSSBatch.v%2$02d\"\r\n!\r\n", dssatPath, dssatVersion));

            // Body Section
            sbData.append("@FILEX                                                                                        TRTNO     RP     SQ     OP     CO\r\n");
            for (int i = 0; i < results.size(); i++) {
                result = results.get(i);
                String folderPath = getObjectOr(result, "exname", "Experiment_" + i);
                folderPath += File.separator;

                // Get DSSAT Sequence info
                LinkedHashMap dssatSeqData = getObjectOr(result, "dssat_sequence", new LinkedHashMap());
                ArrayList<LinkedHashMap> dssatSeqArr = getObjectOr(dssatSeqData, "data", new ArrayList<LinkedHashMap>());
                // If missing, set default value
                if (dssatSeqArr.isEmpty()) {
                    LinkedHashMap tmp = new LinkedHashMap();
                    tmp.put("sq", "1");
                    tmp.put("op", "1");
                    tmp.put("co", "0");
                    dssatSeqArr.add(tmp);
                }
                // Output all info
                for (int j = 0; j < dssatSeqArr.size(); j++) {
                    sbData.append(String.format("%1$-92s %2$6s %3$6s %4$6s %5$6s %6$6s\r\n",
                            folderPath + exFileName,
                            "1",
                            "1",
                            getObjectOr(dssatSeqArr.get(j), "sq", "1"),
                            getObjectOr(dssatSeqArr.get(j), "op", "1"),
                            getObjectOr(dssatSeqArr.get(j), "co", "0")));
                }
            }

            // Output finish
            bwB.write(sbError.toString());
            bwB.write(sbData.toString());
            bwB.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * DSSAT Batch File Output method
     *
     * @param arg0 file output path
     * @param result data holder object
     */
    @Override
    public void writeFile(String arg0, Map result) {

        // Initial variables
        BufferedWriter bwB;                         // output object
        StringBuilder sbData = new StringBuilder(); // construct the data info in the output

        try {

            // Set default value for missing data
            setDefVal();

            // Initial BufferedWriter
            arg0 = revisePath(arg0);
            outputFile = new File(arg0 + "DSSBatch.v45");
            bwB = new BufferedWriter(new FileWriter(outputFile));

            // Output Batch File
            // Titel Section
            String crop = getCropName(result);
            String dssatPath = "C:\\DSSAT45\\";
            String exFileName = getExName(result);
            int dssatVersion;
            int expNo = 1;
            // Get file name
            try {
                exFileName = exFileName.substring(0, exFileName.length() - 2) + "." + exFileName.substring(exFileName.length() - 2) + "X";
            } catch (Exception e) {
                exFileName = "TEMP.XXX";
            }
            // Get version number
            try {
                String cropVersion = getObjectOr(result, "crop_model_version", "").replaceAll("\\D", "");
                dssatVersion = Integer.parseInt(cropVersion);
            } catch (Exception e) {
                dssatVersion = 45;
            }

            // Write title section
            sbData.append("$BATCH(").append(crop).append(")\r\n!\r\n");
            sbData.append(String.format("! Command Line : %1$sDSCSM%2$03d.EXE B DSSBatch.v%2$02d\r\n", dssatPath, dssatVersion));
            sbData.append("! Crop         : ").append(crop).append("\r\n");
            sbData.append("! Experiment   : ").append(exFileName).append("\r\n");
            sbData.append("! ExpNo        : ").append(expNo).append("\r\n");
            sbData.append(String.format("! Debug        : %1$sDSCSM%2$03d.EXE \" B DSSBatch.v%2$02d\"\r\n!\r\n", dssatPath, dssatVersion));

            // Body Section
            sbData.append("@FILEX                                                                                        TRTNO     RP     SQ     OP     CO\r\n");
            // Get DSSAT Sequence info
            LinkedHashMap dssatSeqData = getObjectOr(result, "dssat_sequence", new LinkedHashMap());
            ArrayList<LinkedHashMap> dssatSeqArr = getObjectOr(dssatSeqData, "data", new ArrayList<LinkedHashMap>());
            // If missing, set default value
            if (dssatSeqArr.isEmpty()) {
                LinkedHashMap tmp = new LinkedHashMap();
                tmp.put("sq", "1");
                tmp.put("op", "1");
                tmp.put("co", "0");
                dssatSeqArr.add(tmp);
            }
            // Output all info
            for (int i = 0; i < dssatSeqArr.size(); i++) {
                sbData.append(String.format("%1$-92s %2$6s %3$6s %4$6s %5$6s %6$6s",
                        exFileName,
                        "1",
                        "1",
                        getObjectOr(dssatSeqArr.get(i), "sq", "1"),
                        getObjectOr(dssatSeqArr.get(i), "op", "1"),
                        getObjectOr(dssatSeqArr.get(i), "co", "0")));
            }

            // Output finish
            bwB.write(sbError.toString());
            bwB.write(sbData.toString());
            bwB.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Get crop name string
     *
     * @param result experiment data holder
     * @return
     */
    private String getCropName(Map result) {
        String ret;
        String crid = null;

        // Get crop id
        LinkedHashMap mgnData = getObjectOr(result, "management", new LinkedHashMap());
        ArrayList<LinkedHashMap> events = getObjectOr(mgnData, "events", new ArrayList<LinkedHashMap>());
        for (int i = 0; i < events.size(); i++) {
            if ("planting".equals(events.get(i).get("event"))) {
                crid = (String) events.get(i).get("crid");
                break;
            }
        }
        if (crid == null) {
            String exname = getExName(result);
            if (exname.length() > 2) {
                crid = exname.substring(exname.length() - 2, exname.length());
            }
        }

        // Get crop name string
        if (crid == null || "".equals(crid)) {
            ret = "";
        } else if ("BH".equals(crid)) {
            ret = "Bahia";
        } else if ("BA".equals(crid)) {
            ret = "Barley";
        } else if ("BR".equals(crid)) {
            ret = "Brachiaria";
        } else if ("CB".equals(crid)) {
            ret = "Cabbage";
        } else if ("CS".equals(crid)) {
            ret = "Cassava";
        } else if ("CH".equals(crid)) {
            ret = "Chickpea";
        } else if ("CO".equals(crid)) {
            ret = "Cotton";
        } else if ("CP".equals(crid)) {
            ret = "Cowpea";
        } else if ("BN".equals(crid)) {
            ret = "Drybean";
        } else if ("FB".equals(crid)) {
            ret = "FabaBean";
        } else if ("FA".equals(crid)) {
            ret = "Fallow";
        } else if ("GB".equals(crid)) {
            ret = "GreenBean";
        } else if ("MZ".equals(crid)) {
            ret = "Maize";
        } else if ("ML".equals(crid)) {
            ret = "Millet";
        } else if ("PN".equals(crid)) {
            ret = "Peanut";
        } else if ("PR".equals(crid)) {
            ret = "Pepper";
        } else if ("PI".equals(crid)) {
            ret = "PineApple";
        } else if ("PT".equals(crid)) {
            ret = "Potato";
        } else if ("RI".equals(crid)) {
            ret = "Rice";
        } else if ("SG".equals(crid)) {
            ret = "Sorghum";
        } else if ("SB".equals(crid)) {
            ret = "Soybean";
        } else if ("SC".equals(crid)) {
            ret = "Sugarcane";
        } else if ("SU".equals(crid)) {
            ret = "Sunflower";
        } else if ("SW".equals(crid)) {
            ret = "SweetCorn";
        } else if ("TN".equals(crid)) {
            ret = "Tanier";
        } else if ("TR".equals(crid)) {
            ret = "Taro";
        } else if ("TM".equals(crid)) {
            ret = "Tomato";
        } else if ("VB".equals(crid)) {
            ret = "Velvetbean";
        } else if ("WH".equals(crid)) {
            ret = "Wheat";
        } else {
            ret = crid;
            sbError.append("! Warning: Undefined crop id: [").append(crid).append("]");
        }
        return ret;
    }
}
