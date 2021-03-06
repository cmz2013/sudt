package cn.sudt.lang.ip;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * IPV4工具类
 * 
 * @author chongming
 */

public class Ipv4Utils {
	private String IP_ERROR = "IP address error: ";
	private String MASK_ERROR = "Mask error: ";
	private String IPV4_MODE = "%s.%s.%s.%s";
	
	/**
	 * 判断是否为127IP
	 * @param pre
	 * @return
	 */
	private boolean is127Ip(long pre) {
		if (127l != pre) {
			return false;
		} 
		return true;
	}

	/**
	 * 判断是否为255IP
	 * @param pre
	 * @return
	 */
	private boolean is255Ip(long pre) {
		if (255l != pre) {
			return false;
		} 
		return true;
	}

	/**
	 * 判断是否为0IP
	 * @param arr
	 * @return
	 */
	private boolean isZeroIp(long pre) {
		if (0l != pre) {
			return false;
		}
		return true;
	}

	/**
	 * 广播地址或网络号
	 * 
	 * @param ip
	 * @param maskOneDigit:掩码1的位数
	 * @return
	 * @throws Exception 
	 */
	private boolean isNetOrBroadIp(long[] ip, int maskOneDigit) throws Exception{
		long ipLong = ipToLong(ip);
		long ipNum = ipCount(maskOneDigit);
		long maskLong = maskToLong(maskOneDigit);

		long netIpLong = ipLong & maskLong;
		long broadIpLong = (ipLong & maskLong) + ipNum;
		
		if((ipLong == netIpLong) || (ipLong == broadIpLong)){
			return true;
		}
		return false;
	}
	
	/**
	 * 位串转为整数
	 * @param binaryStr
	 * @return
	 */
	private long binaryToLong(String binaryStr) {
		long res = 0;
		for (int i = 8 - binaryStr.length(); i < 8; i++) {
			res += Math.pow(2, i);
		}
		return res;
	}
	
	/**
	 * 校验掩码
	 * 
	 * @param maskOneDigit:掩码1的位数
	 */
	public boolean checkMask(int maskOneDigit) {
		if (maskOneDigit < 0 || maskOneDigit > 32) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 校验IP格式
	 * 
	 * @param ip
	 * @return
	 */
	public boolean checkIpFormat(String ip)  {
		try {
			splitIpToArray(ip);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 可供主机使用的IP地址
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public boolean isHostIP(String ip) throws Exception {
		long[] arr = null;
		try {
			arr = splitIpToArray(ip);
		} catch (Exception e) {
			throw new Exception(IP_ERROR + e.getMessage());
		}
		
		if (isZeroIp(arr[0]) || is255Ip(arr[0]) || is127Ip(arr[0])) {
			return false;
		}
		return true;
	}
	
	/**
	 * 可供主机使用的IP地址
	 * @param ip
	 * @param mask
	 * @return
	 * 
	 * @throws Exception
	 * 	
	 */
	public boolean isHostIP(String ip, String mask) throws Exception {
		int maskOneDigit = getMaskOneDigit(mask);
		return isHostIP(ip, maskOneDigit);
	}
	
	
	/**
	 * 可供主机使用的IP地址
	 * @param ip
	 * @param maskOneDigit:掩码1的位数
	 * @return
	 * 
	 * @throws Exception
	 * 	
	 */
	public boolean isHostIP(String ip, int maskOneDigit) throws Exception {
		if(!checkMask(maskOneDigit)) {
			throw new Exception(MASK_ERROR + maskOneDigit + " not in [0-32]");
		}
		
		long[] arr = null;
		try {
			arr = splitIpToArray(ip);
		} catch (Exception e) {
			throw new Exception(IP_ERROR + e.getMessage());
		}
		if (isZeroIp(arr[0]) || is255Ip(arr[0]) || is127Ip(arr[0])) {
			return false;
		} 
		if (isNetOrBroadIp(arr, maskOneDigit)) {
			return false;
		}
		return true;
	}

	/**
	 * IP地址格式化
	 * @param ip: eg 192.168.1.1
	 * @return: eg 192.168.001.001
	 */
	public String formatIp(String ip) {
		String[] ips = ip.split("\\.");
		StringBuffer newip = new StringBuffer();
		for (int i = 0; i < ips.length; i++) {
			if (i != 0) {
				newip.append(".");
			}
			if (ips[i].length() == 3) {
				newip.append(ips[i]);
			} else {
				for (int j = 0; j < (3 - ips[i].length()); j++) {
					newip.append("0");
				}
				newip.append(ips[i]);
			}
		}
		return newip.toString();
	}
	
	/**
	 * 是否为广播地址或网络号
	 * 
	 * @param ip
	 * @param maskOneDigit:掩码1的位数
	 * @return
	 * @throws Exception 
	 */
	public boolean isNetOrBroadIp(String ip, int maskOneDigit) throws Exception{
		try {
			return isNetOrBroadIp(splitIpToArray(ip), maskOneDigit);
		} catch (Exception e) {
			throw new Exception(IP_ERROR + e.getMessage());
		}
	}
	
	/**
	 * 计算掩码十进制值
	 * @param mask1Num:掩码1的位数
	 * @return
	 */
	public long maskToLong(int maskOneDigit) {
		long longMask = 0;
		for (int i = 31; i >= 32 - maskOneDigit; i--) {
			longMask += Math.pow(2, i);
		}
		return longMask;
	}
	
	/**
	 * 计算掩码1的位数
	 * 
	 * @param mask：掩码
	 * @return
	 * @throws Exception 
	 */
	public int getMaskOneDigit(String mask) throws Exception {
		int count = 0;
		long[] maskArr = null;
		try {
			maskArr = splitIpToArray(mask);
		} catch (Exception e) {
			throw new Exception(MASK_ERROR + e.getMessage());
		}
		
		flag: for (int i = 0; i < maskArr.length; i++) {
			String bitStr = Long.toBinaryString(maskArr[i]);
			int zeroIndex = bitStr.indexOf('0');
			/**
			 * 掩码高位全1，低位全0
			 */
			if (zeroIndex >= 0) {
				int oneIndex = bitStr.indexOf('1', zeroIndex);
				if (oneIndex > 0) {
					throw new Exception(MASK_ERROR + maskArr[i]);
				}
			}
			
			for (int j = 0; j < bitStr.length(); j++) {
				if ('1' == bitStr.charAt(j)) {
					count++;
				} else {
					break flag;
				}
			}
		}
		return count;
	}
	
	/**
	 * 掩码点分十进制形式
	 * @param maskOneDigit
	 * @return
	 * @throws Exception 
	 */
	public String getMask(int maskOneDigit) throws Exception {
		if (!checkMask(maskOneDigit)) {
			throw new Exception(MASK_ERROR + maskOneDigit + " not in [0-32]");
		}
		
		String mask = "";
		String binaryStr = "";
		for (int i = 0; i < maskOneDigit; i++) {
			binaryStr += "1";
			if (binaryStr.length() == 8) {
				binaryStr = "";
				mask += "255.";
			}
		}
		
		if (binaryStr.length() > 0) {
			mask += binaryToLong(binaryStr);
		}
		
		if ("".equals(mask)) {
			mask = String.format(IPV4_MODE, 0, 0, 0, 0);
		} else {
			String[] maskArray = mask.split("\\.");
			if (1 == maskArray.length) {
				mask = String.format(IPV4_MODE, maskArray[0], 0, 0, 0);
			} else if (2 == maskArray.length) {
				mask = String.format(IPV4_MODE, maskArray[0], maskArray[1], 0, 0);
			} else if (3 == maskArray.length) {
				mask = String.format(IPV4_MODE, maskArray[0], 
						maskArray[1], maskArray[2], 0);
			} else if (4 == maskArray.length) {
				mask = String.format(IPV4_MODE, maskArray[0], maskArray[1], 
						maskArray[2], maskArray[3]);
			}
		}
		return mask;
	}

	/**
	 * 计算子网IP地址个数
	 * 
	 * @param maskOneDigit：掩码1的位数
	 * @return
	 * @throws Exception 
	 */
	public long ipCount(int maskOneDigit) throws Exception {
		if (!checkMask(maskOneDigit)) {
			throw new Exception(MASK_ERROR + maskOneDigit + " not in [0-32]");
		}

		long number = 0;
		for (int i = 0; i < 32 - maskOneDigit; i++) {
			number += Math.pow(2, i);
		}
		return number;
	}

	/**
	 * 拆分IP或掩码成数组
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public long[] splitIpToArray (String param) throws Exception {
		long[] ipArray = new long[4];
		String[] arr = param.split("\\.");
		if (arr.length != 4) {
			throw new Exception("format error");
		}
		
		for(int i = 0; i < arr.length; i++){
			ipArray[i] = Long.parseLong(arr[i]);
			if (ipArray[i] < 0) {
				throw new Exception(ipArray[i] + " < 0");
			} else if (ipArray[i] > 255) {
				throw new Exception(ipArray[i] + " > 255");
			}
		}
		return ipArray;
	}

	/**
	 * ip或掩码转换成10进制整数
	 * @param ipArr:IP地址段
	 * @return
	 */
	public long ipToLong(long[] ipArr) {
	/*long res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			res |= ipArr[i];
		}
		return res;*/
		
		return (ipArr[0] << 24) 
				+ (ipArr[1] << 16) 
				+ (ipArr[2] << 8) 
				+ ipArr[3];
	}

	/**
	 * 将10进制整数转换成点分十进制IP或掩码
	 * @param longIp
	 * @return
	 */
	public String longToIP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		//右移24位
		sb.append(String.valueOf(longIp >>> 24));
		sb.append(".");
		//将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16)); 
		sb.append(".");
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append(String.valueOf(longIp & 0x000000FF));
		return sb.toString();
	}
	
	/**
	 * 两个IP段是否有交集
	 * @param startIP1 
	 * @param endIp1
	 * @param startIP2
	 * @param endIp2
	 * @return boolean   
	 */
	public boolean isIntersection(
			long startIP1, long endIp1, long startIP2, long endIp2){
		if(startIP2 <= endIp1 && endIp2 >= startIP1){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 地址范围形式转换
	 * @param segIp
	 * @param maskOneDigit
	 * 
	 * @return  例如：192.168.1.35/27
	 * 			返回:192.168.1.32-192.168.1.63
	 * 
	 * @throws Exception 
	 */
	public String getNetworkSegment(String segIp, int maskOneDigit) throws Exception {
		long ipLong = 0;
		try {
			ipLong = ipToLong(splitIpToArray(segIp));
		} catch (Exception e) {
			throw new Exception(IP_ERROR + e.getMessage());
		}
		long ipNum = ipCount(maskOneDigit);
		long maskLong = maskToLong(maskOneDigit);

		long netIpLong = ipLong & maskLong;
		long broadIpLong = (ipLong & maskLong) + ipNum;
		return longToIP(netIpLong) + "-" + longToIP(broadIpLong);
	}
	
	/**
	 * 根据域名获取IP
	 * 
	 * @param hostDN:主机域名,eg:eecs.pku.edu.cn
	 * @return
	 * @throws UnknownHostException
	 */
	public String getIpByDN(String hostDN) throws UnknownHostException  {
		InetAddress address = InetAddress.getByName(hostDN);
		return address.getHostAddress();
	}
	
	/**
	 * 获取主机的IP地址
	 * @return
	 * @throws SocketException
	 */
	public List<String> getHostIp() throws SocketException {
		List<String> ipList = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface
				.getNetworkInterfaces();
		InetAddress ip = null;
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				String ipStr = ip.getHostAddress();
				if (!ipStr.equals("127.0.0.1") && !ipStr.equals("0.0.0.0")) {
					ipList.add(ip.getHostAddress());
				}	
			}	
		}
		return ipList;
	}
	
}
