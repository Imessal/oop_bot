import java.io.IOException;
import java.util.logging.*;

class LoggerBot{
    static Logger getBotLoggerInFils(String name){
        Logger log = Logger.getLogger(name);
        try {
            System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL] [%4$s] [%2$s] %5$s%6$s%n");
            log.setUseParentHandlers(false);

            FileHandler chatFH = new FileHandler("logs/chats/log-%g.txt", 1000000, 5, true);
            chatFH.setFormatter(new SimpleFormatter());
            chatFH.setLevel(Level.INFO);
            log.addHandler(chatFH);

            FileHandler infoFH = new FileHandler("logs/all/log-%g.txt", 1000000, 5, true);
            infoFH.setFormatter(new SimpleFormatter());
            infoFH.setLevel(Level.CONFIG);
            log.addHandler(infoFH);

            log.setLevel(Level.CONFIG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return log;
    }

    static Logger getBotLoggerInSystemOut(String name){
        Logger log = Logger.getLogger(name);
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL] [%4$s] [%2$s] %5$s%6$s%n");
        log.setUseParentHandlers(false);
        StreamHandler fh = new StreamHandler(System.out, new SimpleFormatter());
        fh.setFormatter(new SimpleFormatter());
        fh.setLevel(Level.CONFIG);
        log.addHandler(fh);
        log.setLevel(Level.CONFIG);
        return log;
    }
}
