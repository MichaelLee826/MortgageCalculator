package michaellee.mortgagecalculator.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by MichaelLee826 on 2016-08-19-0019.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private String WXAppID = "wxe1309186360d6399";
    //正式版：wxe1309186360d6399
    //测试版：wx0cccc66f5792e9d0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, WXAppID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        //System.out.println("resp.errCode:" + baseResp.errCode + ",resp.errStr:" + baseResp.errStr);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                System.out.println("分享成功");
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                System.out.println("分享取消");
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                System.out.println("分享拒绝");
                break;
        }

        finish();
    }
}