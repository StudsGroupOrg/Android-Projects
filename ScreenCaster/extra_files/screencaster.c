/*
 * screencaster.c
 *
 *  Created on: Sep 10, 2013
 *      Author: Piyush
 */

#include<android/log.h>
#include<errno.h>
#include<fcntl.h>
#include<linux/fb.h>
#include<stdio.h>
#include<string.h>
#include<stdlib.h>
//#include<malloc.h>
#include<sys/mman.h>
#include<sys/time.h>
#include<unistd.h>
//#include<assert.h>

//#include"zlib.h"
#include"datatypes.h"
#include"declarations.h"

int main(int argc,char* argv[]) {

	/***********************************DECLARATIONS***********************************/
	char data_dir[100],filename[100],input_buffer[50]="";
	extern int root;
	extern int pid,cpid,ppid;
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

	fbfd=open(cmd.data[KEY('z')],O_RDONLY);
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
	}

	//cpid = ppid = pid = getpid();
	//signal(SIGUSR1,sigcatcher);
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

	i=1;
	while(i<=100000) {
		ioct=ioctl(fbfd,FBIOGET_VSCREENINFO,&vscreeninfo);
		if(ioct==-1) {
			__android_log_print(ANDROID_LOG_ERROR,TAG_ERR,
					"VSCREEN-IOCTL failed ioct=%d errno=%d",ioct,errno);
			//return 3;
		}
	printD("%8u %8u | %8u %8u | %8u %8u\r",vscreeninfo.xoffset,vscreeninfo.yoffset,
			vscreeninfo.hsync_len,vscreeninfo.vsync_len,
			vscreeninfo.sync,vscreeninfo.vmode);
	i++;
	}
	/*************************************~VSCREEN*************************************/


	/**************************************FSCREEN*************************************/
	printD("\nFscreen Info:-\n");
	printD(" Device ID : %s\n",fscreeninfo.id);
	printD(" Start of FB physical address : %u\n",fscreeninfo.smem_start);
	printD(" Length of FB : %ld\n",fscreeninfo.smem_len);
	printD(" Length of Line : %ld\n",fscreeninfo.line_length);
	printD(" Start of MMIO physical address : %ld\n",fscreeninfo.mmio_start);
	printD(" Length of MMIO : %ld\n",fscreeninfo.mmio_len);
	/*************************************~FSCREEN*************************************/


	/*************************************SPIN-LOCK************************************/
	/*while(val!=VAL_EXIT && cpid && !cmd.print_status) {
		//fgets(input_buffer,sizeof(input_buffer),stdin);
		//fscanf(stdin,"%s",input_buffer);
		//fgetline();
		//scanf("%s",input_buffer);
		val = VAL_REC;//getTokenValue(input_buffer);
		switch(val) {
		case VAL_CAP:
			printC(!cmd.print_status,"%d\n",VAL_CAP);
			return getFrame(&cmd,fbuffer,size,vscreeninfo.xres,vscreeninfo.yres);
			break;
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
				exit(0);
			} else {	//Child code follows
				gettimeofday(&start,NULL);
				pid = getpid();
				__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
						"C: I am the child(PID)=%d",pid);
				startRecording(&cmd,fbuffer,size);
				//kill(ppid,SIGUSR1);
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
	}*/
	/************************************~SPIN-LOCK************************************/


	/**********************************CLEANUP-WRAPUP**********************************/
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,"Unmap = %d",/*munmap(fbuffer,size)*/0);
	close(fbfd);
	gettimeofday(&finish,NULL);
	printD("\nAll sys calls successfull\n");
	__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
								"All sys calls successfull");
	printD("PID = %d | Lifespan = %ldseconds\n",pid,finish.tv_sec-start.tv_sec);
	/*********************************~CLEANUP-WRAPUP**********************************/

	return 0; //End of Program
}
