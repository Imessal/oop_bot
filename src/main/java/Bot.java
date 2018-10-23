import java.util.Scanner;

public class Bot {
    private static Kinoman context = null;

    public static void main(String[] args) {
        startBot();
    }

    private static void startBot() {
        System.out.println("Привет, я Киноман!");
        System.out.println("Могу помочь с выбором фильма, сериала и т.д.");
        System.out.println("Для справки введи \"помощь\"");
        work();
    }

    private static void work() {
        FiniteStateMachine fsm = new FiniteStateMachine();
        Scanner sc = new Scanner(System.in);
        FiniteStateMachine.State state;
        while (true) {
            String request = sc.nextLine().toLowerCase();
            state = fsm.workWithRequest(request);
            switch (state) {
                case Ready:
                    break;
                case Requesting:
                    System.out.println("Минутку...");
                    context = Kinoman.createKinomanOnRequest(request);
                    context.showNext();
                    break;
                case ShowingSimilar:
                    context.showSimilar();
                    break;
                case ShowingNext:
                    context.showNext();
                    break;
                case ShowingHelp:
                    Kinoman.printHelp();
                    break;
                case ShowingValidRequests:
                    Kinoman.printValidRequest();
                    break;
                default:
                    System.out.println("Ну, я ничего не понял(");
                    System.out.println("Может ты что-то не правильно написал? Псмотреть как надо - \"помощь\" ");
            }
        }
    }
}
