import java.lang.reflect.Method;
import java.util.*;


public class FiniteStateMachine{

    private static Kinoman context = null;

    private enum State {
        Ready("покажи"),
        Requesting("похожие", "следующий", "покажи"),
        ShowingSimilar("следующий", "покажи"),
        ShowingNext("следующий", "покажи", "похожие");

        State(String... in) {
            inputs = Arrays.asList(in);
        }

        State nextState(String input, State current) {
            if (inputs.contains(input)) {
                return map.getOrDefault(input, current);
            }
            return current;
        }

        final List<String> inputs;
        final static Map<String, State> map = new HashMap<>();

        static {
            map.put("покажи", State.Requesting);
            map.put("похожие", State.ShowingSimilar);
            map.put("следующий", State.ShowingNext);
        }
    }

    public void workWithRequest() {
        Scanner sc = new Scanner(System.in);
        State state = State.Ready;
        while (true) {
            System.out.println(state.inputs);
            System.out.print("> ");
            String input = sc.nextLine();
            state = state.nextState(input, state);
            switch (state){
                case Ready:
                    System.out.println("Готов сделать запрос");
                    break;
                case Requesting:
//                    System.out.println("В данный момент реквестирую фильм");
                    Kinoman km = Kinoman.createKinomanOnRequest(input);
                    context = km;
                    context.showNext();
                    break;
                case ShowingSimilar:
//                    System.out.println("Показываю похожие...");
                    context.showSimilar();
                    break;
                case ShowingNext:
//                    System.out.println("Показываю следующие");
                    context.showNext();
                    break;
                default:
//                    System.out.println("Ошибка ввода");

            }
        }
    }
}