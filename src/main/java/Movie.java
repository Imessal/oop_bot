class Movie{
    private String name;
    private String link;
    private String rating;
    private String year;
    private int id;

    Movie(String name, int id, String link){
        this.name = name;
        this.id = id;
        this.link = link;
    }

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

    String getName(){
        return name;
    }
    String getLink(){
        return link;
    }
    int getId(){
        return id;
    }
    void setRating(String rating){
        if (this.rating == null){
            this.rating = rating;
        }
    }
    String getRating(){return rating;}
    void setYear(String year){
        if (this.year == null){
            this.year = year;
        }
    }
    String getYear(){return year;}
}
