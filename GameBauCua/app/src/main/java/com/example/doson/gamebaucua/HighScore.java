package com.example.doson.gamebaucua;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class HighScore extends AppCompatActivity {
    SharedPreferences luuTru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        TextView tvTiLeThang = (TextView) findViewById(R.id.tvTiLeThang);
        TextView tvSoTran = (TextView) findViewById(R.id.tvSoTran);
        TextView tvTongTien = (TextView) findViewById(R.id.tvTongTien);
        TextView tvSoTienThangLonNhat = (TextView) findViewById(R.id.tvSoTienLonNhat);
        TextView tvChuoiThang = (TextView) findViewById(R.id.tvChuoiT);

        try{
            luuTru = getSharedPreferences("luuthongtin", Context.MODE_PRIVATE);
            int soTran = luuTru.getInt("SoTran", 0);
            int soTranThang = luuTru.getInt("SoTranThang",0);
            int tongTienThang = luuTru.getInt("TongTienThang",0);
            int tienThangLon = luuTru.getInt("TienThangLon",0);
            int chuoiThang = luuTru.getInt("ChuoiThang",0);

            DecimalFormat df = new DecimalFormat("0.00");
            float tiLeThang = (Float.parseFloat(String.valueOf(soTranThang))/Float.parseFloat(String.valueOf(soTran)))*100;


            tvTiLeThang.setText(String.valueOf(df.format(tiLeThang))+"%");
            tvSoTran.setText(String.valueOf(soTran));
            tvTongTien.setText(String.valueOf(tongTienThang));
            tvSoTienThangLonNhat.setText(String.valueOf(tienThangLon));
            tvChuoiThang.setText(String.valueOf(chuoiThang)+" Trận");
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
