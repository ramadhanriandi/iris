package com.example.android.iris;

import android.content.Context;

import java.util.*;
import java.io.*;

public class BM {
    private ArrayList<Float> arrayOfPersen;
    private ArrayList<String> arrayOfJawaban;
    private ArrayList<String> arrayOfPertanyaan;

    public BM() {
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

    public void BMSearch(String pattern, Context context) {
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
                float kecocokan;
                String jawaban = null;
                int max_count = 0;
                int count_character_pattern = 0;
                int lines = 1;
                int lines_count = 0;
                int i = 0;
                int n = 0;
                int j = 0;
                int lo = 0;

                int last[] = buildLast(pattern);
                int m = pattern.length();
                while (text != null) {
                    n = text_length;
                    i = m - 1;
                    if (i > n - 1) {      // pattern is longer than text
                        text = readerFilePertanyaan.readLine();
                        lines++;
                    } else {
                        j = m - 1;
                        do {
                            try {
                                if (pattern.charAt(j) == text.charAt(i)) {
                                    count_character_pattern++;
                                    if (j == 0) {       // match
                                        kecocokan = 100;
                                        break;
                                    } else { // looking-glass technique
                                        i--;
                                        j--;
                                    }
                                } else { // character jump technique
                                    lo = last[text.charAt(i)]; //last occ
                                    i = i + m - Math.min(j, 1 + lo);
                                    j = m - 1;
                                }
                            } catch (NullPointerException e) {
                            } finally {
                                if (count_character_pattern > max_count) {
                                    max_count = count_character_pattern;
                                }
                            }
                        } while (i <= n - 1);
                    }
                    // return -1; // no match
                    kecocokan = (float) max_count / text_length * 100;
                    if (kecocokan > 100) {
                        kecocokan = 100;
                    }
                    if (kecocokan > 60) {
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
                    }
                }
                readerFilePertanyaan.close();
                readerFileJawaban.close();
            } catch (IOException e) {
            }
        }
    }

    // Return array storing index of last occurrence of each ASCII char in pattern.
    public static int[] buildLast(String pattern) {
        int last[] = new int[128]; // ASCII char set
        // initialize array
        for(int i = 0; i < 128; i++) {
            last[i] = -1;
        }
        for (int i = 0; i < pattern.length(); i++) {
            last[pattern.charAt(i)] = i;
        }
        return last;
    }
    /*public static void main(String args[]) {
        String pattern = "Apakah chatbot ?";
        String filePertanyaan = "pertanyaan.txt";
        String fileJawaban = "jawaban.txt";
        // ArrayList<Float> arrayPersen = new ArrayList<Float>();
        // ArrayList<String> arrayJawaban = new ArrayList<String>();

        BM bm = new BM();
        bm.BMSearch(pattern, filePertanyaan, fileJawaban);
        // System.out.println(bm.getArrayOfJawaban().size());
        for(String s : bm.getArrayOfJawaban()){
            System.out.println(s);
        }
        for(String s : bm.getArrayOfPertanyaan()){
            System.out.println(s);
        }
    }*/
}