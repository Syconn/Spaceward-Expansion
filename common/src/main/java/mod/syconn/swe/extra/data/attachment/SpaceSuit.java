package mod.syconn.swe.extra.data.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.swe.common.dimensions.PlanetManager;
import mod.syconn.swe.common.inventory.ExtendedPlayerInventory;
import mod.syconn.swe.extra.core.FluidHandlerItem;
import mod.syconn.swe.extra.helpers.AnimatorHelper;
import mod.syconn.swe.extra.platform.Services;
import mod.syconn.swe.items.SpaceArmor;
import mod.syconn.swe.network.Network;
import mod.syconn.swe.network.messages.BiBoundUpdateSpaceSuit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class SpaceSuit implements IAttachmentType<SpaceSuit>, Container {

    private boolean parachute;
    private AnimatorHelper chute;
    private int oxygen;
    private SimpleContainer container;

    public SpaceSuit() {
        this.parachute = false;
        this.chute = new AnimatorHelper(20);
        this.oxygen = maxO2();
        this.container = new SimpleContainer(2);
    }

    public SpaceSuit(boolean parachute, AnimatorHelper chute, int oxygen, List<ItemStack> stacks) {
        this.parachute = parachute;
        this.chute = chute;
        this.oxygen = oxygen;
        this.container = new SimpleContainer(stacks.toArray(new ItemStack[0]));
    }

    public Codec<SpaceSuit> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("parachute").forGetter(SpaceSuit::parachute),
                AnimatorHelper.CODEC.fieldOf("chute").forGetter(SpaceSuit::chuteAnim),
                Codec.INT.fieldOf("oxygen").forGetter(SpaceSuit::O2),
                ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(SpaceSuit::getStacks)
        ).apply(instance, SpaceSuit::new));
    }

    public boolean parachute() {
        return parachute;
    }

    public AnimatorHelper chuteAnim() {
        return chute;
    }

    public void parachute(boolean activate, Player player) {
        if (activate) chute.start();
        parachute = activate;
        sync(player);
    }

    public int O2() {
        return oxygen;
    }

    public void decreaseO2(Player p) {
        if (p.getInventory() instanceof ExtendedPlayerInventory ext && Services.FLUID_HANDLER.has(ext.getSpaceUtil().getFirst())) {
            FluidHandlerItem handler = Services.FLUID_HANDLER.get(ext.getSpaceUtil().getFirst());
            if ((SpaceArmor.hasFullKit(p) && handler != null && !handler.getFluidHolder().isEmpty() || PlanetManager.getSettings(p).breathable())) {
                if (oxygen < maxO2()) setO2(oxygen + 1, p);
            } else setO2(new Random().nextInt(2) > 0 ? O2() : O2() - 1, p);
        }
    }

    public int maxO2() {
        return 300;
    }

    public void setO2(int o2, Player player) {
        oxygen = o2;
        sync(player);
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) { // TODO
        CompoundTag t = new CompoundTag();
        t.putBoolean("parachute", parachute);
        t.put("animchute", chute.serializeNBT());
        t.putInt("oxygen", oxygen);
        ContainerHelper.saveAllItems(t, container.getItems(), provider);
        return t;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) { // TODO
        parachute = nbt.getBoolean("parachute");
        chute = new AnimatorHelper(nbt.getCompound("animchute"));
        oxygen = nbt.getInt("oxygen");
        ContainerHelper.loadAllItems(nbt, container.getItems(), provider);
    }

    public int getContainerSize() {
        return container.getContainerSize();
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }

    public ItemStack getItem(int pSlot) {
        return container.getItem(pSlot);
    }

    public ItemStack removeItem(int pSlot, int pAmount) {
        return container.removeItem(pSlot, pAmount);
    }

    public ItemStack removeItemNoUpdate(int pSlot) {
        return container.removeItemNoUpdate(pSlot);
    }

    public void setItem(int pSlot, ItemStack pStack) {
        container.setItem(pSlot, pStack);
    }

    public void setChanged() {
        container.setChanged();
    }

    public boolean stillValid(Player pPlayer) {
        return container.stillValid(pPlayer);
    }

    public void clearContent() {
        container.clearContent();
    }

    public NonNullList<ItemStack> getStacks() {
        return container.getItems();
    }

    private CompoundTag writeSyncedData() {
        CompoundTag t = new CompoundTag();
        t.putBoolean("parachute", parachute);
        t.put("animchute", chute.serializeNBT());
        t.putInt("oxygen", oxygen);
        return t;
    }

    public SpaceSuit readSyncedData(SpaceSuit suit, CompoundTag nbt) {
        suit.parachute = nbt.getBoolean("parachute");
        suit.chute = new AnimatorHelper(nbt.getCompound("animchute"));
        suit.oxygen = nbt.getInt("oxygen");
        return suit;
    }

    private void sync(Player player) {
        BiBoundUpdateSpaceSuit packet = new BiBoundUpdateSpaceSuit(writeSyncedData());
        if (player instanceof ServerPlayer serverPlayer) Network.sendToPlayer(packet, serverPlayer);
        else Network.sendToServer(packet);
    }
}



