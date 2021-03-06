package loader.request;

public class RequestProcessor extends Thread {

	private static RequestProcessor PROCESSOR = new RequestProcessor();

	private RequestQueue requestQueue = new RequestQueue();
	private volatile boolean running = true;

	public static void sendRequest(ResourceRequest request) {
		PROCESSOR.addRequestToQueue(request);
	}

	public static void cleanUp() {
		PROCESSOR.kill();
	}

	public synchronized void run() {
		while (running || requestQueue.hasRequests()) {
			if (requestQueue.hasRequests()) {
				requestQueue.acceptNextRequest().doResourceRequest();
			} else {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void kill() {
		running = false;
		indicateNewRequests();
	}

	private synchronized void indicateNewRequests() {
		notify();
	}

	private RequestProcessor() {
		this.start();
	}

	private void addRequestToQueue(ResourceRequest request) {
		if(!running) return;
		boolean isPaused = !requestQueue.hasRequests();
		requestQueue.addRequest(request);
		if (isPaused) {
			indicateNewRequests();
		}
	}

}
