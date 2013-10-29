package studs.group.in.screencaster.utils.execution;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.util.Log;

public class ExecutionManager implements Runnable {
	private static final String TAG = ExecutionManager.class.getSimpleName();

	private String prog;
	private String dir;
	private String cmd;
	private static boolean active;
	private static Process proc;
	private static OutputStream execOTS; //stdin of native proc
	private static InputStream  execINS; //stdout of native proc
	private static InputStream  execERS; //stderr of native proc
	
	public ExecutionManager(String programName,String binaryDir,String command) {
		prog=programName;
		dir=binaryDir;
		cmd=command;
	}
	
	public ExecutionManager() {
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		active = true;
		try {
			Log.d(TAG,"Start");
			execShellCommandThread(prog,dir,cmd);
			BufferedReader brCleanOut = new BufferedReader(new InputStreamReader(execINS));
			BufferedReader brCleanErr = new BufferedReader(new InputStreamReader(execERS));
		    String line;
			while (active) {
				while((line=brCleanOut.readLine()) != null); {
					Log.i(TAG," Out: "+line);
				}
			    while((line=brCleanErr.readLine()) != null); {
			    	Log.i(TAG," Err: "+line);
			    }
			}
		    brCleanOut.close();
		    brCleanErr.close();
		    execINS.close();
		    execERS.close();
			Log.d(TAG,"Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		active = false;
		//Let the tread exit
	}
	
	public void setArguments(String programName,String binaryDir,String command) {
		prog=programName;
		dir=binaryDir;
		cmd=command;
	}

	private void execShellCommandThread(String programName,String binaryDir,String command) throws Exception {
		String[] progArgs = command.split(" ");
		proc = Runtime.getRuntime().exec(progArgs,null,null);
		execOTS = proc.getOutputStream();
		execINS = proc.getInputStream();
		execERS = proc.getErrorStream();
	}
	
	public static int destroyForcibily(String stopCommand) throws Exception {
		int exitVal=-10;
		Log.d(TAG,"Forceful destruction");
		execOTS.flush();
		execOTS.write((stopCommand).getBytes());
		execOTS.close();
        proc.waitFor();
		exitVal = proc.exitValue();
		proc.destroy();
		active = false;
		Log.d(TAG,"Destroyed");
		return exitVal;
	}
	
	public static int execShellCommand(String program,String binDir,String command) throws Exception {
		int exitVal;
		
		Process proc = Runtime.getRuntime().exec(program,null,null);
		OutputStream execOS = proc.getOutputStream();
		if(binDir==null) {
			Log.d(TAG,"Executing : p="+program+" cmd=/system/bin/"+command);
			execOS.write(("/system/bin/"+command).getBytes());
		} else {
			Log.d(TAG,"Executing : p="+program+" cmd="+binDir+"/"+command);
			execOS.write((binDir+"/"+command).getBytes());
		}
        execOS.flush();
        execOS.close();
        proc.waitFor();
		exitVal = proc.exitValue();
		Log.d(TAG,"Finished exit="+exitVal);
		return exitVal;
	}

	public static boolean isActive() {
		// TODO Auto-generated method stub
		return active;
	}
}
