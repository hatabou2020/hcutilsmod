package com.htbcraft.hcutilsmod.mods.spawner;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.matrix.MatrixStack;
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
    public Visibility func_230444_a_(MatrixStack p_230444_1_, ToastGui p_230444_2_, long p_230444_3_) {
        // すでに表示している座標の場合は即非表示
        if (!this.visibility) {
            LOGGER.info("Already displayed!! " + this.blockPos);
            return Visibility.HIDE;
            // HIDE を返すと音が鳴るのがイケてない。
        }

        // 設定されている時間だけ表示
        if (p_230444_3_ > (HCSettings.getInstance().timeFindSpawner * 1000L)) {
            LOGGER.info("Time up!! " + this.blockPos);
            blockPosList.remove(this.blockPos);
            return Visibility.HIDE;
        }

        // トーストの枠
        p_230444_2_.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        p_230444_2_.blit(p_230444_1_, 0, 0, 0, 0, this.func_230445_a_(), this.func_238540_d_());

        // スポナーのアイコン
        p_230444_2_.getMinecraft().getTextureManager().bindTexture(SPAWNER_ICON);
        RenderSystem.enableBlend();
        AbstractGui.blit(p_230444_1_, 6, 6, 0, 0, 0, 20, 20, 20, 20);
        RenderSystem.enableBlend();

        // 見つけたスポナーの座標
        String text = "X:" + this.blockPos.getX() + " Y:" + this.blockPos.getY() + " Z:" + this.blockPos.getZ();
        p_230444_2_.getMinecraft().fontRenderer.drawString(p_230444_1_, I18n.format("hcutilsmod.findspawner.text"), 30.0F, 7.0F, 14737632);
        p_230444_2_.getMinecraft().fontRenderer.drawString(p_230444_1_, text, 36.0F, 18.0F, 14737632);

        return Visibility.SHOW;
    }
}
