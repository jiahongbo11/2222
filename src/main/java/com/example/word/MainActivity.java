package com.example.word;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etSearch;
    ImageButton btnSearch;
    TextView tvWordResult;
    TextView tvMeanResult;
    TextView tvSentenceResult;
    TextView tvSentenceCHS;
    TextView tvPhoneticsymbolUS;
    TextView tvPhoneticsymbolUK;
    ImageButton btnAdd;
    ImageButton btnWordBook;

    WordValue wordValue = null;
    Database database;
    Dictionary dictionary;
    Dictionary new_dictionary;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.mi_dictionary:
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mi_wordnote:
                intent.setClass(this, WordnoteActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mi_help:
                intent.setClass(this,HelpActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        tvWordResult = findViewById(R.id.tv_wordresult);
        tvMeanResult = findViewById(R.id.tv_meanresult);
        tvMeanResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvSentenceResult = findViewById(R.id.tv_sentenceresult);
        tvSentenceCHS = findViewById(R.id.et_sentenceCHS);
        tvPhoneticsymbolUS = findViewById(R.id.tv_phoneticsymbolUS);
        tvPhoneticsymbolUK = findViewById(R.id.tv_phoneticsymbolUK);
        btnAdd = findViewById(R.id.btn_add);
        btnWordBook = findViewById(R.id.btn_wordbook);

        dictionary = new Dictionary(this,"dict");
        new_dictionary = new Dictionary(this,"newdict");

        //??????
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if("".equals(etSearch.getText().toString())){
                            showToastInThread("???????????????");
                        }else{
                            wordValue = dictionary.getWordFromInternet(etSearch.getText().toString());

                            if(wordValue != null && wordValue.getInterpret() != null && !"".equals(wordValue.getPsA())){
                                showResearchWordInterpret(wordValue);

                                Log.d("MainActivity", "if??????");
                                Log.d("MainActivity", "??????"+wordValue.getInterpret());
                            }else{
                                showToastInThread("?????????????????????");
                                Log.d("MainActivity", "else??????");
                            }
                        }
                    }
                }).start();
            }
        });

        //???????????????
        btnWordBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WordnoteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if("".equals(etSearch.getText().toString())){
                            //??????????????????
                            showToastInThread("???????????????");
                        }else{
                            //???????????????
                            //?????????????????????
                            wordValue  = dictionary.getWordFromDict(etSearch.getText().toString());
                            if(wordValue == null){
                                //???????????????????????????????????????
                                //???????????????
                                wordValue = dictionary.getWordFromInternet(etSearch.getText().toString());
                                if(wordValue == null){
                                    //????????????
                                    showToastInThread("?????????????????????????????????");
                                }else{
                                    //????????????
                                    dictionary.insertWordToDict(wordValue,true);
                                    showToastInThread("???????????????????????????");
                                }
                            }else{
                                //????????????
                                showToastInThread("????????????????????????????????????????????????");
                            }
                        }
                    }
                }).start();
            }
        });
    }



    //????????????ui??????
    private void showResearchWordInterpret(final WordValue wordValue){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //??????ui??????
                tvWordResult.setText(wordValue.getWord());
                tvMeanResult.setText(wordValue.getInterpret());
                tvSentenceCHS.setText(wordValue.getSentTrans());
                tvSentenceResult.setText(wordValue.getSentOrig());
                tvPhoneticsymbolUK.setText("???["+wordValue.getPsE()+"]");
                tvPhoneticsymbolUS.setText("???["+wordValue.getPsA()+"]");
            }
        });
    }


    //???????????????toast
    private void showToastInThread(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
            }
        });
    }



}

