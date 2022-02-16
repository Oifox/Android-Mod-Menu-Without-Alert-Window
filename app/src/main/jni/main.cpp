#include <list>
#include <vector>
#include <string.h>
#include <pthread.h>
#include <cstring>
#include <jni.h>
#include <unistd.h>
#include <fstream>
#include <iostream>
#include <string>
#include <pthread.h>
#include <jni.h>
#include <Includes/Utils.h>
#include <Substrate/SubstrateHook.h>
#include "KittyMemory/MemoryPatch.h"
#include <Icon.h>
#include <Chams.h>

using namespace std;
template <typename T>
struct monoArray
{
    void* klass;
    void* monitor;
    void* bounds;
    int   max_length;
    void* vector [1];
    int getLength()
    {
        return max_length;
    }
    T getPointer()
    {
        return (T)vector;
    }
};

std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

using namespace std;
std::string utf16le_to_utf8(const std::u16string &u16str) {
    if (u16str.empty()) { return std::string(); }
    const char16_t *p = u16str.data();
    std::u16string::size_type len = u16str.length();
    if (p[0] == 0xFEFF) {
        p += 1;
        len -= 1;
    }

    std::string u8str;
    u8str.reserve(len * 3);

    char16_t u16char;
    for (std::u16string::size_type i = 0; i < len; ++i) {

        u16char = p[i];

        if (u16char < 0x0080) {
            u8str.push_back((char) (u16char & 0x00FF));
            continue;
        }
        if (u16char >= 0x0080 && u16char <= 0x07FF) {
            u8str.push_back((char) (((u16char >> 6) & 0x1F) | 0xC0));
            u8str.push_back((char) ((u16char & 0x3F) | 0x80));
            continue;
        }
        if (u16char >= 0xD800 && u16char <= 0xDBFF) {
            uint32_t highSur = u16char;
            uint32_t lowSur = p[++i];
            uint32_t codePoint = highSur - 0xD800;
            codePoint <<= 10;
            codePoint |= lowSur - 0xDC00;
            codePoint += 0x10000;
            u8str.push_back((char) ((codePoint >> 18) | 0xF0));
            u8str.push_back((char) (((codePoint >> 12) & 0x3F) | 0x80));
            u8str.push_back((char) (((codePoint >> 06) & 0x3F) | 0x80));
            u8str.push_back((char) ((codePoint & 0x3F) | 0x80));
            continue;
        }
        {
            u8str.push_back((char) (((u16char >> 12) & 0x0F) | 0xE0));
            u8str.push_back((char) (((u16char >> 6) & 0x3F) | 0x80));
            u8str.push_back((char) ((u16char & 0x3F) | 0x80));
            continue;
        }
    }

    return u8str;
}

typedef struct _monoString {
    void *klass;
    void *monitor;
    int length;

    const char *toChars(){
        u16string ss((char16_t *) getChars(), 0, getLength());
        string str = utf16le_to_utf8(ss);
        return str.c_str();
    }

    char chars[0];

    char *getChars() {
        return chars;
    }

    int getLength() {
        return length;
    }
    std::string get_string() {
        
        return std::string(toChars());
    }
} monoString;

monoString *CreateMonoString(const char *str) {
    monoString *(*String_CreateString)(void *instance, const char *str) = (monoString *(*)(void *, const char *))getAbsoluteAddress("libil2cpp.so", 0x96CB6C); 

    return String_CreateString(NULL, str);
}

//     #########
//    ##	   ##
//	  #			
//	  #
//	  #			
//	  ##	   ##
//	   #########

extern "C" {
/* C CODE START */
const char *libName = "libil2cpp.so";

struct My_Patches {MemoryPatch Noclip, MoveBT, BuyZone, Attach;} hexPatches;

bool noclip, exitmenu, movebeforetimer, buyzone, fov, attachments, allduck = false;
int fov_value = 60;

bool chams, wireframe, outline, glow = false;
int rc, gc, bc = 1;
int orr2, ogg2, obb2 = 1;
int gr, gg, gb = 1;
int wwidth, owidth = 1;

bool karambit, butterfly, bayonet = false;
bool giveall = false;

string mapname = "bloxpoligom";
bool gomap, gamemode = false;

int smscount = 1;
bool chatfor = false;
string textChat = "TELEGRAM: @INSINETEAMDEV - ЛУЧШИЕ ЧИТЫ НА БЛОКПОСТ МОБАЙЛ";
bool isauth = false;
string token = "Null";

std::string to_string(int param)
{
    std::string str = "";
    for(str = ""; param ; param /= 10)
        str += (char)('0' + param % 10);
    reverse(str.begin(), str.end());
	if (str == "") str = "0";
    return str;
}

std::string newPage (std::string name, std::string icon) {
	return ("NEWPAGE`" + name + "`" + icon);
}

std::string newText (int pageid, int featureid, std::string text) {
	return ("TEXT`" + to_string(pageid) + "`" + to_string(pageid) + "`" + text);
}
	
std::string newLink (int pageid, std::string text, std::string link) {
	return ("LINK`" + to_string(pageid) + "`" + text + "`" + link);
}

std::string newSwitch (int pageid, int featureid, std::string text) {
	return ("SWITCH`" + to_string(pageid) + "`" + to_string(featureid) + "`" + text);
}

std::string newCheck (int pageid, int featureid, std::string text) {
	return ("CHECK`" + to_string(pageid) + "`" + to_string(featureid) + "`" + text);
}

std::string newButton (int pageid, int featureid, std::string text) {
	return ("BUTTON`" + to_string(pageid) + "`" + to_string(featureid) + "`" + text);
}

std::string newSlider (int pageid, int featureid, std::string text, int min, int max, int progress) {
	return ("SLIDER`" + to_string(pageid) + "`" + to_string(featureid) + "`" + text + "`" + to_string(min) + "`" + to_string(max) + "`" + to_string(progress));
}

std::string newInputStr (int pageid, int featureid, std::string text) {
	return ("INPUT_STR`" + to_string(pageid) + "`" + to_string(featureid) + "`" + text);
}

JNIEXPORT jstring JNICALL Java_il2cpp_typefaces_Menu_resultToken(JNIEnv *env, jobject activityObject) {
	return env->NewStringUTF(token.c_str());
}

JNIEXPORT jobjectArray JNICALL Java_il2cpp_Main_getFeatures(JNIEnv *env, jobject activityObject) {
	jobjectArray ret;
	const char *features[] = {
		newPage("Players", "icon1.png").c_str(),
		newPage("Visuals", "icon2.png").c_str(),
		newPage("Skinchanger", "icon3.png").c_str(),
		newPage("Map hack", "icon4.png").c_str(),
		newPage("Chat hack", "icon5.png").c_str(),
		newPage("TikTok", "icon6.png").c_str(),
		
		// PLAYERS 0
		newText(0, 0, "Movement").c_str(),
		newSwitch(0, 10, "Noclip (kick)").c_str(),
		newSwitch(0, 11, "Move before timer").c_str(),
		newSwitch(0, 12, "Buy zone").c_str(),
		
		newText(0, 0, "Gui funcions").c_str(),
		newSlider(0, 13, "Fov value", 1, 200, 60).c_str(),
		newSwitch(0, 14, "Set camera fov").c_str(),
		newSwitch(0, 15, "Unlock attachments").c_str(),
		newButton(0, 16, "Exit to menu").c_str(),
		
		newText(0, 0, "Local visual").c_str(),
		newSwitch(0, 17, "Fake duck players").c_str(),
		newSwitch(0, 51, "No anim players").c_str(),
		// VISUALS 1
		
		newText(1, 0, "Wallhack").c_str(),
		newCheck(1, 18, "Wallhack").c_str(),
		
		newSlider(1, 19, "Wireframe Red", 1, 255, 1).c_str(),
		newSlider(1, 20, "Wireframe Green", 1, 255, 1).c_str(),
		newSlider(1, 21, "Wireframe Blue", 1, 255, 1).c_str(),
		
		newText(1, 0, "Wireframe wallhack").c_str(),
		newCheck(1, 22, "Wireframe").c_str(),
		newSlider(1, 23, "Width line", 1, 10, 1).c_str(),
		
		newText(1, 0, "Outline wallhack").c_str(),
		newCheck(1, 24, "Outline").c_str(),
		newSlider(1, 61, "Outline Red", 1, 255, 1).c_str(),
		newSlider(1, 62, "Outline Green", 1, 255, 1).c_str(),
		newSlider(1, 63, "Outline Blue", 1, 255, 1).c_str(),
		newSlider(1, 25, "Width line", 1, 10, 1).c_str(),
		
		newText(1, 0, "Glow wallhack").c_str(),
		newSlider(1, 64, "Glow Red", 1, 255, 1).c_str(),
		newSlider(1, 65, "Glow Green", 1, 255, 1).c_str(),
		newSlider(1, 66, "Glow Blue", 1, 255, 1).c_str(),
		newCheck(1, 26, "Glow").c_str(),
		
		// SKINCHANGER 2
		
		newText(2, 0, "Knifes").c_str(),
		newButton(2, 27, "Karambit").c_str(),
		newButton(2, 28, "Butterfly").c_str(),
		newButton(2, 29, "M9 Bayonet").c_str(),
		//newInputStr(2, 50, "Skin ID").c_str(),
		newButton(2, 52, "Give all weapons").c_str(),
		
		// MAP HACK 3
		newText(3, 0, "Map changer hack").c_str(),
		newInputStr(3, 30, "Map name").c_str(),
		newButton(3, 31, "Go to map").c_str(),
		newSwitch(3, 32, "Change map create gamemode").c_str(),
		
		// CHAT HACK 4
		newText(4, 0, "Spam chat hack").c_str(),
		newInputStr(4, 33, "Spam text").c_str(),
		newSlider(4, 36, "Messages count", 1, 10000000, 1).c_str(),
		newCheck(4, 37, "Messages multiple").c_str(),
		newButton(4, 70, "Token to message").c_str(),
		newInputStr(4, 71, "Acc token").c_str(),
		newButton(4, 72, "Auth token").c_str(),
		
		// TIKTOK 5
		newText(5, 0, "Выкладывай видео с читом в тикток, указывая @insineteamdev и ставив тег #isnineteamdev и сможешь попасть сюда!").c_str(),
		newText(5, 0, "Marceldosvyzev").c_str(),
		newLink(5, "TIKTOK 1", "https://vm.tiktok.com/ZSeq5bakE/").c_str(),
		newLink(5, "TIKTOK 2", "https://vm.tiktok.com/ZSebpa2Xs/").c_str(),
		newText(5, 0, "Ebanina-Kozla").c_str(),
		newLink(5, "TIKTOK 1", "https://vm.tiktok.com/ZSeqafyPA/").c_str(),
		newText(5, 0, "Матье бал").c_str(),
		newLink(5, "TIKTOK 1", "https://vm.tiktok.com/ZSebpVs1k/").c_str(),
		newText(5, 0, "Govno_bobra 1337").c_str(),
		newLink(5, "TIKTOK 1 (Разьеб нахуй)", "https://vm.tiktok.com/ZSebpHV4t/").c_str(),
		newLink(5, "TIKTOK 2", "https://vm.tiktok.com/ZSebpW4f1/").c_str(),
		newText(5, 0, "IvanZolo248").c_str(),
		newLink(5, "TIKTOK 1", "https://vm.tiktok.com/ZSebpQFEn/").c_str()
	};
	
	int Total_Feature = (sizeof features / sizeof features[0]); 
	ret = (jobjectArray) env->NewObjectArray(Total_Feature, env->FindClass("java/lang/String"), env->NewStringUTF(""));
	int i;
	for (i = 0; i < Total_Feature; i++)
		env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));
	return (ret);
} 

void hexChange(bool &var, MemoryPatch &patch) {
	var = !var;
	if (var) {
		patch.Modify();
	} else {
		patch.Restore();
	}
}

void updateChams() {
	SetWallhack(chams);
	SetWireframe(wireframe);
	SetGlow(glow);
	SetOutline(outline);
	SetOR(orr2);SetOG(ogg2);SetOB(obb2);
	SetGR(gr);SetGG(gg);SetGB(gb);
	SetR(rc);SetG(gc);SetB(bc);SetWireframeWidth(wwidth);SetOutlineWidth(owidth);
}

JNIEXPORT void JNICALL Java_il2cpp_Main_onCheckTap(JNIEnv *env, jobject activityObject, jint page, jint feature, jint checked) {
    // On checkbox //
	switch (feature) {
		case 18:
			chams = !chams;
			updateChams();
			break;
		case 22:
			wireframe = !wireframe;
			updateChams();
			break;
		case 24:
			outline = !outline;
			updateChams();
			break;
		case 26:
			glow = !glow;
			updateChams();
			break;
		case 37:
			chatfor = !chatfor;
			break;
	}
}

JNIEXPORT void JNICALL Java_il2cpp_Main_onSwitchTap(JNIEnv *env, jobject activityObject, jint page, jint feature, jint checked) {
    // On switch //
	switch (feature) {
		case 10:
			hexChange(noclip, hexPatches.Noclip);
			break;
		case 11:
			hexChange(movebeforetimer, hexPatches.MoveBT);
			break;
		case 12:
			hexChange(buyzone, hexPatches.BuyZone);
			break;
		case 14:
			fov = !fov;
			break;
		case 15:
			hexChange(attachments, hexPatches.Attach);
			break;
		case 17:
			allduck = !allduck;
			break;
		case 32:
			gamemode = !gamemode;
			break;
	}
}

JNIEXPORT void JNICALL Java_il2cpp_Main_onButtonTap(JNIEnv *env, jobject activityObject, jint page, jint feature) {
    // On tap button //
	switch (feature) {
		case 16:
			exitmenu = true;
			break;
		case 27:
			karambit = true;
			break;
		case 28:
			butterfly = true;
			break;
		case 29:
			bayonet = true;
			break;
		case 31:
			gomap = true;
			break;
		case 52:
			giveall = true;
			break;
		case 70:
			textChat = token;
			break;
	}
}

JNIEXPORT void JNICALL Java_il2cpp_Main_onTextChange(JNIEnv *env, jobject activityObject, jint page, jint feature, jstring value) {
    // On input //
	string val = jstring2string(env, value);
	switch (feature) {
		case 30:
			mapname = val;
			break;
		case 33:
			textChat = val;
			break;
	}
}

JNIEXPORT void JNICALL Java_il2cpp_Main_onSliderChange(JNIEnv *env, jobject activityObject, jint page, jint feature, jint value) {
    // On slider //
	switch (feature) {
		case 13:
			fov_value = value;
			break;
		case 19:
			rc = value;
			break;
		case 20:
			gc = value;
			break;
		case 21:
			bc = value;
			break;
		case 23:
			wwidth = value;
			break;
		case 25:
			owidth = value;
			break;
		case 36:
			smscount = value;
			break;
		case 61:
			orr2 = value;
			break;
		case 62:
			ogg2 = value;
			break;
		case 63:
			obb2 = value;
			break;
		case 64:
			gr = value;
			break;
		case 65:
			gg = value;
			break;
		case 66:
			gb = value;
			break;
	}
	updateChams();
}

/* C CODE END */
}

// ---------- Hooking ---------- //

namespace Offsets{
	enum Ofssets {
		Noclip        = 0x0,
		MoveBT        = 0x0,
		BuyZone       = 0x0,
		SetFov        = 0x0,
		Update1       = 0x0,
		ExitMenu      = 0x0,
		LoadMap       = 0x10,
		SpamChat      = 0xFF,
		ChatUpdate    = 0x0,
		PlayerUpdate  = 0x0,
		StartServer   = 0xF,
		UnlockAttach  = 0x1,
	};
}

void (*old_startserv) (void *instance, void* ps);
void startserv (void *instance, void* ps) {
	if (instance) {
		if (gamemode) {
			*(monoString* *) ((uint64_t) ps + 0xC)  = CreateMonoString(mapname.c_str());
			*(monoString* *) ((uint64_t) ps + 0x14) = CreateMonoString(mapname.c_str());
			*(monoString* *) ((uint64_t) ps + 0x1C)  = CreateMonoString(mapname.c_str());
			*(monoString* *) ((uint64_t) ps + 0x24) = CreateMonoString(mapname.c_str());
			*(monoString* *) ((uint64_t) ps + 0x2C)  = CreateMonoString(mapname.c_str());
		}
	}
	old_startserv(instance, ps);
}

void (*loadMap) (void* instance, monoString* mapnames);
void (*exitMenu) (void* instance);

void (*addItem) (void* instance, int type, int wid, int uid, int menu, monoString* sign, int cat);
void (*addPrime) (void* instance);
void (*selectItem) (void* instance, int id);

void (*sendMessage) (void *instance, int team, monoString* text);

void* (*old_addDefault) (void* instance);
void addDefault (void* instance) {
	if (instance) {
		if (karambit) {
			karambit = false;
			addPrime(instance);
		}
		if (butterfly) {
			butterfly = false;
			addItem(instance, 0, 69, 777, 0, CreateMonoString("_"), 0);
		}
		if (bayonet) {
			bayonet = false;
			addItem(instance, 0, 72, 777, 0, CreateMonoString("_"), 0);
		}
		if (giveall) {
			giveall = false;
			for (int id = 0; id <= 100; id++) {
				if (id != 3 && id != 69 && id != 72) addItem(instance, 0, id, 777, 0, CreateMonoString("_"), 0);
			}
		}
	}
	old_addDefault(instance);
}

void (*old_player_update) (void *instance, void* pl);
void player_update (void *instance, void* pl) {
	if (instance) {
		if (allduck)
			*(bool *) ((uint64_t) pl + 0xA5) = true;
		else
			*(bool *) ((uint64_t) pl + 0xA5) = false;
	}
    old_player_update(instance,  pl);
}

void *lc;
void (*old_getLocal) (void *instance, void *pl);
void getLocal (void *instance, void *pl) {
	if (instance) lc = pl;
	old_getLocal(instance, pl);
}

void (*old_chatmsg) (void *instance, int t, monoString* text);
void chatmsg (void *instance, int t, monoString* text) {
	old_chatmsg(instance, t, text);
	if (lc) text = *(monoString**) ((uint64_t) lc + 0xC);
	if (instance && chatfor) {
		for (int i = 0; i < smscount; i++) {
			old_chatmsg(instance, t, CreateMonoString(textChat.c_str()));
		}
	}
}

void (*old_fovset) (float fovv,  float speed);
void fovset(float fovv, float speed) {
	if (fov)
		old_fovset(fov_value, 120);
	else
		old_fovset(fovv, speed);
}

void (*old_update1) (void* instance);
void update1 (void* instance) {
	if (instance) {
		if (gomap) {
			gomap = false;
			loadMap(instance, CreateMonoString(mapname.c_str()));
		}
		if (exitmenu) {
			exitmenu = false;
			exitMenu(instance);
		}
	}
	
	old_update1(instance);
}

void *hack_thread(void *) {
    
    ProcMap il2cppMap;
    do {
        il2cppMap = KittyMemory::getLibraryMap(libName);
        sleep(1);
    } while (!isLibraryLoaded(libName) && mlovinit());
		sleep(2);
	
    // ---------- Hook ---------- //
    
	// Wallhack
	setShader("unity_SHC");
	LogShaders();
	Wallhack();
	updateChams();
	
    // ---------- Hook ---------- //
    
	MSHookFunction((void *) getAbsoluteAddress(libName, 0x10), (void *) addDefault, (void **) &old_addDefault);
	
	addItem = (void (*)(void*, int, int, int, int, monoString*, int)) getAbsoluteAddress("libil2cpp.so", 0x10);
	addPrime = (void (*)(void*)) getAbsoluteAddress("libil2cpp.so", 0x10D);
	
	sendMessage = (void (*)(void*, int, monoString*)) getAbsoluteAddress("libil2cpp.so", 0x1);
	
	
	loadMap  = (void (*)(void*, monoString*)) getAbsoluteAddress("libil2cpp.so", Offsets::LoadMap);
	exitMenu = (void (*)(void*)) getAbsoluteAddress("libil2cpp.so", Offsets::ExitMenu);
	
	hexPatches.Noclip = MemoryPatch::createWithHex("libil2cpp.so", Offsets::Noclip, "00 00 A0 E3 1E FF 2F E1");
	hexPatches.MoveBT = MemoryPatch::createWithHex("libil2cpp.so", Offsets::MoveBT, "00 00 A0 E3 1E FF 2F E1");
	hexPatches.BuyZone = MemoryPatch::createWithHex("libil2cpp.so", Offsets::BuyZone, "00 00 A0 E3 1E FF 2F E1");
	hexPatches.Attach = MemoryPatch::createWithHex("libil2cpp.so", Offsets::UnlockAttach, "00 00 A0 E3 1E FF 2F E1");
	
	MSHookFunction((void *) getAbsoluteAddress(libName, Offsets::SetFov), (void *) fovset, (void **) &old_fovset);	
	MSHookFunction((void *) getAbsoluteAddress(libName, Offsets::Update1), (void *) update1, (void **) &old_update1);	
	MSHookFunction((void *) getAbsoluteAddress(libName, Offsets::SpamChat), (void *) chatmsg, (void **) &old_chatmsg);	
	MSHookFunction((void *) getAbsoluteAddress(libName, Offsets::PlayerUpdate), (void *) player_update, (void **) &old_player_update);	
	MSHookFunction((void *) getAbsoluteAddress(libName, Offsets::StartServer), (void *) startserv, (void **) &old_startserv);	
	
	MSHookFunction((void *) getAbsoluteAddress(libName, 0x182828283), (void *) getLocal, (void **) &old_getLocal);	
	
	return NULL;
}

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *globalEnv;
    vm->GetEnv((void **) &globalEnv, JNI_VERSION_1_6);

    pthread_t ptid;
    pthread_create(&ptid, NULL, hack_thread, NULL);

    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
JNI_OnUnload(JavaVM *vm, void *reserved) {}
