package cn.brent.toolbox.commons.exception;


/**
 * 业务 EXception
 */
public class BizException extends RuntimeException {

	/** */
	private static final long serialVersionUID = 1L;
	
	protected String errorCode;

	public BizException(String errorCode) {
		super(errorCode);
		this.errorCode=errorCode;
	}
	
	public BizException(String errorCode,String message) {
		super(message);
		this.errorCode=errorCode;
	}
	
	public BizException(ErrorCode errorCode) {
		super(errorCode.getDesc());
		this.errorCode=errorCode.getCode();
	}

	public BizException(String errorCode,String message, Throwable cause) {
		super(message, cause);
		this.errorCode=errorCode;
	}

	public BizException(String errorCode,Throwable cause) {
		super(cause);
		this.errorCode=errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
	
	/** 
	 * 重写fillInStackTrace
	 * 优点：会有更好的性能
	 * 缺点：没有异常链
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
	
}
