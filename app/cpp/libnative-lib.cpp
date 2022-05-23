#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_nnss_dev_bfta_App_baseUrl(JNIEnv *env, jobject thiz) {
    std::string url = "Null";
    url = "https://us-central1-bluefletch-learning-assignment.cloudfunctions.net/";
    return env->NewStringUTF(url.c_str());
}