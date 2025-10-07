package com.equipement.batch;

import com.equipement.entity.Cable;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.equipement.Repository.CableRepository;
import java.util.ArrayList;
import java.util.List;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CsvCableImporter {

    @Autowired
    private CableRepository cableRepository;

    /**
     * Read the packaged resource `/data/equipementt` and persist parsed cables.
     * Returns the list of saved Cable entities.
     */
    /**
     * Import and save CSV. If clearFirst is true, delete all existing Cable rows first.
     */
    public List<Cable> importAndSave(boolean clearFirst) {
        List<Cable> saved = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream("/data/equipementt")) {
            if (is == null) {
                System.out.println("data/equipementt introuvable dans resources/data");
                return saved;
            }

            if (clearFirst) {
                System.out.println("Clearing existing Cable rows before import");
                cableRepository.deleteAll();
            }

            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is))) {
                br.readLine(); // skip header
                String raw;
                int lineNo = 0;
                while ((raw = br.readLine()) != null) {
                    lineNo++;
                    String line = raw.trim();
                    // If the whole line is wrapped in quotes, remove the outermost pair first
                    if (line.length() >= 2 && line.startsWith("\"") && line.endsWith("\"")) {
                        line = line.substring(1, line.length() - 1);
                    }
                    // Unescape doubled-quotes produced by packaging the whole line inside quotes
                    line = line.replace("\"\"", "\"");

                    // Use OpenCSV to reliably parse the (now normal) CSV line
                    String[] parts = null;
                    try (java.io.StringReader sr = new java.io.StringReader(line)) {
                        CSVParser parser = new CSVParserBuilder()
                                .withSeparator(',')
                                .withQuoteChar('"')
                                .withEscapeChar('"')
                                .build();
                        try (CSVReader lr = new CSVReaderBuilder(sr).withCSVParser(parser).build()) {
                            parts = lr.readNext();
                        }
                    } catch (Exception e) {
                        System.out.println("[CSV PARSE ERROR] lineNo=" + lineNo + " err=" + e.getMessage());
                        parts = parseCsvLine(line); // fallback to custom parser
                    }

                    // Normalize tokens: trim and map empty -> null
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i] == null) continue;
                        String s = parts[i].trim();
                        if (s.isEmpty()) parts[i] = null; else parts[i] = s;
                    }
                    // Debug: print first 20 parsed lines to log to help diagnose parsing
                    if (lineNo <= 20) {
                        System.out.println("[CSV PARSE] lineNo=" + lineNo + " parts=" + java.util.Arrays.toString(parts));
                    }

                    Cable c = mapFieldsToCable(parts);
                    if (c.getState() == null) c.setState("ko");

                    if (lineNo <= 20) {
                        System.out.println("[CSV MAP] lineNo=" + lineNo + " cableBeforeSave=" + c);
                    }
                    Cable savedEntity = cableRepository.save(c);
                    if (lineNo <= 20) {
                        System.out.println("[CSV SAVED] lineNo=" + lineNo + " id=" + savedEntity.getIdCable() + " saved=" + savedEntity);
                    }
                    saved.add(savedEntity);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'import CSV: " + e.getMessage());
            e.printStackTrace();
        }
        return saved;
    }

    /**
     * Parse a single CSV line into fields. Handles quoted fields and doubled quotes as escaped quotes.
     */
    private String[] parseCsvLine(String line) {
        if (line == null) return new String[0];
        List<String> tokens = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch == '"') {
                    // if next is also quote, it's an escaped quote
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++; // skip next
                    } else {
                        inQuotes = false; // end quoted section
                    }
                } else {
                    cur.append(ch);
                }
            } else {
                if (ch == '"') {
                    inQuotes = true;
                } else if (ch == ',') {
                    tokens.add(cur.length() == 0 ? null : cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(ch);
                }
            }
        }
        // add last token
        tokens.add(cur.length() == 0 ? null : cur.toString());
        return tokens.toArray(new String[0]);
    }

    private Cable mapFieldsToCable(String[] f) {
        Cable c = new Cable();
        if (f == null || f.length == 0) return c;
        // CSV columns: Type,Serial Nb,Channel Nb,Line Name,Point Nb,State,Auto Test,Easting (m),Northing (m),Elevation (m),Noise (μV),Distortion (dB),Phase(μs),Gain (%),Version,Last Test Date,Cx Master
        c.setType(emptyToNull(get(f,0)));
        c.setSerialNb(emptyToNull(get(f,1)));
        c.setChannelNb(parseInteger(get(f,2)));
        c.setLineName(emptyToNull(get(f,3)));
        c.setPointNb(parseDouble(get(f,4)));
        c.setState(emptyToNull(get(f,5)));
        c.setAutoTest(emptyToNull(get(f,6)));
        c.setEasting(parseLong(get(f,7)));
        c.setNorthing(parseLong(get(f,8)));
        c.setElevation(parseInteger(get(f,9)));
        c.setNoise(parseDouble(get(f,10)));
        c.setDistortion(parseDouble(get(f,11)));
        c.setPhase(parseDouble(get(f,12)));
        c.setGain(parseDouble(get(f,13)));
        c.setVersion(emptyToNull(get(f,14)));
        c.setLastTestDate(parseDate(get(f,15)));
        c.setCxMaster(parseLong(get(f,16)));
        return c;
    }

    private String get(String[] f, int idx) {
        if (f == null || idx >= f.length) return null;
        return f[idx];
    }

    private String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private Double parseDouble(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return null; }
    }

    private Long parseLong(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try { return Long.parseLong(s.trim()); } catch (Exception e) { return null; }
    }

    private Integer parseInteger(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return null; }
    }

    private Date parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        String t = s.trim();
        int idx = t.indexOf(" UTC");
        if (idx > 0) t = t.substring(0, idx);
        SimpleDateFormat[] fmts = new SimpleDateFormat[]{
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                new SimpleDateFormat("yyyy-MM-dd")
        };
        for (SimpleDateFormat f : fmts) {
            try { return f.parse(t); } catch (ParseException ignored) {}
        }
        return null;
    }

    // no manual CSV splitter needed — using OpenCSV CSVReader
}
