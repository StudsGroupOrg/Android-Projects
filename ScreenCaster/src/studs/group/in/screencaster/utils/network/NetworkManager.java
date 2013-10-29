package studs.group.in.screencaster.utils.network;


public class NetworkManager {
	//private static final String TAG = NetworkManager.class.getSimpleName();
	
	private ControlHandler contHandler;
	private Thread contThread;

	public NetworkManager() {
		// TODO Auto-generated method stub
		contHandler=null;
		contThread=null;
	}

	public void startCast() {
		// TODO Auto-generated method stub
		if(contHandler==null) {
			contHandler = new ControlHandler();
		}
		if(!contHandler.isActive()) {
			contThread = null;
			contThread = new Thread(contHandler);
			contThread.start();
		}
	}

	public void stopCast() {
		// TODO Auto-generated method stub
		if(isActive()) {
			contHandler.disconnect();
			//contThread.interrupt();
			//contHandler=null;
			//contThread=null;
		}
	}

	public void setRotation(double angleDegree) {
		// TODO Auto-generated method stub
		if(isActive()) {
			contHandler.setScreenRotation(angleDegree);
		}
	}

	public void setZoomLevel(double zoomFactor) {
		// TODO Auto-generated method stub
		if(isActive()) {
			contHandler.setZoomLevel(zoomFactor);
		}
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return contHandler!=null && contHandler.isActive();
	}
}