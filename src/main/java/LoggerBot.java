import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class LoggerBot{
    static Logger getBotLogger(String name){
        Logger log = Logger.getLogger(name);
        try {
            System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL] [%4$s] [%2$s] %5$s%6$s%n");
            log.setUseParentHandlers(false);

            FileHandler chatFH = new FileHandler("logs/chats/log-%g.txt", 50000, 5, true);
            chatFH.setFormatter(new SimpleFormatter());
            chatFH.setLevel(Level.INFO);
            log.addHandler(chatFH);

            FileHandler infoFH = new FileHandler("logs/all/log-%g.txt", 50000, 5, true);
            infoFH.setFormatter(new SimpleFormatter());
            infoFH.setLevel(Level.CONFIG);
            log.addHandler(infoFH);

            log.setLevel(Level.CONFIG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return log;
    }
}
