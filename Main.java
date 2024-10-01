class Main {
    public static void main(String args[])
    {
        int size;
        if(args.length > 0){
            size = Integer.parseInt(args[0]);
        }
        else {
            size = 10;
        }
        for (int i=0;i<size;i++) {
            for (int j=-1;j<i;j++){
                System.out.print("*");
            }
            System.out.print("\n");
        }
    }
}