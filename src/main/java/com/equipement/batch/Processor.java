package com.equipement.batch;

import com.equipement.entity.Cable;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
public class Processor implements ItemProcessor<String[], Cable> {

    @Override
    public Cable process(String[] fields) {
        System.out.println("[v0] ===== PROCESSOR START =====");
        System.out.println("[v0] Processing cable with " + fields.length + " fields");
        
        if (fields == null || fields.length == 0) {
            System.err.println("[v0] Empty fields array, skipping");
            return null;
        }
        
        if (fields.length < 17) {
            System.err.println("[v0] ERROR: Expected 17 fields but got " + fields.length);
            System.err.println("[v0] Cannot process this line, skipping");
            return null;
        }
        
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

        // Default state to "ko" if null (required field in DB)
        if (c.getState() == null || c.getState().trim().isEmpty()) {
            c.setState("ko");
        }
        
        System.out.println("[v0] CREATED CABLE:");
        System.out.println("[v0]   type=" + c.getType());
        System.out.println("[v0]   serialNb=" + c.getSerialNb());
        System.out.println("[v0]   channelNb=" + c.getChannelNb());
        System.out.println("[v0]   lineName=" + c.getLineName());
        System.out.println("[v0]   pointNb=" + c.getPointNb());
        System.out.println("[v0]   state=" + c.getState());
        System.out.println("[v0]   autoTest=" + c.getAutoTest());
        System.out.println("[v0]   easting=" + c.getEasting());
        System.out.println("[v0]   version=" + c.getVersion());
        System.out.println("[v0] ===== PROCESSOR END =====");
        
        return c;
    }

    private String read(String[] f, int idx) {
        if (f == null || idx >= f.length) {
            System.err.println("[v0] Index " + idx + " out of bounds (length=" + (f != null ? f.length : 0) + ")");
            return null;
        }
        String s = f[idx];
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        return s;
    }

    private Integer parseInteger(String[] f, int idx) {
        String s = read(f, idx);
        if (s == null) return null;
        try { 
            return Integer.parseInt(s); 
        } catch (NumberFormatException e) { 
            System.err.println("[v0] Failed to parse integer at index " + idx + ": '" + s + "'");
            return null; 
        }
    }

    private Long parseLong(String[] f, int idx) {
        String s = read(f, idx);
        if (s == null) return null;
        try { 
            return Long.parseLong(s); 
        } catch (NumberFormatException e) { 
            System.err.println("[v0] Failed to parse long at index " + idx + ": '" + s + "'");
            return null; 
        }
    }

    private Double parseDouble(String[] f, int idx) {
        String s = read(f, idx);
        if (s == null) return null;
        try { 
            return Double.parseDouble(s); 
        } catch (NumberFormatException e) { 
            System.err.println("[v0] Failed to parse double at index " + idx + ": '" + s + "'");
            return null; 
        }
    }

    private Date parseDate(String[] f, int idx) {
        String s = read(f, idx);
        if (s == null) return null;
        
        // Remove UTC timezone suffix
        String t = s;
        int pos = t.indexOf(" UTC");
        if (pos > 0) {
            t = t.substring(0, pos);
        }
        
        // Try multiple date formats
        String[] fmts = new String[]{
            "dd/MM/yyyy HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd"
        };
        
        for (String fmt : fmts) {
            try { 
                return new SimpleDateFormat(fmt).parse(t); 
            } catch (ParseException ignored) { }
        }
        
        System.err.println("[v0] Failed to parse date at index " + idx + ": '" + s + "'");
        return null;
    }
}
