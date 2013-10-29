package studs.group.in.screencaster.device;

import java.io.Serializable;

public class Device implements Serializable {
	/**
	 * This class is used for data synchronization
	 * over the network. Any updates to this
	 * source file HAVE to be made to both 
	 * client and server codes.
	 * 
	 * The package name for the class needs to be
	 * the same at both sides.
	 */
	private static final long serialVersionUID = 12L;

	/****************** Message & values ******************/
	private String Message;
	
	public static final String MsgConnect		= "INIT";
	public static final String MsgUpdate		= "UPDT";
	public static final String MsgDisconnect	= "DISC";
	public static final String MsgServerAlive	= "ALIV";
	/***************** ~Message & values ******************/
	
	private int		frameWidth;
	private int		frameHeight;
	private double	pivotAngle;
	private double	zoomFactor;
	
	private byte[]	pixelFormat;
	private int		pixelSize;

	public Device() {
		setFrameWidth(0);
		setFrameHeight(0);
		setPivotAngle(0);
		setZoomFactor(1);
		
		setPixelFormat(new byte[4]);
		setPixelSize(0);
	}
	
	public Device(int width, int height) {
		// TODO Auto-generated constructor stub
		setFrameWidth(width);
		setFrameHeight(height);
		setPivotAngle(0);
		setZoomFactor(1);
		
		setPixelFormat(new byte[4]);
		setPixelSize(0);
	}
	
	public Device(int width, int height, double angle, double zoom, byte[] pixFormat, int pixLength) {
		// TODO Auto-generated constructor stub
		setFrameWidth(width);
		setFrameHeight(height);
		setPivotAngle(angle);
		setZoomFactor(zoom);
		setPixelFormat(pixFormat);
		setPixelSize(pixLength);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String ret = new String("Width="+getFrameWidth()+" Height="+getFrameHeight()+"\n"
						+" Angle="+getPivotAngle()+" Zoom="+getZoomFactor()+"\n"
						+" PixelFormat="+(char)getPixelFormat()[0]+(char)getPixelFormat()[1]
										+(char)getPixelFormat()[2]+(char)getPixelFormat()[3]+"\n"
						+" PixelSize="+getPixelSize()+" bytes\n"
						+" Message=\""+Message+"\"\n");
		return ret;
	}

	public void setMessage(String message) {
		Message = message;
	}
	
	public String getMessage() {
		// TODO Auto-generated method stub
		return Message;
	}

	/*************** Auto generated getters & setters ****************/
	public byte[] getPixelFormat() {
		return pixelFormat;
	}

	public void setPixelFormat(byte[] pixelFormat) {
		this.pixelFormat = pixelFormat;
	}

	public int getPixelSize() {
		return pixelSize;
	}

	public void setPixelSize(int pixelSize) {
		this.pixelSize = pixelSize;
	}

	public double getPivotAngle() {
		return pivotAngle;
	}

	public void setPivotAngle(double pivotAngle) {
		this.pivotAngle = pivotAngle;
	}

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}
	/*************** Auto generated getters & setters ****************/
}