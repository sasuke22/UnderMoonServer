package com.qiqiim.server.test;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTest {
	private static List<String> res = new ArrayList<>();
	private static HashMap<Character, String[]> map = new HashMap<>();
	public static void main(String[] args) {
		System.out.println(divide(1000,3));
	}

	/**
	 * 串联所有单词的子串
	 */
	public List<Integer> findSubstring(String s, String[] words) {
		List<Integer> res = new ArrayList<>();
		int wordNum = words.length;
		if (wordNum == 0) {
			return res;
		}
		int wordLen = words[0].length();
		HashMap<String, Integer> allWords = new HashMap<>();
		for (String w : words) {
			int value = allWords.getOrDefault(w, 0);
			allWords.put(w, value + 1);
		}
		//将所有移动分成 wordLen 类情况
		for (int j = 0; j < wordLen; j++) {
			HashMap<String, Integer> hasWords = new HashMap<>();
			int num = 0; //记录当前 HashMap2（这里的 hasWords 变量）中有多少个单词
			//每次移动一个单词长度
			for (int i = j; i < s.length() - wordNum * wordLen + 1; i = i + wordLen) {
				boolean hasRemoved = false; //防止情况三移除后，情况一继续移除
				while (num < wordNum) {
					String word = s.substring(i + num * wordLen, i + (num + 1) * wordLen);
					if (allWords.containsKey(word)) {
						int value = hasWords.getOrDefault(word, 0);
						hasWords.put(word, value + 1);
						//出现情况三，遇到了符合的单词，但是次数超了
						if (hasWords.get(word) > allWords.get(word)) {
							// hasWords.put(word, value);
							hasRemoved = true;
							int removeNum = 0;
							//一直移除单词，直到次数符合了
							while (hasWords.get(word) > allWords.get(word)) {
								String firstWord = s.substring(i + removeNum * wordLen, i + (removeNum + 1) * wordLen);
								int v = hasWords.get(firstWord);
								hasWords.put(firstWord, v - 1);
								removeNum++;
							}
							num = num - removeNum + 1; //加 1 是因为我们把当前单词加入到了 HashMap 2 中
							i = i + (removeNum - 1) * wordLen; //这里依旧是考虑到了最外层的 for 循环，看情况二的解释
							break;
						}
						//出现情况二，遇到了不匹配的单词，直接将 i 移动到该单词的后边（但其实这里
						//只是移动到了出现问题单词的地方，因为最外层有 for 循环， i 还会移动一个单词
						//然后刚好就移动到了单词后边）
					} else {
						hasWords.clear();
						i = i + num * wordLen;
						num = 0;
						break;
					}
					num++;
				}
				if (num == wordNum) {
					res.add(i);

				}
				//出现情况一，子串完全匹配，我们将上一个子串的第一个单词从 HashMap2 中移除
				if (num > 0 && !hasRemoved) {
					String firstWord = s.substring(i, i + wordLen);
					int v = hasWords.get(firstWord);
					hasWords.put(firstWord, v - 1);
					num = num - 1;
				}

			}

		}
		return res;
	}

	/**
	 * 两数相除，返回商
	 */
	public static int divide(int dividend, int divisor) {
		if(dividend==Integer.MIN_VALUE&&divisor==-1)
			return Integer.MAX_VALUE;

		boolean k=(dividend>0&&divisor>0)||(dividend<0&&divisor<0);
		int result=0;
		dividend=-Math.abs(dividend);
		divisor=-Math.abs(divisor);
		while(dividend <= divisor) {//因为转成负数计算了，所以变成小于
			int temp = divisor;
			int c = 1;
			while(dividend-temp<=temp) {
				temp = temp<<1;
				c = c<<1;
			}
			dividend-=temp;
			result+=c;
		}
		return k?result:-result;
	}

	/**
	 * 自己实现indexOf（needle）
	 */
	public static int strStr(String haystack, String needle) {
		if (haystack == null || needle == null) return -1;
		if (haystack.length() == 0 && needle.length() == 0) return 0;
		else if (haystack.length() == 0) return -1;
		else if (needle.length() == 0) return 0;
		else {
			for (int i = 0; i <= haystack.length() - needle.length(); i++) {
				if (haystack.charAt(i) == needle.charAt(0)){
					int start = i;
					for (int j = 0; j < needle.length(); j++) {
						if (haystack.charAt(start) == needle.charAt(j)){
							start++;
							if (j == needle.length()-1)
								return start - j-1;
						} else
							break;
					}
				}
			}
			return -1;
		}
	}

	/**
	 * 移除元素
	 */
	public static int removeElement(int[] nums, int val) {
		if (nums.length <= 0) return 0;
		int size = 0;
		for (int i = 0,j = 0; i < nums.length; i++) {
			if (nums[i] != val){
				nums[j] = nums[i];
				j++;
				size++;
			}
		}
		return size;
	}

	/**
	 * 删除排序数组中的重复项
	 */
	public static int removeDuplicates(int[] nums) {
		if (nums.length <= 0) return 0;
		int size = 1;
		int dup = nums[0];
		for (int i = 1,j = 1; i < nums.length; i++) {
			if (nums[i] != dup){
				dup = nums[i];
				nums[j] = dup;
				size++;
				j++;
			}
		}
		return size;
	}

	/**
	 * K个一组翻转列表
	 */
	public ListNode reverseKGroup(ListNode head, int k) {
		ListNode tail = head;
		ListNode newHead = head;
		for (int i = 0; i < k; i++) {
			if (tail == null) {// 如果没有k个剩余，就直接返回head
				return head;
			}
			tail = tail.next;
		}
		newHead = reverse(head, tail);
		head.next = reverseKGroup(tail, k);
		return newHead;
	}

	private static ListNode reverse(ListNode head, ListNode tail) {
		ListNode pre = null;
		ListNode next = null;
		while (head != tail) {
			next = head.next;
			head.next = pre;
			pre = head;
			head = next;
		}
		return pre;
	}

	/**
	 * 两两交换链表中的节点
	 */
	public ListNode swapPairs(ListNode head) {
		// 1->2->3->4
		if (head == null || head.next == null)
			return head;
		ListNode firstNode = head;//1->2->3->4
		ListNode secondNode = head.next;//2->3->4
		firstNode.next = swapPairs(secondNode.next);//2->3->4
		secondNode.next = firstNode;//2->1->swap
		return secondNode;
	}

	/**
	 * 两两合并链表
	 */
	public static ListNode merge(ListNode[] lists, int start, int end) {
		if (start == end)//如果相同，就直接返回第一个
			return lists[start];
		int mid = start + (end - start) /2;
		ListNode l1 = merge(lists, start, mid);
		ListNode l2 = merge(lists, mid+1, end);
		return mergeTwoLists(l1, l2);
	}

	/**
	 * 合并k个链表
	 */
	public static ListNode mergeKLists(ListNode[] lists) {
		if (lists.length == 0) return null;
		return merge(lists, 0, lists.length - 1);
	}

	/**
	 * 给一个n，生成n对应的括号组合
	 */
	public static List<String> generateParenthesis(int n) {
		if (n == 0) {
			return new ArrayList<>();
		}
		// 这里 dp 数组我们把它变成列表的样子，方便调用而已
		List<List<String>> dp = new ArrayList<>(n);

		List<String> dp0 = new ArrayList<>();
		dp0.add("");
		dp.add(dp0);

		for (int i = 1; i <= n; i++) {
			List<String> cur = new ArrayList<>();
			for (int j = 0; j < i; j++) {
				List<String> str1 = dp.get(j);
				List<String> str2 = dp.get(i - 1 - j);
				for (String s1 : str1) {
					for (String s2 : str2) {
						// 枚举右括号的位置
						cur.add("(" + s1 + ")" + s2);
					}
				}
			}
			dp.add(cur);
		}
		return dp.get(n);
	}

	/**
	 * 合并两个链表
	 */
	public static ListNode mergeTwoLists(ListNode l1,ListNode l2){
		ListNode result = new ListNode(-1);
		ListNode temple = result;
		while (l1 != null || l2 != null){
			if (l1 == null) {
				temple.next = l2;
				break;
			}
			if (l2 == null){
				temple.next = l1;
				break;
			}
			if (l1.val > l2.val){
				temple.next = l2;
				l2 = l2.next;
			} else {
				temple.next = l1;
				l1 = l1.next;
			}
			temple = temple.next;
		}
		return result.next;
	}

	/**
	 * 判断是否为有效的括号
	 */
	private static boolean isValid(String s) {
		List<Character> list = new ArrayList<>();
		char[] chars = s.toCharArray();
		for (int i = 0;i < chars.length;i++) {
			Character aChar = chars[i];
			switch (aChar){
				case '(':
				case '[':
				case '{':
					list.add(aChar);
					break;
				case ')':
					if (list.size() == 0) return false;
					Character last = list.remove(list.size()-1);
					if (last != '(')
						return false;
					break;
				case ']':
					if (list.size() == 0) return false;
					last = list.remove(list.size()-1);
					if (last != '[')
						return false;
					break;
				case '}':
					if (list.size() == 0) return false;
					last = list.remove(list.size()-1);
					if (last != '{')
						return false;
					break;
			}
		}
		return list.size() == 0;
	}

	/**
	 * 删除链表倒数第n个节点，并返回头节点
	 */
	public static ListNode removeNthFromEnd(ListNode head, int n) {
		ListNode result = new ListNode(0);
		result.next = head;
		ListNode first = result;//0,1,2
		ListNode second = result;//0,1,2
		for (int i = 1; i < n + 1; i++) {
			first = first.next;//2
		}
		if (first == null){
			return null;
		}
		while (first.next != null){
			first = first.next;//null
			second = second.next;//1,2
		}
		second.next = second.next.next;//2 = null
		return result.next;
	}

	public static class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}
	/**
	 * 找出四个数相加等于target
	 */
	public static List<List<Integer>> fourSum(int[] nums, int target) {
		List<List<Integer>> res = new ArrayList<>();
		if (nums.length < 4) return res;
		Arrays.sort(nums);
		for (int i = 0; i < nums.length - 3; i++) {
			if (i > 0 && nums[i] == nums[i - 1]){
				continue;
			}
			int L = i + 1, end = nums.length -1, R = end - 1;
			if (nums[nums.length - 3] + nums[nums.length -2] + nums[nums.length-1] + nums[i] < target){//遍历到i的最大值，如果都小于target，证明无
				continue;
			}
			if (nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target)//如果最小都比target大，后面都不用遍历了
				break;
			while (i < end-2){
				int secondTarget = target - nums[i] - nums[end];
				while (L < R){
					int sum = nums[L] + nums[R];
					if (sum > secondTarget)
						R--;
					else if (sum < secondTarget)
						L++;
					else{
						res.add(Arrays.asList(nums[i],nums[L],nums[R],nums[end]));
						while (L < R && nums[L + 1] == nums[L]) L++;
						while (R > L && nums[R - 1] == nums[R]) R--;
						L++;
						R--;
					}
				}
				while (end > i && nums[end] == nums[end - 1]) end--;
				end--;
				L = i + 1;
				R = end - 1;
			}
		}
		return res;
	}

	/**
	 * 找到9键上三个数对应的所有字母组合
	 */
	public static List<String> letterCombinations(String digits) {
		if (digits.length() == 0) return res;
		map.put('2',new String[]{"a","b","c"});
		map.put('3',new String[]{"d","e","f"});
		map.put('4',new String[]{"g","h","i"});
		map.put('5',new String[]{"j","k","l"});
		map.put('6',new String[]{"m","n","o"});
		map.put('7',new String[]{"p","q","r","s"});
		map.put('8',new String[]{"t","u","v"});
		map.put('9',new String[]{"w","x","y","z"});
		char aChar = digits.charAt(0);
		String[] list = map.get(aChar);
		for (int i = 0; i < list.length; i++) {
			insertChar(digits, list[i], 1);
		}
		return res;
	}

	private static void insertChar(String str, String sb, int index){
		if (index == str.length()){
			res.add(sb);
			return;
		}
		char aChar = str.charAt(index);
		String[] list = map.get(aChar);
		for (int i = 0; i < list.length; i++) {
			insertChar(str, sb + list[i], index + 1);
		}
	}

	/**
	 * 找出最接近target的三位数之和
	 */
	public static int threeSumClosest(int[] nums, int target) {
		Arrays.sort(nums);
		int ans = nums[0] + nums[1] + nums[2];
		for (int i = 0; i < nums.length; i++) {
			int L = i + 1;
			int R = nums.length - 1;
			int sum;
			while (L < R){
				sum = nums[i] + nums[L] + nums[R];
				if (Math.abs(sum - target) < Math.abs(ans - target)){//找到比答案更接近target的数
					ans = sum;
				}
				if (sum > target)
					R--;
				else if (sum < target)
					L++;
				else
					return ans;//有等于target的，直接返回
			}
		}
		return ans;
	}

	/**
	 * 找出和为0的三个数组合
	 */
	public static List<List<Integer>> threeSum(int[] nums) {
		List<List<Integer>> ans = new ArrayList<>();
		int len = nums.length;
		if(len < 3) return ans;
		Arrays.sort(nums); // 排序
		for (int i = 0; i < len ; i++) {
			if(nums[i] > 0) break; // 如果当前数字大于0，则三数之和一定大于0，所以结束循环
			if(i > 0 && nums[i] == nums[i-1]) continue; // 去重
			int L = i+1;
			int R = len-1;
			while(L < R){
				int sum = nums[i] + nums[L] + nums[R];
				if(sum == 0){
					ans.add(Arrays.asList(nums[i],nums[L],nums[R]));
					while (L<R && nums[L] == nums[L+1]) L++; // 去重
					while (L<R && nums[R] == nums[R-1]) R--; // 去重
					L++;
					R--;
				}
				else if (sum < 0) L++;
				else R--;
			}
		}
		return ans;
	}

	/**
	 * 最长公共前缀
	 */
	static String longestCommonPrefix(String[] strs){
		if(strs.length == 0)
			return "";
		String ans = strs[0];
		for(int i =1;i<strs.length;i++) {
			int j=0;
			for(;j<ans.length() && j < strs[i].length();j++) {
				if(ans.charAt(j) != strs[i].charAt(j))
					break;
			}
			ans = ans.substring(0, j);
			if(ans.equals(""))
				return ans;
		}
		return ans;
	}

	/**
	 * 数组中只有一个数出现一次，其他出现三次，找出这个数
	 */
	public static int singleNum(int[] arr){
		int ones = 0;//记录只出现过1次的bits
		int twos = 0;//记录只出现过2次的bits
		int threes;
		for(int i = 0; i < arr.length; i++){
			int t = arr[i];
			twos |= ones&t;//要在更新ones前面更新twos
			ones ^= t;
			threes = ones&twos;//ones和twos中都为1即出现了3次
			ones &= ~threes;//抹去出现了3次的bits
			twos &= ~threes;
		}
		return ones;
	}

	/**
	 * 数组中只有一个数出现一次，其他出现三次，找出这个数
	 */
	public static int single4Num(int[] arr){
		int ones = 0;//记录只出现过1次的bits
		int twos = 0;//记录只出现过2次的bits
		int threes = 0;
		int fours;
		for(int i = 0; i < arr.length; i++){
			int t = arr[i];
			threes |= twos&ones&t;
			twos |= ones&t;//要在更新ones前面更新twos
			ones ^= t;
			fours = ones&twos&threes;//ones和twos中都为1即出现了3次
			ones &= ~fours;//抹去出现了3次的bits
			twos &= ~fours;
			threes &= ~fours;
		}
		return ones;
	}
	
	/**
	 * 罗马转数字
	 */
	public static int romanToInt(String num){
		int sum = 0;
		int preNum = getValue(num.charAt(0));
		int secondNum = 0;
		for(int i = 1;i < num.length();i++){
			secondNum = getValue(num.charAt(i));
			if(preNum < secondNum){
				sum -= preNum;
			}else{
				sum += preNum;
			}
			preNum = secondNum;//i = num.length()时，这个等于chatAt(num.length())
		}
		//所以最后还要加一次
		sum += preNum;
		return sum;
	}
	
	private static int getValue(char ch){
		switch(ch){
			case 'M': return 1000;
			case 'D': return 500;
			case 'C': return 100;
			case 'L': return 50;
			case 'X': return 10;
			case 'V': return 5;
			case 'I': return 1;
			default: return 0;
		}
	}
	
	/**
	 * 转罗马数字 
	 */
	public static String intToRoman(int num){
		int[] nums = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
		String[] symbol = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
		StringBuilder builder = new StringBuilder();
		int index = 0;
		while(index < nums.length){
			while(num >= nums[index]){
				builder.append(symbol[index]);
				num -= nums[index];
			}
			index++;
		}
		return builder.toString();
	}
	
	/**
	 * 获取两数的最大面积 
	 */
	public static int maxArea(int[] height){
		int i = 0,j = height.length - 1,area = 0;
		while(i < j){
			if(height[i] > height[j])
				area = Math.max(area, (j-i)*height[j--]);
			else
				area = Math.max(area, (j-i)*height[i++]);
		}
		return area;
	}
	
	/**
	 * 回文数，数字反转（带符号）是否和原来相等 
	 */
	public static boolean isReverse(int x){
		//思考：这里大家可以思考一下，为什么末尾为 0 就可以直接返回 false
        if (x < 0 || (x % 10 == 0 && x != 0)) return false;
        int revertedNumber = 0;
        while (x > revertedNumber) {//这里是为了只反转后半段，还有如果是奇位数，会导致x>revertedNumber
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }
        return x == revertedNumber || x == revertedNumber / 10;
	}
	
	/**
	 * 正则，不太对 
	 */
	public static void atoi(String str){
		String pattern = "^\\s*([+-]?\\d+)";
		Pattern r = Pattern.compile(str);
		Matcher m = r.matcher(pattern);
		if(m.find()){
			System.out.println(m.group(1));
		}else
			System.out.println("find nothing");
	}
	
	/**
	 * 整数反转，不能大于和小于int的最大值最小值 
	 */
	public static int reverse(int x){
		int ans = 0;
        while (x != 0) {
            int pop = x % 10;
            if (ans > Integer.MAX_VALUE / 10 || (ans == Integer.MAX_VALUE / 10 && pop > 7)) 
                return 0;
            if (ans < Integer.MIN_VALUE / 10 || (ans == Integer.MIN_VALUE / 10 && pop < -8)) 
                return 0;
            ans = ans * 10 + pop;
            x /= 10;
        }
        return ans;
	}
	
	public static String Ztrans(String s,int numRows){
		StringBuilder result = new StringBuilder();
		ArrayList<StringBuilder> list = new ArrayList(){};
		for(int i = 0;i < numRows;i++){
			list.add(new StringBuilder());
		}
		
		boolean dropDown = false;//方向
		int row = 0;
		for(int i = 0;i < s.length();i++){
			list.get(row).append(s.charAt(i));
			if(row == 0 || row == numRows - 1)
				dropDown = !dropDown;
			row += dropDown ? 1 : -1;
		}
		for(int i = 0;i < numRows;i++){
			result.append(list.get(i));
		}
		return result.toString();
	}
	
	/**
	 * 筛选出最长左右都相同的子字符串（回文字符串） 
	 */
	public static String findMaxSame(String s){
		int maxLen = 0;
		int start = 0,end = 0;
		for(int i = 1;i < s.length() - 1;i++){
			int len1 = centerExpand(s, i, i);
			int len2 = centerExpand(s, i, i+1);
			if(maxLen < Math.max(len1, len2)){
				maxLen = Math.max(len1, len2);
				start = i - (maxLen-1)/2;
				end = i + maxLen/2;
			}
		}
		return s.substring(start,end +1);
	}
	
	public static int centerExpand(String s,int left,int right){
		int i = left,j = right;
		while(i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j)){
			i--;
			j++;
		}
		return j - i - 1;
	}
	
	public static int findX(int[] nums1,int[] nums2,int start1,int start2,int end1,int end2,int k){
		int n = nums1.length;
		int m = nums2.length;
		int need = (n + m)/2;
		int center = 0;
		if(n > m) return findX(nums2,nums1,end2,start2,start1,end1,k);
		if(k > 1){
			if(k > end1 - start1){//数组1的长度没了
				System.out.println(nums2[need-1]);
				center = (nums2[need-1] + nums2[need])/2;
				return center;
			}  
			if(nums1[k] > nums2[k]){//去除nums2的k之前的数
				center = findX(nums1,nums2,start1,nums1.length,k,nums2.length,k/2);
			}else if (nums1[k] < nums2[k]){//去除nums1的k之前的数
				center = findX(nums1,nums2,k,nums1.length,start2,nums2.length,k/2);
			}	
		}
		return center;
	}
	
	private static int getKth(int[] nums1, int start1, int end1, int[] nums2, int start2, int end2, int k) {
        int len1 = end1 - start1 + 1;
        int len2 = end2 - start2 + 1;
        //让 len1 的长度小于 len2，这样就能保证如果有数组空了，一定是 len1 
        if (len1 > len2) return getKth(nums2, start2, end2, nums1, start1, end1, k);
        if (len1 == 0) return nums2[start2 + k - 1];

        if (k == 1) return Math.min(nums1[start1], nums2[start2]);

        int i = start1 + Math.min(len1, k / 2) - 1;//试用math.min是防止出现k大于一个数组剩余长度的情况
        int j = start2 + Math.min(len2, k / 2) - 1;

        if (nums1[i] > nums2[j]) {
            return getKth(nums1, start1, end1, nums2, j + 1, end2, k - (j - start2 + 1));
        }
        else {
            return getKth(nums1, i + 1, end1, nums2, start2, end2, k - (i - start1 + 1));
        }
    }
}
