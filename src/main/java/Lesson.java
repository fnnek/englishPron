/**
 * Created by fnnek on 29.05.17.
 */
public class Lesson
{
    private String id;
    private String name;

    private Word[] word;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Word[] getWord ()
    {
        return word;
    }

    public void setWord (Word[] word)
    {
        this.word = word;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", word = "+word+"]";
    }
}
