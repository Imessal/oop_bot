class Movie{
    String name;
    String rating;
    String link;
    private String annotation;

    String toStringMovie(){
        return name + "\nРейтинг - " + rating +"\n"+ link + "\n" + "Аннотация:\n" + annotation;
    }

    void addAnnotation(){
        annotation = KinopoiskParser.getAnnotation(link);
    }
}
