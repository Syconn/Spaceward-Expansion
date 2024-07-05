package mod.syconn.swe.world.data;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.CompoundTag;

public class PixelImage {

    private int height;
    private int width;
    private int[][] pixels;

    public PixelImage(NativeImage image) {
        this.height = image.getHeight();
        this.width = image.getWidth();
        this.pixels = getPixelFromImage(image);
    }

    public PixelImage(int height, int width, int[][] pixels) {
        this.height = height;
        this.width = width;
        this.pixels = pixels;
    }

    public int[][] getPixelFromImage(NativeImage image){
        int[][] output = new int[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                output[x][y] = image.getPixelRGBA(x, y);
            }
        }
        return output;
    }

    public DynamicTexture getImageFromPixels(){
        NativeImage output = new NativeImage(width, height, true);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                output.setPixelRGBA(x, y, pixels[x][y]);
            }
        }
        return new DynamicTexture(output);
    }

    public static PixelImage read(CompoundTag tag){
        int[][] pixels = new int[tag.getInt("width")][tag.getInt("height")];
        for (int x = 0; x < tag.getInt("width"); x++) {
            for (int y = 0; y < tag.getInt("height"); y++) {
                pixels[x][y] = tag.getInt(x + "_" + y);
            }
        }
        return new PixelImage(tag.getInt("width"), tag.getInt("height"), pixels);
    }

    public CompoundTag write(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("width", width);
        tag.putInt("height", height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tag.putInt(x + "_" + y, pixels[x][y]);
            }
        }

        return tag;
    }
}
