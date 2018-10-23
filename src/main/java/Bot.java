import java.util.Scanner;

public class Bot {
    private static Kinoman context = null;

    public static void main(String[] args) {
        startBot();
    }

    private static void startBot() {
        System.out.println("Привет, я Киноман!");
        System.out.println("Могу порекомендовать фильм или сериал к просмотру.");
        System.out.println("Для справки введи \"помощь\"");
        work();
    }

    private static void work() {
        //Scanner scanner = new Scanner(System.in);
        FiniteStateMachine fsm = new FiniteStateMachine();
        Scanner sc = new Scanner(System.in);
        while (true) {
            String input = sc.nextLine();
            fsm.workWithRequest(input);
        }
    }
}
