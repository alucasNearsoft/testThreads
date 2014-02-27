import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class TheThreads {
	
	private static String fruit;

	public static void main(String[] args) {
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch done = new CountDownLatch(1);
		
		String[] fruits = {"apples", "pears", "grapes", "bananas", "kiwis",
		"pears"};
		List<String> theList = new ArrayList<>();

		for (String f: fruits) {
			theList.add(f);
		}
		
		System.out.println("theList: "+ theList);
		
		Runnable r = new Runnable()
		{
			@Override
			public void run() {
				try {
					start.await();
					TheThreads.fruit += " touch";
					//System.out.println(TheThreads.fruit);
					done.countDown();
				}
				catch(InterruptedException ie) {
					System.err.println(ie);
				}
			}
		};
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		
		for (String ss: theList) {
			fruit = ss;
			executor.execute(r);	
			try {
				start.countDown();
				done.await();
				TheThreads.fruit += " mainTouched";
				System.out.println(TheThreads.fruit);
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		executor.shutdownNow();
		System.out.println("theList: " + theList);
	}
}
