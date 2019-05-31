public class Main{

    public static void main(String[] args) {
        int[] dupes = new int[] {0,1,2,4,4,5,6,7};
        int[] noDupes = new int[] {0,1,2,4,3,5,6,7};

        System.out.println(doesArrayContainDupes(dupes));
        System.out.println(doesArrayContainDupes(noDupes));
    }



    public static boolean doesArrayContainDupes(int[] arr){
        int startIndex = 0;
        for(int i = arr.length-1; i > 0 && startIndex < arr.length; i--){
            int end = arr[i];
            int start = arr[startIndex];
            if(end == start)
                return true;
            startIndex++;
        }
        return false;
    }
}