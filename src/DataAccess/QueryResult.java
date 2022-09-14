package DataAccess;

public class QueryResult {
	public Boolean Successful() {
		return this.ErrorMessage==null || this.ErrorMessage.length()==0;
	};
	public String ErrorMessage;
	public int Id;
}
