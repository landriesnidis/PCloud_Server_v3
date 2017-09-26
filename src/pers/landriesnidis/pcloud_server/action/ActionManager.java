package pers.landriesnidis.pcloud_server.action;

import java.util.HashMap;

import pers.landriesnidis.pcloud_server.action.exception.ActionUnregisteredException;

public class ActionManager {

    private static final ActionManager manager = new ActionManager();  
    //静态工厂方法   
    public static ActionManager getInstance() {  
        return manager;  
    }  
    
    //请求类型
    private HashMap<String, HashMap<String, String>> hmRequestType = new HashMap<String, HashMap<String,String>>();
    
    //动作表
    private HashMap<String, String> hmActions_GET 	= new HashMap<String, String>();
    private HashMap<String, String> hmActions_POST 	= new HashMap<String, String>();
    
    //构造函数
    private ActionManager() {
    	hmRequestType.put("GET", hmActions_GET);
    	hmRequestType.put("POST", hmActions_POST);
    }  

    /**
     * 通过名称获取处理实例
     * @param requestType	请求类型（GET/POST）
     * @param actionName	Action名称
     * @return
     * @throws ActionUnregisteredException actionName所对应的Action未被注册的异常
     */
	public BaseServletAction createActionByName(String requestType,
			String actionName) throws ActionUnregisteredException {
		// 获取actionName对应的类名
		String className = hmRequestType.get(requestType).get(actionName);
		// 判断对应的类是否已注册
		if (className == null) {
			throw new ActionUnregisteredException(actionName);
		}
		
		BaseServletAction action = null;
		try {
			//动态实例化Action处理对象
			action = (BaseServletAction) Class.forName(className).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return action;
	}

    /**
     * 添加Action处理对象
     * @param requestType	请求类型（GET/POST）
     * @param class1	Action处理类
     */
    public void registerAction(String requestType,Class<?> class1){
    	try{
    		String actionClassName = class1.getName();
    		BaseServletAction action = (BaseServletAction) Class.forName(actionClassName).newInstance();
    		String actionName = new String(action.getActionName());
    		action = null;
    		hmRequestType.get(requestType).put(actionName, actionClassName);
    		System.out.println("ActionManager : registered Action -> " + actionName + " ,RequestType -> " + requestType);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
}
