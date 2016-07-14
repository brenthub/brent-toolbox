package cn.brent.toolbox;

import java.util.BitSet;
import java.util.Random;

import org.junit.Test;

public class BitSetTest {

	public static void main(String[] args) {
		BitSet bs=BitSet.valueOf(new byte[]{6});
		BitSet ba=BitSet.valueOf(new byte[]{7});
		bs.and(ba);
		System.out.println(bs);
	}
	
	public static void tertCost(){
		BitSet bs=new BitSet();
		BitSet ba=new BitSet();
		Random rd=new Random();
		for(int i=0;i<300000000;i++){
			if(rd.nextBoolean()){
				bs.set(i);
			}
			if(rd.nextBoolean()){
				ba.set(i);
			}
		}
		System.out.println("begin");
		long time=System.currentTimeMillis();
		bs.and(ba);
		System.out.println(System.currentTimeMillis()-time);
	}
	
	/**
	 * 2.5亿个整数中找出不重复的整数的个数
	 */
	public static void getNum(){
		BitSet bs=new BitSet();
		int sum=0;
		Random rd=new Random();
		for(int i=0;i<250000000;i++){
			int k=rd.nextInt(250000000-1);
			if(!bs.get(k)){
				sum++;
				bs.set(k);
			}
		}
		System.out.println(sum);
	}
	
	@Test
	public void test(){
		BitSet bs=BitSet.valueOf(new long[]{7,1});
		
		//从后面开始数0-64位换算成第1个long,以此类推
		System.out.println(bs);
		
		//基数（所有位置为true的总个数）
		System.out.println(bs.cardinality());
		
		//位置个数
		System.out.println("length:"+bs.length());
		
		//使用空间的位数
		System.out.println("size:"+bs.size());
		
		//从后面开始64位64位换算成byte数组，前面的多出的0将无视
		System.out.println("long array size:"+bs.toLongArray().length);
		
		//从后面开始8位8位换算成byte数组，前面的多出的0将无视
		System.out.println("byte array size:"+bs.toByteArray().length);
		
		//获取0号位置的值
		System.out.println(bs.get(0));
		
		//将0位置值为false
		bs.clear(0);
		System.out.println(bs);
		
		//从2到64号位置全置为false(64之前，不包括64)
		bs.clear(2,64);
		System.out.println(bs);
		
		//将所有位设置为 false。 
		bs.clear();
		System.out.println(bs);
		
		//或运算，其它还有and(与),xor(异或)
		bs.or(BitSet.valueOf(new byte[]{3}));
		System.out.println(bs);
		
		//与（非set）
		bs.andNot(BitSet.valueOf(new byte[]{1}));//{0,1} & ~{0}
		System.out.println(bs);
		
		//将此位置的值取反
		bs.flip(1);
		System.out.println(bs);
		
		//从0位置开始，下一个值为true的位置，没有找到返回-1
		System.out.println(bs.nextSetBit(0));
		
		//将一段区间取反（不包括3）
		bs.flip(1, 3);
		System.out.println(bs);
		
	}
	
}
