import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext;

public class OpenALTest {
    public static void main(String[] args) {
        // 打开默认设备
        long device = alcOpenDevice((ByteBuffer) null);
        System.out.println("device: " + device);

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        // 创建上下文
        long context = alcCreateContext(device, (IntBuffer) null);
        System.out.println("context: " + context);


        boolean useTLC = deviceCaps.ALC_EXT_thread_local_context && alcSetThreadContext(context);
        if (!useTLC) {
            if (!alcMakeContextCurrent(context)) {
                throw new IllegalStateException();
            }
        }

        ALCapabilities caps = AL.createCapabilities(deviceCaps, MemoryUtil::memCallocPointer);


        // 激活上下文
        alcMakeContextCurrent(context);


        IntBuffer buffer = IntBuffer.allocate(2048);
        IntBuffer source = IntBuffer.allocate(2048);

        alGenBuffers(buffer);
        alGenSources(source);


        // 销毁上下文
        alcDestroyContext(context);
        // 关闭设备
        alcCloseDevice(device);
    }
}
