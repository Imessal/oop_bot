import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

class WebsiteOpener {
    private static Logger log = Bot.log;

    static BufferedReader getWebsiteContent(String link) {
        URL url = stringToURL(link);
        assert url != null : "Сcылка равна null";
        try {
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true); //Вроде как разрешает дописать UserAgent and cookie к URL соединению
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/68.0.3440.106 YaBrowser/18.9.1.954 Yowser/2.5 Safari/537.36");
            conn.setRequestProperty("Cookie", "i=tYtnVke7iPVfy5TwPeQtLm0W/NVmTHtyyMwoq2I6t5XTTvLj6WfH" +
                    "3z5wW76YxO2JcCXCchgxn1y2zhvB6j1OVcOeWqA=; _ym_uid=1532285716574910676; mda=0; " +
                    "yandexuid=5405908851528993672; user-source=vk; _ym_d=1533117300; my_perpages=%5B%5D;" +
                    " lfiltr=all; blcrm=1; users_info[check_sh_bool]=none; show_rated=true; mobile=no; tc=449;" +
                    " noflash=false; refresh_yandexuid=5405908851528993672;" +
                    " _ym_wasSynced=%7B%22time%22%3A1539347369141%2C%22params%22%3A%7B%22eu%22%3A0%7D%2C%22bkParams%22%3A%7B%7D%7D; " +
                    "yp=1539433771.yu.5405908851528993672; cmtchd=MTUzOTM1Mjc3MzU1OA==; " +
                    "crookie=OQyVh8JRUOFE7TrjfEVPi2JphvUzbeFc5RCV36WmOTdfv1aW38MIdl8h7tpKtNeHtzcfUWE3bMwsOa78IDQyRxof7ms=; " +
                    "PHPSESSID=er6cq58tm5frdva08kfjng0837; user_country=ru; yandex_gid=54; uid=15119391; " +
                    "_csrf_csrf_token=0H06ONimvjhBBxQSpFteEEOSnMXDHDxcRDKhrrO2m9U; " +
                    "Session_id=3%3A1539419503.5.0.1539419503068%3ApPenBQ%3A12b.1%7C1110000015119391.-1.0%7C30%3A175513.289407.XXTeVY9FEJtdTd1LEYq5CnpRIcA; " +
                    "kpunk=1; desktop_session_key=d47bfbe3f84126a74eae11ef0c2dd0d9637788dd2a0e3f74d8bf3c588cbe793ce349" +
                    "9179293b38dfc7bc4bb31190e7bd1ef816536457a768ee5bef9a12093075b971bb54845bf4ddb49a081b45e25f61c0fc2" +
                    "8f29ec67dac96f70a24e997ac6d; desktop_session_key.sig=9XlYcYXbQ6z_dZCiX0y9iRnW3-w; _ym_isad=1;" +
                    " SLG_wptGlobTipTmp=1; SLG_GWPT_Show_Hide_tmp=1; disable_alert_feature=211%2C213%2C212; kdetect=1;" +
                    " rheftjdd=rheftjddVal");
            conn.connect();
            return new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "windows-1251"));
        } catch (IOException e) {
            log.config("Не получилось открыть страницу - " + url.toString());
        }
        return null;
    }

    private static URL stringToURL(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
