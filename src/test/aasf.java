package test;

import weaver.general.TimeUtil;

public class aasf {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("sss"+TimeUtil.dateAdd(TimeUtil.getCurrentDateString(), -7));
		int n = 17;
		int s = 0;
		for (int i = 1; i <= n; i++) {
			if (n % i == 0)
				s = s + 1;
		}
		if (s == 2) {
			System.out.println("yes");
		} else {
			System.out.println("no");
		}
	}

}
