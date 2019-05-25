package com.example.android.iris;
import android.content.Context;

import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.lang.*;

class Regex {
    private ArrayList<Float> arrayOfPersen;
    private ArrayList<String> arrayOfJawaban;
    private ArrayList<String> arrayOfPertanyaan;

    public Regex() {
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

    public void RegexSearch(String pattern, Context context){
        if(pattern.length() > 0) {
            try {
                //buffer reader file
                InputStream inputPertanyaan = context.getResources().openRawResource(R.raw.pertanyaan);
                InputStream inputJawaban = context.getResources().openRawResource(R.raw.jawaban);
                BufferedReader readerFilePertanyaan = new BufferedReader(new InputStreamReader(inputPertanyaan));
                BufferedReader readerFileJawaban = new BufferedReader(new InputStreamReader(inputJawaban));
                readerFileJawaban.mark(1);

                String[] words = pattern.split(" ");

                String theRegex;
                Pattern regexString;
                Matcher regexMatcher;

                String text = readerFilePertanyaan.readLine();
                int count_ketemu = 0;
                int lines = 1;
                int lines_count = 0;
                String jawaban = null;
                float kecocokan;

                while (text != null) {
                    for (int i = 0; i < words.length; i++) {
                        if (words[i] == "." || words[i] == "!" || words[i] == "?") {
                            continue;
                        } else {
                            regexString = Pattern.compile(changeRegexString(words[i]));
                            regexMatcher = regexString.matcher(text);
                            if (regexMatcher.find()) {
                                count_ketemu++;
                                // System.out.println(count_ketemu);
                            }
                        }
                    }
                    kecocokan = (float) count_ketemu / words.length * 100;
                    if (kecocokan > 100) {//??
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
                    text = readerFilePertanyaan.readLine();
                    if (text != null) {
                        lines++;
                        lines_count = 0;
                        readerFileJawaban.reset();
                        count_ketemu = 0;
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    public String changeRegexString(String s){
        char uc = Character.toUpperCase(s.charAt(0));
        char lc = Character.toLowerCase(s.charAt(0));

        return ("\\s?("+uc+"|"+lc+")" + s.substring(1,s.length()) + "\\s?");
    }

    /*public static void main(String[] args) {
        Regex reg = new Regex();
        reg.RegexSearch("Apakah chatbot itu?", "pertanyaan.txt", "jawaban.txt");
    }*/
}