package com.example.android.iris;

import android.content.res.AssetManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private String historyChat = "Iris: Halo, nama saya Iris. Anda boleh bertanya apapun\n";
    private BM bm = new BM();
    private KMP kmp = new KMP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typewriter answerTypewriter = findViewById(R.id.answer_typewriter);
        answerTypewriter.setCharacterDelay(75);
        answerTypewriter.animateText("Halo, nama saya Iris. Anda boleh bertanya apapun");
        ImageView avatar = findViewById(R.id.avatar_image_view);
        avatar.setImageResource(R.drawable.pearl_normal_talking);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView history = findViewById(R.id.history_text_view);
                history.setText(historyChat);
                ImageView a = findViewById(R.id.avatar_image_view);
                a.setImageResource(R.drawable.pearl_normal);
            }
        }, 75*50);
    }

    public void getAnswer(View view){
        EditText questionEditText = findViewById(R.id.question_edit_text);
        Typewriter answerTypewriter = findViewById(R.id.answer_typewriter);
        String answer = "";
        String words = questionEditText.getText().toString();
        historyChat = historyChat + "Anda: " + words + "\n";
        try {
            bm.BMSearch(words, this);
            kmp.KMPSearch(words, this);
            Float sumBM = new Float(0);
            Float sumKMP = new Float(0);
            if (bm.getArrayOfPersen().size() > 0) {
                for (int i = 0; i < bm.getArrayOfPersen().size(); i++) {
                    sumBM += bm.getArrayOfPersen().get(i);
                }
                sumBM = sumBM / bm.getArrayOfPersen().size();
            }
            if (kmp.getArrayOfJawaban().size() > 0) {
                for (int i = 0; i < kmp.getArrayOfPersen().size(); i++) {
                    sumKMP += kmp.getArrayOfPersen().get(i);
                }
                sumKMP = sumKMP / kmp.getArrayOfJawaban().size();
            }
            if (sumKMP >= sumBM && sumKMP != 0) {
                if (kmp.getArrayOfPersen().size() > 0) {
                    int i = 0;
                    while (i < kmp.getArrayOfPersen().size()) {
                        if (kmp.getArrayOfPersen().get(i) >= 90) {
                            answer = kmp.getArrayOfJawaban().get(i);
                            answerTypewriter.setCharacterDelay(75);
                            answerTypewriter.animateText(answer);
                            break;
                        }
                        i++;
                    }
                    if (i >= kmp.getArrayOfPersen().size()) {
                        answer = "Mungkin pertanyaan yang anda maksud adalah : ";
                        for (int j = 0; j < kmp.getArrayOfPersen().size(); j++) {
                            answer = answer + "\n" + "\t" + (j + 1) + ". " + kmp.getArrayOfPertanyaan().get(j);
                            if (j >= kmp.getArrayOfPersen().size()) {
                                answer = answer + "\n";
                            }
                        }
                        answerTypewriter.setCharacterDelay(75);
                        answerTypewriter.animateText(answer);
                    }
                } else {
                    answer = "Maaf, saya tidak memahami perkataan Anda";
                    answerTypewriter.setCharacterDelay(75);
                    answerTypewriter.animateText(answer);
                }
            } else if (sumBM != 0) {
                if (bm.getArrayOfPersen().size() > 0) {
                    int i = 0;
                    while (i < bm.getArrayOfPersen().size()) {
                        if (bm.getArrayOfPersen().get(i) >= 90) {
                            answer = bm.getArrayOfJawaban().get(i);
                            answerTypewriter.setCharacterDelay(75);
                            answerTypewriter.animateText(answer);
                            break;
                        }
                        i++;
                    }
                    if (i >= bm.getArrayOfPersen().size() && bm.getArrayOfPersen().size() > 0) {
                        answer = "Mungkin pertanyaan yang anda maksud adalah : ";
                        for (int j = 0; j < bm.getArrayOfPersen().size(); j++) {
                            answer = answer + "\n" + "\t" + (j + 1) + ". " + bm.getArrayOfPertanyaan().get(j);
                            if (j >= bm.getArrayOfPersen().size()) {
                                answer = answer + "\n";
                            }
                        }
                        answerTypewriter.setCharacterDelay(75);
                        answerTypewriter.animateText(answer);
                    }
                } else {
                    answer = "Maaf, saya tidak memahami perkataan Anda";
                    answerTypewriter.setCharacterDelay(75);
                    answerTypewriter.animateText(answer);
                }
            } else {
                Regex rgx = new Regex();
                rgx.RegexSearch(words, this);
                if (rgx.getArrayOfPersen().size() > 0) {
                    int i = 0;
                    int count = 0;
                    for (int j = 0; j < rgx.getArrayOfPersen().size(); j++) {
                        if (rgx.getArrayOfPersen().get(j) >= 70) {
                            count++;
                        }
                    }
                    if (count == 1) {
                        while (i < rgx.getArrayOfPersen().size()) {
                            if (rgx.getArrayOfPersen().get(i) >= 90) {
                                answer = rgx.getArrayOfJawaban().get(i);
                                answerTypewriter.setCharacterDelay(75);
                                answerTypewriter.animateText(answer);
                                break;
                            }
                            i++;
                        }
                    }
                    if ((i >= rgx.getArrayOfPersen().size() && rgx.getArrayOfPersen().size() > 0) || count > 1) {
                        answer = "Mungkin pertanyaan yang anda maksud adalah : ";
                        for (int j = 0; j < rgx.getArrayOfPersen().size(); j++) {
                            answer = answer + "\n" + "\t" + (j + 1) + ". " + rgx.getArrayOfPertanyaan().get(j);
                            if (j >= rgx.getArrayOfPersen().size()) {
                                answer = answer + "\n";
                            }
                            if (j > 1) {
                                break;
                            }
                        }
                        answerTypewriter.setCharacterDelay(75);
                        answerTypewriter.animateText(answer);
                    }
                    if (count < 1) {
                        answer = "Maaf, saya tidak memahami perkataan Anda";
                        answerTypewriter.setCharacterDelay(75);
                        answerTypewriter.animateText(answer);
                    }
                } else {
                    answer = "Maaf, saya tidak memahami perkataan Anda";
                    answerTypewriter.setCharacterDelay(75);
                    answerTypewriter.animateText(answer);
                }
            }
        } catch (Exception e){
            answer = "Maaf, saya tidak memahami perkataan Anda";
            answerTypewriter.setCharacterDelay(75);
            answerTypewriter.animateText(answer);
        }
        ImageView avatar = findViewById(R.id.avatar_image_view);
        avatar.setImageResource(R.drawable.pearl_normal_talking);
        TextView historyTextView = findViewById(R.id.history_text_view);
        historyTextView.setText(historyChat);
        final ScrollView historyScrollView = findViewById(R.id.history_scroll_view);
        historyScrollView.post(new Runnable() {
            @Override
            public void run() {
                TextView historyTV = findViewById(R.id.history_text_view);
                historyScrollView.smoothScrollTo(0, historyTV.getBottom());
            }
        });
        historyChat = historyChat + "Iris: " + answer + "\n";
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView historyTextView = findViewById(R.id.history_text_view);
                historyTextView.setText(historyChat);
                ImageView a = findViewById(R.id.avatar_image_view);
                a.setImageResource(R.drawable.pearl_normal);
                historyScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView historyTV = findViewById(R.id.history_text_view);
                        historyScrollView.smoothScrollTo(0, historyTV.getBottom());
                    }
                });
            }
        }, 75*answer.length()+150);
    }
}
