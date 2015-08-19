package cn.brent.toolbox.util;

import org.junit.Test;

import cn.brent.toolbox.security.DESEncry;

public class DESEncryTest {

	@Test
	public void test(){
		DESEncry d=new DESEncry("83");
		DESEncry e=new DESEncry("83");
		System.out.println(d.encrypt("abcdedfg123中"));
		System.out.println(e.decrypt(d.encrypt("abcdedfg123中")));
	}
}
