package cn.brent.toolbox.utils.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang3.StringUtils;

import cn.brent.toolbox.utils.EncodeUtil;


/**
 * DES加密工具类<br/>
 * 一种对称加密算法，DES 使用一个 56 位的密钥以及附加的 8 位奇偶校验位（每组的第8为作为奇偶校验位），产生最大 64
 * 位的分组大小。这是一个迭代的分组密码，使用称为 Feistel
 * 的技术，其中将加密的文本块分成两半。使用子密钥对其中一半应用循环功能，然后将输出与另一半进行
 * “异或”运算；接着交换这两半，这一过程会继续下去，但最后一个循环不交换。DES 使用 16 轮循环，使用异或，置换，代换，移位操作四种基本运算。
 * 
 */
public class DESEncry {

	private static String strDefaultKey = "DESEncry";
	
	private final static String DES = "DES";
	
	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	/**
	 * 默认构造方法，使用默认密钥
	 * 
	 * @throws Exception
	 */
	public DESEncry() throws Exception {
		this(strDefaultKey);
	}

	/**
	 * 指定密钥构造方法
	 * 
	 * @param strKey
	 *            指定的密钥
	 * @throws Exception
	 */
	public DESEncry(String strKey) {
		try {
			if(strKey.length()<16){
				strKey=StringUtils.rightPad(strKey, 16, '0');
			}
			// 生成一个可信任的随机数源
			SecureRandom sr = new SecureRandom();
			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(strKey.getBytes());
			// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成解密操作
			encryptCipher = Cipher.getInstance(DES);
			encryptCipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
			decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, securekey,sr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密字符串并用十六进制编码
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public String encrypt(String strIn){
		try {
			return EncodeUtil.hexEncode(encryptCipher.doFinal(strIn.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密字符串
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public String decrypt(String strIn){
		try {
			return new String(decryptCipher.doFinal(EncodeUtil.hexDecode(strIn)));
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
