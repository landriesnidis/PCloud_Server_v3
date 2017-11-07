package pers.landriesnidis.pcloud_server.action.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 快速构建Json格式的Http请求返回信息工具类
 * @author 李贵杰
 * @version 2016年12月28日19:14:08
 */
public class ActionJson {
	private String action;
	private String flag;
	private String content;
	private String message;
	
	public ActionJson(){}
	public ActionJson(String action){
		this.action = action;
	}
	
	public ActionJson setAction(String action) {
		this.action = action;
		return this;
	}
	public ActionJson setFlag(String flag) {
		this.flag = flag;
		return this;
	}
	public ActionJson setFlag(int flag) {
		this.flag = (flag==0)?"false":"true";
		return this;
	}
	public ActionJson setFlag(boolean flag) {
		this.flag = (!flag)?"false":"true";
		return this;
	}
	public boolean getFlag() {
		return flag.equals("ture");
	}
	public ActionJson setContent(String content) {
		this.content = content;
		return this;
	}
	public ActionJson setMessage(String message) {
		this.message = message;
		return this;
	}
	public JSONObject toJsonObject(){
		JSONObject json = new JSONObject();
		try {
			json.put("action", action);
			json.put("flag", flag);
			json.put("content", content);
			json.put("message", message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	@Override
	public String toString() {
		return toJsonObject().toString();
	}
	
}
