public class  CodingBat {

// Warmup 1
    public static boolean nearHundred(int n) {
        int x2 = Math.abs(200 - n);
        int x1 = Math.abs(100 -n);
        if((x1 <= 10) || (x2 <= 10))
        {
            return true;
        }
        return false;
    }

    public boolean monkeyTrouble(boolean aSmile, boolean bSmile) {
        if((aSmile && bSmile) || (!aSmile && !bSmile)){
            return true;
        }
        return false;
    }

    public int sumDouble(int a, int b) {
        if(a != b){
            return a+b;
        }
        return 2*(a+b);
    }

    public int diff21(int n) {
        if(n > 21){
            return 2*(Math.abs(21 -n));
        }
        return Math.abs(21-n);
    }

    public boolean parrotTrouble(boolean talking, int hour) {
        if(talking && ((hour < 7) || (hour > 20))){
            return true;
        }
        return false;
    }

    public boolean sleepIn(boolean weekday, boolean vacation) {
        if(!weekday || vacation){
            return true;
        }
        return false;
    }
    public boolean makes10(int a, int b) {
        int suma = a +b;
        if(((a == 10 || b == 10)) || suma == 10){
            return true;
        }
        return false;
    }

    public boolean posNeg(int a, int b, boolean negative) {
        if(negative == true && (a < 0 && b < 0)){
            return true;
        }else if(!negative && ((a<0 && b>0) || (a >0 && b<0))){
            return true;
        }
        return false;
    }
    public String notString(String str) {
        String dodanie = "not";
        String [] tab = str.split(" ");
        if(tab[0].equals(dodanie)){
            return str;
        }
        return dodanie +" "+ str;
    }
    public static String frontBack(String str) {
        if(str.length() > 3){
            String srodek = str.substring(1, str.length()-1);
            char poczatek = str.charAt(0);
            char koniec = str.charAt(str.length()-1);
            String wynik = koniec + srodek + poczatek;
            return  wynik;
        }
        return str;
    }



    public static String everyNth(String str, int n) {
        String nowy = "";
        for(int i = 0; i < str.length(); i = i + n){
            nowy =  nowy + str.charAt(i);
        }
        return nowy;
    }

    public boolean icyHot(int temp1, int temp2) {
        if((temp1 < 0 && temp2 > 100) || (temp1 > 100 && temp2 < 0)){
            return true;
        }
        return false;
    }
    public static int countHi(String str) {
        int wynik = 0;
        for(int i = 1; i <= str.length() ; i++){
            if(str.contains("hi") == true){
                if(str.indexOf("hi") != str.lastIndexOf("hi")){
                    int x= str.indexOf("hi") + 1;
                    wynik++;
                    str = str.substring(x, str.length());

                }else{
                    wynik++;
                    i = str.length();       }
            }
        }
        return wynik;
    }
    public static boolean xyzThere(String str) {
        for(int i=0; i< str.length(); i++){
            if(str.contains(".xyz")){
                str = str.substring(str.indexOf(".xyz")+ 4, str.length());
            }else if(str.contains("xyz")){
                return true;
            }
        }
        return false;
    }
    public static boolean haveThree(int[] nums) {
        int licznik = 0;
        for(int i = 0; i < nums.length; i++){
            if((nums[i] == 3)){
                licznik++;
            }
        }
        for(int i = 0; i < nums.length-1; i++){
            if((nums[i] == 3) && nums[i+1] ==3){
            return false;
            }
        }
        if(licznik == 3){
            return true;
        }
        return false;
    }

    public boolean has22(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            if ((nums[i] == 2) && nums[i + 1] == 2) {
                return true;
            }
        }
        return false;
    }

    public static String frontTimes(String str, int n) {
        String nowy = "";
        for(int i = 0; i < n; i++){
            if(str.length() > 3){
                String front = str.substring(0, 3);
                nowy = nowy + front;
            }else {
                nowy = nowy + str;
            }
        }
        return nowy;
    }
    public static int stringMatch(String a, String b) {
        int licznik = 0;
        int mniejszy = 0;
        if(a.length() > b.length()){
            mniejszy = b.length();
        }else {
            mniejszy = a.length();
        }
        for(int i = 0; i < mniejszy - 2; i++){
            if(a.substring(i, i+2).equals(b.substring(i,i +2 ))){
                licznik++;
            }
        }
        return licznik;
    }
    public static boolean has271(int[] nums) {
        boolean czy = false;
        int val = 0;
        for(int i = 0; i < nums.length-2; i++){
            val = nums[i+2];
            if(nums[i] == nums[i+1]-5){
                czy = true;
                if(nums[i] == val + 1){
                    czy = true;
                    return true;
                }
            }
        }
        return false;
    }
    public static String seeColor(String str) {
        String color = "";
        if(str.length() >= 3){
            if(str.substring(0,3).equals("red")){
                color = "red";
            }else if(str.length()>=4){
                if(str.substring(0,4).equals("blue")){
                    color = "blue";
                }
            }else{
                color = "";
            }
        }
        return color;
    }



    public static void main(String[] args){
        char mychar = 'a';
//        System.out.println(mychar);
     //   System.out.println(nearHundred(111));
//        System.out.println(countHi("ABChihi"));
//        System.out.println(xyzThere("abc.xyz.xyzxyz"));
//        System.out.println(haveThree(new int[]{3, 1, 1, 3,3,4,3}));
        System.out.println(frontBack("abcde"));
        System.out.println(everyNth("Chocolates", 4));
        System.out.println(frontTimes("Ab", 4));
        System.out.println(stringMatch("xxcaazz", "xxbaaz"));
        System.out.println(has271(new int[]{2,7,3}));
        System.out.println(seeColor("blu"));
    }

}