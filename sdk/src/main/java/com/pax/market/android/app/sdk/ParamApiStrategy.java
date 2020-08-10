package com.pax.market.android.app.sdk;

import android.content.Context;

import com.pax.market.android.app.sdk.util.PreferencesUtils;
import com.pax.market.api.sdk.java.api.param.ParamApi;
import com.pax.market.api.sdk.java.base.constant.ResultCode;
import com.pax.market.api.sdk.java.base.dto.DownloadResultObject;
import com.pax.market.api.sdk.java.base.dto.InnerDownloadResultObject;
import com.pax.market.api.sdk.java.base.dto.LastFailObject;

public class ParamApiStrategy extends ParamApi{
    private static final String LAST_DOWNLOAD = "lastDownload";
    private Context context;

    public ParamApiStrategy(Context context, String baseUrl, String appKey, String appSecret, String terminalSN) {
        super(baseUrl, appKey, appSecret, terminalSN);
        this.context = context;
    }


    public DownloadResultObject downloadParamToPath(String packageName, int versionCode, String saveFilePath) {
        LastFailObject failTask = PreferencesUtils.getObject(context, LAST_DOWNLOAD, LastFailObject.class);
        InnerDownloadResultObject downloadResultObject = super.downloadParamToPath(packageName, versionCode, saveFilePath, failTask);
        if (downloadResultObject.getBusinessCode() == ResultCode.SDK_DOWNLOAD_IOEXCEPTION.getCode()) {
            // save one download IOException record
            PreferencesUtils.putObject(context, LAST_DOWNLOAD, downloadResultObject.getLastFailObject());
        } else {
            PreferencesUtils.remove(context, LAST_DOWNLOAD);
        }

        DownloadResultObject resultObject = new DownloadResultObject();
        resultObject.setMessage(downloadResultObject.getMessage());
        resultObject.setBusinessCode(downloadResultObject.getBusinessCode());
        resultObject.setParamSavePath(saveFilePath);
        return resultObject;
    }

}
