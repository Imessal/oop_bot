import java.util.*;


class FiniteStateMachine{

    private static Kinoman context = null;

    enum State {
        Ready("покажи", "помощь", "возможные запросы"),
        Requesting("похожие", "следующий", "покажи","помощь", "возможные запросы"),
        ShowingSimilar("следующий", "покажи","помощь", "возможные запросы"),
        ShowingNext("следующий", "покажи", "похожие","помощь", "возможные запросы"),
        Error("следующий", "покажи","помощь", "возможные запросы"),
        LaunchError("покажи","помощь", "возможные запросы"),
        ShowingHelp("покажи", "помощь", "возможные запросы"),
        ShowingValidRequests("покажи", "помощь", "возможене запросы");

        State(String... in) {
            inputs = Arrays.asList(in);
        }

        State nextState(String input, State current) {
            if (inputs.contains(findRequest(input))) {
                return map.getOrDefault(findRequest(input), current);
            }
            System.out.println("Ошибка ввода: невернвый аргумент");
            if (current.equals(State.Ready) ||current.equals(State.LaunchError))
                return State.LaunchError;
            else
            return State.Error;
        }

        final List<String> inputs;
        final static Map<String, State> map = new HashMap<>();

        static {
            map.put("покажи", State.Requesting);
            map.put("похожие", State.ShowingSimilar);
            map.put("следующий", State.ShowingNext);
            map.put("помощь", State.ShowingHelp);
            map.put("возможные запросы", State.ShowingValidRequests);
        }
    }

    State state = State.Ready;

    void workWithRequest(String input) {
        state = state.nextState(input, state);
        switch (state) {
            case Ready:
                System.out.println("Готов сделать запрос");
                break;
            case Requesting:
                System.out.println("В данный момент реквестирую фильм");
                context = Kinoman.createKinomanOnRequest(input);
                context.showNext();
                break;
            case ShowingSimilar:
                System.out.println("Показываю похожие...");
                context.showSimilar();
                break;
            case ShowingNext:
                System.out.println("Показываю следующие");
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
    static String findRequest(String input){
        String request = "покажи";
        if(input.contains(request)){
            return request;
        }
        else return input;
    }
}