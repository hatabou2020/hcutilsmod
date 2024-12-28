package com.htbcraft.hcutilsmod.mods.spawner;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;

public class FindSpawnerToast implements Toast {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("toast/advancement");
    private static final ResourceLocation SPAWNER_ICON = ResourceLocation.fromNamespaceAndPath(HCUtilsMod.MOD_ID, "toast/spawner");
    private static final LinkedHashSet<BlockPos> blockPosList = new LinkedHashSet<>();

    private final BlockPos blockPos;
    private final long timeout;
    private final Boolean visibility;

    private Visibility wantedVisibility = Visibility.HIDE;

    public FindSpawnerToast(BlockPos blockPos, long timeout) {
        this.blockPos = blockPos;
        this.timeout = timeout;

        // 表示している座標との重複をチェックするためにLinkedHashSetを使う
        this.visibility = blockPosList.add(blockPos);
    }

    @Override
    public Visibility getWantedVisibility() {
        return wantedVisibility;
    }

    @Override
    public void update(ToastManager p_369172_, long p_365500_) {
        // すでに表示している座標の場合は即非表示
        if (!this.visibility) {
            LOGGER.info("Already displayed!! " + this.blockPos);
            wantedVisibility = Visibility.HIDE;
            // HIDE を返すと音が鳴るのがイケてない。
        }

        // 設定されている時間だけ表示
        if (p_365500_ > (this.timeout * 1000L)) {
            LOGGER.info("Time up!! " + this.blockPos);
            blockPosList.remove(this.blockPos);
            wantedVisibility = Visibility.HIDE;
        }
    }

    @Override
    public void render(GuiGraphics p_361731_, Font p_363216_, long p_366222_) {
        // トーストの枠
        p_361731_.blitSprite(RenderType::guiTextured, TEXTURE, 0, 0, this.width(), this.height());

        // スポナーのアイコン
        p_361731_.blitSprite(RenderType::guiTextured, SPAWNER_ICON, 6, 6, 20, 20);

        // 見つけたスポナーの座標
        String text = "X:" + this.blockPos.getX() + " Y:" + this.blockPos.getY() + " Z:" + this.blockPos.getZ();
        p_361731_.drawString(p_363216_, Component.translatable("hcutilsmod.findspawner.text"), 30, 7, 14737632);
        p_361731_.drawString(p_363216_, text, 36, 18, 14737632);

        wantedVisibility = Visibility.SHOW;
    }
}
