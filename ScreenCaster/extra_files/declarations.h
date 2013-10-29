/*
 * declarations.h
 *
 *  Created on: Sep 10, 2013
 *      Author: Piyush
 */

#include"datatypes.h"

#ifndef DECLARATIONS_H_
#define DECLARATIONS_H_

/* Set to 1 to enable(and compile)
 * debugging prints printD()
 * else set to 0 (not compiled)
 */
#define FLAG_DEBUG  1

#define BUFCHUNK  4096

#define DEF_DIR_FRAME	"/mnt/sdcard/ScreenCaster/images"
#define DEF_DIR_VIDEO	"/mnt/sdcard/ScreenCaster/videos"
#define DEF_FBDEV		"/dev/graphics/fb0"
#define DEF_FRAME_CAP	1
#define DEF_IP_ADDR		"192.168.2.3"
#define DEF_IP_PORT		50000
#define DEF_VIDEO_REC	10 //seconds

#define KEY(x) (x-'a')

#define TAG_ERR   "CustomError"
#define TAG_INFO  "CustomInfo"
#define TAG_WARN  "CustomWarning"
#define TAG_DBUG  "CustomDebug"

#define TOK_EXIT "EXIT"
#define TOK_CAP  "CAPT"
#define TOK_CST  "CAST"
#define TOK_REC  "RECD"
#define TOK_STOP "STOP"

#define VAL_EXIT  0
#define VAL_CAP   1
#define VAL_CST   2
#define VAL_REC   3
#define VAL_STOP  4
#define VAL_UNDEF 9

/*
 * Conditional printfs
 */
#define printC(x,...) if(x)	printf(__VA_ARGS__)

/*
 * Debugging printfs
 */
#if FLAG_DEBUG
  #define printD(...)	printf(__VA_ARGS__)
#else
  #define printD(...)	;
#endif


/*
 * Function declarations follow
 */
void displayAppInfo(CommandData*);
void displayAppUsage(CommandData*);
void getFBDev(int,char**,char*);
int  getFrame(CommandData*,unsigned int*,long,long,long);
void getFramesDirectory(int,char**,char*);
void getVideosDirectory(int,char**,char*);
int  getTokenValue(char*);
void initCommandData(CommandData*);
int  isSilentMode(int,char**);
int recorder(int,char**); ///Special!! - Copy of main();
int  setCommandData(CommandData*,int,char**);
void sigcatcher(int);
void startRecording(CommandData*,unsigned int*,long);

#endif /* DECLARATIONS_H_ */
