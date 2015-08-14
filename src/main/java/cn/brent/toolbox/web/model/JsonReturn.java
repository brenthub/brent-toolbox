package cn.brent.toolbox.web.model;

public class JsonReturn {

	boolean ok;
	String errCode;
	String msg;
	Object data;

	public boolean isOk() {
		return ok;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getMsg() {
		return msg;
	}

	public Object getData() {
		return data;
	}

	public JsonReturn setOk(boolean ok) {
		this.ok = ok;
		return this;
	}

	public JsonReturn setErrCode(String errCode) {
		this.errCode = errCode;
		return this;
	}

	public JsonReturn setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public JsonReturn setData(Object data) {
		this.data = data;
		return this;
	}

	public static JsonReturn fail(String errCode, String msg) {
		JsonReturn re = new JsonReturn();
		re.setOk(false);
		re.setErrCode(errCode);
		re.setMsg(msg);
		return re;
	}

	public static JsonReturn fail(String msg) {
		JsonReturn re = new JsonReturn();
		re.setOk(false);
		re.setMsg(msg);
		return re;
	}

	public static JsonReturn ok(Object data) {
		JsonReturn re = new JsonReturn();
		re.setOk(true);
		re.setData(data);
		return re;
	}

	public static JsonReturn ok() {
		JsonReturn re = new JsonReturn();
		re.setOk(true);
		return re;
	}

	public static JsonReturn fail() {
		JsonReturn re = new JsonReturn();
		re.setOk(false);
		return re;
	}

	public static JsonReturn expired() {
		JsonReturn re = new JsonReturn();
		re.setOk(false);
		re.setErrCode("100");
		return re;
	}
}
