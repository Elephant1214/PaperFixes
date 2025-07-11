package me.elephant1214.paperfixes.mixin.common.world.chunk.no_io_sleep;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.elephant1214.paperfixes.configuration.PaperFixesConfig;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThreadedFileIOBase.class)
public abstract class ThreadedFileIOBaseMixin implements Runnable {
    @WrapOperation(method = "processQueue", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V", ordinal = 0))
    private void onlySleepIfEnabled(long time, Operation<Void> original) {
        if (PaperFixesConfig.performance.ioThreadSleep) {
            original.call(2L);
        }
    }
}
