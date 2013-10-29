package studs.group.in.screencaster.device;

import java.nio.ByteBuffer;

public class Native {
	static{ 
		System.loadLibrary("screencast");
	}
	public static native int haveRoot();
	public static native int getNumberOfPortions(String fbDev);
	public static native int getCurrent(String fbDev);
	public static native int destroySharedMemory(ByteBuffer bytBuff,long len);

	public static native ByteBuffer getFBPortion(String fbDev,int portion);
}
