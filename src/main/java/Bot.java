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
        //Scanner scanner = new Scanner(System.in);
        FiniteStateMachine fsm = new FiniteStateMachine();
        fsm.workWithRequest();
            System.out.println("Ну, я ничего не понял(");
            System.out.println("Может ты что-то не правильно написал? Псмотреть как надо - \"помощь\" ");

        }
    }

