/*
 * jni_defs.c
 *
 *  Created on: Sep 14, 2013
 *      Author: Piyush
 */
#include<android/log.h>
#include<errno.h>
#include<fcntl.h>
#include<linux/fb.h>
#include<stdlib.h>
#include<sys/mman.h>

#include"jni_head.h"

#define TAG	"Jni_Code.c"

int num_of_buffers=0;

struct fb_fix_screeninfo fscreeninfo;
struct fb_var_screeninfo vscreeninfo;

/*
 * Frame buffer data initializer
 */
int initialize(const char* fb) {
	int fbfd,ioct;

	fbfd=open(fb,O_RDONLY);
	if(fbfd==-1) {
		__android_log_print(ANDROID_LOG_ERROR,TAG,
			"Could not open framebuffer fbfd=%d errno=%d",fbfd,errno);
		return -10;
	}
	ioct=ioctl(fbfd,FBIOGET_FSCREENINFO,&fscreeninfo);
	if(ioct==-1) {
		__android_log_print(ANDROID_LOG_ERROR,TAG,
				"FSCREEN-IOCTL failed ioct=%d errno=%d",ioct,errno);
		return -10;
	}
	ioct=ioctl(fbfd,FBIOGET_VSCREENINFO,&vscreeninfo);
	if(ioct==-1) {
		__android_log_print(ANDROID_LOG_ERROR,TAG,
				"VSCREEN-IOCTL failed ioct=%d errno=%d",ioct,errno);
		return -10;
	}

	num_of_buffers = fscreeninfo.smem_len /
					(vscreeninfo.xres *	vscreeninfo.yres *
						(vscreeninfo.bits_per_pixel/8) );

	return fbfd;
}

/*
 * Class:     studs_group_in_screencaster_device_Native
 * Method:    haveRoot
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_studs_group_in_screencaster_device_Native_haveRoot
	(JNIEnv* env,jclass cls) {
	return setuid(0);
}

/*
 * Class:     studs_group_in_screencaster_device_Native
 * Method:    getFBPortions
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_studs_group_in_screencaster_device_Native_getNumberOfPortions
(JNIEnv* env,jclass cls,jstring fbdev) {
	const char* fb = (*env)->GetStringUTFChars(env,fbdev,0);
	int fbfd;

	fbfd=initialize(fb);
	if(fbfd==-1 || num_of_buffers==0) {
		(*env)->ReleaseStringUTFChars(env,fbdev,fb);
		return -1;
	}
	(*env)->ReleaseStringUTFChars(env,fbdev,fb);

	return num_of_buffers;
}

/*
 * Class:     studs_group_in_screencaster_device_Native
 * Method:    getCurrent
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_studs_group_in_screencaster_device_Native_getCurrent
	(JNIEnv* env,jclass cls,jstring fbdev) {
	const char* fb = (*env)->GetStringUTFChars(env,fbdev,0);
	int fbfd,curr;

	fbfd=initialize(fb);
	if(fbfd==-1 || num_of_buffers==0) {
		(*env)->ReleaseStringUTFChars(env,fbdev,fb);
		return -1;
	}

	curr = vscreeninfo.yoffset/vscreeninfo.yres;
	//__android_log_print(ANDROID_LOG_INFO,TAG_INFO,
	//		"FB=%s getCurr : curr=%d yoff=%u yres=%u",fb,curr,vscreeninfo.yoffset,vscreeninfo.yres);
	(*env)->ReleaseStringUTFChars(env,fbdev,fb);
	close(fbfd);
	return curr;
}

/*
 * Class:     studs_group_in_screencaster_device_Native
 * Method:    destroySharedMemory
 * Signature: (Ljava/nio/ByteBuffer;J)I
 */
JNIEXPORT jint JNICALL Java_studs_group_in_screencaster_device_Native_destroySharedMemory
(JNIEnv* env,jclass cls,jobject byt_buff,jlong size) {
	unsigned int* fbuffer = (*env)->GetDirectBufferAddress(env,byt_buff);
	if(size==0) {
		return -1;
	} else {
		__android_log_print(ANDROID_LOG_INFO,TAG,
					"destroyShm : addr=%u size=%ld",fbuffer,size);
		return munmap(fbuffer,size);
	}
}

/*
 * Class:     studs_group_in_screencaster_device_Native
 * Method:    getFBPortion
 * Signature: (Ljava/lang/String;I)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_studs_group_in_screencaster_device_Native_getFBPortion
	(JNIEnv* env,jclass cls,jstring fbdev,jint portion) {
	unsigned int* fbuffer;
	const char* fb = (*env)->GetStringUTFChars(env,fbdev,0);
	int fbfd,ioct;
	long size,offset,screen;

	fbfd=initialize(fb);
	if(fbfd==-1 || num_of_buffers==0) {
		return NULL;
	}

	size = fscreeninfo.smem_len;
	screen = size/num_of_buffers;
	offset = portion*screen;

	fbuffer=(unsigned int*)mmap(0,screen,PROT_READ,MAP_SHARED,fbfd,offset);
	close(fbfd);
	__android_log_print(ANDROID_LOG_INFO,TAG,"FB=%s getFBH : addr=%u size=%ld",fb,fbuffer,screen);
	(*env)->ReleaseStringUTFChars(env,fbdev,fb);

	if(fbuffer!=NULL) {
		return (*env)->NewDirectByteBuffer(env,fbuffer,screen);
	} else {
		return NULL;
	}
}
