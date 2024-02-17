package com.example.demo1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class function {
    public static void AddLog(String username, String message){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("record.log", true))){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String time = dtf.format(now);
            String msg="["+time+"]  user: "+username+" -> "+message;
            bw.write(msg);
            bw.newLine();
            
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public static String hashString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder(2 * hashedBytes.length);
            
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing the string", e);
        }
    }
    
    public static String getTerm(){
        String entranceDate;
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        
        String[] miladiDate = dtf.format(now).split("-");
        int[] shamsiDate = gregorian_to_jalali(Integer.parseInt(miladiDate[0]), Integer.parseInt(miladiDate[1]), Integer.parseInt(miladiDate[2]));
        
        if(shamsiDate[1]<8)
            entranceDate = shamsiDate[0]+"-1";
        else
            entranceDate = shamsiDate[0]+"-2";
        
        return entranceDate;
    }
    
    //convert shamsi to miladi date
    public static int[] gregorian_to_jalali(int gy, int gm, int gd) {
        int[] out = {
                (gm > 2) ? (gy + 1) : gy,
                0,
                0
        };
        {
            int[] g_d_m = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
            out[2] = 355666 + (365 * gy) + ((int) ((out[0] + 3) / 4)) - ((int) ((out[0] + 99) / 100)) + ((int) ((out[0] + 399) / 400)) + gd + g_d_m[gm - 1];
        }
        out[0] = -1595 + (33 * ((int) (out[2] / 12053)));
        out[2] %= 12053;
        out[0] += 4 * ((int) (out[2] / 1461));
        out[2] %= 1461;
        if (out[2] > 365) {
            out[0] += (int) ((out[2] - 1) / 365);
            out[2] = (out[2] - 1) % 365;
        }
        if (out[2] < 186) {
            out[1] = 1 + (int)(out[2] / 31);
            out[2] = 1 + (out[2] % 31);
        } else {
            out[1] = 7 + (int)((out[2] - 186) / 30);
            out[2] = 1 + ((out[2] - 186) % 30);
        }
        return out;
    }
    
    public static boolean isCorrectTime(String entrance){
        boolean flag = true;
        
        String correctDate = switch (entrance.split("-")[0]) {
            case "1402" -> "1402-11-30";
            case "1401" -> "1402-11-29";
            case "1400" -> "1402-11-19";
            default -> "1402-11-27";
        };
        
        //calculating current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String[] miladiDate = dtf.format(now).split("-");
        int[] nowArr = gregorian_to_jalali(Integer.parseInt(miladiDate[0]), Integer.parseInt(miladiDate[1]), Integer.parseInt(miladiDate[2]));
        String nowDate = nowArr[0]+"-"+nowArr[1]+"-"+nowArr[2];
        
        if(!correctDate.equals(nowDate))
            flag=false;
        
        
        return flag;
    }
}
