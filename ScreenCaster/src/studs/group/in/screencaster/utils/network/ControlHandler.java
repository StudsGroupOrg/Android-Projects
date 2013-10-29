package studs.group.in.screencaster.utils.network;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import studs.group.in.screencaster.device.Device;
import studs.group.in.screencaster.essentials.Essential;
import android.util.Log;

public class ControlHandler implements Runnable {
	private static final String TAG = ControlHandler.class.getSimpleName();

	private boolean active = false;
	private boolean init = false;
	private boolean lock = false;
	private boolean disconnect = false;

	private Device dvc;
	private DataHandler dataHandler;
	private Thread dataThread;
	
	private int cPortNo;
	private ServerSocketChannel ss;
	private SocketChannel s;
	private OutputStream os;
	private ObjectOutputStream oos;

	public ControlHandler() {
		// TODO Auto-generated constructor stub
		dataHandler = new DataHandler();
		cPortNo = Essential.IP_CPORT;
	    
	    /********** Initialize device data **********/
	    dvc = new Device(Essential.frameWidth,Essential.frameHeight);
	    dvc.setPixelSize(4);
	    dvc.getPixelFormat()[0] = 'B';
	    dvc.getPixelFormat()[1] = 'G';
	    dvc.getPixelFormat()[2] = 'R';
	    dvc.getPixelFormat()[3] = 'A';
	    dvc.setMessage(Device.MsgConnect);
	    /********* ~Initialize device data **********/
	    
	    init = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		active = true;
	    //Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	    while (!Thread.currentThread().isInterrupted() && !disconnect) {
	    	try {
				ss = ServerSocketChannel.open();
			    ss.socket().bind(new InetSocketAddress(cPortNo));
			    Log.d(TAG,"Port bound");
			    s = ss.accept();
			    Log.d(TAG,"Connection accepted");
			    os = s.socket().getOutputStream();
			    oos = new ObjectOutputStream(os);
			    dvc.setMessage(Device.MsgConnect); //Required for re-connection
			    oos.reset();
			    oos.writeObject(dvc);	//Connecting to client
			    Log.d(TAG, "Device object sent : "+dvc);
			    dataStart();	//Start the data server thread
			    
			    /********** Write control data ***********/
			    while(true) {
				    if(!lock) {
				    	lock = true;
				    	if(disconnect) {
				    		dvc.setMessage(Device.MsgDisconnect);
				    	} else {
					    	dvc.setMessage(Device.MsgServerAlive);
					    }
				    }
				    oos.reset();
				    oos.writeObject(dvc);
				    Log.d(TAG, "Device object sent : "+dvc.getMessage());
				    if(disconnect) {
				    	//lock = true even after break
					    Thread.sleep(500);
				    	break;
				    }
			    	lock = false;
				    Thread.sleep(250);
			    }
			    /********** ~Write control data **********/

			    if(disconnect) {
			    	break;
			    }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG,"CATCH1 Exception : "+e.getMessage());
				dataStop();	//Stop the data server thread
			}
			cleanUp();
	    }
	    
		//Going to exit
		dataStop();
		cleanUp();

    	lock = false;
		active = false;
		Log.d(TAG, "Exiting");
	}

	private void cleanUp() {
	    try {
	    	if(oos!=null) {
	    		oos.close();
	    		oos=null;
	    	}
	    	if(os!=null) {
	    		os.close();
	    		os=null;
	    	}
	    	if(s!=null && s.isOpen()) {
	    		s.close();
	    		s=null;
	    	}
	    	if(ss!=null && ss.isOpen()) {
	    		ss.close();
	    		ss=null;
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"CATCH2 Exception : "+e.getMessage());
		}
	}

	public void dataStart() {
		// TODO Auto-generated method stub
		if(!DataHandler.isActive()) {
			dataThread = null;
			dataThread = new Thread(dataHandler);
			dataThread.start();
		}
	}

	public void dataStop() {
		// TODO Auto-generated method stub
		if(DataHandler.isActive()) {
			dataHandler.disconnect();/////
			//dataThread.interrupt();
		}
	}
	
	public void clear() {
		// TODO Auto-generated catch block
		active = false;
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return init && active;
	}

	public void setScreenRotation(double angleDegree) {
		// TODO Auto-generated method stub
		dvc.setPivotAngle(angleDegree);
		update(Device.MsgUpdate);
	}

	public void setZoomLevel(double zoomFactor) {
		// TODO Auto-generated method stub
		dvc.setZoomFactor(zoomFactor);
		update(Device.MsgUpdate);
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		disconnect = true;
    	//update(Device.MsgDisconnect);
	}

	private void update(final String message) {
		// TODO Auto-generated method stub
		/*Thread th = new Thread() {
			@Override
			public void run() {*/
		    	while(lock);
				lock = true;
				dvc.setMessage(message);
			/*}
		};
		th.start();*/
	}

}