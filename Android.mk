LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_PACKAGE_NAME := Updater
LOCAL_PRIVILEGED_MODULE := true
LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_SRC_FILES := $(call all-java-files-under, app/src/main)
LOCAL_USE_AAPT2 := true

LOCAL_STATIC_ANDROID_LIBRARIES := \
    com.google.android.material_material \
    androidx.core_core \
    androidx.appcompat_appcompat \

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/app/src/main/res
LOCAL_REQUIRED_MODULES := privapp_whitelist_org.justcaf.otaapp.xml

include $(BUILD_PACKAGE)
include $(CLEAR_VARS)
include $(BUILD_MULTI_PREBUILT)
