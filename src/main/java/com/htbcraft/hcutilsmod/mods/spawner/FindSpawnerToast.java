package com.htbcraft.hcutilsmod.mods.spawner;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;

public class FindSpawnerToast implements IToast {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation SPAWNER_ICON = new ResourceLocation("textures/block/spawner.png");
    private static final LinkedHashSet<BlockPos> blockPosList = new LinkedHashSet<BlockPos>();

    private final BlockPos blockPos;
    private final Boolean visibility;

    public FindSpawnerToast(BlockPos blockPos) {
        this.blockPos = blockPos;

        // 表示している座標との重複をチェックするためにLinkedHashSetを使う
        this.visibility = blockPosList.add(blockPos);
    }

    @Override
    public Visibility draw(ToastGui toastGui, long delta) {
        // すでに表示している座標の場合は即非表示
        if (!this.visibility) {
            LOGGER.info("Already displayed!! " + this.blockPos);
            return Visibility.HIDE;
            // HIDE を返すと音が鳴るのがイケてない。
        }

        // 設定されている時間だけ表示
        if (delta > (HCSettings.getInstance().timeFindSpawner * 1000L)) {
            LOGGER.info("Time up!! " + this.blockPos);
            blockPosList.remove(this.blockPos);
            return Visibility.HIDE;
        }

        // トーストの枠
        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        toastGui.blit(0, 0, 0, 0, 160, 32);

        // スポナーのアイコン
        toastGui.getMinecraft().getTextureManager().bindTexture(SPAWNER_ICON);
        RenderSystem.enableBlend();
        AbstractGui.blit(6, 6, 0, 0, 0, 20, 20, 20, 20);
        RenderSystem.enableBlend();

        // 見つけたスポナーの座標
        String text = "X:" + this.blockPos.getX() + " Y:" + this.blockPos.getY() + " Z:" + this.blockPos.getZ();
        toastGui.getMinecraft().fontRenderer.drawString(I18n.format("hcutilsmod.findspawner.text"), 30.0F, 7.0F, 14737632);
        toastGui.getMinecraft().fontRenderer.drawString(text, 36.0F, 18.0F, 14737632);

        return Visibility.SHOW;
    }
}
