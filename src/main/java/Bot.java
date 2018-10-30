import java.util.Scanner;

public class Bot{
    public static void main(String[] args) {
        TelegramBot.Start(System.getenv("TOKEN"));
        workOnConsole();
    }

    private static void workOnConsole(){
        User user = new User();
        user.FSM = new FiniteStateMachine();
        user.kinoman = new Kinoman("фильм", "по годам");

        Scanner sc = new Scanner(System.in);
        while (true){
            String request = sc.nextLine().toLowerCase();
            request = SpellChecker.check(request);
            String answer = Elector.getAnswer(user, request).answer;
            System.out.println(answer);
        }
    }
}
