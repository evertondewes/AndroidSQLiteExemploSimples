package com.example.androidsqliteexemplosimples;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = this.openOrCreateDatabase("baseNomes", Context.MODE_PRIVATE, null);

        b.execSQL("CREATE table if not exists  frase (id_frase INTEGER PRIMARY KEY AUTOINCREMENT, texto varchar(60))");


        carregarTabela();
    }

    public void adicionar(View v) {
        EditText et = (EditText) findViewById(R.id.editText);
        Button c;

        b.execSQL("INSERT INTO frase(texto) values('" + et.getText().toString() + "')");
        carregarTabela();
    }


    public void apagarTudo(View v) {
        b.execSQL("delete FROM frase;");
        carregarTabela();
    }

    public void carregarTabela() {
        Cursor c = b.rawQuery("SELECT id_frase, texto FROM frase;", new String[]{});

        LinearLayout linearLayout = (LinearLayout)  findViewById(R.id.llResultado);
        linearLayout.removeAllViews();
        while (c.moveToNext()) {

            TextView textView = new TextView(this);
            textView.setText(c.getString(0) + ", " + c.getString(1));
            linearLayout.addView(textView);

            BotaoAcaoTexto bat = new BotaoAcaoTexto(this);
            bat.setText("Excluir " + c.getString(0) );
            bat.id_frase = Integer.parseInt(c.getString(0));
            bat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BotaoAcaoTexto bt = (BotaoAcaoTexto) view;
                    b.execSQL("delete FROM frase WHERE id_frase = '"+ bt.id_frase +"';");
                    carregarTabela();
                    Log.d("Id:", String.valueOf(bt.id_frase));
                }
            });
            linearLayout.addView(bat);
        }

        c.close();
    }

}
