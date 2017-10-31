package pw.androidthanatos.router;

/**
 * RouterIntecepter
 *
 * @author liuxiongfei
 *         2017/10/29
 */

public interface RouterIntecepter {

    Request chain(Request request);
    void onLost(String msg);
    void onSuccess();
}
