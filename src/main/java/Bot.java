import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Bot
{
    private static String pathToCommands = String.format("src%smain%1$sresources%1$sCommands.txt", File.separator);
    private static final Map<String, Method> commands = getDict();

    public static void main(String[] args)	{
        startBot();
    }

    private static Map<String, Method> getDict(){
        Map<String, Method> dict = new HashMap<>();
        try {
            dict.put("команды", Bot.class.getMethod("printCommands"));
            dict.put("что посмотреть", Bot.class.getMethod("adviseFilm"));
            dict.put("чп", Bot.class.getMethod("adviseFilm"));
        }
        catch (NoSuchMethodException e){System.out.println(e.getLocalizedMessage());}
        return dict;
    }

    private static void startBot() {
        System.out.println("Привет, я Ботик!");
        System.out.println("Чтобы узнать возможные команды, введите \"Команды\"");

        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            String request = scanner.nextLine().toLowerCase().trim();
            if (commands.containsKey(request)) {
                try {commands.get(request).invoke(Bot.class);}
                catch (IllegalAccessException e) {System.out.println(e.getLocalizedMessage());}
                catch (InvocationTargetException k) {System.out.println(k.getMessage());}
            }
            else {
                System.out.println("Я ни чего не понял, давай еще раз.");
                System.out.println("Чтобы узнать возможные команды, введи \"Команды\"");
            }
        }
    }

    public static void printCommands(){
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(pathToCommands), StandardCharsets.UTF_8))){
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void adviseFilm(){
        System.out.println("Что хочешь посмотреть фильм или сериал?");
        Scanner sc = new Scanner(System.in);
        String typeOfAnim = sc.nextLine().toLowerCase().trim();
        System.out.println("Выводит по рейтингу или рандомно?");
        String sortingType = sc.nextLine().toLowerCase().trim();


        Kinoman km = new Kinoman(typeOfAnim, sortingType);
        lab:
        while (true){
            km.showNext();
            System.out.println("\"Следующий\" или \"назад\"?");
            String answer = sc.nextLine().toLowerCase().trim();
            switch (answer) {
                case "следующий":
                    break;
                case "назад":
                    break lab;
                default:
                    System.out.println("Что-то?");
                    break;
            }
        }

        System.out.println("Чем еще могу помочь?");
    }
}
