package pers.landriesnidis.pcloud_server.action.exception;

public class ActionUnregisteredException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private String actionName;
	
	public ActionUnregisteredException(String actionName) {
		this.actionName = actionName;
	}
	
	@Override
	public String toString() {
		return "This Action is not registered in the ActionManager : " + actionName;
	}
}
