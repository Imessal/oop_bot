public class Main {
    public static void main(String args[]){
        DatabaseController controller = new DatabaseController();
        controller.createNewTable();
        controller.addUser(1234);
        controller.getUserInfo(1234);
        controller.updateUserInfo(1234, DatabaseController.UserInfo.last_class_num_request, 6);
        controller.removeUser(1234);
    }
}
