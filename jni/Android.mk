LOCAL_PATH := $(call my-dir)
LIBPLAYER_PATH := $(LOCAL_PATH)/../../LibPlayer

include $(CLEAR_VARS)

LOCAL_MODULE    := libamplayerjni
LOCAL_SRC_FILES := amlogic_playerservice_AmPlayer.c sys_conf.c
LOCAL_ARM_MODE := arm
LOCAL_C_INCLUDES := $(LIBPLAYER_PATH)/amplayer/player/include \
    $(LIBPLAYER_PATH)/amplayer/control/include \
    $(LIBPLAYER_PATH)/amcodec/include \
    $(LIBPLAYER_PATH)/amffmpeg \
    $(JNI_H_INCLUDE) 

LOCAL_MODULE_PATH := ${LOCAL_PATH}/../lib
LOCAL_STATIC_LIBRARIES := libamplayer libamcontroler libamcodec libavformat libavcodec libavutil libamadec 
LOCAL_SHARED_LIBRARIES += libutils libmedia libz

LOCAL_PRELINK_MODULE := false

include $(BUILD_SHARED_LIBRARY)
