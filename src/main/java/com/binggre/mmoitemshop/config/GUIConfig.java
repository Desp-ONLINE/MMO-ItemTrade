package com.binggre.mmoitemshop.config;

import com.binggre.binggreapi.objects.items.CustomItemStack;
import com.binggre.binggreapi.utils.ColorManager;
import lombok.Getter;

import java.util.List;

@Getter
public class GUIConfig {

    public static final String ROSE = "#FFB7C5";
    public static final String CORAL = "#FFB7B2";
    public static final String PEACH = "#FFCBA4";
    public static final String CREAM = "#FFE9A8";
    public static final String MINT = "#A7E9C5";
    public static final String SKY = "#A8D8EA";
    public static final String LAVENDER = "#CDB4DB";
    public static final String GRAY = "#9AA0A6";
    public static final String WHITE = "#F5F5F5";

    private final String title = "§d §6상점 §f: §f<name>";
    private final int size = 6;

    private final CustomItemStack trade = CustomItemStack.create(
            22, "emerald",
            ColorManager.format(MINT + "✿ 교환하기 ✿"),
            List.of(), 1, 0
    );
    private final CustomItemStack previous = CustomItemStack.create(
            47, "arrow",
            ColorManager.format(PEACH + "◀ " + WHITE + "이전"),
            List.of(), 1, 0
    );
    private final CustomItemStack next = CustomItemStack.create(
            51, "arrow",
            ColorManager.format(WHITE + "다음 " + PEACH + "▶"),
            List.of(), 1, 0
    );

    public void init() {
    }
}
