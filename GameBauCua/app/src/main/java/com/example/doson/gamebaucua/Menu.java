package com.example.doson.gamebaucua;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Button btnChoi = (Button) findViewById(R.id.btnChoi);
        btnChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);

            }
        });

        Button btnDiemCao = (Button) findViewById(R.id.btnDiemCao);
        btnDiemCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HighScore.class);
                startActivity(intent);
            }
        });
        Button btnThoat = (Button) findViewById(R.id.btnThoat);
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        Menu.this);

                // set title
                alertDialogBuilder.setTitle("Bạn có muốn thoát?");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Chọn Có để thoát!")
                        .setCancelable(false)
                        .setPositiveButton("Có",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String exme="Đã thoát thành công";
                                Toast t = Toast.makeText(Menu.this,exme, Toast.LENGTH_SHORT);
                                t.show();
                                Menu.this.finish();
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("Không",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String exme1 = "Chưa thoát";
                                Toast t1 = Toast.makeText(Menu.this, exme1, Toast.LENGTH_SHORT);
                                t1.show();
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                        System.exit(0);
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.",
                                Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }
}
