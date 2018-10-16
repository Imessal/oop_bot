import java.net.URL;

class Movie {
    String name;
    String rating;
    URL url;
    String annotation;

    public String toString(){
        return name + "\nРейтинг - " + rating +"\n"+ url + "\n" + "Аннотация:\n" + annotation;
    }

    void addAnnotation(){
        annotation = KinopoiskParser.getAnnotation(url);
    }
}
