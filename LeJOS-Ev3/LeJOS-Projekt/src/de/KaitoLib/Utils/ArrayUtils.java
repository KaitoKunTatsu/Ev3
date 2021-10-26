package de.KaitoLib.Utils;

public class ArrayUtils {

	
	/**
	 * This class includes methods for annoying array problems
	 * 
	 * @author KaitoKunTatsu | Joshua | KaitoKunTatsu#1415
	 */
	
	
	/**		
	 * 	@param arr1 / First array to merge
	 * 	@param arr2 / Second array to merge
	 * 	@return retArr / new array including arr1 and arr2
	 * */
	public static Object[] mergeArrays(Object[] arr1, Object[] arr2) 
	{
		Object[] retArr = new Object[arr1.length + arr2.length];
		int counter = 0;
		for (Object element : arr1) 
		{
			retArr[counter] = element;
			counter++;
		}
		for (Object element : arr2) 
		{
			retArr[counter] = element;
			counter++;
		}
		return retArr;
	}
	
	public static boolean[] mergeArrays(boolean[] arr1, boolean[] arr2) 
	{
		boolean[] retArr = new boolean[arr1.length + arr2.length];
		int counter = 0;
		for (boolean element : arr1) 
		{
			retArr[counter] = element;
			counter++;
		}
		for (boolean element : arr2) 
		{
			retArr[counter] = element;
			counter++;
		}
		return retArr;
	}
	
	public static char[] mergeArrays(char[] arr1, char[] arr2) 
	{
		char[] retArr = new char[arr1.length + arr2.length];
		int counter = 0;
		for (char element : arr1) 
		{
			retArr[counter] = element;
			counter++;
		}
		for (char element : arr2) 
		{
			retArr[counter] = element;
			counter++;
		}
		return retArr;
	}
	
	public static String[] mergeArrays(String[] arr1, String[] arr2) 
	{
		String[] retArr = new String[arr1.length + arr2.length];
		int counter = 0;
		for (String element : arr1) 
		{
			retArr[counter] = element;
			counter++;
		}
		for (String element : arr2) 
		{
			retArr[counter] = element;
			counter++;
		}
		return retArr;
	}
	
	public static int[] mergeArrays(int[] arr1, int[] arr2) 
	{
		int[] retArr = new int[arr1.length + arr2.length];
		int counter = 0;
		for (int element : arr1) 
		{
			retArr[counter] = element;
			counter++;
		}
		for (int element : arr2) 
		{
			retArr[counter] = element;
			counter++;
		}
		return retArr;
	}
	
	/**
	 * 	Method to print an array due iterating through it
	 * 	@param arr / array to print
	 * */
	
	public static void printArr(String[] arr) 
	{
		for (String element : arr) 
		{
			System.out.print(element);
		}
	}
	
	public static void printArr(int[] arr) 
	{
		for (int element : arr) 
		{
			System.out.print(element);
		}
	}
	
	public static void printArr(boolean[] arr) 
	{
		for (boolean element : arr) 
		{
			System.out.print(element);
		}
	}
	
	public static void printArr(char[] arr) 
	{
		for (char element : arr) 
		{
			System.out.print(element);
		}
	}
	
	/**
	 * 	@param arr / Array where an element should be added
	 * 	@param element / Element which should be added
	 * 	@return newArr / New Array containing the old one plus the new element pasted at the end
	 * */
	
	public static String[] addElementToArray(String[] arr, String element) 
	{
		String[] newArr = new String[arr.length + 1];
		int counter = 0;
		for (String x : arr) 
		{
			newArr[counter] = x;
			counter++;
		}
		newArr[counter] = element;
		return newArr;
	}
	
	public static int[] addElementToArray(int[] arr, int element) 
	{
		int[] newArr = new int[arr.length + 1];
		int counter = 0;
		for (int x : arr) 
		{
			newArr[counter] = x;
			counter++;
		}
		newArr[counter] = element;
		return newArr;
	}
	
	public static boolean[] addElementToArray(boolean[] arr, boolean element) 
	{
		boolean[] newArr = new boolean[arr.length + 1];
		int counter = 0;
		for (boolean x : arr) 
		{
			newArr[counter] = x;
			counter++;
		}
		newArr[counter] = element;
		return newArr;
	}
	
	public static char[] addElementToArray(char[] arr, char element) 
	{
		char[] newArr = new char[arr.length + 1];
		int counter = 0;
		for (char x : arr) 
		{
			newArr[counter] = x;
			counter++;
		}
		newArr[counter] = element;
		return newArr;
	}
	
	/**
	 * 	@param arr / Array where space should be added
	 * 	@param value / Integer which says how many spaces should be added
	 * 	@return newArr / Old array plus spaces added
	 * */
	
	public static String[] addSpaceToArray(String[] arr, int value) 
	{
		String[] newArr = new String[arr.length + value];
		int counter = 0;
		for (String element : arr) 
		{
			newArr[counter] = element;
			counter++;
		}
		return newArr;
	}
	
	public static int[] addSpaceToArray(int[] arr, int value) 
	{
		int[] newArr = new int[arr.length + value];
		int counter = 0;
		for (int element : arr) 
		{
			newArr[counter] = element;
			counter++;
		}
		return newArr;
	}

	public static Long[] addSpaceToArray(Long[] arr, int value) 
	{
		Long[] newArr = new Long[arr.length + value];
		int counter = 0;
		for (Long element : arr) 
		{
			newArr[counter] = element;
			counter++;
		}
		return newArr;
	}

	
	/**
	 * 	Method to search an element in array using binary search
	 * 	@param arr / Array which is used to search in
	 * 	@param element / Item to be searched for 
	 * 	@param ignoreCapitalLetters / if true it compares the element to the array due ignoring capital letters in both
	 * 	@return returns -1 if element not found, returns index of the item in the array similar to the element
	 * */

	public static int binarySearch(int[] arr, int element) 
	{
		int first = 0;
		int last = arr.length-1;
		int mid = (first +first)/2;
		while(first <= last)  
		{
			if (arr[mid] == element) 
			{
				return mid;
			}
			if (arr[mid] < element) 
			{
				first = mid+1;
			}
			else if (arr[mid] > element) 
			{
				last = mid-1;
			}
			mid = (first + last)/2;
			
		}
		return -1;
	}
	
	public static int binarySearch(String[] arr, String element) 
	{
		int first = 0;
		int last = arr.length-1;
		int mid = (first + last)/2;

		while(first <= last) 
		{				
			if (arr[mid].equals(element)) 
			{
				return mid;
			}
			if (arr[mid].compareTo(element) < 0 ) 
			{
				first = mid+1;
			}
			else if (arr[mid].compareTo(element) > 0) 
			{
				last = mid-1;
			}
			mid = (first + last)/2;
			
		}
		return -1;
	}	
	
	public static int binarySearch(String[] arr, String element, boolean ignoreCapitalLetters) 
	{
		
		int first = 0;
		int last = arr.length-1;
		int mid = (first + last)/2;
		String lE = element;
		String arrMid = arr[mid];
		
		while(first <= last) 
		{	
			if (ignoreCapitalLetters) {
				arrMid = arr[mid].toLowerCase(); 
				lE = element.toLowerCase();
			} else
			{
				arrMid = arr[mid];
			}
				
			if (arrMid.equals(lE)) 
			{
				return mid;
			}
			if (arrMid.compareTo(lE) < 0 ) 
			{
				first = mid+1;
			}
			if (arrMid.compareTo(lE) > 0) 
			{
				last = mid-1;
			}
			mid = (first + last)/2;
			
		}
		return -1;
	}
	
		
	
	
	/**
	 * 	Method to search an element in array using linear search
	 * 	@param arr / Array which is used to search in
	 * 	@param element / Item to be searched for 
	 * 	@param ignoreCapitalLetters / if true it compares the element to the array due ignoring capital letters in both
	 * 	@return returns -1 if element not found, returns index of the item in the array similar to the element 
	 * */

	public static int linearSearch(int[] arr, int element) 
	{
		for(int i=0; i < arr.length; i++) 
		{
			if (arr[i] == element) return i;
		}
		return -1;
	}
	
	public static int linearSearch(String[] arr, String element) 
	{
		for(int i=0; i < arr.length; i++) 
		{
			if (arr[i] == element) return i;
		}
		return -1;
	}
	
	public static int linearSearch(String[] arr, String element, boolean ignoreCapitalLetters) 
	{
		for(int i=0; i < arr.length; i++) 
		{
			if (ignoreCapitalLetters) 
			{
				if (arr[i].toLowerCase().equals(element.toLowerCase())) return i;
			}
			else {
				if (arr[i] == element) return i;
			}
		}
		return -1;
	}
	
	/**
	 * 	Method to convert an array to a string. Different elements are divided though ;;
	 * 
	 * 	@param arr / Array which should be converted 
	 * 	@return returns a string including all elements from arr
	 * */
	
	public static String toString(String[] arr) 
	{
		String newStr = arr[0];
		try 
		{
			for (int i = 1; i < arr.length; i++) 
			{
				newStr = newStr + ";;" + arr[i];
			}	
		} 
		catch (Exception e)
		{}
		return newStr;
	}
	
	public static String toString(int[] arr) 
	{
		String newStr = Integer.toString(arr[0]);
		try 
		{
			for (int i = 1; i < arr.length; i++) 
			{
				newStr = newStr + ";;" + Integer.toString(arr[i]);
			}	
		} 
		catch (Exception e)
		{}
		return newStr;
	}
	
	/**
	 * 	Method to add an item to an array
	 * 	If larger than last item: add new item at the end
	 * 	If smaller than first item: add new item in the first place
	 * 	Else: place new item next to the one which is the most similar
	 *  
	 * 	@param arr / Array in which the element should be sorted in
	 * 	@param element / Item to sort in 
	 * 	@return New array including the old one and the element
	 * */

	private static int compare(int[] arr, int old, int pNew, int element) 
	{
		if ((element - arr[old]) * (element - arr[old]) < (element - arr[pNew]) * (element - arr[pNew])) return old;
		else return pNew;
	}
	
	public static int[] sortIntoArr(int[] arr, int element) 
	{
		if (element >= arr[arr.length-1]) 
		{
			return addElementToArray(arr, element);
		}
		if (element <= arr[0]) 
		{
			int[] newArr = new int[arr.length + 1];
			int counter = 1;
			newArr[0] = element;
			for (int x : arr) 
			{
				newArr[counter] = x;
				counter++;
			}
			newArr[counter] = element;
			return newArr;
		}
		else 
		{
			int[] newArr = new int[arr.length+1];
			int nearest = 0;
			
			for (int i = 1; i < arr.length; i++) 
			{
				nearest = compare(arr, nearest, i, element);
			}
			int counter = 0;
			for (int i = 0; i < arr.length; i++) 
			{
				if (counter == nearest) 
				{
					newArr[i] = element;
					i--;
				}
				else 
				{
					newArr[counter] = arr[i];
				}
				counter++;
			}
			return newArr;
		}
		
	}
}
