public class  CodingBat {
    public static boolean nearHundred(int n) {
        int x2 = Math.abs(200 - n);
        int x1 = Math.abs(100 -n);
        if((x1 <= 10) || (x2 <= 10))
        {
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
//    public String everyNth (String str, int n) {
//        if(n > 0){
//            String [] z = str.split(" ");
//            return z[n];
//        }
//
//    }

    public boolean icyHot(int temp1, int temp2) {
        if((temp1 < 0 && temp2 > 100) || (temp1 > 100 && temp2 < 0)){
            return true;
        }
        return false;
    }
    public static int countHi(String str) {
        int wynik = 0;
        for(int i = 1; i < str.length() ; i++){
            if(str.contains("hi") == true){
                i = str.indexOf("hi") + 1;
                str = str.substring(i, str.length());
                wynik++;
            }
        }
        return wynik;
    }



    public static void main(String[] args){

     //   System.out.println(nearHundred(111));
        System.out.println(countHi("abhi hi hi "));
    }

}