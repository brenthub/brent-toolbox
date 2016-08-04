package junit;

import org.junit.Test;

import cn.brent.toolbox.utils.security.DESEncry;

public class DESEncryTest {

	@Test
	public void test(){
		DESEncry d=new DESEncry("83");
		DESEncry e=new DESEncry("83");
		System.out.println(d.encryptHex("abcdedfg123中"));
		System.out.println(e.decryptHex(d.encryptHex("abcdedfg123中")));
		
		d=new DESEncry("NFDWDMIS","ADMINIST");
		e=new DESEncry("NFDWDMIS","ADMINIST");
		System.out.println(d.encryptBase64("admin$DMIS$405245122751401"));
		System.out.println(e.decryptBase64(d.encryptBase64("admin$DMIS$405245122751401")));
	}
}
