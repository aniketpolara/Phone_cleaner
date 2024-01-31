package com.cleanPhone.mobileCleaner.tools;

import static com.cleanPhone.mobileCleaner.ads.DH_GllobalItem.showInterstialAds;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.cleanPhone.mobileCleaner.HomeActivity;
import com.cleanPhone.mobileCleaner.ParentActivity;
import com.cleanPhone.mobileCleaner.R;
import com.cleanPhone.mobileCleaner.ads.DH_GllobalItem;
import com.cleanPhone.mobileCleaner.utility.GlobalData;
import com.cleanPhone.mobileCleaner.utility.SharedPrefUtil;
import com.cleanPhone.mobileCleaner.utility.Util;

import java.net.MalformedURLException;
import java.net.URL;


public class PrivateBrowser extends ParentActivity implements AdvancedWebView.Listener {
    private String TAG = "PrivateBrowser";
    private Context context;
    public EditText j;
    public String k;
    public LinearLayout l;
    private ProgressBar loading;
    public LinearLayout m;
    public ImageView n;
    private boolean noti_result_back;
    private AdvancedWebView webView;
    public ImageView search;
    int admob = 3;

    public long DownloadData(String str, Uri uri) throws MalformedURLException {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FilenameUtils.getName(new URL(str).getPath()));
        if (downloadManager != null) {
            return downloadManager.enqueue(request);
        }
        return 0L;
    }

    public void closebrowser() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.new_dialog_junk_cancel);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setGravity(17);
        View space;


        dialog.findViewById(R.id.dialog_img).setVisibility(View.GONE);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getString(R.string.mbc_private_browsing));
        ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(getString(R.string.mbc_are_u_sure));
        dialog.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                PrivateBrowser.this.w(view);
            }
        });
        dialog.show();
    }

    private void getids() {
        this.l = (LinearLayout) findViewById(R.id.layout_hide);
        this.m = (LinearLayout) findViewById(R.id.layout_webview);
        this.n = (ImageView) findViewById(R.id.iv_back);
        this.j = (EditText) findViewById(R.id.edittextSearch);
        this.webView = (AdvancedWebView) findViewById(R.id.webview_browser);
        this.loading = (ProgressBar) findViewById(R.id.progressBar);
        this.search = (ImageView) findViewById(R.id.imgSea);

        Util.appendLogmobiclean(this.TAG, "PrivateBrowser onCreate()", GlobalData.FILE_NAME);
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

    private void redirectToNoti() {
        this.noti_result_back = getIntent().getBooleanExtra(GlobalData.NOTI_RESULT_BACK, false);
        getIntent().getBooleanExtra(GlobalData.HEADER_NOTI_TRACK, false);
    }

    public void startwebsearch(String str) {
        this.l.setVisibility(View.GONE);
        this.m.setVisibility(View.VISIBLE);
        Util.appendLogmobiclean(this.TAG, "startwebsearch(String url)", GlobalData.FILE_NAME);
        this.webView.addHttpHeader("X-Requested-With", "");
        this.webView.loadUrl(str);
    }

    public void w(View view) {
        this.webView.clearFormData();
        this.webView.clearHistory();
        this.webView.clearCache(true);
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();
        Toast.makeText(this, "" + getResources().getString(R.string.mbc_digital_footprints), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.webView.onActivityResult(i, i2, intent);
    }

    @Override
    public void onBackPressed() {
        Util.appendLogmobiclean(this.TAG, "onBackPressed() CAll Clear CHACE DATA IN BROWSER", GlobalData.FILE_NAME);
        if (this.noti_result_back) {
            startActivity(new Intent(this.context, HomeActivity.class));
        } else if (!this.webView.canGoBack()) {
            closebrowser();
        } else {
            this.webView.goBack();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        GlobalData.SETAPPLAnguage(this);
        Util.saveScreen(getClass().getName(), this);
        getWindow().getAttributes().windowAnimations = R.style.DefaultDialogAnimation;
        setContentView(R.layout.activity_private_browser2);
        this.context = this;
        getids();
        redirectToNoti();


        showInterstialAds(PrivateBrowser.this);

        LinearLayout llBanner = findViewById(R.id.llBanner);
        DH_GllobalItem.fbAdaptiveBanner(this, llBanner, new DH_GllobalItem.BannerCallback() {
            @Override
            public void onAdLoad() {
                llBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFail() {
                llBanner.setVisibility(View.GONE);
            }
        });

        this.webView.setListener(this, this);
        this.webView.setGeolocationEnabled(false);
        this.webView.setMixedContentAllowed(true);
        this.webView.setCookiesEnabled(true);
        this.webView.setThirdPartyCookiesEnabled(true);
        this.webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                try {
                    PrivateBrowser.this.DownloadData(str, Uri.parse(str));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String str) {
                PrivateBrowser.this.loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                PrivateBrowser.this.loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                PrivateBrowser.this.loading.setVisibility(View.INVISIBLE);
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() { // from class: com.mobiclean.phoneclean.tools.PrivateBrowser.3
            @Override // android.webkit.WebChromeClient
            public void onReceivedTitle(WebView webView, String str) {
                super.onReceivedTitle(webView, str);
            }
        });
        this.webView.addHttpHeader("X-Requested-With", "");
        this.n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrivateBrowser.this.closebrowser();
                return;
            }
        });

        this.j.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("ResourceType")
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    PrivateBrowser privateBrowser = PrivateBrowser.this;
                    privateBrowser.k = privateBrowser.j.getText().toString();
                    String str = PrivateBrowser.this.TAG;
                    Util.appendLogmobiclean(str, "SERACH BROWSER URL >>>>>>>" + PrivateBrowser.this.k, GlobalData.FILE_NAME);
                    if (!PrivateBrowser.this.k.equals("")) {
                        if (!PrivateBrowser.isConnectingToInternet(PrivateBrowser.this.context)) {
                            Log.e("------bbbbbb", "" + PrivateBrowser.this.k);
                            AlertDialog.Builder builder = new AlertDialog.Builder(PrivateBrowser.this.context, R.style.AppCompatAlertDialogStyle);
                            builder.setTitle("" + PrivateBrowser.this.getResources().getString(R.string.mbc_nointernet));
                            builder.setIcon(17301543);
                            builder.setMessage("" + PrivateBrowser.this.getResources().getString(R.string.mbc_game_network_issue));
                            builder.setPositiveButton(R.string.mbc_ok, (DialogInterface.OnClickListener) null);
                            builder.show();
                        } else if (!PrivateBrowser.this.k.startsWith("http://") && !PrivateBrowser.this.k.startsWith("https://")) {
                            if (PrivateBrowser.this.k.contains(".com") && PrivateBrowser.this.k.contains("@")) {
                                PrivateBrowser privateBrowser2 = PrivateBrowser.this;
                                privateBrowser2.startwebsearch("https://www.google.com/search?q=" + PrivateBrowser.this.k);
                                InputMethodManager inputMethodManager = (InputMethodManager) PrivateBrowser.this.getSystemService("input_method");
                                if (inputMethodManager != null) {
                                    inputMethodManager.hideSoftInputFromWindow(PrivateBrowser.this.j.getWindowToken(), 0);
                                }
                            } else if (!PrivateBrowser.this.k.contains(".com") && !PrivateBrowser.this.k.contains(".net") && !PrivateBrowser.this.k.contains(".org") && !PrivateBrowser.this.k.contains(".gov") && !PrivateBrowser.this.k.contains(".in")) {
                                PrivateBrowser privateBrowser3 = PrivateBrowser.this;
                                privateBrowser3.startwebsearch("https://www.google.com/search?q=" + PrivateBrowser.this.k);
                                InputMethodManager inputMethodManager2 = (InputMethodManager) PrivateBrowser.this.getSystemService("input_method");
                                if (inputMethodManager2 != null) {
                                    inputMethodManager2.hideSoftInputFromWindow(PrivateBrowser.this.j.getWindowToken(), 0);
                                }
                            } else {
                                PrivateBrowser privateBrowser4 = PrivateBrowser.this;
                                privateBrowser4.startwebsearch("http://" + PrivateBrowser.this.k);
                                InputMethodManager inputMethodManager3 = (InputMethodManager) PrivateBrowser.this.getSystemService("input_method");
                                if (inputMethodManager3 != null) {
                                    inputMethodManager3.hideSoftInputFromWindow(PrivateBrowser.this.j.getWindowToken(), 0);
                                }
                            }
                        } else {
                            PrivateBrowser privateBrowser5 = PrivateBrowser.this;
                            privateBrowser5.startwebsearch("" + PrivateBrowser.this.k);
                            InputMethodManager inputMethodManager4 = (InputMethodManager) PrivateBrowser.this.getSystemService("input_method");
                            if (inputMethodManager4 != null) {
                                inputMethodManager4.hideSoftInputFromWindow(PrivateBrowser.this.j.getWindowToken(), 0);
                            }
                        }
                    } else {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(PrivateBrowser.this.context, R.style.AppCompatAlertDialogStyle);
                        builder2.setIcon(17301543);
                        builder2.setMessage("" + PrivateBrowser.this.getResources().getString(R.string.mbc_pleasetype));
                        builder2.setPositiveButton(R.string.mbc_ok, (DialogInterface.OnClickListener) null);
                        builder2.show();
                    }
                    return true;
                }
                return false;
            }
        });
        new SharedPrefUtil(this).saveLastTimeUsed(SharedPrefUtil.LUSED_PRIVATE_BROWSER, System.currentTimeMillis());
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
        this.webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(String str) {
        EditText editText = this.j;
        editText.setText("" + str);
        this.webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageStarted(String str, Bitmap bitmap) {
        EditText editText = this.j;
        editText.setText("" + str);
        this.webView.setVisibility(View.INVISIBLE);
    }

    @Override
    @SuppressLint({"NewApi"})
    public void onResume() {
        super.onResume();
        this.webView.onResume();
    }
}
