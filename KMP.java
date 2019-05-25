package com.example.android.iris;

import android.content.Context;

import java.util.*;
import java.io.*;

class KMP{
    private ArrayList<Float> arrayOfPersen;
    private ArrayList<String> arrayOfJawaban;
    private ArrayList<String> arrayOfPertanyaan;

    public KMP() {
        arrayOfPersen = new ArrayList<Float>();
        arrayOfJawaban = new ArrayList<String>();
        arrayOfPertanyaan = new ArrayList<String>();
    }

    public ArrayList<Float> getArrayOfPersen() {
        return arrayOfPersen;
    }

    public ArrayList<String> getArrayOfJawaban() {
        return arrayOfJawaban;
    }

    public ArrayList<String> getArrayOfPertanyaan() {
        return arrayOfPertanyaan;
    }

    public void addArrayOfPersen(Float kecocokan) {
        arrayOfPersen.add(kecocokan);
    }

    public void addArrayOfJawaban(String jawaban) {
        arrayOfJawaban.add(jawaban);
    }

    public void addArrayOfPertanyaan(String pertanyaan) {
        arrayOfPertanyaan.add(pertanyaan);
    }

    public void KMPSearch(String pattern, Context context){
        arrayOfJawaban.clear();
        arrayOfPersen.clear();
        arrayOfPertanyaan.clear();
        if(pattern.length() > 0) {
            try {
                //buffer reader file
                InputStream inputPertanyaan = context.getResources().openRawResource(R.raw.pertanyaan);
                InputStream inputJawaban = context.getResources().openRawResource(R.raw.jawaban);
                BufferedReader readerFilePertanyaan = new BufferedReader(new InputStreamReader(inputPertanyaan));
                BufferedReader readerFileJawaban = new BufferedReader(new InputStreamReader(inputJawaban));
                readerFileJawaban.mark(1);

                String text = readerFilePertanyaan.readLine();

                int text_length = text.length();
                int pattern_length = pattern.length();
                int borderArray[] = new int[pattern.length()];
                int pattern_idx = 0;
                int text_idx = 0;
                int count_character_pattern = 0;
                int max_count = 0;
                int lines = 1;
                int lines_count = 0;
                String jawaban = null;
                float kecocokan;

                borderFunction(pattern, borderArray);

                while (text != null) {
                    // System.out.println(text);
                    // System.out.println(lines);
                    // System.out.println(arrayOfJawaban);
                    while (text_idx < text_length) {
                        if (text.charAt(text_idx) == pattern.charAt(pattern_idx)) {
                            count_character_pattern++;
                            text_idx++;
                            pattern_idx++;
                            if (pattern_idx == pattern_length) {//pattern found
                                max_count = count_character_pattern;
                                break;
                            }
                        } else {
                            if (count_character_pattern > max_count) {
                                max_count = count_character_pattern;
                            }
                            count_character_pattern = 0;
                            if (pattern_idx == 0) {
                                text_idx++;
                            } else {
                                pattern_idx = borderArray[pattern_idx - 1];
                            }
                        }
                    }
                    // System.out.println("max count : " + max_count + " text_length : " + text_length);
                    kecocokan = (float) max_count / text_length * 100;
                    if (kecocokan > 100) {// kecocokan > 100% ??
                        kecocokan = 100;
                    }
                    // System.out.println(kecocokan);
                    if (kecocokan > 60) {// kecocokan yang disimpan ke array adalah yang > 60%
                        while (lines != lines_count) {
                            jawaban = readerFileJawaban.readLine();// read file jawaban sampai ketemu line ke lines_count
                            lines_count++;
                        }
                        addArrayOfJawaban(jawaban);
                        addArrayOfPersen(kecocokan);
                        addArrayOfPertanyaan(text);
                    }
                    lines++;
                    text = readerFilePertanyaan.readLine();
                    if (text != null) {
                        text_length = text.length();
                        max_count = 0;
                        count_character_pattern = 0;
                        lines_count = 0;
                        readerFileJawaban.reset();
                        text_idx = 0;
                        pattern_idx = 0;
                    }
                }
                readerFileJawaban.close();
                readerFilePertanyaan.close();
            } catch (IOException e) {
            }
        }
    }

    public void borderFunction(String pattern, int borderArray[]){
        borderArray[0] = 0; // pasti 0
        int j = 0;//  batas bawah/pembanding
        int i = 1;// index borderArray

        while(i < pattern.length()){
            if(pattern.charAt(i) == pattern.charAt(j)){
                borderArray[i] = j + 1;
                i++;
                j++;
            }
            else{
                if(j == 0){
                    borderArray[i] = 0;
                    i++;
                }
                else{
                    j = borderArray[j-1];
                }
            }
        }
    }

    /*public static void main(String[] args) {
        String pattern = "Apakah chatbot?";

        String filePertanyaan = "pertanyaan.txt";
        String fileJawaban = "jawaban.txt";

        // ArrayList<Float> arrayPersen = new ArrayList<Float>();
        // ArrayList<String> arrayJawaban = new ArrayList<String>();

        KMP test = new KMP();
        test.KMPSearch(pattern, filePertanyaan, fileJawaban);
        // System.out.println(arrayJawaban.size());
        for(String s : test.getArrayOfJawaban()){
            System.out.println(s);
        }
        for(String s : test.getArrayOfPertanyaan()){
            System.out.println(s);
        }
    }*/
}