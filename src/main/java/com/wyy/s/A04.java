package com.wyy.s;

public class A04 {

	public static int nnn = 0;
	  public boolean findNumberIn2DArray(int[][] matrix, int target) {
		  for(int i = 0;i<matrix.length;i++) {
			  if(matrix[i][0]>target) {
				  return false;
			  }
				for(int j=0;j<matrix[i].length;j++) {
					nnn++;
					if(matrix[i][j]==target) {
						return true;
					}else if(matrix[i][j]>target) {
						break;
					}
				}
			}
		  return false;
	    }
	  public boolean findNumberIn2DArray2(int[][] matrix, int target) {
		for(int i = 0;i<matrix.length;i++) {
			if(matrix[i][0]>target) {
				  return false;
			}
			if(exist(matrix[i],target)) {
				return true;
			}
				
		}
		return false;
	}
	  
	  public static boolean exist(int[] arr, int target) {
	        if (arr.length == 0 || arr == null) {
	            return false;
	        }
	        int L = 0;
	        int R = arr.length - 1;
	        int mid = 0;
	        while (L < R) {
	        	nnn++;
	            mid = (L + R) / 2;
	            if (arr[mid] == target) {
	                return true;
	            } else if (arr[mid] > target) {
	                R = mid - 1;
	            } else {
	                L = mid + 1;
	            }
	        }
	        return arr[L] == target;
	    }
	public static void main(String[] args) {
		int n= 10000;
		int[][] matrix = new int[n][n];
		for(int i=0;i<n;i++) {
			int[] matrix_ = new int[n];
			for(int j=0;j<n;j++) {
				matrix_[j] = (i*10+j)*2;
			}
			matrix[i] = matrix_;
		}
		int target = 110001;
		nnn=0;
		System.out.println(System.currentTimeMillis());
		System.out.println( new A04().findNumberIn2DArray2(matrix, target));
		System.out.println(System.currentTimeMillis());
		System.out.println(nnn);
		System.out.println("-----");
		nnn=0;
		System.out.println(System.currentTimeMillis());
		System.out.println( new A04().findNumberIn2DArray(matrix, target));
		System.out.println(System.currentTimeMillis());
		System.out.println(nnn);

	}

}
