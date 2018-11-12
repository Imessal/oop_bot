class Movie{
    String name;
    String link;
    String rating;
    String year;
    int id;

    String toStringMovie(){
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(name).append("]").append("(").append(link).append(")");
        if (year!=null){
            sb.append("\nГод - ");
            sb.append(year);
        }
        if (rating!=null){
            sb.append("\nРейтинг - ");
            sb.append(rating);
        }
        return sb.toString();
    }

    String getAnnotation(){
        return KinopoiskParser.getAnnotation(link);
    }
}
