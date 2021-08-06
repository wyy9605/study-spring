package mj;

public class DevOps {

	public static void main(String[] args) {
		for(int i=1;i<=100;i++) {
			System.out.println(printOne(i));
		}

	}
	
	public static String printOne(int num) {
		if(num%15==0) {
			return "DevOps";
		}else if(num%3==0) {
			return "Dev" ;
		}else if(num%5==0) {
			return "Ops";
		}else {
			return String.valueOf(num);
		}
	};

}
