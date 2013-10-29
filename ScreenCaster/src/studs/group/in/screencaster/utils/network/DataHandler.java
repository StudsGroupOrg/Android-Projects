package studs.group.in.screencaster.utils.network;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;

import studs.group.in.screencaster.device.Native;
import studs.group.in.screencaster.essentials.Essential;
import android.util.Log;

public class DataHandler implements Runnable {
	private static final String TAG	= DataHandler.class.getSimpleName();

	private static boolean active = false;
	
	private static boolean disconnect = false;

	private int fbCurrent;
	private int numOfPortions;	
	private ByteBuffer[] fbPortion;
	private String fbDev;

	private ServerSocketChannel ss;
	private SocketChannel s;
	private int dPortNo;
	private long frameStart;

	private Timer timer;


	public DataHandler() {
		// TODO Auto-generated constructor stub
		if(!active) { //So that it acts like singleton class
			fbDev = Essential.FBDEV;
			dPortNo = Essential.IP_DPORT;
			
			Essential.grantFBAccess();
			numOfPortions = Native.getNumberOfPortions(fbDev);
			fbPortion = new ByteBuffer[numOfPortions];
			for(int i=0;i<numOfPortions;i++) {
				fbPortion[i] = Native.getFBPortion(fbDev,i);
				fbPortion[i].clear();
			}
			fbCurrent = Native.getCurrent(fbDev);
			
			active = false;

			for(int i=0;i<Native.getNumberOfPortions(fbDev);i++) {
				Log.d(TAG,"FB["+i+"] : byteorder="+fbPortion[i].order()+
					" size="+fbPortion[i].capacity()+" hasArr="+fbPortion[i].hasArray());
			}
			Essential.restrictFBAccess();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		active = true;
		Essential.grantFBAccess();
	    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	    while (!Thread.currentThread().isInterrupted() && !disconnect) {
			try {
				ss = ServerSocketChannel.open();
			    ss.socket().bind(new InetSocketAddress(dPortNo));
			    Log.d(TAG,"Port bound");
			    s = ss.accept();
			    Log.d(TAG,"Connection accepted");
			    frameStart = System.currentTimeMillis();
				fbCurrent = Native.getCurrent(fbDev);
				timer = new Timer();
				startTimer();
			    
			    /********** Write byte buffer data **********/
			    while(fbPortion[fbCurrent].remaining()!=0) {
			    	//start=System.currentTimeMillis();
					//wc=s.write(bytBuff);
					//time = System.currentTimeMillis()-start;
					//Log.i(Values.TAG_DEBUG,"NTWK: Transfer rate was "+wc+"bytes "+time/1000+"sec");
					s.write(fbPortion[fbCurrent]);
					if(fbPortion[fbCurrent].remaining()==0) {
					  	fbPortion[fbCurrent].clear();
						//fbCurrent = Native.getCurrent(fbDev);
						Log.w(TAG,"Frame in "+
								(System.currentTimeMillis()-frameStart)+" ms Curr="+fbCurrent);
						frameStart = System.currentTimeMillis();
						if(disconnect) {
							break;
						}
					}
			    }
			    /********** ~Write byte buffer data **********/
			    
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG,"CATCH1 Exception : "+e.getMessage());
				disconnect = true;
			}
			cleanUp();
			stopTimer();
	    }
		//Going to exit
		cleanUp();
		stopTimer();
		//Let the thread exit
		for(int i=0;i<numOfPortions;i++) {
			Log.d(TAG,"Network destroyFBL munmap="+
							Native.destroySharedMemory(fbPortion[i],fbPortion[i].capacity()));
		}
		Log.d(TAG, TAG+" stopped");
		Essential.restrictFBAccess();
		active = false;
	}

	void cleanUp() {
	    try {
	    	if(s!=null && s.isOpen()) {
	    		s.close();
	    	}
	    	if(ss!=null && ss.isOpen()) {
	    		ss.close();
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"CATCH2 Exception : "+e.getMessage());
		}
	}

	public static boolean isActive() {
		// TODO Auto-generated method stub
		return active;
	}
	
	private void startTimer() {
		// TODO Auto-generated method stub
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  int ret;
				  ret = Native.getCurrent(fbDev);
				  if(0<=ret && ret<=numOfPortions) {
					  fbCurrent = ret;
				  }
			  }
		}, 50, 50);
	}

	private void stopTimer() {
		// TODO Auto-generated method stub
		if(timer!=null) {
			timer.cancel();
			timer=null;
		}
	}

	public void disconnect() {
		// TODO Auto-generated method stub
		stopTimer();
		disconnect = true;
		//cleanUp();
	}
}