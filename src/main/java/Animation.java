import java.net.URL;

class Animation {
    String name;
    String rating;
    URL url;
    String annotation;

    void showInfo(){
        System.out.println("\n" + name);
        System.out.println("Рейтинг - " + rating);
        System.out.println(url + "\n");
        System.out.println("Описание:");
        System.out.println(annotation+ "\n");
    }
}
