public class Helper {
    
    /** 
    * Class constructor.
    */
    private Helper () {}

    /**
    * This method is used to check if a number is prime or not
    * @param x A positive integer number
    * @return boolean True if x is prime; Otherwise, false
    */
    public static boolean isPrime(int x) {
/*      boolean flag = false; 
      if(x == 0||x == 1){  
        //System.out.println(x + " is not prime number");      
         flag = false;
       }*/
    //  else{      
         for (int i = 2; i < x; i++) {      
          //System.out.println(x + " is not prime number");      
          if (x % i == 0) {
           return false;
         }

      }
         return true;
      //return flag; 

    }
    

    /**
    * This method is used to get the largest prime factor 
    * @param x A positive integer number
    * @return int The largest prime factor of x
    */
    public static int getLargestPrimeFactor(int x) { //finish this method!!!!
        int largestPrimeNumber = 1; //0 or 1? 1 but I put 0
        for (int i = 2; i < x; i++) { //i = 1, but also i = 2? i = 2 but i put 1
          if (x % i == 0) {
            if (isPrime(i) == true) {
              largestPrimeNumber = i;
            }
          }
        }
        return largestPrimeNumber;

      }
      

    
}