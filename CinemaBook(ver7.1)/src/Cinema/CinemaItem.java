package Cinema;
import UserItem.DefaultUserItem;
public class CinemaItem extends DefaultUserItem {
	public static final String TITLE = "title";
	public static final String DIRECTOR = "director";
	public static final String ACTOR = "actor";
	public static final String PLAYWIGHT = "playwright";
	public static final String REVIEW = "review";
	public static final String GENRE = "genre";
	public static final String COMMENT = "comment";
	public static final String DATE = "date";
	public static final String IMAGE = "image";
	public static final String[] COLUMNNAME_ARRAY = new String[]{TITLE,DIRECTOR,ACTOR,PLAYWIGHT,REVIEW,GENRE,COMMENT,DATE,IMAGE};
	public CinemaItem(String[] columnNamesArray) {
		super(columnNamesArray);
	}
}