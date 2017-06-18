/**
 * Created by fnnek on 29.05.17.
 */
public class Course
{
    private Lesson lesson;

    public Lesson getLesson ()
    {
        return lesson;
    }

    public void setLesson (Lesson lesson)
    {
        this.lesson = lesson;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lesson = "+lesson+"]";
    }
}