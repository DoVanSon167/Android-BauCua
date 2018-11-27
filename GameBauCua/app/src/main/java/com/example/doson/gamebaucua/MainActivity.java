package com.example.doson.gamebaucua;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.os.Handler.*;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    CustomGridviewBanCo adapter;
    Integer[] dsHinh = {R.drawable.nai,R.drawable.bau,R.drawable.ga,R.drawable.ca,R.drawable.cua,R.drawable.tom};
    AnimationDrawable cdXiNgau1, cdXiNgau2, cdXiNgau3;
    ImageView hinhXiNgau1, hinhXiNgau2, hinhXiNgau3;
    Random randomXiNgau;
    int giaTriXiNgau1, giaTriXiNgau2, giaTriXiNgau3;
    public static Integer[] gtDatCuoc = new Integer[6];
    int tienThuong;
    TextView tvTien,tvThoiGian;
    int tongtiencu, tongtienmoi;
    int kiemtra;

    int soTran,soTranThang,tongTienThang,tienThangLon,chuoiThang,chuoiThangLonNhat;

    //Lưu trữ với SharedPrederences
    SharedPreferences luuTru;

    //SoudPool load âm thanh đơn giản (lắc xí ngầu)
    SoundPool soundPool;
    int id_amthanh;
    AudioManager audioManager;
    // Maximumn sound stream.
    int MAX_STREAMS = 1;
    // Stream type.
    int streamType = AudioManager.STREAM_MUSIC;
    float volume;


    //Load nhạc nền
    MediaPlayer nhacnen = new MediaPlayer();
    CheckBox ktAmthanh;

    CountDownTimer demtg;

    Timer timer = new Timer();
    Handler handler;
    Callback callback = new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //lắc xí ngầu ramdom
            RamdomXiNgau1();
            RamdomXiNgau2();
            RamdomXiNgau3();

            //Tính tiền thưởng sau khi đặt cược và lắc
            for (int i = 0; i < gtDatCuoc.length; i++){
                if (gtDatCuoc[i] != 0){
                    if (i == giaTriXiNgau1){
                        tienThuong += gtDatCuoc[i];
                    }
                    if (i == giaTriXiNgau2){
                        tienThuong += gtDatCuoc[i];
                    }
                    if (i == giaTriXiNgau3){
                        tienThuong += gtDatCuoc[i];
                    }
                    if (i != giaTriXiNgau1 && i != giaTriXiNgau2 && i != giaTriXiNgau3){
                        tienThuong -= gtDatCuoc[i];
                    }
                }
            }

            int st = luuTru.getInt("SoTran", 0);
            int stt = luuTru.getInt("SoTranThang", 0);
            int ttt = luuTru.getInt("TongTienThang", 0);
            int ttl = luuTru.getInt("TienThangLon", 0);
            chuoiThangLonNhat = luuTru.getInt("ChuoiThang", 0);

            if (tienThuong > 0){

                soTran = st + 1;
                soTranThang = stt + 1;
                tongTienThang = ttt + tienThuong;
                if (tienThuong > ttl){
                    tienThangLon = tienThuong;
                }else {
                    tienThangLon = ttl;
                }
                chuoiThang += 1;
                if (chuoiThang > chuoiThangLonNhat){
                    chuoiThangLonNhat = chuoiThang;
                }

                Toast.makeText(getApplicationContext(),"Bạn đã thắng được " + tienThuong +" Bitcoin", Toast.LENGTH_SHORT).show();
            }else if (tienThuong == 0){
                soTran = st + 1;
                soTranThang = stt;
                chuoiThang = 0;
                tongTienThang = ttt;
                tienThangLon = ttl;
                Toast.makeText(getApplicationContext(),"Hên quá, xém chết!", Toast.LENGTH_SHORT).show();
            }else {
                soTran = st + 1;
                soTranThang = stt;
                chuoiThang = 0;
                tongTienThang = ttt;
                tienThangLon = ttl;
                Toast.makeText(getApplicationContext(),"Bạn đã thua " + tienThuong + " Bitcoin", Toast.LENGTH_SHORT).show();
            }

            //Lưu dữ liệu tiền thưởng và hiển thị số tiền mới
            LuuDuLieu(tienThuong);
            tvTien.setText(String.valueOf(tongtienmoi));

            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hinhXiNgau1 = (ImageView) findViewById(R.id.xingau1);
        hinhXiNgau2 = (ImageView) findViewById(R.id.xingau2);
        hinhXiNgau3 = (ImageView) findViewById(R.id.xingau3);
        tvTien = (TextView) findViewById(R.id.tvTien);
        tvThoiGian = (TextView) findViewById(R.id.tvThoiGian);
        ktAmthanh = (CheckBox) findViewById(R.id.cbamthanh);

        gridView = (GridView) findViewById(R.id.banco);
        adapter = new CustomGridviewBanCo(this, R.layout.custombanco,dsHinh);
        gridView.setAdapter(adapter);

        luuTru = getSharedPreferences("luuthongtin", Context.MODE_PRIVATE);
        tongtiencu = luuTru.getInt("TongTien", 2000);
        tvTien.setText(String.valueOf(tongtiencu));

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex = (float) audioManager.getStreamMaxVolume(streamType);

        // Volumn (0 --> 1)
        //this.volume = currentVolumeIndex / maxVolumeIndex;
        volume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        //this.setVolumeControlStream(streamType);
        setVolumeControlStream(streamType);

        //Kiểm tra SDK, trên 21 dùng SoundPool Builder
        if (Build.VERSION.SDK_INT >= 21) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
            id_amthanh = soundPool.load(this, R.raw.dicesound,1);
        }else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        //Cấu hình và phát nhạc nền
        nhacnen = MediaPlayer.create(this, R.raw.xoso);
        nhacnen.start();

        //Bật tắt âm thanh nhạc nền
        ktAmthanh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    nhacnen.stop();
                }else {
                    try {
                        nhacnen.prepare();
                        nhacnen.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Đếm thời gian tăng tiền
        demtg = new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long milis = millisUntilFinished;
                long gio = TimeUnit.MILLISECONDS.toHours(milis);
                long phut = TimeUnit.MILLISECONDS.toMinutes(milis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milis));
                long giay = TimeUnit.MILLISECONDS.toSeconds(milis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milis));

                String giophutgiay = String.format("%02d:%02d:%02d", gio, phut, giay);
                tvThoiGian.setText(giophutgiay);
            }

            @Override
            public void onFinish() {
                SharedPreferences.Editor edit = luuTru.edit();
                tongtiencu = luuTru.getInt("TongTien", 2000);
                tongtienmoi = tongtiencu + 500;
                edit.putInt("TongTien", tongtienmoi);
                edit.apply();

                tvTien.setText(String.valueOf(tongtienmoi));
                demtg.cancel();
                demtg.start();

            }
        };

        demtg.start();
        handler = new Handler(callback);
    }

    private void LuuDuLieu(int tienThuong){
        SharedPreferences.Editor edit = luuTru.edit();
        tongtiencu = luuTru.getInt("TongTien", 2000);
        tongtienmoi = tongtiencu + tienThuong;
        edit.putInt("SoTran", soTran);
        edit.putInt("SoTranThang", soTranThang);
        edit.putInt("TongTienThang", tongTienThang);
        edit.putInt("TienThangLon", tienThangLon);
        edit.putInt("ChuoiThang", chuoiThangLonNhat);
        edit.putInt("TongTien", tongtienmoi);
        edit.apply();
    }

    public void LacXiNgau(View v){
        hinhXiNgau1.setImageResource(R.drawable.xingau);
        hinhXiNgau2.setImageResource(R.drawable.xingau);
        hinhXiNgau3.setImageResource(R.drawable.xingau);

        cdXiNgau1 = (AnimationDrawable) hinhXiNgau1.getDrawable();
        cdXiNgau2 = (AnimationDrawable) hinhXiNgau2.getDrawable();
        cdXiNgau3 = (AnimationDrawable) hinhXiNgau3.getDrawable();

        kiemtra = 0;
        for (int i = 0; i < gtDatCuoc.length; i++){
            kiemtra += gtDatCuoc[i];
        }
        if (kiemtra == 0){
            Toast.makeText(getApplicationContext(),"Bạn chưa đặt cược!", Toast.LENGTH_SHORT).show();
        }else if (kiemtra > tongtiencu) {
            Toast.makeText(getApplicationContext(),"Bạn không đủ tiền để đặt cược!",Toast.LENGTH_SHORT).show();
        }else {
            soundPool.play(id_amthanh,1.0f,1.0f,1,0,1);
            cdXiNgau1.start();
            cdXiNgau2.start();
            cdXiNgau3.start();

            tienThuong = 0;
            timer.schedule(new LacXiNgau(),3000);
        }

    }

    class LacXiNgau extends TimerTask{

        @Override
        public void run() {
        handler.sendEmptyMessage(0);
        }
    }

    private void RamdomXiNgau1(){
        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd){
            case 0:
                hinhXiNgau1.setImageResource(dsHinh[0]);
                giaTriXiNgau1 = rd;
                break;
            case 1:
                hinhXiNgau1.setImageResource(dsHinh[1]);
                giaTriXiNgau1 = rd;
                break;
            case 2:
                hinhXiNgau1.setImageResource(dsHinh[2]);
                giaTriXiNgau1 = rd;
                break;
            case 3:
                hinhXiNgau1.setImageResource(dsHinh[3]);
                giaTriXiNgau1 = rd;
                break;
            case 4:
                hinhXiNgau1.setImageResource(dsHinh[4]);
                giaTriXiNgau1 = rd;
                break;
            case 5:
                hinhXiNgau1.setImageResource(dsHinh[5]);
                giaTriXiNgau1 = rd;
                break;
        }
    }
    private void RamdomXiNgau2(){
        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd){
            case 0:
                hinhXiNgau2.setImageResource(dsHinh[0]);
                giaTriXiNgau2 = rd;
                break;
            case 1:
                hinhXiNgau2.setImageResource(dsHinh[1]);
                giaTriXiNgau2 = rd;
                break;
            case 2:
                hinhXiNgau2.setImageResource(dsHinh[2]);
                giaTriXiNgau2 = rd;
                break;
            case 3:
                hinhXiNgau2.setImageResource(dsHinh[3]);
                giaTriXiNgau2 = rd;
                break;
            case 4:
                hinhXiNgau2.setImageResource(dsHinh[4]);
                giaTriXiNgau2 = rd;
                break;
            case 5:
                hinhXiNgau2.setImageResource(dsHinh[5]);
                giaTriXiNgau2 = rd;
                break;
        }
    }
    private void RamdomXiNgau3(){
        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd){
            case 0:
                hinhXiNgau3.setImageResource(dsHinh[0]);
                giaTriXiNgau3 = rd;
                break;
            case 1:
                hinhXiNgau3.setImageResource(dsHinh[1]);
                giaTriXiNgau3 = rd;
                break;
            case 2:
                hinhXiNgau3.setImageResource(dsHinh[2]);
                giaTriXiNgau3 = rd;
                break;
            case 3:
                hinhXiNgau3.setImageResource(dsHinh[3]);
                giaTriXiNgau3 = rd;
                break;
            case 4:
                hinhXiNgau3.setImageResource(dsHinh[4]);
                giaTriXiNgau3 = rd;
                break;
            case 5:
                hinhXiNgau3.setImageResource(dsHinh[5]);
                giaTriXiNgau3 = rd;
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (nhacnen.isPlaying()) {
            nhacnen.stop();
        }
        nhacnen.release();
        super.onBackPressed();
    }
}
