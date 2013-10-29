LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS    := -llog

LOCAL_MODULE    := screencast
LOCAL_SRC_FILES := jni_code.c

include $(BUILD_SHARED_LIBRARY)