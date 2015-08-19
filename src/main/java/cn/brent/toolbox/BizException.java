package cn.brent.toolbox;

public class BizException extends RuntimeException {

	/** */
	private static final long serialVersionUID = 1L;
	
	private Integer error_code;

	public BizException(String msg) {
		super(msg);
	}
	
	public BizException(int error_code, String msg) {
		super(msg);
		this.error_code = error_code;
	}

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

}
