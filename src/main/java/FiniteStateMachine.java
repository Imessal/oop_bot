import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FiniteStateMachine{

    enum State {
        Ready("/start", "покажи", "помощь", "возможные запросы"),
        SayHello("/start", "покажи", "помощь", "возможные запросы"),
        Requesting("/start", "похожие", "следующий", "покажи", "помощь", "возможные запросы", "аннотация", "игнорировать"),
        ShowingSimilar("/start", "следующий", "покажи","помощь", "возможные запросы"),
        ShowingNext("/start", "похожие", "следующий", "покажи", "помощь", "возможные запросы", "аннотация", "игнорировать"),
        ShowingAnnotation("/start", "похожие", "следующий", "покажи", "помощь", "возможные запросы"),
        Error("/start", "следующий", "покажи","помощь", "возможные запросы", "аннотация", "игнорировать"),
        LaunchError("/start", "покажи","помощь", "возможные запросы"),
        ShowingHelp("/start", "покажи", "помощь", "возможные запросы"),
        ShowingValidRequests("/start", "покажи", "помощь", "возможные запросы"),
        BanningCurrentRequest("/start", "покажи", "помощь", "возможные запросы", "следующий" );

        State(String... in) {
            inputs = Arrays.asList(in);
        }

        State nextState(String request, State current) {
            if (inputs.contains(findRequest(request))) {
                return map.getOrDefault(findRequest(request), current);
            }
            else if (current.equals(State.Ready) || current.equals(State.LaunchError)) {
                return State.LaunchError;
            } else {
                return State.Error;
            }
        }

        final List<String> inputs;
        final static Map<String, State> map = new HashMap<>();

        static {
            map.put("/start", State.SayHello);
            map.put("покажи", State.Requesting);
            map.put("аннотация", State.ShowingAnnotation);
            map.put("похожие", State.ShowingSimilar);
            map.put("следующий", State.ShowingNext);
            map.put("помощь", State.ShowingHelp);
            map.put("возможные запросы", State.ShowingValidRequests);
            map.put("игнорировать", State.BanningCurrentRequest);
        }
    }

    State state = State.Ready;

    FiniteStateMachine.State workWithRequest(String request) {
        state = state.nextState(request, state);
        return state;
    }

    static String findRequest(String request){
        if(request.startsWith("покажи")){
            return "покажи";
        }
        else return request;
    }
}