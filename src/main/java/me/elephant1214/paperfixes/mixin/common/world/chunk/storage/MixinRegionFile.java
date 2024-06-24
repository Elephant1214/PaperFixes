package me.elephant1214.paperfixes.mixin.common.world.chunk.storage;

import me.elephant1214.paperfixes.PaperFixes;
import net.minecraft.world.chunk.storage.RegionFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Mixin(RegionFile.class)
public abstract class MixinRegionFile {
    @Shadow
    private RandomAccessFile dataFile;
    @Unique
    private IntBuffer paperFixes$header;

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/io/RandomAccessFile;seek(J)V",
                    ordinal = 0
            )
    )
    private void invokeReadHeader(RandomAccessFile instance, long pos) throws IOException {
        this.dataFile.seek(pos);
        this.paperFixes$header = paperFixes$headerAsInts();
    }

    @Unique
    private IntBuffer paperFixes$headerAsInts() {
        ByteBuffer header = ByteBuffer.allocate(8192);
        while (header.hasRemaining()) {
            try {
                if (this.dataFile.getChannel().read(header) == -1)
                    throw new EOFException();
            } catch (IOException e) {
                PaperFixes.LOGGER.error(e);
            }
        }
        header.clear();
        return header.asIntBuffer();
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/io/RandomAccessFile;readInt()I",
                    ordinal = 0
            )
    )
    private int readHeaderAsInts0(RandomAccessFile raf) {
        return this.paperFixes$header.get();
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/io/RandomAccessFile;readInt()I",
                    ordinal = 2
            )
    )
    private int readHeaderAsInts2(RandomAccessFile raf) {
        return this.paperFixes$header.get();
    }
}
