public class User {
    String first_name;
    String username;
    private int id;
    private Long chatId;
    Kinoman kinoman;
    FiniteStateMachine FSM;

    User(int id, long chatId){
        this.id = id;
        this.chatId = chatId;
    }

    public int getId(){
        return id;
    }
    public long getChatId(){
        return chatId;
    }
}