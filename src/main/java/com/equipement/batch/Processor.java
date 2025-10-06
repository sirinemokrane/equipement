package com.equipement.batch;

import com.equipement.entity.Cable;
import org.springframework.batch.item.ItemProcessor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Single, clean ItemProcessor implementation.
 */
public class Processor implements ItemProcessor<String[], Cable> {

    @Override
    public Cable process(String[] fields) {
        if (fields == null || fields.length == 0) return null;
        Cable c = new Cable();

        c.setType(read(fields, 0));
        c.setSerialNb(read(fields, 1));
        c.setChannelNb(parseInteger(fields, 2));
        c.setLineName(read(fields, 3));
        c.setPointNb(parseDouble(fields, 4));
        c.setState(read(fields, 5));
        c.setAutoTest(read(fields, 6));
        c.setEasting(parseLong(fields, 7));
        c.setNorthing(parseLong(fields, 8));
        c.setElevation(parseInteger(fields, 9));
        c.setNoise(parseDouble(fields, 10));
        c.setDistortion(parseDouble(fields, 11));
        c.setPhase(parseDouble(fields, 12));
        c.setGain(parseDouble(fields, 13));
        c.setVersion(read(fields, 14));
        c.setLastTestDate(parseDate(fields, 15));
        c.setCxMaster(parseLong(fields, 16));

        if (c.getState() == null) c.setState("ko");
        return c;
    }

    private String read(String[] f, int idx) {
        if (f == null || idx >= f.length) return null;
        String s = f[idx];
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) s = s.substring(1, s.length() - 1);
        return s;
    }

    private Integer parseInteger(String[] f, int idx) {
        String s = read(f, idx);
        if (s == null) return null;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; }
    }

    private Long parseLong(String[] f, int idx) {
        String s = read(f, idx);
        if (s == null) return null;
        try { return Long.parseLong(s); } catch (NumberFormatException e) { return null; }
    }

    private Double parseDouble(String[] f, int idx) {
        String s = read(f, idx);
        if (s == null) return null;
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return null; }
    }

    private Date parseDate(String[] f, int idx) {
        String s = read(f, idx);
        if (s == null) return null;
        String t = s;
        int pos = t.indexOf(" UTC");
        if (pos > 0) t = t.substring(0, pos);
        String[] fmts = new String[]{"dd/MM/yyyy HH:mm:ss","yyyy-MM-dd'T'HH:mm:ss","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"};
        for (String fmt : fmts) {
            try { return new SimpleDateFormat(fmt).parse(t); } catch (ParseException ignored) { }
        }
        return null;
    }
}
