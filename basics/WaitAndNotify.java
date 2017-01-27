public class WaitAndNotify {
   public static void main( String[ ] args ) {
      final int n = 8;
      MyThread[ ] mt = new MyThread[n];
      char c = 'A';

      //*** construct and initialize the threads
      for (int i = 0; i < n; i++) mt[i] = new MyThread(i, c++);
      //*** start the threads, in reverse order
      for (int i = n - 1; i >= 0; i--) mt[i].start();
   }
}












class MyThread extends Thread {
   public MyThread( int i, char ch ) { id = i; c = ch; }
   public void run() {
      while (true) writeChar();
   }
   //*** synchronized to ensure only 1 thread at a time
   //    writes to the queue, etc. Threads wait their turn, however.
   private synchronized void writeChar() {
      //*** release lock and wait until it's your turn
      while (turn != id) 
         try {
           wait(20); // 20 milliseconds
         } catch(InterruptedException e) { }

      queue[turn++] = this.c;
      if (turn == n) { //*** queue full?
        System.out.println(queue); //** if so, print it
        turn = 0;                  //   and reset turn to zero
      }
      notifyAll(); //*** awaken any waiting threads
   }
   private int id; //*** identifier
   private char c; //*** character to write to shared array
   private static int turn = 0;
   private static final int n = 8; 
   private static char queue[ ] = new char[n]; //** shared array
}
