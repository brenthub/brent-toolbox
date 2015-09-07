package cn.brent.toolbox.captcha;

public interface CaptchaValidator {

	/**
	 * 获取验证码
	 * @param sessionId
	 * @param bizcode
	 * @return
	 */
	String get(String sessionId,String bizCode);
	
	/**
	 * 清除验证码
	 * @param sessionId
	 * @param bizcode
	 */
	void clear(String sessionId,String bizCode);
	
	/**
	 * 校验验证码
	 * @param sessionId
	 * @param bizcode
	 * @param code
	 * @param clear
	 * @return
	 */
	boolean validate(String sessionId,String bizCode,String code,boolean clear);
	
}
