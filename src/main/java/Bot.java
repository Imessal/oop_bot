import java.util.ArrayList;
import java.util.Scanner;


public class Bot
{
    private static Kinoman context = null;

    public static void main(String[] args)	{
        startBot();
    }

    private static void startBot() {
        System.out.println("Привет, я Киноман!");
        System.out.println("Могу порекомендовать фильм или сериал к просмотру.");
        System.out.println("Для справки введи \"помощь\"");
        work();
    }

    private static void work() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String request = scanner.nextLine().toLowerCase().trim();
            if (request.equals("помощь")) {
                printHelp();
                continue;
            }
            if (request.equals("возможные запросы")) {
                printValidRequest();
                continue;
            }
            if (request.equals("следующий")) {
                if (context != null) {
                    context.showNext();
                } else {
                    System.out.println("Сначала сделай запрос.");
                    System.out.println("Узнать как - \"помощь\"");
                }
                continue;
            }
            if (request.equals("похожие")) {
                if (context != null) {
                    context.showSimilar();
                } else {
                    System.out.println("Сначала сделай запрос.");
                    System.out.println("Узнать как - \"помощь\"");
                }
                continue;
            }
            if (request.startsWith("покажи")) {
                Kinoman km = createKinomanOnRequest(request);
                context = km;
                km.showNext();
                continue;
            }
            System.out.println("Ну, я ничего не понял(");
            System.out.println("Может ты что-то не правильно написал? Псмотреть как надо - \"помощь\" ");

        }
    }

    static Kinoman createKinomanOnRequest(String request){
        String typeOfMovie = "фильм";
        String sortingType = "годам";
        ArrayList<String> genre = new ArrayList<>();
        String[] words = request.split(" ");
        for (String word : words){
            if (LinkBuilder.getTypeOfMovieDict().containsKey(word)){
                typeOfMovie = word;
                break;
            }
        }
        for (String word : words){
            if (LinkBuilder.getSortingTypeDict().containsKey(word) || word.startsWith("рандомный")){
                sortingType = word;
                break;
            }
        }
        for (String word : words){
            if (LinkBuilder.getGenreDict().containsKey(word)){
                genre.add(word);
            }
        }
        return new Kinoman(typeOfMovie, sortingType, genre.toArray(new String[0]));
    }

    private static void printHelp(){
        System.out.println("Просто напиши - \"Покажи\" + что хочешь посмотреть. Так же можешь указать жанр и способ сортировки");
        System.out.println("\nПример запроса:");
        System.out.println("Покажи мне мультфильм, и выводи их по годам, пожалуйста\n");
        System.out.println("После вывода фильма, можно попросить показать \"похожие\"");
        System.out.println("Чтобы продолжить вывод, напиши \"следующий\" или введи новый запрос");
        System.out.println("Чтобы узнать знакомые мне жанры, сортировки вывода и т.п, напиши \"возможные запросы\"");
    }

    private static void printValidRequest(){
        System.out.print("Могу найти: ");
        StringBuilder strBuild = new StringBuilder();
        for (String movie: LinkBuilder.getTypeOfMovieDict().keySet()){
            strBuild.append(movie);
            strBuild.append(", ");
        }
        System.out.println(strBuild.deleteCharAt(strBuild.length()-2).toString());

        System.out.print("Выводить могу по: ");
        strBuild.delete(0, strBuild.length());
        for (String sortingType : LinkBuilder.getSortingTypeDict().keySet()){
            strBuild.append(sortingType);
            strBuild.append(", ");
        }
        System.out.println(strBuild.deleteCharAt(strBuild.length()-2).toString());

        System.out.print("Знаю такие жанры, как: ");
        strBuild.delete(0, strBuild.length());
        for (String genre : LinkBuilder.getGenreDict().keySet()){
            strBuild.append(genre);
            strBuild.append(", ");
        }
        System.out.println(strBuild.deleteCharAt(strBuild.length()-2).toString());
    }
}
