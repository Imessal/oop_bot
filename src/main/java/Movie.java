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

    public String getName(){
        return name;
    }
    public String getLink(){
        return link;
    }
    public String getRating(){
        return rating;
    }
    public String getYear(){
        return year;
    }
    public int getId(){
        return id;
    }
    public void setRating(String rating){
        if (this.rating==null){
            this.rating = rating;
        }
    }
    public void setYear(String year){
        if (this.rating==null){
            this.rating = year;
        }
    }
}
