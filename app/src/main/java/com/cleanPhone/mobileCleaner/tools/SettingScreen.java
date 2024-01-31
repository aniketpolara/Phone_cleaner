package com.cleanPhone.mobileCleaner.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.work.Data;

import com.google.firebase.messaging.ServiceStarter;
import com.cleanPhone.mobileCleaner.MobiClean;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

public class SettingScreen extends ParentActivity {
    private SeekBar audioseekbar;
    private SeekBar filesseekbar;
    private SeekBar imageSeekbar;
    private LinearLayout layout;
    private Button okbtn;
    private SeekBar otherseekbar;
    private ImageButton seek_minus1;
    private ImageButton seek_minus2;
    private ImageButton seek_minus3;
    private ImageButton seek_minus4;
    private ImageButton seek_minus5;
    private ImageButton seek_plus1;
    private ImageButton seek_plus2;
    private ImageButton seek_plus3;
    private ImageButton seek_plus4;
    private ImageButton seek_plus5;
    private SharedPrefUtil sharedPrefUtil;
    private TextView tvaudio;
    private TextView tvfiles;
    private TextView tvimage;
    private TextView tvothers;
    private TextView tvvideo;
    private SeekBar videoseekbar;
    private String[] imageArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "6 MB", "7 MB", "8 MB", "9 MB", "10 MB", "15 MB", "20 MB", "25 MB"};
    private int[] imageArraySize = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25};
    private String[] videoArray = {"0 MB", "5 MB", "10 MB", "15 MB", "25 MB", "30 MB", "50 MB", "75 MB", "100 MB", "200 MB", "350 MB", "500 MB", "750 MB", "1 GB"};
    private int[] videoArraySize = {0, 5, 10, 15, 25, 30, 50, 75, 100, 200, 350, ServiceStarter.ERROR_UNKNOWN, 750, 1024};
    private String[] audioArray = {"0 MB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "7 MB", "10 MB", "12 MB", "15 MB", "20 MB", "25 MB", "50 MB", "100 MB"};
    private int[] audioArraysize = {0, 1, 2, 3, 4, 5, 7, 10, 12, 15, 20, 25, 50, 100};
    private String[] filesArray = {"0 KB", "50 KB", "100 KB", "500 KB", "1 MB", "2 MB", "3 MB", "4 MB", "5 MB", "10 MB", "15 MB", "20 MB", "25 MB", "50 MB"};
    private int[] filesArraySize = {0, 50, 100, ServiceStarter.ERROR_UNKNOWN, 1024, 2048, 3072, 4096, 5120, Data.MAX_DATA_BYTES, 15360, 20480, 25600, 51200};
    private String[] othersArray = {"0 MB", "1 MB", "2 MB", "5 MB", "10 MB", "20 MB", "30 MB", "50 MB", "75 MB", "100 MB", "200 MB", "500 MB", "750 MB", "1 GB"};
    private int[] othersArraysize = {0, 1, 2, 5, 10, 20, 30, 50, 75, 100, 200, ServiceStarter.ERROR_UNKNOWN, 750, 1024};


    public int getValueFromSeekbar(int i) {
        if (i > 99) {
            return 13;
        }
        return i / 8;
    }

    private void init() {
        this.sharedPrefUtil = new SharedPrefUtil(this);
        this.imageSeekbar = (SeekBar) findViewById(R.id.sbimages);
        this.audioseekbar = (SeekBar) findViewById(R.id.sbaudio);
        this.videoseekbar = (SeekBar) findViewById(R.id.sbvideo);
        this.filesseekbar = (SeekBar) findViewById(R.id.sbfile);
        this.otherseekbar = (SeekBar) findViewById(R.id.sbothers);
        this.seek_minus1 = (ImageButton) findViewById(R.id.btn_seek1_minus1);
        this.seek_minus2 = (ImageButton) findViewById(R.id.btn_seek2_minus2);
        this.seek_minus3 = (ImageButton) findViewById(R.id.btn_seek3_minus3);
        this.seek_minus4 = (ImageButton) findViewById(R.id.btn_seek4_minus4);
        this.seek_minus5 = (ImageButton) findViewById(R.id.btn_seek5_minus5);
        this.seek_plus1 = (ImageButton) findViewById(R.id.btn_seek1_plus1);
        this.seek_plus2 = (ImageButton) findViewById(R.id.btn_seek2_plus2);
        this.seek_plus3 = (ImageButton) findViewById(R.id.btn_seek3_plus3);
        this.seek_plus4 = (ImageButton) findViewById(R.id.btn_seek4_plus4);
        this.seek_plus5 = (ImageButton) findViewById(R.id.btn_seek5_plus5);
        this.tvimage = (TextView) findViewById(R.id.sbimagesval);
        this.tvvideo = (TextView) findViewById(R.id.sbvideoval);
        this.tvaudio = (TextView) findViewById(R.id.sbaudioval);
        this.tvfiles = (TextView) findViewById(R.id.sbfilesval);
        this.tvothers = (TextView) findViewById(R.id.sbothersval);
        this.okbtn = (Button) findViewById(R.id.settingokbtn);
    }

    private void setListners() {
        this.imageSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i < 8) {
                    TextView textView = SettingScreen.this.tvimage;
                    textView.setText("" + SettingScreen.this.imageArray[0]);
                } else if (i >= 8 && i < 16) {
                    TextView textView2 = SettingScreen.this.tvimage;
                    textView2.setText("" + SettingScreen.this.imageArray[1]);
                } else if (i >= 16 && i < 24) {
                    TextView textView3 = SettingScreen.this.tvimage;
                    textView3.setText("" + SettingScreen.this.imageArray[2]);
                } else if (i >= 24 && i < 32) {
                    TextView textView4 = SettingScreen.this.tvimage;
                    textView4.setText("" + SettingScreen.this.imageArray[3]);
                } else if (i >= 32 && i < 40) {
                    TextView textView5 = SettingScreen.this.tvimage;
                    textView5.setText("" + SettingScreen.this.imageArray[4]);
                } else if (i >= 40 && i < 48) {
                    TextView textView6 = SettingScreen.this.tvimage;
                    textView6.setText("" + SettingScreen.this.imageArray[5]);
                } else if (i >= 48 && i < 56) {
                    TextView textView7 = SettingScreen.this.tvimage;
                    textView7.setText("" + SettingScreen.this.imageArray[6]);
                } else if (i >= 56 && i < 64) {
                    TextView textView8 = SettingScreen.this.tvimage;
                    textView8.setText("" + SettingScreen.this.imageArray[7]);
                } else if (i >= 64 && i < 72) {
                    TextView textView9 = SettingScreen.this.tvimage;
                    textView9.setText("" + SettingScreen.this.imageArray[8]);
                } else if (i >= 72 && i < 80) {
                    TextView textView10 = SettingScreen.this.tvimage;
                    textView10.setText("" + SettingScreen.this.imageArray[9]);
                } else if (i >= 80 && i < 88) {
                    TextView textView11 = SettingScreen.this.tvimage;
                    textView11.setText("" + SettingScreen.this.imageArray[10]);
                } else if (i >= 88 && i < 96) {
                    TextView textView12 = SettingScreen.this.tvimage;
                    textView12.setText("" + SettingScreen.this.imageArray[11]);
                } else if (i >= 96 && i <= 99) {
                    TextView textView13 = SettingScreen.this.tvimage;
                    textView13.setText("" + SettingScreen.this.imageArray[12]);
                } else if (i > 99) {
                    TextView textView14 = SettingScreen.this.tvimage;
                    textView14.setText("" + SettingScreen.this.imageArray[13]);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.videoseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i < 8) {
                    TextView textView = SettingScreen.this.tvvideo;
                    textView.setText("" + SettingScreen.this.videoArray[0]);
                } else if (i >= 8 && i < 16) {
                    TextView textView2 = SettingScreen.this.tvvideo;
                    textView2.setText("" + SettingScreen.this.videoArray[1]);
                } else if (i >= 16 && i < 24) {
                    TextView textView3 = SettingScreen.this.tvvideo;
                    textView3.setText("" + SettingScreen.this.videoArray[2]);
                } else if (i >= 24 && i < 32) {
                    TextView textView4 = SettingScreen.this.tvvideo;
                    textView4.setText("" + SettingScreen.this.videoArray[3]);
                } else if (i >= 32 && i < 40) {
                    TextView textView5 = SettingScreen.this.tvvideo;
                    textView5.setText("" + SettingScreen.this.videoArray[4]);
                } else if (i >= 40 && i < 48) {
                    TextView textView6 = SettingScreen.this.tvvideo;
                    textView6.setText("" + SettingScreen.this.videoArray[5]);
                } else if (i >= 48 && i < 56) {
                    TextView textView7 = SettingScreen.this.tvvideo;
                    textView7.setText("" + SettingScreen.this.videoArray[6]);
                } else if (i >= 56 && i < 64) {
                    TextView textView8 = SettingScreen.this.tvvideo;
                    textView8.setText("" + SettingScreen.this.videoArray[7]);
                } else if (i >= 64 && i < 72) {
                    TextView textView9 = SettingScreen.this.tvvideo;
                    textView9.setText("" + SettingScreen.this.videoArray[8]);
                } else if (i >= 72 && i < 80) {
                    TextView textView10 = SettingScreen.this.tvvideo;
                    textView10.setText("" + SettingScreen.this.videoArray[9]);
                } else if (i >= 80 && i < 88) {
                    TextView textView11 = SettingScreen.this.tvvideo;
                    textView11.setText("" + SettingScreen.this.videoArray[10]);
                } else if (i >= 88 && i < 96) {
                    TextView textView12 = SettingScreen.this.tvvideo;
                    textView12.setText("" + SettingScreen.this.videoArray[11]);
                } else if (i >= 96 && i <= 99) {
                    TextView textView13 = SettingScreen.this.tvvideo;
                    textView13.setText("" + SettingScreen.this.videoArray[12]);
                } else if (i > 99) {
                    TextView textView14 = SettingScreen.this.tvvideo;
                    textView14.setText("" + SettingScreen.this.videoArray[13]);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.videoseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i < 8) {
                    TextView textView = SettingScreen.this.tvvideo;
                    textView.setText("" + SettingScreen.this.videoArray[0]);
                } else if (i >= 8 && i < 16) {
                    TextView textView2 = SettingScreen.this.tvvideo;
                    textView2.setText("" + SettingScreen.this.videoArray[1]);
                } else if (i >= 16 && i < 24) {
                    TextView textView3 = SettingScreen.this.tvvideo;
                    textView3.setText("" + SettingScreen.this.videoArray[2]);
                } else if (i >= 24 && i < 32) {
                    TextView textView4 = SettingScreen.this.tvvideo;
                    textView4.setText("" + SettingScreen.this.videoArray[3]);
                } else if (i >= 32 && i < 40) {
                    TextView textView5 = SettingScreen.this.tvvideo;
                    textView5.setText("" + SettingScreen.this.videoArray[4]);
                } else if (i >= 40 && i < 48) {
                    TextView textView6 = SettingScreen.this.tvvideo;
                    textView6.setText("" + SettingScreen.this.videoArray[5]);
                } else if (i >= 48 && i < 56) {
                    TextView textView7 = SettingScreen.this.tvvideo;
                    textView7.setText("" + SettingScreen.this.videoArray[6]);
                } else if (i >= 56 && i < 64) {
                    TextView textView8 = SettingScreen.this.tvvideo;
                    textView8.setText("" + SettingScreen.this.videoArray[7]);
                } else if (i >= 64 && i < 72) {
                    TextView textView9 = SettingScreen.this.tvvideo;
                    textView9.setText("" + SettingScreen.this.videoArray[8]);
                } else if (i >= 72 && i < 80) {
                    TextView textView10 = SettingScreen.this.tvvideo;
                    textView10.setText("" + SettingScreen.this.videoArray[9]);
                } else if (i >= 80 && i < 88) {
                    TextView textView11 = SettingScreen.this.tvvideo;
                    textView11.setText("" + SettingScreen.this.videoArray[10]);
                } else if (i >= 88 && i < 96) {
                    TextView textView12 = SettingScreen.this.tvvideo;
                    textView12.setText("" + SettingScreen.this.videoArray[11]);
                } else if (i >= 96 && i <= 99) {
                    TextView textView13 = SettingScreen.this.tvvideo;
                    textView13.setText("" + SettingScreen.this.videoArray[12]);
                } else if (i > 99) {
                    TextView textView14 = SettingScreen.this.tvvideo;
                    textView14.setText("" + SettingScreen.this.videoArray[13]);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.audioseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i < 8) {
                    TextView textView = SettingScreen.this.tvaudio;
                    textView.setText("" + SettingScreen.this.audioArray[0]);
                } else if (i >= 8 && i < 16) {
                    TextView textView2 = SettingScreen.this.tvaudio;
                    textView2.setText("" + SettingScreen.this.audioArray[1]);
                } else if (i >= 16 && i < 24) {
                    TextView textView3 = SettingScreen.this.tvaudio;
                    textView3.setText("" + SettingScreen.this.audioArray[2]);
                } else if (i >= 24 && i < 32) {
                    TextView textView4 = SettingScreen.this.tvaudio;
                    textView4.setText("" + SettingScreen.this.audioArray[3]);
                } else if (i >= 32 && i < 40) {
                    TextView textView5 = SettingScreen.this.tvaudio;
                    textView5.setText("" + SettingScreen.this.audioArray[4]);
                } else if (i >= 40 && i < 48) {
                    TextView textView6 = SettingScreen.this.tvaudio;
                    textView6.setText("" + SettingScreen.this.audioArray[5]);
                } else if (i >= 48 && i < 56) {
                    TextView textView7 = SettingScreen.this.tvaudio;
                    textView7.setText("" + SettingScreen.this.audioArray[6]);
                } else if (i >= 56 && i < 64) {
                    TextView textView8 = SettingScreen.this.tvaudio;
                    textView8.setText("" + SettingScreen.this.audioArray[7]);
                } else if (i >= 64 && i < 72) {
                    TextView textView9 = SettingScreen.this.tvaudio;
                    textView9.setText("" + SettingScreen.this.audioArray[8]);
                } else if (i >= 72 && i < 80) {
                    TextView textView10 = SettingScreen.this.tvaudio;
                    textView10.setText("" + SettingScreen.this.audioArray[9]);
                } else if (i >= 80 && i < 88) {
                    TextView textView11 = SettingScreen.this.tvaudio;
                    textView11.setText("" + SettingScreen.this.audioArray[10]);
                } else if (i >= 88 && i < 96) {
                    TextView textView12 = SettingScreen.this.tvaudio;
                    textView12.setText("" + SettingScreen.this.audioArray[11]);
                } else if (i >= 96 && i <= 99) {
                    TextView textView13 = SettingScreen.this.tvaudio;
                    textView13.setText("" + SettingScreen.this.audioArray[12]);
                } else if (i > 99) {
                    TextView textView14 = SettingScreen.this.tvaudio;
                    textView14.setText("" + SettingScreen.this.audioArray[13]);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.filesseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i < 8) {
                    TextView textView = SettingScreen.this.tvfiles;
                    textView.setText("" + SettingScreen.this.filesArray[0]);
                } else if (i >= 8 && i < 16) {
                    TextView textView2 = SettingScreen.this.tvfiles;
                    textView2.setText("" + SettingScreen.this.filesArray[1]);
                } else if (i >= 16 && i < 24) {
                    TextView textView3 = SettingScreen.this.tvfiles;
                    textView3.setText("" + SettingScreen.this.filesArray[2]);
                } else if (i >= 24 && i < 32) {
                    TextView textView4 = SettingScreen.this.tvfiles;
                    textView4.setText("" + SettingScreen.this.filesArray[3]);
                } else if (i >= 32 && i < 40) {
                    TextView textView5 = SettingScreen.this.tvfiles;
                    textView5.setText("" + SettingScreen.this.filesArray[4]);
                } else if (i >= 40 && i < 48) {
                    TextView textView6 = SettingScreen.this.tvfiles;
                    textView6.setText("" + SettingScreen.this.filesArray[5]);
                } else if (i >= 48 && i < 56) {
                    TextView textView7 = SettingScreen.this.tvfiles;
                    textView7.setText("" + SettingScreen.this.filesArray[6]);
                } else if (i >= 56 && i < 64) {
                    TextView textView8 = SettingScreen.this.tvfiles;
                    textView8.setText("" + SettingScreen.this.filesArray[7]);
                } else if (i >= 64 && i < 72) {
                    TextView textView9 = SettingScreen.this.tvfiles;
                    textView9.setText("" + SettingScreen.this.filesArray[8]);
                } else if (i >= 72 && i < 80) {
                    TextView textView10 = SettingScreen.this.tvfiles;
                    textView10.setText("" + SettingScreen.this.filesArray[9]);
                } else if (i >= 80 && i < 88) {
                    TextView textView11 = SettingScreen.this.tvfiles;
                    textView11.setText("" + SettingScreen.this.filesArray[10]);
                } else if (i >= 88 && i < 96) {
                    TextView textView12 = SettingScreen.this.tvfiles;
                    textView12.setText("" + SettingScreen.this.filesArray[11]);
                } else if (i >= 96 && i <= 99) {
                    TextView textView13 = SettingScreen.this.tvfiles;
                    textView13.setText("" + SettingScreen.this.filesArray[12]);
                } else if (i > 99) {
                    TextView textView14 = SettingScreen.this.tvfiles;
                    textView14.setText("" + SettingScreen.this.filesArray[13]);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.otherseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i < 8) {
                    TextView textView = SettingScreen.this.tvothers;
                    textView.setText("" + SettingScreen.this.othersArray[0]);
                } else if (i >= 8 && i < 16) {
                    TextView textView2 = SettingScreen.this.tvothers;
                    textView2.setText("" + SettingScreen.this.othersArray[1]);
                } else if (i >= 16 && i < 24) {
                    TextView textView3 = SettingScreen.this.tvothers;
                    textView3.setText("" + SettingScreen.this.othersArray[2]);
                } else if (i >= 24 && i < 32) {
                    TextView textView4 = SettingScreen.this.tvothers;
                    textView4.setText("" + SettingScreen.this.othersArray[3]);
                } else if (i >= 32 && i < 40) {
                    TextView textView5 = SettingScreen.this.tvothers;
                    textView5.setText("" + SettingScreen.this.othersArray[4]);
                } else if (i >= 40 && i < 48) {
                    TextView textView6 = SettingScreen.this.tvothers;
                    textView6.setText("" + SettingScreen.this.othersArray[5]);
                } else if (i >= 48 && i < 56) {
                    TextView textView7 = SettingScreen.this.tvothers;
                    textView7.setText("" + SettingScreen.this.othersArray[6]);
                } else if (i >= 56 && i < 64) {
                    TextView textView8 = SettingScreen.this.tvothers;
                    textView8.setText("" + SettingScreen.this.othersArray[7]);
                } else if (i >= 64 && i < 72) {
                    TextView textView9 = SettingScreen.this.tvothers;
                    textView9.setText("" + SettingScreen.this.othersArray[8]);
                } else if (i >= 72 && i < 80) {
                    TextView textView10 = SettingScreen.this.tvothers;
                    textView10.setText("" + SettingScreen.this.othersArray[9]);
                } else if (i >= 80 && i < 88) {
                    TextView textView11 = SettingScreen.this.tvothers;
                    textView11.setText("" + SettingScreen.this.othersArray[10]);
                } else if (i >= 88 && i < 96) {
                    TextView textView12 = SettingScreen.this.tvothers;
                    textView12.setText("" + SettingScreen.this.othersArray[11]);
                } else if (i >= 96 && i <= 99) {
                    TextView textView13 = SettingScreen.this.tvothers;
                    textView13.setText("" + SettingScreen.this.othersArray[12]);
                } else if (i > 99) {
                    TextView textView14 = SettingScreen.this.tvothers;
                    textView14.setText("" + SettingScreen.this.othersArray[13]);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int[] iArr = SettingScreen.this.imageArraySize;
                SettingScreen settingScreen = SettingScreen.this;
                GlobalData.imageCriteia = iArr[settingScreen.getValueFromSeekbar(settingScreen.imageSeekbar.getProgress())];
                int[] iArr2 = SettingScreen.this.videoArraySize;
                SettingScreen settingScreen2 = SettingScreen.this;
                GlobalData.videoCriteia = iArr2[settingScreen2.getValueFromSeekbar(settingScreen2.videoseekbar.getProgress())];
                int[] iArr3 = SettingScreen.this.audioArraysize;
                SettingScreen settingScreen3 = SettingScreen.this;
                GlobalData.audioCriteia = iArr3[settingScreen3.getValueFromSeekbar(settingScreen3.audioseekbar.getProgress())];
                int[] iArr4 = SettingScreen.this.filesArraySize;
                SettingScreen settingScreen4 = SettingScreen.this;
                GlobalData.fileCriteia = iArr4[settingScreen4.getValueFromSeekbar(settingScreen4.filesseekbar.getProgress())];
                int[] iArr5 = SettingScreen.this.othersArraysize;
                SettingScreen settingScreen5 = SettingScreen.this;
                GlobalData.otherCriteia = iArr5[settingScreen5.getValueFromSeekbar(settingScreen5.otherseekbar.getProgress())];
                SharedPrefUtil sharedPrefUtil = SettingScreen.this.sharedPrefUtil;
                sharedPrefUtil.saveString("IMAGE_SETTING", "" + SettingScreen.this.imageSeekbar.getProgress());
                SharedPrefUtil sharedPrefUtil2 = SettingScreen.this.sharedPrefUtil;
                sharedPrefUtil2.saveString("VIDEO_SETTING", "" + SettingScreen.this.videoseekbar.getProgress());
                SharedPrefUtil sharedPrefUtil3 = SettingScreen.this.sharedPrefUtil;
                sharedPrefUtil3.saveString("AUDIO_SETTING", "" + SettingScreen.this.audioseekbar.getProgress());
                SharedPrefUtil sharedPrefUtil4 = SettingScreen.this.sharedPrefUtil;
                sharedPrefUtil4.saveString("FILE_SETTING", "" + SettingScreen.this.filesseekbar.getProgress());
                SharedPrefUtil sharedPrefUtil5 = SettingScreen.this.sharedPrefUtil;
                sharedPrefUtil5.saveString("OTHER_SETTING", "" + SettingScreen.this.otherseekbar.getProgress());
                System.gc();
                GlobalData.afterDelete = true;
                MobiClean.getInstance().duplicatesData = null;
                Intent intent = new Intent(SettingScreen.this, SpaceManagerActivity.class);
                intent.putExtra("apply", true);
                SettingScreen.this.setResult(-1, intent);
                SettingScreen.this.finish();
            }
        });
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_setting_screen);
        Util.saveScreen(getClass().getName(), this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        init();
        setListners();
        String string = this.sharedPrefUtil.getString("IMAGE_SETTING");
        if (string == null) {
            string = "8";
        }
        this.imageSeekbar.setProgress(Integer.parseInt(string));
        String string2 = this.sharedPrefUtil.getString("VIDEO_SETTING");
        if (string2 == null) {
            string2 = "16";
        }
        this.videoseekbar.setProgress(Integer.parseInt(string2));
        String string3 = this.sharedPrefUtil.getString("AUDIO_SETTING");
        if (string3 == null) {
            string3 = "8";
        }
        this.audioseekbar.setProgress(Integer.parseInt(string3));
        String string4 = this.sharedPrefUtil.getString("FILE_SETTING");
        this.filesseekbar.setProgress(Integer.parseInt(string4 != null ? string4 : "8"));
        String string5 = this.sharedPrefUtil.getString("OTHER_SETTING");
        this.otherseekbar.setProgress(Integer.parseInt(string5 != null ? string5 : "16"));
        this.seek_plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.imageSeekbar.getProgress();
                if (progress < 8) {
                    SettingScreen.this.imageSeekbar.setProgress(8);
                } else if (progress >= 8 && progress < 16) {
                    SettingScreen.this.imageSeekbar.setProgress(16);
                } else if (progress >= 16 && progress < 24) {
                    SettingScreen.this.imageSeekbar.setProgress(24);
                } else if (progress >= 24 && progress < 32) {
                    SettingScreen.this.imageSeekbar.setProgress(32);
                } else if (progress >= 32 && progress < 40) {
                    SettingScreen.this.imageSeekbar.setProgress(40);
                } else if (progress >= 40 && progress < 48) {
                    SettingScreen.this.imageSeekbar.setProgress(48);
                } else if (progress >= 48 && progress < 56) {
                    SettingScreen.this.imageSeekbar.setProgress(56);
                } else if (progress >= 56 && progress < 64) {
                    SettingScreen.this.imageSeekbar.setProgress(64);
                } else if (progress >= 64 && progress < 72) {
                    SettingScreen.this.imageSeekbar.setProgress(72);
                } else if (progress >= 72 && progress < 80) {
                    SettingScreen.this.imageSeekbar.setProgress(80);
                } else if (progress >= 80 && progress < 88) {
                    SettingScreen.this.imageSeekbar.setProgress(88);
                } else if (progress >= 88 && progress < 96) {
                    SettingScreen.this.imageSeekbar.setProgress(96);
                } else if (progress >= 96) {
                    SettingScreen.this.imageSeekbar.setProgress(100);
                }
            }
        });
        this.seek_plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.videoseekbar.getProgress();
                if (progress < 8) {
                    SettingScreen.this.videoseekbar.setProgress(8);
                } else if (progress >= 8 && progress < 16) {
                    SettingScreen.this.videoseekbar.setProgress(16);
                } else if (progress >= 16 && progress < 24) {
                    SettingScreen.this.videoseekbar.setProgress(24);
                } else if (progress >= 24 && progress < 32) {
                    SettingScreen.this.videoseekbar.setProgress(32);
                } else if (progress >= 32 && progress < 40) {
                    SettingScreen.this.videoseekbar.setProgress(40);
                } else if (progress >= 40 && progress < 48) {
                    SettingScreen.this.videoseekbar.setProgress(48);
                } else if (progress >= 48 && progress < 56) {
                    SettingScreen.this.videoseekbar.setProgress(56);
                } else if (progress >= 56 && progress < 64) {
                    SettingScreen.this.videoseekbar.setProgress(64);
                } else if (progress >= 64 && progress < 72) {
                    SettingScreen.this.videoseekbar.setProgress(72);
                } else if (progress >= 72 && progress < 80) {
                    SettingScreen.this.videoseekbar.setProgress(80);
                } else if (progress >= 80 && progress < 88) {
                    SettingScreen.this.videoseekbar.setProgress(88);
                } else if (progress >= 88 && progress < 96) {
                    SettingScreen.this.videoseekbar.setProgress(96);
                } else if (progress >= 96) {
                    SettingScreen.this.videoseekbar.setProgress(100);
                }
            }
        });
        this.seek_plus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.audioseekbar.getProgress();
                if (progress < 8) {
                    SettingScreen.this.audioseekbar.setProgress(8);
                } else if (progress >= 8 && progress < 16) {
                    SettingScreen.this.audioseekbar.setProgress(16);
                } else if (progress >= 16 && progress < 24) {
                    SettingScreen.this.audioseekbar.setProgress(24);
                } else if (progress >= 24 && progress < 32) {
                    SettingScreen.this.audioseekbar.setProgress(32);
                } else if (progress >= 32 && progress < 40) {
                    SettingScreen.this.audioseekbar.setProgress(40);
                } else if (progress >= 40 && progress < 48) {
                    SettingScreen.this.audioseekbar.setProgress(48);
                } else if (progress >= 48 && progress < 56) {
                    SettingScreen.this.audioseekbar.setProgress(56);
                } else if (progress >= 56 && progress < 64) {
                    SettingScreen.this.audioseekbar.setProgress(64);
                } else if (progress >= 64 && progress < 72) {
                    SettingScreen.this.audioseekbar.setProgress(72);
                } else if (progress >= 72 && progress < 80) {
                    SettingScreen.this.audioseekbar.setProgress(80);
                } else if (progress >= 80 && progress < 88) {
                    SettingScreen.this.audioseekbar.setProgress(88);
                } else if (progress >= 88 && progress < 96) {
                    SettingScreen.this.audioseekbar.setProgress(96);
                } else if (progress >= 96) {
                    SettingScreen.this.audioseekbar.setProgress(100);
                }
            }
        });
        this.seek_plus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.filesseekbar.getProgress();
                if (progress < 8) {
                    SettingScreen.this.filesseekbar.setProgress(8);
                } else if (progress >= 8 && progress < 16) {
                    SettingScreen.this.filesseekbar.setProgress(16);
                } else if (progress >= 16 && progress < 24) {
                    SettingScreen.this.filesseekbar.setProgress(24);
                } else if (progress >= 24 && progress < 32) {
                    SettingScreen.this.filesseekbar.setProgress(32);
                } else if (progress >= 32 && progress < 40) {
                    SettingScreen.this.filesseekbar.setProgress(40);
                } else if (progress >= 40 && progress < 48) {
                    SettingScreen.this.filesseekbar.setProgress(48);
                } else if (progress >= 48 && progress < 56) {
                    SettingScreen.this.filesseekbar.setProgress(56);
                } else if (progress >= 56 && progress < 64) {
                    SettingScreen.this.filesseekbar.setProgress(64);
                } else if (progress >= 64 && progress < 72) {
                    SettingScreen.this.filesseekbar.setProgress(72);
                } else if (progress >= 72 && progress < 80) {
                    SettingScreen.this.filesseekbar.setProgress(80);
                } else if (progress >= 80 && progress < 88) {
                    SettingScreen.this.filesseekbar.setProgress(88);
                } else if (progress >= 88 && progress < 96) {
                    SettingScreen.this.filesseekbar.setProgress(96);
                } else if (progress >= 96) {
                    SettingScreen.this.filesseekbar.setProgress(100);
                }
            }
        });
        this.seek_plus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.otherseekbar.getProgress();
                if (progress < 8) {
                    SettingScreen.this.otherseekbar.setProgress(8);
                } else if (progress >= 8 && progress < 16) {
                    SettingScreen.this.otherseekbar.setProgress(16);
                } else if (progress >= 16 && progress < 24) {
                    SettingScreen.this.otherseekbar.setProgress(24);
                } else if (progress >= 24 && progress < 32) {
                    SettingScreen.this.otherseekbar.setProgress(32);
                } else if (progress >= 32 && progress < 40) {
                    SettingScreen.this.otherseekbar.setProgress(40);
                } else if (progress >= 40 && progress < 48) {
                    SettingScreen.this.otherseekbar.setProgress(48);
                } else if (progress >= 48 && progress < 56) {
                    SettingScreen.this.otherseekbar.setProgress(56);
                } else if (progress >= 56 && progress < 64) {
                    SettingScreen.this.otherseekbar.setProgress(64);
                } else if (progress >= 64 && progress < 72) {
                    SettingScreen.this.otherseekbar.setProgress(72);
                } else if (progress >= 72 && progress < 80) {
                    SettingScreen.this.otherseekbar.setProgress(80);
                } else if (progress >= 80 && progress < 88) {
                    SettingScreen.this.otherseekbar.setProgress(88);
                } else if (progress >= 88 && progress < 96) {
                    SettingScreen.this.otherseekbar.setProgress(96);
                } else if (progress >= 96) {
                    SettingScreen.this.otherseekbar.setProgress(100);
                }
            }
        });
        this.seek_minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.imageSeekbar.getProgress();
                if (progress <= 8) {
                    SettingScreen.this.imageSeekbar.setProgress(0);
                } else if (progress > 8 && progress <= 16) {
                    SettingScreen.this.imageSeekbar.setProgress(8);
                } else if (progress > 16 && progress <= 24) {
                    SettingScreen.this.imageSeekbar.setProgress(16);
                } else if (progress > 24 && progress <= 32) {
                    SettingScreen.this.imageSeekbar.setProgress(24);
                } else if (progress > 32 && progress <= 40) {
                    SettingScreen.this.imageSeekbar.setProgress(32);
                } else if (progress > 40 && progress <= 48) {
                    SettingScreen.this.imageSeekbar.setProgress(40);
                } else if (progress > 48 && progress <= 56) {
                    SettingScreen.this.imageSeekbar.setProgress(48);
                } else if (progress > 56 && progress <= 64) {
                    SettingScreen.this.imageSeekbar.setProgress(56);
                } else if (progress > 64 && progress <= 72) {
                    SettingScreen.this.imageSeekbar.setProgress(64);
                } else if (progress > 72 && progress <= 80) {
                    SettingScreen.this.imageSeekbar.setProgress(72);
                } else if (progress > 80 && progress <= 88) {
                    SettingScreen.this.imageSeekbar.setProgress(80);
                } else if (progress > 88 && progress <= 96) {
                    SettingScreen.this.imageSeekbar.setProgress(88);
                } else if (progress > 96) {
                    SettingScreen.this.imageSeekbar.setProgress(96);
                }
            }
        });
        this.seek_minus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.videoseekbar.getProgress();
                if (progress <= 8) {
                    SettingScreen.this.videoseekbar.setProgress(0);
                } else if (progress > 8 && progress <= 16) {
                    SettingScreen.this.videoseekbar.setProgress(8);
                } else if (progress > 16 && progress <= 24) {
                    SettingScreen.this.videoseekbar.setProgress(16);
                } else if (progress > 24 && progress <= 32) {
                    SettingScreen.this.videoseekbar.setProgress(24);
                } else if (progress > 32 && progress <= 40) {
                    SettingScreen.this.videoseekbar.setProgress(32);
                } else if (progress > 40 && progress <= 48) {
                    SettingScreen.this.videoseekbar.setProgress(40);
                } else if (progress > 48 && progress <= 56) {
                    SettingScreen.this.videoseekbar.setProgress(48);
                } else if (progress > 56 && progress <= 64) {
                    SettingScreen.this.videoseekbar.setProgress(56);
                } else if (progress > 64 && progress <= 72) {
                    SettingScreen.this.videoseekbar.setProgress(64);
                } else if (progress > 72 && progress <= 80) {
                    SettingScreen.this.videoseekbar.setProgress(72);
                } else if (progress > 80 && progress <= 88) {
                    SettingScreen.this.videoseekbar.setProgress(80);
                } else if (progress > 88 && progress <= 96) {
                    SettingScreen.this.videoseekbar.setProgress(88);
                } else if (progress > 96) {
                    SettingScreen.this.videoseekbar.setProgress(96);
                }
            }
        });
        this.seek_minus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.audioseekbar.getProgress();
                if (progress <= 8) {
                    SettingScreen.this.audioseekbar.setProgress(0);
                } else if (progress > 8 && progress <= 16) {
                    SettingScreen.this.audioseekbar.setProgress(8);
                } else if (progress > 16 && progress <= 24) {
                    SettingScreen.this.audioseekbar.setProgress(16);
                } else if (progress > 24 && progress <= 32) {
                    SettingScreen.this.audioseekbar.setProgress(24);
                } else if (progress > 32 && progress <= 40) {
                    SettingScreen.this.audioseekbar.setProgress(32);
                } else if (progress > 40 && progress <= 48) {
                    SettingScreen.this.audioseekbar.setProgress(40);
                } else if (progress > 48 && progress <= 56) {
                    SettingScreen.this.audioseekbar.setProgress(48);
                } else if (progress > 56 && progress <= 64) {
                    SettingScreen.this.audioseekbar.setProgress(56);
                } else if (progress > 64 && progress <= 72) {
                    SettingScreen.this.audioseekbar.setProgress(64);
                } else if (progress > 72 && progress <= 80) {
                    SettingScreen.this.audioseekbar.setProgress(72);
                } else if (progress > 80 && progress <= 88) {
                    SettingScreen.this.audioseekbar.setProgress(80);
                } else if (progress > 88 && progress <= 96) {
                    SettingScreen.this.audioseekbar.setProgress(88);
                } else if (progress > 96) {
                    SettingScreen.this.audioseekbar.setProgress(96);
                }
            }
        });
        this.seek_minus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.filesseekbar.getProgress();
                if (progress <= 8) {
                    SettingScreen.this.filesseekbar.setProgress(0);
                } else if (progress > 8 && progress <= 16) {
                    SettingScreen.this.filesseekbar.setProgress(8);
                } else if (progress > 16 && progress <= 24) {
                    SettingScreen.this.filesseekbar.setProgress(16);
                } else if (progress > 24 && progress <= 32) {
                    SettingScreen.this.filesseekbar.setProgress(24);
                } else if (progress > 32 && progress <= 40) {
                    SettingScreen.this.filesseekbar.setProgress(32);
                } else if (progress > 40 && progress <= 48) {
                    SettingScreen.this.filesseekbar.setProgress(40);
                } else if (progress > 48 && progress <= 56) {
                    SettingScreen.this.filesseekbar.setProgress(48);
                } else if (progress > 56 && progress <= 64) {
                    SettingScreen.this.filesseekbar.setProgress(56);
                } else if (progress > 64 && progress <= 72) {
                    SettingScreen.this.filesseekbar.setProgress(64);
                } else if (progress > 72 && progress <= 80) {
                    SettingScreen.this.filesseekbar.setProgress(72);
                } else if (progress > 80 && progress <= 88) {
                    SettingScreen.this.filesseekbar.setProgress(80);
                } else if (progress > 88 && progress <= 96) {
                    SettingScreen.this.filesseekbar.setProgress(88);
                } else if (progress > 96) {
                    SettingScreen.this.filesseekbar.setProgress(96);
                }
            }
        });
        this.seek_minus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingScreen.this.multipleClicked()) {
                    return;
                }
                int progress = SettingScreen.this.otherseekbar.getProgress();
                if (progress <= 8) {
                    SettingScreen.this.otherseekbar.setProgress(0);
                } else if (progress > 8 && progress <= 16) {
                    SettingScreen.this.otherseekbar.setProgress(8);
                } else if (progress > 16 && progress <= 24) {
                    SettingScreen.this.otherseekbar.setProgress(16);
                } else if (progress > 24 && progress <= 32) {
                    SettingScreen.this.otherseekbar.setProgress(24);
                } else if (progress > 32 && progress <= 40) {
                    SettingScreen.this.otherseekbar.setProgress(32);
                } else if (progress > 40 && progress <= 48) {
                    SettingScreen.this.otherseekbar.setProgress(40);
                } else if (progress > 48 && progress <= 56) {
                    SettingScreen.this.otherseekbar.setProgress(48);
                } else if (progress > 56 && progress <= 64) {
                    SettingScreen.this.otherseekbar.setProgress(56);
                } else if (progress > 64 && progress <= 72) {
                    SettingScreen.this.otherseekbar.setProgress(64);
                } else if (progress > 72 && progress <= 80) {
                    SettingScreen.this.otherseekbar.setProgress(72);
                } else if (progress > 80 && progress <= 88) {
                    SettingScreen.this.otherseekbar.setProgress(80);
                } else if (progress > 88 && progress <= 96) {
                    SettingScreen.this.otherseekbar.setProgress(88);
                } else if (progress > 96) {
                    SettingScreen.this.otherseekbar.setProgress(96);
                }
            }
        });
    }
}
