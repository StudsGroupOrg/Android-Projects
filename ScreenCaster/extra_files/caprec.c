/*
 * definitions.c
 *
 *  Created on: Sep 10, 2013
 *      Author: Piyush
 */

#include<android/log.h>
#include<errno.h>
#include<fcntl.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<sys/time.h>
#include<linux/fb.h>
#include<sys/mman.h>
#include<unistd.h>

#include"datatypes.h"
#include"declarations.h"

int record=1;
int pid=0,cpid=0,ppid=0;

/*
 * Checks if UID is root(0)
 * If not, then tries to get root UID
 */
int checkRoot() {
	int ret,uid;

	switch(uid=getuid()) {
	case 0:
		__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Got root :)");
		ret = 1;
		break;
	default:
		__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Current UID = %d",uid);
		ret = setuid(0);
		if(ret==0) {
			__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Got root :)");
			ret = 1;
		} else {
			__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,"Cant get root err=%d",errno);
			ret = 0;
		}
		break;
	}

	return ret;
}

/*
 * Displays version info and credits
 */
void displayAppInfo(CommandData* cmd) {
	printC(cmd->print_status,"\n\tScreenCaster v1.0 by StudsGroup\n");
}

/*
 * Displays the help menu
 */
void displayAppUsage(CommandData* cmd) {
	printC(cmd->print_status,"Usage: %s [OPTIONS]=<PARAM>\n",cmd->app_name);
	printC(cmd->print_status,"[OPTIONS] : <Purpose>\n\t\t{DEFAULT}\n\n");
	printC(cmd->print_status,"\t-a : Specify to turn ON silent interaction\n");
	printC(cmd->print_status,"\t-c : Directory to store the images"
									"\n\t\t{%s}\n",DEF_DIR_FRAME);
	printC(cmd->print_status,"\t-f : Number of frames to capture"
									"\n\t\t{%d}\n",DEF_FRAME_CAP);
	printC(cmd->print_status,"\t-h : Display this help menu\n");
	printC(cmd->print_status,"\t-i : IPv4 address to cast to"
									"\n\t\t{%s}\n",DEF_IP_ADDR);
	printC(cmd->print_status,"\t-p : Port number {%d}\n",DEF_IP_PORT);
	printC(cmd->print_status,"\t-r : Number of frames to record\n");
	printC(cmd->print_status,"\t      {%dsec recorded}\n",DEF_VIDEO_REC);
	printC(cmd->print_status,"\t-s : Duration of recording {%dseconds}\n",DEF_VIDEO_REC);
	printC(cmd->print_status,"\t-v : Directory to store the videos"
									"\n\t\t{%s}\n",DEF_DIR_VIDEO);
	printC(cmd->print_status,"\t-z : Framebuffer device file"
									"\n\t\t{%s}\n",DEF_FBDEV);
	printC(cmd->print_status,"\nSAMPLE : %s -c=/my/own/directory\n",cmd->app_name);
}

/*
 * Stores framebuffer filename with path in
 * buffer specified on command line
 * If not specified stores default filename
 * into the buffer
 */
void getFBDev(int argc,char** argv,char* destBuffer) {
	int i=0,fd=0,arg;
	char sdcard[50] = DEF_FBDEV;

	for(arg=1;arg<argc;arg++) {
		if(argv[arg][0]=='-' && argv[arg][1]=='z' && argv[arg][2]=='=') {
			for(i=3;argv[arg][i];i++) {
				destBuffer[i-3]=argv[arg][i];
			}
			break;
		}
	}
	if(i==0 || i==3) {
		destBuffer[0]=sdcard[0];
		for(i=1;sdcard[i-1];i++) {
			destBuffer[i]=sdcard[i];
		}
		__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Framebuffer : %s",destBuffer);
		return;
	}
	destBuffer[i-3]=0;
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Framebuffer : %s",destBuffer);
}

/*
 * Captures raw RGBA frame from framebuffer(fbuffer)
 * and stores it in specified directory with
 * correct filename
 */
int getFrame(CommandData* cmd,unsigned int* fbuffer,long size,long width,long height) {
	char filename[200];
	char ffcmd[150]="ffmpeg -f fbdev -i /dev/graphics/fb0 -r 15 -t 20 /mnt/sdcard/ScreenCaster/videos/Vid_new4.flv 2> /dev/null";
	int opfd,write_count;
	//long frame_num = atol(cmd->data[KEY('f')]);
	struct timeval time,finish;

	/*sprintf(filename,"%s/Frame%04ld",cmd->data[KEY('c')],frame_num);

	opfd=open(filename,O_RDWR|O_TRUNC|O_CREAT,0777);

	if(opfd==-1) {
		__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,
				"Couldn't open/create file %s",filename);
	}
	else if((write_count=write(opfd,fbuffer,size))!=size) {
		__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,
				"Frame%04ld size = ",frame_num,write_count);
	}
	close(opfd);
	gettimeofday(&time,NULL);
	sprintf(ffcmd,"ffmpeg -vcodec rawvideo -f rawvideo -pix_fmt rgb32 -s %ldx%ld -i %s -f image2 -q:v 2 -vcodec mjpeg %s/Snap_%ld.jpg"
						,width,height,filename,cmd->data[KEY('c')],time.tv_sec);
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"FFcmd_cap : %s",ffcmd);
	opfd = system(ffcmd);
	sprintf(ffcmd,"rm -f %s",filename);
	system(ffcmd);*/
	gettimeofday(&time,NULL);
		sprintf(filename,"rootex \"%s\"",ffcmd);

		__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"C: Recording start\nFFcmd = %s",filename);
		opfd = system(ffcmd);
		//pause();
		/*while(record) {
			if((write_count=write(fd,fbuffer,size))!=size) {
				__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
						"C: L(%d) Write wc=%ld size=%ld",record,write_count,size);
			}
			record--;
		}*/
	gettimeofday(&finish,NULL);
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"C: Recording completed in %ldsec ret=%d",
													finish.tv_sec-time.tv_sec,opfd);
	return 0;
}

/*
 * Stores the frames directory specified by command
 * into the buffer
 * If not specified stores the default directory
 * pathname into the buffer
 */
void getFramesDirectory(int argc,char* argv[],char* destBuffer) {
	int i=0,fd=0,arg;
	char sdcard[50] = DEF_DIR_FRAME;

	for(arg=1;arg<argc;arg++) {
		if(argv[arg][0]=='-' && argv[arg][1]=='c' && argv[arg][2]=='=') {
			for(i=3;argv[arg][i];i++) {
				destBuffer[i-3]=argv[arg][i];
			}
			break;
		}
	}
	if(i==0 || i==3) {
		destBuffer[0]=sdcard[0];
		for(i=1;sdcard[i-1];i++) {
			destBuffer[i]=sdcard[i];
		}
		__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Image dir : %s",destBuffer);
		return;
	}
	destBuffer[i-3]=0;
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Image dir : %s",destBuffer);
}

/*
 * Stores the video directory specified by command
 * into the buffer
 * If not specified stores the default directory
 * pathname into the buffer
 */
void getVideosDirectory(int argc,char* argv[],char* destBuffer) {
	int i=0,fd=0,arg;
	char sdcard[50] = DEF_DIR_VIDEO;

	for(arg=1;arg<argc;arg++) {
		if(argv[arg][0]=='-' && argv[arg][1]=='v' && argv[arg][2]=='=') {
			for(i=3;argv[arg][i];i++) {
				destBuffer[i-3]=argv[arg][i];
			}
			break;
		}
	}
	if(i==0 || i==3) {
		destBuffer[0]=sdcard[0];
		for(i=1;sdcard[i-1];i++) {
			destBuffer[i]=sdcard[i];
		}
		__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Video dir : %s",destBuffer);
		return;
	}
	destBuffer[i-3]=0;
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Video dir : %s",destBuffer);
}

/*
 * Returns token value corresponding
 * to input token
 */
int getTokenValue(char* input) {
	int pos=0,diff='a'-'A';

	//Sanitize input string
	while(input[pos]) {
		if('a'<=input[pos] &&
				input[pos]<='z') {
			input[pos]-=diff;
		}
		pos++;
	}
	pos=0;
	do {
		__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
				"TOK[%d] : %d",pos,input[pos]);
		pos++;
	} while(input[pos]);

	if(!strcmp(input,TOK_EXIT)) {
			return VAL_EXIT;
	}

	if(!strcmp(input,TOK_CAP)) {
			return VAL_CAP;
	}

	if(!strcmp(input,TOK_CST)) {
			return VAL_CST;
	}

	if(!strcmp(input,TOK_REC)) {
			return VAL_REC;
	}

	if(!strcmp(input,TOK_STOP)) {
			return VAL_STOP;
	}

	return VAL_UNDEF;
}

/*
 * Initializes the CommandData structure
 * variable
 */
void initCommandData(CommandData* cmd) {
	int i;

	cmd->app_name[0]=0;
	for(i=0;i<26;i++) {
		cmd->data[i][0]=0;
	}
	cmd->print_status=0;
}

/*
 * Checks if the user has specified to inhibit
 *  printing to the console
 */
int isSilentMode(int argc,char* argv[]) {
	int i,j;
	for(i=0;i<argc;i++) {
		for(j=0;argv[i][j];j++) {
			if( argv[i][j]=='-' &&
				argv[i][j+1]=='a' &&
				!argv[i][j+2] ) {
				__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
						"Silent interaction active");
				return 1;
			}
		}
	}
	return 0;
}

/*
 * Set fields of CommandData structure
 */
int setCommandData(CommandData* cmd,int argc,char** argv) {
	int i=0,tmp=0;

	while(argv[0][i++]);
	i--;
	while(argv[0][--i]!='/' && i!=0);
	if(i) i++;
	tmp=i;

	for(;argv[0][i];i++) {
		if(argv[0][i]!='.' && argv[0][i]!='/') {
			cmd->app_name[i-tmp]=argv[0][i];
		}
	}

	getFBDev(argc,argv,cmd->data[KEY('z')]);
	getFramesDirectory(argc,argv,cmd->data[KEY('c')]);
	getVideosDirectory(argc,argv,cmd->data[KEY('v')]);
	cmd->print_status=!isSilentMode(argc,argv);

	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Param : %s",cmd->app_name);
	for(i=0;i<26;i++) {
		if(cmd->data[i][0]!=0)
			__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
					"data[%c] = %s",'a'+i,cmd->data[i]);
	}

	return 0;
}

/*
 * Signal handler function
 */
void sigcatcher(int signum) {

	switch(signum) {
	case SIGUSR1:
		/*if(ppid==pid) {	//Parent process
			__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
					"P: kill()ing child(PID)=%d",cpid);
			kill(cpid,SIGUSR1);
			cpid = ppid;
		} else {	//Child process*/
			__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
					"C: Exiting");
			record=0;
		//}
	}
}

/*
 * Records a continuous data stream from
 * framebuffer to file in specific directory
 */
void startRecording(CommandData* cmd,unsigned int* fbuffer,long size) {
	int fd,fbfd;
	off_t offset=0;
	long write_count;
	char filename[100];
	char ffcmd[100];

	struct timeval finish,start;

	sprintf(filename,"/mnt/sdcard/ScreenCaster/frame0");
	fd=open(filename,O_RDWR|O_CREAT|O_TRUNC,0777);
	if(fd<0) {
		__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,"C: Cant open file '%s'",filename);
		return;
	}

	while(record) {
		if((write_count=write(fd,fbuffer,size))!=size) {
			__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
					"C: L(%d) Write wc=%ld size=%ld",record,write_count,size);
		}
		record--;
	}
	close(fd);

	sprintf(filename,"/mnt/sdcard/ScreenCaster/temp.txt");
	fd=open(filename,O_RDWR|O_CREAT|O_TRUNC,0777);
	if(fd<0) {
		__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,"C: Cant open file '%s'",filename);
		return;
	}

	sprintf(ffcmd,"%d %u %ld\n",getpid(),fbuffer,size);
	if((write_count=write(fd,ffcmd,strlen(ffcmd)))!=strlen(ffcmd)) {
		__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
				"C: L(%d) Write wc=%ld size=%ld",record,write_count,size);
	}
	close(fd);
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"C: PID=%d Address=%u Size=%ld pause()ing",pid,fbuffer,size);
	sleep(60);//pause();
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"C: Resumed after 1min",fbuffer,size);
}

/*
 * Recorder function
 */
int recorder(int argc,char* argv[]) {

	/***********************************DECLARATIONS***********************************/
	char data_dir[100],filename[100],input_buffer[50]="";
	extern int root;
	int fbfd,ioct,map,val=10;
	int* ofile,opfd;
	long frame_num=1,loop=1,read_count=0,reqd_frames,write_count=0,size;
	unsigned int i,j;
	unsigned int* fbuffer;

	struct fb_fix_screeninfo fscreeninfo;
	struct fb_var_screeninfo vscreeninfo;
	struct timeval finish,start;

	CommandData cmd;
	/**********************************~DECLARATIONS***********************************/


	/***********************************INITITIALIZE***********************************/
	gettimeofday(&start,NULL);
	initCommandData(&cmd);
	setCommandData(&cmd,argc,argv);

	if((argc<2) || (argc==2 && argv[1][0]=='-'
					&& argv[1][1]=='h' && !argv[1][2])) {
		displayAppUsage(&cmd);
		displayAppInfo(&cmd);
		__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Displaying help menu");
		return 0;
	}

	if(checkRoot()==0) {
		//return -1;
	}

	if((fbfd=open(cmd.data[KEY('c')],O_DIRECTORY))<0) {
		printD("Cannot open directory '%s'\n",cmd.data[KEY('c')]);
		__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,
				"Cannot open directory '%s'",cmd.data[KEY('c')]);
		return 1;
	}
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Data Dir : %s",cmd.data[KEY('c')]);
	close(fbfd);

	/*fbfd=open(cmd.data[KEY('z')],O_RDONLY);
	if(fbfd==-1) {
		__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,
			"Could not open framebuffer fbfd=%d errno=%d",fbfd,errno);
		return 2;
	}
	ioct=ioctl(fbfd,FBIOGET_VSCREENINFO,&vscreeninfo);
	if(ioct==-1) {
		__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,
				"VSCREEN-IOCTL failed ioct=%d errno=%d",ioct,errno);
		return 3;
	}
	ioct=ioctl(fbfd,FBIOGET_FSCREENINFO,&fscreeninfo);
	if(ioct==-1) {
		__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,
				"FSCREEN-IOCTL failed ioct=%d errno=%d",ioct,errno);
		return 4;
	}*/

	cpid = ppid = pid = getpid();
	signal(SIGUSR1,sigcatcher);
	/**********************************~INITITIALIZE***********************************/


	/**************************************MMAP()**************************************/
	/*size = fscreeninfo.smem_len/2;
	fbuffer=(unsigned int*)mmap(0,size,PROT_READ,MAP_SHARED,fbfd,0);
	printD("Address: %x\n", fbuffer);*/
	/*************************************~MMAP()**************************************/


	/**************************************VSCREEN*************************************/
	printD("\nVscreen Info:-\n");
	printD(" Xres   = %4ld | Yres   = %4ld\n",vscreeninfo.xres,vscreeninfo.yres);
	printD(" BPP    = %4ld | Height = %4ld | Width = %4ld\n",vscreeninfo.bits_per_pixel,
													vscreeninfo.height,
													vscreeninfo.width);
	printD(" Xres_V = %4ld | Yres_V = %4ld\n",vscreeninfo.height,vscreeninfo.width);
	printD(" Pixel format : RGBX_%ld%ld%ld%ld\n",vscreeninfo.red.length,
												vscreeninfo.green.length,
												vscreeninfo.blue.length,
												vscreeninfo.transp.length);
	printD(" Begin of bitfields:-\n");
	printD("  Red    : %ld\n",vscreeninfo.red.offset);
	printD("  Green  : %ld\n",vscreeninfo.green.offset);
	printD("  Blue   : %ld\n",vscreeninfo.blue.offset);
	printD("  Transp : %ld\n",vscreeninfo.transp.offset);
	/*************************************~VSCREEN*************************************/


	/**************************************FSCREEN*************************************/
	printD("\nFscreen Info:-\n");
	printD(" Device ID : %s\n",fscreeninfo.id);
	printD(" Start of FB physical address : %ld\n",fscreeninfo.smem_start);
	printD(" Length of FB : %ld\n",fscreeninfo.smem_len);
	printD(" Length of Line : %ld\n",fscreeninfo.line_length);
	printD(" Start of MMIO physical address : %ld\n",fscreeninfo.mmio_start);
	printD(" Length of MMIO : %ld\n",fscreeninfo.mmio_len);
	/*************************************~FSCREEN*************************************/


	/*************************************SPIN-LOCK************************************/
	while(val!=VAL_EXIT && cpid && !cmd.print_status) {
		//fgets(input_buffer,sizeof(input_buffer),stdin);
		//fscanf(stdin,"%s",input_buffer);
		//fgetline();
		//scanf("%s",input_buffer);
		val = VAL_REC;//getTokenValue(input_buffer);
		switch(val) {
		case VAL_CAP:
			printC(!cmd.print_status,"%d\n",VAL_CAP);
			return getFrame(&cmd,fbuffer,size,vscreeninfo.xres,vscreeninfo.yres);
			//break;
		case VAL_EXIT:
			printC(!cmd.print_status,"%d\n",VAL_EXIT);
			//Do not do anything here
			break;
		case VAL_CST:
			printC(!cmd.print_status,"%d\n",VAL_CST);
			break;
		case VAL_REC:
			printC(!cmd.print_status,"%d\n",VAL_REC);
			if((cpid == ppid) && (ppid == pid)) {
				__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"P: fork()ing...");
				cpid = fork();
			}
			if(cpid<0) {
				__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,"P: fork() failed!");
				break;
			}
			if(cpid) {	//Parent code follows
				__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
						"P: Parent(PID)=%d | Child(PID)=%d",ppid,cpid);
				pause();
				return 0;
			} else {	//Child code follows
				gettimeofday(&start,NULL);
				pid = getpid();
				__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
						"C: I am the child(PID)=%d",pid);
				startRecording(&cmd,fbuffer,size);
				__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
						"C: Signaling Parent...");
				kill(ppid,SIGUSR1);
			}
			break;
		case VAL_STOP:
			printC(!cmd.print_status,"%d\n",VAL_STOP);
			if(cpid!=ppid) {
				__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
						"P: kill()ing child(PID)=%d",cpid);
				kill(cpid,SIGUSR1);
				cpid = ppid;
			}
			break;
		case VAL_UNDEF:
			//printf("UNDEF");
			printC(!cmd.print_status,"%d\n",VAL_UNDEF);
			__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,
					"Undefined input '%s'",input_buffer);
			return -1;
			break;
		}
	}
	/************************************~SPIN-LOCK************************************/


	/**********************************CLEANUP-WRAPUP**********************************/
	munmap(fbuffer,size);
	close(fbfd);
	gettimeofday(&finish,NULL);
	printD("\nAll sys calls successfull\n");
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
								"All sys calls successfull");
	printD("PID = %d | Lifespan = %ldseconds\n",pid,finish.tv_sec-start.tv_sec);
	/*********************************~CLEANUP-WRAPUP**********************************/

	return 0; //End of Program
}
