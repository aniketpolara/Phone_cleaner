package com.cleanPhone.mobileCleaner.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.cleanPhone.mobileCleaner.filestorage.DialogConfigs;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import kotlin.text.Typography;

public class AdvancedWebView extends WebView {
    public static final String DATABASES_SUB_FOLDER = "/databases";
    public static final String LANGUAGE_DEFAULT_ISO3 = "eng";
    public static final String PACKAGE_NAME_DOWNLOAD_MANAGER = "com.android.providers.downloads";
    public static final int REQUEST_CODE_FILE_PICKER = 51426;
    public WeakReference<Activity> mActivity;
    public WebChromeClient mCustomWebChromeClient;
    public WebViewClient mCustomWebViewClient;
    public ValueCallback<Uri> mFileUploadCallbackFirst;
    public ValueCallback<Uri[]> mFileUploadCallbackSecond;
    public WeakReference<Fragment> mFragment;
    public boolean mGeolocationEnabled;
    public final Map<String, String> mHttpHeaders;
    public String mLanguageIso3;
    public long mLastError;
    public Listener mListener;
    public final List<String> mPermittedHostnames;
    public int mRequestCodeFilePicker;
    public String mUploadableFileTypes;

    public interface Listener {
        void onDownloadRequested(String str, String str2, String str3, long j, String str4, String str5);

        void onExternalPageRequest(String str);

        void onPageError(int i, String str, String str2);

        void onPageFinished(String str);

        void onPageStarted(String str, Bitmap bitmap);
    }

    public AdvancedWebView(Context context) {
        super(context);
        this.mPermittedHostnames = new LinkedList();
        this.mRequestCodeFilePicker = REQUEST_CODE_FILE_PICKER;
        this.mUploadableFileTypes = "*/*";
        this.mHttpHeaders = new HashMap();
        init(context);
    }

    public static String decodeBase64(String str) throws IllegalArgumentException, UnsupportedEncodingException {
        return new String(Base64.decode(str, 0), "UTF-8");
    }

    public static String getLanguageIso3() {
        try {
            return Locale.getDefault().getISO3Language().toLowerCase(Locale.US);
        } catch (MissingResourceException unused) {
            return LANGUAGE_DEFAULT_ISO3;
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean handleDownload(Context context, String str, String str2) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 9) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
            if (i >= 11) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(1);
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, str2);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            try {
                try {
                    downloadManager.enqueue(request);
                } catch (SecurityException unused) {
                    if (Build.VERSION.SDK_INT >= 11) {
                        request.setNotificationVisibility(0);
                    }
                    downloadManager.enqueue(request);
                }
                return true;
            } catch (IllegalArgumentException unused2) {
                openAppSettings(context, PACKAGE_NAME_DOWNLOAD_MANAGER);
                return false;
            }
        }
        throw new RuntimeException("Method requires API level 9 or above");
    }


    public static String makeUrlUnique(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if (str.contains("?")) {
            sb.append(Typography.amp);
        } else {
            if (str.lastIndexOf(47) <= 7) {
                sb.append(IOUtils.DIR_SEPARATOR_UNIX);
            }
            sb.append('?');
        }
        sb.append(System.currentTimeMillis());
        sb.append('=');
        sb.append(1);
        return sb.toString();
    }

    @SuppressLint({"NewApi"})
    private static boolean openAppSettings(Context context, String str) {
        if (Build.VERSION.SDK_INT >= 9) {
            try {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.parse("package:" + str));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            } catch (Exception unused) {
                return false;
            }
        }
        throw new RuntimeException("Method requires API level 9 or above");
    }

    @SuppressLint({"NewApi"})
    public static void setAllowAccessFromFileUrls(WebSettings webSettings, boolean z) {
        if (Build.VERSION.SDK_INT >= 16) {
            webSettings.setAllowFileAccessFromFileURLs(z);
            webSettings.setAllowUniversalAccessFromFileURLs(z);
        }
    }

    public void addHttpHeader(String str, String str2) {
        this.mHttpHeaders.put(str, str2);
    }


    public String getFileUploadPromptLabel() {
        try {
            return this.mLanguageIso3.equals("zho") ? decodeBase64("6YCJ5oup5LiA5Liq5paH5Lu2") : this.mLanguageIso3.equals("spa") ? decodeBase64("RWxpamEgdW4gYXJjaGl2bw==") : this.mLanguageIso3.equals("hin") ? decodeBase64("4KSP4KSVIOCkq+CkvOCkvuCkh+CksiDgpJrgpYHgpKjgpYfgpII=") : this.mLanguageIso3.equals("ben") ? decodeBase64("4KaP4KaV4Kaf4Ka/IOCmq+CmvuCmh+CmsiDgpqjgpr/gprDgp43gpqzgpr7gpprgpqg=") : this.mLanguageIso3.equals("ara") ? decodeBase64("2KfYrtiq2YrYp9ixINmF2YTZgSDZiNin2K3Yrw==") : this.mLanguageIso3.equals("por") ? decodeBase64("RXNjb2xoYSB1bSBhcnF1aXZv") : this.mLanguageIso3.equals("rus") ? decodeBase64("0JLRi9Cx0LXRgNC40YLQtSDQvtC00LjQvSDRhNCw0LnQuw==") : this.mLanguageIso3.equals("jpn") ? decodeBase64("MeODleOCoeOCpOODq+OCkumBuOaKnuOBl+OBpuOBj+OBoOOBleOBhA==") : this.mLanguageIso3.equals("pan") ? decodeBase64("4KiH4Kmx4KiVIOCoq+CovuCoh+CosiDgqJrgqYHgqKPgqYs=") : this.mLanguageIso3.equals("deu") ? decodeBase64("V8OkaGxlIGVpbmUgRGF0ZWk=") : this.mLanguageIso3.equals("jav") ? decodeBase64("UGlsaWggc2lqaSBiZXJrYXM=") : this.mLanguageIso3.equals("msa") ? decodeBase64("UGlsaWggc2F0dSBmYWls") : this.mLanguageIso3.equals("tel") ? decodeBase64("4LCS4LCVIOCwq+CxhuCxluCwsuCxjeCwqOCxgSDgsI7gsILgsJrgsYHgsJXgsYvgsILgsKHgsL8=") : this.mLanguageIso3.equals("vie") ? decodeBase64("Q2jhu41uIG3hu5l0IHThuq1wIHRpbg==") : this.mLanguageIso3.equals("kor") ? decodeBase64("7ZWY64KY7J2YIO2MjOydvOydhCDshKDtg50=") : this.mLanguageIso3.equals("fra") ? decodeBase64("Q2hvaXNpc3NleiB1biBmaWNoaWVy") : this.mLanguageIso3.equals("mar") ? decodeBase64("4KSr4KS+4KSH4KSyIOCkqOCkv+CkteCkoeCkvg==") : this.mLanguageIso3.equals("tam") ? decodeBase64("4K6S4K6w4K+BIOCuleCvh+CuvuCuquCvjeCuquCviCDgrqTgr4fgrrDgr43grrXgr4E=") : this.mLanguageIso3.equals("urd") ? decodeBase64("2KfbjNqpINmB2KfYptmEINmF24zauiDYs9uSINin2YbYqtiu2KfYqCDaqdix24zaug==") : this.mLanguageIso3.equals("fas") ? decodeBase64("2LHYpyDYp9mG2KrYrtin2Kgg2qnZhtuM2K8g24zaqSDZgdin24zZhA==") : this.mLanguageIso3.equals("tur") ? decodeBase64("QmlyIGRvc3lhIHNlw6dpbg==") : this.mLanguageIso3.equals("ita") ? decodeBase64("U2NlZ2xpIHVuIGZpbGU=") : this.mLanguageIso3.equals("tha") ? decodeBase64("4LmA4Lil4Li34Lit4LiB4LmE4Lif4Lil4LmM4Lir4LiZ4Li24LmI4LiH") : this.mLanguageIso3.equals("guj") ? decodeBase64("4KqP4KqVIOCqq+CqvuCqh+CqsuCqqOCrhyDgqqrgqrjgqoLgqqY=") : "Choose a file";
        } catch (Exception unused) {
            return "Choose a file";
        }
    }

    public boolean hasError() {
        return this.mLastError + 500 >= System.currentTimeMillis();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        if (context instanceof Activity) {
            this.mActivity = new WeakReference<>((Activity) context);
        }
        this.mLanguageIso3 = getLanguageIso3();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setSaveEnabled(true);
        String path = context.getFilesDir().getPath();
        String str = path.substring(0, path.lastIndexOf(DialogConfigs.DIRECTORY_SEPERATOR)) + DATABASES_SUB_FOLDER;
        WebSettings settings = getSettings();
        settings.setAllowFileAccess(false);
        setAllowAccessFromFileUrls(settings, false);
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        int i = Build.VERSION.SDK_INT;
        if (i < 18) {
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        settings.setDatabaseEnabled(true);
        if (i < 19) {
            settings.setDatabasePath(str);
        }
        setMixedContentAllowed(settings, true);
        setThirdPartyCookiesEnabled(true);
        super.setWebViewClient(new WebViewClient() {
            @Override
            public void doUpdateVisitedHistory(WebView webView, String str2, boolean z) {
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.doUpdateVisitedHistory(webView, str2, z);
                } else {
                    super.doUpdateVisitedHistory(webView, str2, z);
                }
            }

            @Override
            public void onFormResubmission(WebView webView, Message message, Message message2) {
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.onFormResubmission(webView, message, message2);
                } else {
                    super.onFormResubmission(webView, message, message2);
                }
            }

            @Override
            public void onLoadResource(WebView webView, String str2) {
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.onLoadResource(webView, str2);
                } else {
                    super.onLoadResource(webView, str2);
                }
            }

            @Override
            public void onPageFinished(WebView webView, String str2) {
                Listener listener;
                if (!AdvancedWebView.this.hasError() && (listener = AdvancedWebView.this.mListener) != null) {
                    listener.onPageFinished(str2);
                }
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.onPageFinished(webView, str2);
                }
            }

            @Override
            public void onPageStarted(WebView webView, String str2, Bitmap bitmap) {
                Listener listener;
                if (!AdvancedWebView.this.hasError() && (listener = AdvancedWebView.this.mListener) != null) {
                    listener.onPageStarted(str2, bitmap);
                }
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.onPageStarted(webView, str2, bitmap);
                }
            }

            @Override
            @SuppressLint({"NewApi"})
            public void onReceivedClientCertRequest(WebView webView, ClientCertRequest clientCertRequest) {
                if (Build.VERSION.SDK_INT >= 21) {
                    WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                    if (webViewClient != null) {
                        webViewClient.onReceivedClientCertRequest(webView, clientCertRequest);
                    } else {
                        super.onReceivedClientCertRequest(webView, clientCertRequest);
                    }
                }
            }

            @Override
            public void onReceivedError(WebView webView, int i2, String str2, String str3) {
                AdvancedWebView.this.setLastError();
                Listener listener = AdvancedWebView.this.mListener;
                if (listener != null) {
                    listener.onPageError(i2, str2, str3);
                }
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.onReceivedError(webView, i2, str2, str3);
                }
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str2, String str3) {
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.onReceivedHttpAuthRequest(webView, httpAuthHandler, str2, str3);
                } else {
                    super.onReceivedHttpAuthRequest(webView, httpAuthHandler, str2, str3);
                }
            }

            @Override
            @SuppressLint({"NewApi"})
            public void onReceivedLoginRequest(WebView webView, String str2, String str3, String str4) {
                if (Build.VERSION.SDK_INT >= 12) {
                    WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                    if (webViewClient != null) {
                        webViewClient.onReceivedLoginRequest(webView, str2, str3, str4);
                    } else {
                        super.onReceivedLoginRequest(webView, str2, str3, str4);
                    }
                }
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.onReceivedSslError(webView, sslErrorHandler, sslError);
                } else {
                    super.onReceivedSslError(webView, sslErrorHandler, sslError);
                }
            }

            @Override
            public void onScaleChanged(WebView webView, float f, float f2) {
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.onScaleChanged(webView, f, f2);
                } else {
                    super.onScaleChanged(webView, f, f2);
                }
            }


            @Override
            public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    webViewClient.onUnhandledKeyEvent(webView, keyEvent);
                } else {
                    super.onUnhandledKeyEvent(webView, keyEvent);
                }
            }

            @Override
            @SuppressLint({"NewApi"})
            public WebResourceResponse shouldInterceptRequest(WebView webView, String str2) {
                if (Build.VERSION.SDK_INT >= 11) {
                    WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                    if (webViewClient != null) {
                        return webViewClient.shouldInterceptRequest(webView, str2);
                    }
                    return super.shouldInterceptRequest(webView, str2);
                }
                return null;
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient != null) {
                    return webViewClient.shouldOverrideKeyEvent(webView, keyEvent);
                }
                return super.shouldOverrideKeyEvent(webView, keyEvent);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String str2) {
                if (!AdvancedWebView.this.isHostnameAllowed(str2)) {
                    Listener listener = AdvancedWebView.this.mListener;
                    if (listener != null) {
                        listener.onExternalPageRequest(str2);
                    }
                    return true;
                }
                WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                if (webViewClient == null || !webViewClient.shouldOverrideUrlLoading(webView, str2)) {
                    webView.loadUrl(str2);
                    return true;
                }
                return true;
            }

            @Override
            @SuppressLint({"NewApi"})
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                if (Build.VERSION.SDK_INT >= 21) {
                    WebViewClient webViewClient = AdvancedWebView.this.mCustomWebViewClient;
                    if (webViewClient != null) {
                        return webViewClient.shouldInterceptRequest(webView, webResourceRequest);
                    }
                    return super.shouldInterceptRequest(webView, webResourceRequest);
                }
                return null;
            }
        });
        super.setWebChromeClient(new WebChromeClient() {
            @Override
            public Bitmap getDefaultVideoPoster() {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    return webChromeClient.getDefaultVideoPoster();
                }
                return super.getDefaultVideoPoster();
            }

            @Override
            public View getVideoLoadingProgressView() {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    return webChromeClient.getVideoLoadingProgressView();
                }
                return super.getVideoLoadingProgressView();
            }

            @Override
            public void getVisitedHistory(ValueCallback<String[]> valueCallback) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.getVisitedHistory(valueCallback);
                } else {
                    super.getVisitedHistory(valueCallback);
                }
            }

            @Override
            public void onCloseWindow(WebView webView) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onCloseWindow(webView);
                } else {
                    super.onCloseWindow(webView);
                }
            }

            @Override
            public void onConsoleMessage(String str2, int i2, String str3) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onConsoleMessage(str2, i2, str3);
                } else {
                    super.onConsoleMessage(str2, i2, str3);
                }
            }

            @Override
            public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    return webChromeClient.onCreateWindow(webView, z, z2, message);
                }
                return super.onCreateWindow(webView, z, z2, message);
            }

            @Override
            public void onExceededDatabaseQuota(String str2, String str3, long j, long j2, long j3, WebStorage.QuotaUpdater quotaUpdater) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onExceededDatabaseQuota(str2, str3, j, j2, j3, quotaUpdater);
                } else {
                    super.onExceededDatabaseQuota(str2, str3, j, j2, j3, quotaUpdater);
                }
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onGeolocationPermissionsHidePrompt();
                } else {
                    super.onGeolocationPermissionsHidePrompt();
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String str2, GeolocationPermissions.Callback callback) {
                AdvancedWebView advancedWebView = AdvancedWebView.this;
                if (advancedWebView.mGeolocationEnabled) {
                    callback.invoke(str2, true, false);
                    return;
                }
                WebChromeClient webChromeClient = advancedWebView.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onGeolocationPermissionsShowPrompt(str2, callback);
                } else {
                    super.onGeolocationPermissionsShowPrompt(str2, callback);
                }
            }

            @Override
            public void onHideCustomView() {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onHideCustomView();
                } else {
                    super.onHideCustomView();
                }
            }

            @Override
            public boolean onJsAlert(WebView webView, String str2, String str3, JsResult jsResult) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    return webChromeClient.onJsAlert(webView, str2, str3, jsResult);
                }
                return super.onJsAlert(webView, str2, str3, jsResult);
            }

            @Override
            public boolean onJsBeforeUnload(WebView webView, String str2, String str3, JsResult jsResult) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    return webChromeClient.onJsBeforeUnload(webView, str2, str3, jsResult);
                }
                return super.onJsBeforeUnload(webView, str2, str3, jsResult);
            }

            @Override
            public boolean onJsConfirm(WebView webView, String str2, String str3, JsResult jsResult) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    return webChromeClient.onJsConfirm(webView, str2, str3, jsResult);
                }
                return super.onJsConfirm(webView, str2, str3, jsResult);
            }

            @Override
            public boolean onJsPrompt(WebView webView, String str2, String str3, String str4, JsPromptResult jsPromptResult) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    return webChromeClient.onJsPrompt(webView, str2, str3, str4, jsPromptResult);
                }
                return super.onJsPrompt(webView, str2, str3, str4, jsPromptResult);
            }

            @Override
            public boolean onJsTimeout() {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    return webChromeClient.onJsTimeout();
                }
                return super.onJsTimeout();
            }

            @Override
            @SuppressLint({"NewApi"})
            public void onPermissionRequest(PermissionRequest permissionRequest) {
                if (Build.VERSION.SDK_INT >= 21) {
                    WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                    if (webChromeClient != null) {
                        webChromeClient.onPermissionRequest(permissionRequest);
                    } else {
                        super.onPermissionRequest(permissionRequest);
                    }
                }
            }

            @Override
            @SuppressLint({"NewApi"})
            public void onPermissionRequestCanceled(PermissionRequest permissionRequest) {
                if (Build.VERSION.SDK_INT >= 21) {
                    WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                    if (webChromeClient != null) {
                        webChromeClient.onPermissionRequestCanceled(permissionRequest);
                    } else {
                        super.onPermissionRequestCanceled(permissionRequest);
                    }
                }
            }

            @Override
            public void onProgressChanged(WebView webView, int i2) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onProgressChanged(webView, i2);
                } else {
                    super.onProgressChanged(webView, i2);
                }
            }


            @Override
            public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onReceivedIcon(webView, bitmap);
                } else {
                    super.onReceivedIcon(webView, bitmap);
                }
            }

            @Override
            public void onReceivedTitle(WebView webView, String str2) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onReceivedTitle(webView, str2);
                } else {
                    super.onReceivedTitle(webView, str2);
                }
            }

            @Override
            public void onReceivedTouchIconUrl(WebView webView, String str2, boolean z) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onReceivedTouchIconUrl(webView, str2, z);
                } else {
                    super.onReceivedTouchIconUrl(webView, str2, z);
                }
            }

            @Override
            public void onRequestFocus(WebView webView) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onRequestFocus(webView);
                } else {
                    super.onRequestFocus(webView);
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    webChromeClient.onShowCustomView(view, customViewCallback);
                } else {
                    super.onShowCustomView(view, customViewCallback);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                if (Build.VERSION.SDK_INT >= 21) {
                    AdvancedWebView.this.openFileInput(null, valueCallback, fileChooserParams.getMode() == 1);
                    return true;
                }
                return false;
            }


            public void openFileChooser(ValueCallback<Uri> valueCallback, String str2) {
                openFileChooser(valueCallback, str2, null);
            }

            public void openFileChooser(ValueCallback<Uri> valueCallback, String str2, String str3) {
                AdvancedWebView.this.openFileInput(valueCallback, null, false);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                if (webChromeClient != null) {
                    return webChromeClient.onConsoleMessage(consoleMessage);
                }
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            @SuppressLint({"NewApi"})
            public void onShowCustomView(View view, int i2, CustomViewCallback customViewCallback) {
                if (Build.VERSION.SDK_INT >= 14) {
                    WebChromeClient webChromeClient = AdvancedWebView.this.mCustomWebChromeClient;
                    if (webChromeClient != null) {
                        webChromeClient.onShowCustomView(view, i2, customViewCallback);
                    } else {
                        super.onShowCustomView(view, i2, customViewCallback);
                    }
                }
            }
        });
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String str2, String str3, String str4, String str5, long j) {
                String guessFileName = URLUtil.guessFileName(str2, str4, str5);
                Listener listener = AdvancedWebView.this.mListener;
                if (listener != null) {
                    listener.onDownloadRequested(str2, guessFileName, str5, j, str4, str3);
                }
            }
        });
    }

    public boolean isHostnameAllowed(String str) {
        if (this.mPermittedHostnames.size() == 0) {
            return true;
        }
        String host = Uri.parse(str).getHost();
        for (String str2 : this.mPermittedHostnames) {
            if (!host.equals(str2)) {
                if (host.endsWith("." + str2)) {
                }
            }
            return true;
        }
        return false;
    }



    @Override
    public void loadUrl(String str, Map<String, String> map) {
        if (map == null) {
            map = this.mHttpHeaders;
        } else if (this.mHttpHeaders.size() > 0) {
            map.putAll(this.mHttpHeaders);
        }
        super.loadUrl(str, map);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        Uri[] uriArr;
        Uri[] uriArr2;
        int i3;
        if (i == this.mRequestCodeFilePicker) {
            if (i2 != -1) {
                ValueCallback<Uri> valueCallback = this.mFileUploadCallbackFirst;
                if (valueCallback != null) {
                    valueCallback.onReceiveValue(null);
                    this.mFileUploadCallbackFirst = null;
                    return;
                }
                ValueCallback<Uri[]> valueCallback2 = this.mFileUploadCallbackSecond;
                if (valueCallback2 != null) {
                    valueCallback2.onReceiveValue(null);
                    this.mFileUploadCallbackSecond = null;
                }
            } else if (intent != null) {
                ValueCallback<Uri> valueCallback3 = this.mFileUploadCallbackFirst;
                if (valueCallback3 != null) {
                    valueCallback3.onReceiveValue(intent.getData());
                    this.mFileUploadCallbackFirst = null;
                } else if (this.mFileUploadCallbackSecond != null) {
                    try {
                    } catch (Exception unused) {
                        uriArr = null;
                    }
                    if (intent.getDataString() != null) {
                        uriArr2 = new Uri[]{Uri.parse(intent.getDataString())};
                    } else if (Build.VERSION.SDK_INT < 16 || intent.getClipData() == null) {
                        uriArr2 = null;
                    } else {
                        int itemCount = intent.getClipData().getItemCount();
                        uriArr = new Uri[itemCount];
                        for (i3 = 0; i3 < itemCount; i3++) {
                            try {
                                uriArr[i3] = intent.getClipData().getItemAt(i3).getUri();
                            } catch (Exception unused2) {
                            }
                        }
                        uriArr2 = uriArr;
                    }
                    this.mFileUploadCallbackSecond.onReceiveValue(uriArr2);
                    this.mFileUploadCallbackSecond = null;
                }
            }
        }
    }

    public boolean onBackPressed() {
        if (canGoBack()) {
            goBack();
            return false;
        }
        return true;
    }

    public void onDestroy() {
        try {
            ((ViewGroup) getParent()).removeView(this);
        } catch (Exception unused) {
        }
        try {
            removeAllViews();
        } catch (Exception unused2) {
        }
        destroy();
    }

    @Override
    @SuppressLint({"NewApi"})
    public void onPause() {
        pauseTimers();
        if (Build.VERSION.SDK_INT >= 11) {
            super.onPause();
        }
    }

    @Override
    @SuppressLint({"NewApi"})
    public void onResume() {
        if (Build.VERSION.SDK_INT >= 11) {
            super.onResume();
        }
        resumeTimers();
    }

    @SuppressLint({"NewApi"})
    public void openFileInput(ValueCallback<Uri> valueCallback, ValueCallback<Uri[]> valueCallback2, boolean z) {
        ValueCallback<Uri> valueCallback3 = this.mFileUploadCallbackFirst;
        if (valueCallback3 != null) {
            valueCallback3.onReceiveValue(null);
        }
        this.mFileUploadCallbackFirst = valueCallback;
        ValueCallback<Uri[]> valueCallback4 = this.mFileUploadCallbackSecond;
        if (valueCallback4 != null) {
            valueCallback4.onReceiveValue(null);
        }
        this.mFileUploadCallbackSecond = valueCallback2;
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        if (z && Build.VERSION.SDK_INT >= 18) {
            intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        }
        intent.setType(this.mUploadableFileTypes);
        WeakReference<Fragment> weakReference = this.mFragment;
        if (weakReference != null && weakReference.get() != null && Build.VERSION.SDK_INT >= 11) {
            this.mFragment.get().startActivityForResult(Intent.createChooser(intent, getFileUploadPromptLabel()), this.mRequestCodeFilePicker);
            return;
        }
        WeakReference<Activity> weakReference2 = this.mActivity;
        if (weakReference2 == null || weakReference2.get() == null) {
            return;
        }
        this.mActivity.get().startActivityForResult(Intent.createChooser(intent, getFileUploadPromptLabel()), this.mRequestCodeFilePicker);
    }


    public void setCookiesEnabled(boolean z) {
        CookieManager.getInstance().setAcceptCookie(z);
    }


    @SuppressLint({"NewApi"})
    public void setGeolocationDatabasePath() {
        Activity activity;
        WeakReference<Fragment> weakReference = this.mFragment;
        if (weakReference != null && weakReference.get() != null && Build.VERSION.SDK_INT >= 11 && this.mFragment.get().getActivity() != null) {
            activity = this.mFragment.get().getActivity();
        } else {
            WeakReference<Activity> weakReference2 = this.mActivity;
            if (weakReference2 == null || weakReference2.get() == null) {
                return;
            }
            activity = this.mActivity.get();
        }
        getSettings().setGeolocationDatabasePath(activity.getFilesDir().getPath());
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public void setGeolocationEnabled(boolean z) {
        if (z) {
            getSettings().setJavaScriptEnabled(true);
            getSettings().setGeolocationEnabled(true);
            setGeolocationDatabasePath();
        }
        this.mGeolocationEnabled = z;
    }

    public void setLastError() {
        this.mLastError = System.currentTimeMillis();
    }

    public void setListener(Activity activity, Listener listener) {
        setListener(activity, listener, REQUEST_CODE_FILE_PICKER);
    }

    public void setMixedContentAllowed(boolean z) {
        setMixedContentAllowed(getSettings(), z);
    }

    @SuppressLint({"NewApi"})
    public void setThirdPartyCookiesEnabled(boolean z) {
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, z);
        }
    }


    @Override
    public void setWebChromeClient(WebChromeClient webChromeClient) {
        this.mCustomWebChromeClient = webChromeClient;
    }

    @Override
    public void setWebViewClient(WebViewClient webViewClient) {
        this.mCustomWebViewClient = webViewClient;
    }

    public void setListener(Activity activity, Listener listener, int i) {
        if (activity != null) {
            this.mActivity = new WeakReference<>(activity);
        } else {
            this.mActivity = null;
        }
        setListener(listener, i);
    }

    @SuppressLint({"NewApi"})
    public void setMixedContentAllowed(WebSettings webSettings, boolean z) {
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(!z ? 1 : 0);
        }
    }

    public void loadHtml(String str, String str2, String str3) {
        loadHtml(str, str2, str3, "utf-8");
    }

    public void loadHtml(String str, String str2, String str3, String str4) {
        loadDataWithBaseURL(str2, str, "text/html", str4, str3);
    }

    @Override
    public void loadUrl(String str) {
        if (this.mHttpHeaders.size() > 0) {
            super.loadUrl(str, this.mHttpHeaders);
        } else {
            super.loadUrl(str);
        }
    }

    public void setListener(Fragment fragment, Listener listener, int i) {
        if (fragment != null) {
            this.mFragment = new WeakReference<>(fragment);
        } else {
            this.mFragment = null;
        }
        setListener(listener, i);
    }

    public AdvancedWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPermittedHostnames = new LinkedList();
        this.mRequestCodeFilePicker = REQUEST_CODE_FILE_PICKER;
        this.mUploadableFileTypes = "*/*";
        this.mHttpHeaders = new HashMap();
        init(context);
    }


    public void setListener(Listener listener, int i) {
        this.mListener = listener;
        this.mRequestCodeFilePicker = i;
    }

    public AdvancedWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPermittedHostnames = new LinkedList();
        this.mRequestCodeFilePicker = REQUEST_CODE_FILE_PICKER;
        this.mUploadableFileTypes = "*/*";
        this.mHttpHeaders = new HashMap();
        init(context);
    }
}
