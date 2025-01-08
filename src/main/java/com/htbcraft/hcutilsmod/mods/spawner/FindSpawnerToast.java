package com.htbcraft.hcutilsmod.mods.spawner;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;

public class FindSpawnerToast implements Toast {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation TEXTURE = new ResourceLocation("toast/advancement");
    private static final ResourceLocation SPAWNER_ICON = new ResourceLocation(HCUtilsMod.MOD_ID, "toast/spawner");
    private static final LinkedHashSet<BlockPos> blockPosList = new LinkedHashSet<>();

    private final BlockPos blockPos;
    private final long timeout;
    private final Boolean visibility;

    public FindSpawnerToast(BlockPos blockPos, long timeout) {
        this.blockPos = blockPos;
        this.timeout = timeout;

        // 表示している座標との重複をチェックするためにLinkedHashSetを使う
        this.visibility = blockPosList.add(blockPos);
    }

    @Override
    public Toast.Visibility render(GuiGraphics p_94896_, ToastComponent p_94897_, long p_94898_) {
        // すでに表示している座標の場合は即非表示
        if (!this.visibility) {
            LOGGER.info("Already displayed!! " + this.blockPos);
            return Visibility.HIDE;
            // HIDE を返すと音が鳴るのがイケてない。
        }

        // 設定されている時間だけ表示
        if (p_94898_ > (this.timeout * 1000L)) {
            LOGGER.info("Time up!! " + this.blockPos);
            blockPosList.remove(this.blockPos);
            return Visibility.HIDE;
        }

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
        );

        // トーストの枠
        p_94896_.blitSprite(TEXTURE, 0, 0, this.width(), this.height());

        // スポナーのアイコン
        p_94896_.blitSprite(SPAWNER_ICON, 6, 6, 20, 20);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // 見つけたスポナーの座標
        String text = "X:" + this.blockPos.getX() + " Y:" + this.blockPos.getY() + " Z:" + this.blockPos.getZ();
        p_94896_.drawString(p_94897_.getMinecraft().font, Component.translatable("hcutilsmod.findspawner.text"), 30, 7, 14737632);
        p_94896_.drawString(p_94897_.getMinecraft().font, text, 36, 18, 14737632);

        return Visibility.SHOW;
    }
}
