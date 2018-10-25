class Movie{
    String name;
    String rating;
    String link;
    //private String annotation;

    String toStringMovie(){
        return "[" + name + "]"+"(" + link + ")" + "\nРейтинг - " + rating;
        //return name + "\nРейтинг - " + rating + "\n" + link;
        //return name;
    }

    /*
    void addAnnotation(){
        annotation = KinopoiskParser.getAnnotation(link);
    }
    */
}
