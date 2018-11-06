class Movie{
    String name;
    String rating;
    String link;

    String toStringMovie(){
        return "[" + name + "]"+"(" + link + ") " + "\nРейтинг - " + rating;
        //return name + "\nРейтинг - " + rating + "\n" + link;
        //return name;
    }


    String getAnnotation(){
        return KinopoiskParser.getAnnotation(link);
    }

}
