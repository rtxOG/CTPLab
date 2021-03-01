package LR1;

public class Palindrome {
    public static void main(String[] args){
        args = new String[] {"java", "LR1.Palindrome","madam", "racecar", "apple", "noon"};
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            if (isPalindrome(s)) {
                System.out.println("Слово " + s + " палиндром.");
            }
            else {
                System.out.println("Слово " + s + " не палиндром.");
            }
        }
    }

    public static String reverseString(String str){
        String str1 = "";
        for (int i = str.length()-1; i >= 0; i--) {
            str1 += str.charAt(i);
        }
        return str1;
    }
    public static boolean isPalindrome(String s){
        if (s.equals(reverseString(s)))
            return true;
        else return false;
    }
}
