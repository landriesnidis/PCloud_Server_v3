package org.nisita.mysql;

import java.sql.SQLException;

public class QueryException extends SQLException {

	private static final long serialVersionUID = 3089669221677784478L;
	
	private String message;

	public QueryException() {
	}

	public QueryException(String message) {
		super(message);
		this.message = message;
	}

	public QueryException(Throwable cause) {
		super(cause);
	}

	public QueryException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public void printStackTrace() {
		System.out.println("未查到信息:" + message);
	}

}
