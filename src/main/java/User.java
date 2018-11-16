class User {
    String first_name;
    String username;
    private int id;
    Kinoman kinoman;
    FiniteStateMachine FSM;

    User(int id){
        this.id = id;
    }

    int getId(){
        return id;
    }
}