/**
 * Created by fnnek on 29.05.17.
 */
public class Word
{
    private String english;

    private String polish;

    public String getEnglish ()
    {
        return english;
    }

    public void setEnglish (String english)
    {
        this.english = english;
    }

    public String getPolish ()
    {
        return polish;
    }

    public void setPolish (String polish)
    {
        this.polish = polish;
    }

    @Override
    public String toString()
    {
        return "polski: "+polish+" | english :"+english;
    }
}