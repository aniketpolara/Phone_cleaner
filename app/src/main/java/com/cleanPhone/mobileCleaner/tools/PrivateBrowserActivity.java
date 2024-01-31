package com.cleanPhone.mobileCleaner.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.Util;

public class PrivateBrowserActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdvancedWebView.Listener {
    private Context context;
    public EditText h;
    public ImageButton j;
    public SwipeRefreshLayout k;
    private ProgressBar loading;
    private AdvancedWebView webView;
    public String i = "";
    private String TAG = "PrivateBrowserActivity";


    public void closebrowser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("" + getResources().getString(R.string.mbc_private_browsing));
        builder.setMessage("" + getResources().getString(R.string.mbc_are_u_sure));
        builder.setPositiveButton(R.string.mbc_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PrivateBrowserActivity.this.webView.clearFormData();
                PrivateBrowserActivity.this.webView.clearHistory();
                PrivateBrowserActivity.this.webView.clearCache(true);
                CookieSyncManager.createInstance(PrivateBrowserActivity.this);
                CookieManager.getInstance().removeAllCookie();
                PrivateBrowserActivity privateBrowserActivity = PrivateBrowserActivity.this;
                Toast.makeText(privateBrowserActivity, "" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_digital_footprints), Toast.LENGTH_SHORT).show();
                PrivateBrowserActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.mbc_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void getid() {
        this.k = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        this.j = (ImageButton) findViewById(R.id.btn_exits);
        this.h = (EditText) findViewById(R.id.et_urlsearch);
        this.webView = (AdvancedWebView) findViewById(R.id.webview_browser);
        this.loading = (ProgressBar) findViewById(R.id.progressBar);
    }

    public static boolean isConnectingToInternet(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && (allNetworkInfo = connectivityManager.getAllNetworkInfo()) != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    public void startwebsearch(String str) {
        Util.appendLogmobiclean(this.TAG, "startwebsearch(String url)", GlobalData.FILE_NAME);
        this.webView.addHttpHeader("X-Requested-With", "");
        this.webView.loadUrl(str);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.webView.onActivityResult(i, i2, intent);
    }

    @Override
    public void onBackPressed() {
        Util.appendLogmobiclean(this.TAG, "onBackPressed() CAll Clear CHACE DATA IN BROWSER", GlobalData.FILE_NAME);
        if (!this.webView.canGoBack()) {
            this.webView.clearFormData();
            this.webView.clearHistory();
            this.webView.clearCache(true);
            CookieSyncManager.createInstance(this);
            CookieManager.getInstance().removeAllCookie();
            Toast.makeText(this, "" + getResources().getString(R.string.mbc_digital_footprints), Toast.LENGTH_SHORT).show();
            super.onBackPressed();
            return;
        }
        this.webView.goBack();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        setContentView(R.layout.activity_private_browser);
        Util.saveScreen(getClass().getName(), this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.context = this;
        String string = getIntent().getExtras().getString("SEARCHTEXT");
        Util.appendLogmobiclean(this.TAG, "onCreate", GlobalData.FILE_NAME);
        getid();
        this.webView.setListener(this, this);
        this.webView.setGeolocationEnabled(false);
        this.webView.setMixedContentAllowed(true);
        this.webView.setCookiesEnabled(true);
        this.webView.setThirdPartyCookiesEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String str) {
                PrivateBrowserActivity.this.loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                PrivateBrowserActivity.this.loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                PrivateBrowserActivity.this.loading.setVisibility(View.INVISIBLE);
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() { // from class: com.mobiclean.phoneclean.tools.PrivateBrowserActivity.2
            @Override // android.webkit.WebChromeClient
            public void onReceivedTitle(WebView webView, String str) {
                super.onReceivedTitle(webView, str);
                Toast.makeText(PrivateBrowserActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
        this.webView.addHttpHeader("X-Requested-With", "");
        EditText editText = this.h;
        editText.setText("" + string);
        this.h.requestFocus();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!string.startsWith("http://") && !string.startsWith("https://")) {
            if (!string.startsWith("www.") && !string.startsWith("WWW.")) {
                if (string.contains(".com") && string.contains("@")) {
                    startwebsearch("https://www.google.com/search?q=" + string);
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.h.getWindowToken(), 0);
                } else {
                    if (!string.contains(".com") && !string.contains(".net") && !string.contains(".org") && !string.contains(".gov") && !string.contains(".in")) {
                        Log.d("----33333", "" + string);
                        startwebsearch("https://www.google.com/search?q=" + this.h.getText().toString());
                    }
                    Log.d("----99999", "http://" + string);
                    startwebsearch("http://" + string);
                }
                this.k.setOnRefreshListener(this);
                this.k.setColorSchemeResources(R.color.redappcolor, R.color.colorPrimary, R.color.card_green, R.color.black);
                this.h.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == 3) {
                            PrivateBrowserActivity privateBrowserActivity = PrivateBrowserActivity.this;
                            privateBrowserActivity.i = privateBrowserActivity.h.getText().toString();
                            String str = PrivateBrowserActivity.this.TAG;
                            Util.appendLogmobiclean(str, "SERACH BROWSER URL >>>>>>>" + PrivateBrowserActivity.this.i, GlobalData.FILE_NAME);
                            if (!PrivateBrowserActivity.this.i.equals("")) {
                                if (!PrivateBrowserActivity.isConnectingToInternet(PrivateBrowserActivity.this.context)) {
                                    Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PrivateBrowserActivity.this.context, R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_nointernet));
                                    builder.setIcon(17301543);
                                    builder.setMessage("" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_game_network_issue));
                                    builder.setPositiveButton(R.string.mbc_ok, (DialogInterface.OnClickListener) null);
                                    builder.show();
                                } else if (!PrivateBrowserActivity.this.i.startsWith("http://") && !PrivateBrowserActivity.this.i.startsWith("https://")) {
                                    if (PrivateBrowserActivity.this.i.contains(".com") && PrivateBrowserActivity.this.i.contains("@")) {
                                        PrivateBrowserActivity privateBrowserActivity2 = PrivateBrowserActivity.this;
                                        privateBrowserActivity2.startwebsearch("https://www.google.com/search?q=" + PrivateBrowserActivity.this.i);
                                        Log.e("------bbbbbb", "https://www.google.com/search?q=" + PrivateBrowserActivity.this.i);
                                        ((InputMethodManager) PrivateBrowserActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                                    } else if (!PrivateBrowserActivity.this.i.contains(".com") && !PrivateBrowserActivity.this.i.contains(".net") && !PrivateBrowserActivity.this.i.contains(".org") && !PrivateBrowserActivity.this.i.contains(".gov") && !PrivateBrowserActivity.this.i.contains(".in")) {
                                        Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                        PrivateBrowserActivity privateBrowserActivity3 = PrivateBrowserActivity.this;
                                        privateBrowserActivity3.startwebsearch("https://www.google.com/search?q=" + PrivateBrowserActivity.this.i);
                                        ((InputMethodManager) PrivateBrowserActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                                    } else {
                                        Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                        PrivateBrowserActivity privateBrowserActivity4 = PrivateBrowserActivity.this;
                                        privateBrowserActivity4.startwebsearch("http://" + PrivateBrowserActivity.this.i);
                                        ((InputMethodManager) PrivateBrowserActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                                    }
                                } else {
                                    PrivateBrowserActivity privateBrowserActivity5 = PrivateBrowserActivity.this;
                                    privateBrowserActivity5.startwebsearch("" + PrivateBrowserActivity.this.i);
                                    Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                    ((InputMethodManager) PrivateBrowserActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                                }
                            } else {
                                Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(PrivateBrowserActivity.this.context, R.style.AppCompatAlertDialogStyle);
                                builder2.setIcon(17301543);
                                builder2.setMessage("" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_pleasetype));
                                builder2.setPositiveButton(R.string.mbc_ok, (DialogInterface.OnClickListener) null);
                                builder2.show();
                            }
                            return true;
                        }
                        return false;
                    }
                });
                this.j.setOnClickListener(new View.OnClickListener() {
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        PrivateBrowserActivity privateBrowserActivity = PrivateBrowserActivity.this;
                        PopupMenu popupMenu = new PopupMenu(privateBrowserActivity, privateBrowserActivity.j);
                        popupMenu.getMenuInflater().inflate(R.menu.browser_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                PrivateBrowserActivity.this.closebrowser();
                                return true;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }
            Log.d("----22222", "http://" + string);
            startwebsearch("http://" + string);
            this.k.setOnRefreshListener(this);
            this.k.setColorSchemeResources(R.color.redappcolor, R.color.colorPrimary, R.color.card_green, R.color.black);
            this.h.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @SuppressLint("ResourceType")
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == 3) {
                        PrivateBrowserActivity privateBrowserActivity = PrivateBrowserActivity.this;
                        privateBrowserActivity.i = privateBrowserActivity.h.getText().toString();
                        String str = PrivateBrowserActivity.this.TAG;
                        Util.appendLogmobiclean(str, "SERACH BROWSER URL >>>>>>>" + PrivateBrowserActivity.this.i, GlobalData.FILE_NAME);
                        if (!PrivateBrowserActivity.this.i.equals("")) {
                            if (!PrivateBrowserActivity.isConnectingToInternet(PrivateBrowserActivity.this.context)) {
                                Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                AlertDialog.Builder builder = new AlertDialog.Builder(PrivateBrowserActivity.this.context, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_nointernet));
                                builder.setIcon(17301543);
                                builder.setMessage("" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_game_network_issue));
                                builder.setPositiveButton(R.string.mbc_ok, (DialogInterface.OnClickListener) null);
                                builder.show();
                            } else if (!PrivateBrowserActivity.this.i.startsWith("http://") && !PrivateBrowserActivity.this.i.startsWith("https://")) {
                                if (PrivateBrowserActivity.this.i.contains(".com") && PrivateBrowserActivity.this.i.contains("@")) {
                                    PrivateBrowserActivity privateBrowserActivity2 = PrivateBrowserActivity.this;
                                    privateBrowserActivity2.startwebsearch("https://www.google.com/search?q=" + PrivateBrowserActivity.this.i);
                                    Log.e("------bbbbbb", "https://www.google.com/search?q=" + PrivateBrowserActivity.this.i);
                                    ((InputMethodManager) PrivateBrowserActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                                } else if (!PrivateBrowserActivity.this.i.contains(".com") && !PrivateBrowserActivity.this.i.contains(".net") && !PrivateBrowserActivity.this.i.contains(".org") && !PrivateBrowserActivity.this.i.contains(".gov") && !PrivateBrowserActivity.this.i.contains(".in")) {
                                    Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                    PrivateBrowserActivity privateBrowserActivity3 = PrivateBrowserActivity.this;
                                    privateBrowserActivity3.startwebsearch("https://www.google.com/search?q=" + PrivateBrowserActivity.this.i);
                                    ((InputMethodManager) PrivateBrowserActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                                } else {
                                    Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                    PrivateBrowserActivity privateBrowserActivity4 = PrivateBrowserActivity.this;
                                    privateBrowserActivity4.startwebsearch("http://" + PrivateBrowserActivity.this.i);
                                    ((InputMethodManager) PrivateBrowserActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                                }
                            } else {
                                PrivateBrowserActivity privateBrowserActivity5 = PrivateBrowserActivity.this;
                                privateBrowserActivity5.startwebsearch("" + PrivateBrowserActivity.this.i);
                                Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                ((InputMethodManager) PrivateBrowserActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                            }
                        } else {
                            Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(PrivateBrowserActivity.this.context, R.style.AppCompatAlertDialogStyle);
                            builder2.setIcon(17301543);
                            builder2.setMessage("" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_pleasetype));
                            builder2.setPositiveButton(R.string.mbc_ok, (DialogInterface.OnClickListener) null);
                            builder2.show();
                        }
                        return true;
                    }
                    return false;
                }
            });
            this.j.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrivateBrowserActivity privateBrowserActivity = PrivateBrowserActivity.this;
                    PopupMenu popupMenu = new PopupMenu(privateBrowserActivity, privateBrowserActivity.j);
                    popupMenu.getMenuInflater().inflate(R.menu.browser_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            PrivateBrowserActivity.this.closebrowser();
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
        Log.d("-----11111", "" + string);
        startwebsearch(string);
        this.k.setOnRefreshListener(this);
        this.k.setColorSchemeResources(R.color.redappcolor, R.color.colorPrimary, R.color.card_green, R.color.black);
        this.h.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("ResourceType")
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    PrivateBrowserActivity privateBrowserActivity = PrivateBrowserActivity.this;
                    privateBrowserActivity.i = privateBrowserActivity.h.getText().toString();
                    String str = PrivateBrowserActivity.this.TAG;
                    Util.appendLogmobiclean(str, "SERACH BROWSER URL >>>>>>>" + PrivateBrowserActivity.this.i, GlobalData.FILE_NAME);
                    if (!PrivateBrowserActivity.this.i.equals("")) {
                        if (!PrivateBrowserActivity.isConnectingToInternet(PrivateBrowserActivity.this.context)) {
                            Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                            AlertDialog.Builder builder = new AlertDialog.Builder(PrivateBrowserActivity.this.context, R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_nointernet));
                            builder.setIcon(17301543);
                            builder.setMessage("" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_game_network_issue));
                            builder.setPositiveButton(R.string.mbc_ok, (DialogInterface.OnClickListener) null);
                            builder.show();
                        } else if (!PrivateBrowserActivity.this.i.startsWith("http://") && !PrivateBrowserActivity.this.i.startsWith("https://")) {
                            if (PrivateBrowserActivity.this.i.contains(".com") && PrivateBrowserActivity.this.i.contains("@")) {
                                PrivateBrowserActivity privateBrowserActivity2 = PrivateBrowserActivity.this;
                                privateBrowserActivity2.startwebsearch("https://www.google.com/search?q=" + PrivateBrowserActivity.this.i);
                                Log.e("------bbbbbb", "https://www.google.com/search?q=" + PrivateBrowserActivity.this.i);
                                ((InputMethodManager) PrivateBrowserActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                            } else if (!PrivateBrowserActivity.this.i.contains(".com") && !PrivateBrowserActivity.this.i.contains(".net") && !PrivateBrowserActivity.this.i.contains(".org") && !PrivateBrowserActivity.this.i.contains(".gov") && !PrivateBrowserActivity.this.i.contains(".in")) {
                                Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                PrivateBrowserActivity privateBrowserActivity3 = PrivateBrowserActivity.this;
                                privateBrowserActivity3.startwebsearch("https://www.google.com/search?q=" + PrivateBrowserActivity.this.i);
                                ((InputMethodManager) PrivateBrowserActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                            } else {
                                Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                                PrivateBrowserActivity privateBrowserActivity4 = PrivateBrowserActivity.this;
                                privateBrowserActivity4.startwebsearch("http://" + PrivateBrowserActivity.this.i);
                                ((InputMethodManager) PrivateBrowserActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                            }
                        } else {
                            PrivateBrowserActivity privateBrowserActivity5 = PrivateBrowserActivity.this;
                            privateBrowserActivity5.startwebsearch("" + PrivateBrowserActivity.this.i);
                            Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                            ((InputMethodManager) PrivateBrowserActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PrivateBrowserActivity.this.h.getWindowToken(), 0);
                        }
                    } else {
                        Log.e("------bbbbbb", "" + PrivateBrowserActivity.this.i);
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(PrivateBrowserActivity.this.context, R.style.AppCompatAlertDialogStyle);
                        builder2.setIcon(17301543);
                        builder2.setMessage("" + PrivateBrowserActivity.this.getResources().getString(R.string.mbc_pleasetype));
                        builder2.setPositiveButton(R.string.mbc_ok, (DialogInterface.OnClickListener) null);
                        builder2.show();
                    }
                    return true;
                }
                return false;
            }
        });
        this.j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrivateBrowserActivity privateBrowserActivity = PrivateBrowserActivity.this;
                PopupMenu popupMenu = new PopupMenu(privateBrowserActivity, privateBrowserActivity.j);
                popupMenu.getMenuInflater().inflate(R.menu.browser_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        PrivateBrowserActivity.this.closebrowser();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public void onDestroy() {
        this.webView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDownloadRequested(String str, String str2, String str3, long j, String str4, String str5) {
    }

    @Override
    public void onExternalPageRequest(String str) {
    }

    @Override
    public void onPageError(int i, String str, String str2) {
    }

    @Override
    public void onPageFinished(String str) {
        this.webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageStarted(String str, Bitmap bitmap) {
        this.webView.setVisibility(View.INVISIBLE);
    }

    @Override
    @SuppressLint({"NewApi"})
    public void onPause() {
        this.webView.onPause();
        super.onPause();
    }

    @Override
    public void onRefresh() {
        this.k.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PrivateBrowserActivity.this.k.setRefreshing(false);
            }
        }, 4000L);
    }

    @Override
    @SuppressLint({"NewApi"})
    public void onResume() {
        super.onResume();
        this.webView.onResume();
    }
}
