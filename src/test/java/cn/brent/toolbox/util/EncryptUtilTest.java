package cn.brent.toolbox.util;

import org.junit.Test;

import cn.brent.toolbox.security.EncryptUtil;

public class EncryptUtilTest {

	@Test
	public void test(){
		System.out.println(EncryptUtil.generatePassword("123456"));

		String pwd = "$S$Dp8dKT6rCRO7hvy2BxynVWxee/wVuegUXcZaE81GqbGj5MM15TQm";
		String spwd = "123456";
		System.out.println(EncryptUtil.checkPassword(spwd, pwd));
	}
}
