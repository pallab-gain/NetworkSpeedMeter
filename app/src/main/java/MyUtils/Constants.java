package MyUtils;

/**
 * Created by xerxes on 1/20/15.
 * "I have not failed, I have just found 10000 ways that won't work."
 */
public class Constants {
    public interface ACTION{
        public static String MAIN_ACTION = "xerxes.networkspeedmonitor.action.main";
        public static String BOOT_RECEIVE = "android.intent.action.BOOT_COMPLETED";
        public static String STARTFOREGROUND_ACTION = "xerxes.networkspeedmonitor.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "xerxes.networkspeedmonitor.action.stopforeground";
        public static String STOP_REAP = "stop_repeat";
        public static String START_REPEAT = "start_repeat";
        public static String UPDATE_FONT_SIZE = "on_font_size_update";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 10234;
    }
}
