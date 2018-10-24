class Movie{
    String name;
    String rating;
    String link;
    //private String annotation;

    String toStringMovie(){
        return "[" + name + "]"+"(" + link + ")" + "\nРейтинг - " + rating; //+ "Аннотация:\n" + annotation;
        //return name;
    }

    /*
    void addAnnotation(){
        annotation = KinopoiskParser.getAnnotation(link);
    }
    */
}
