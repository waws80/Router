package pw.androidthanatos.router;

/**
 * Response
 *  Router 响应体
 * @author liuxiongfei
 *         2017/10/30
 */

public final class Response {

    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_LOST = 404;
    public static final int RESPONSE_CANCEL = 403;
    public int code;
    private String msg;
    private String mPath;
    private String mAction;

    Response(int code, String path, String action, String msg){
        if (code == RESPONSE_OK){
            this.code = RESPONSE_OK;
        }else if (code == RESPONSE_CANCEL){
            this.code = RESPONSE_CANCEL;
        }else {
            this.code = RESPONSE_LOST;
        }
        this.mPath = path;
        this.mAction = action;
        this.msg = msg;
    }

    public String getPath() {
        return mPath;
    }

    public String getAction() {
        return mAction;
    }

    public String getMsg(){
        return msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", path='" + mPath + '\'' +
                ", action='" + mAction + '\'' +
                '}';
    }

}
