class Movie {
    String name;
    String rating;
    String link;
    String annotation;

    public String toStringMovie(){
        return name + "\nРейтинг - " + rating +"\n"+ link + "\n" + "Аннотация:\n" + annotation;
    }

    void addAnnotation(){
        annotation = KinopoiskParser.getAnnotation(link);
    }
}
