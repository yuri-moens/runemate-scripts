package be.yurimoens.runemate.csupermake;

import be.yurimoens.runemate.csupermake.task.HandleBank;
import be.yurimoens.runemate.csupermake.task.SuperMake;
import be.yurimoens.runemate.util.CTime;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.task.TaskScript;

import java.awt.*;

public class CSuperMake extends TaskScript implements PaintListener {

    private StopWatch runtime;
    private int craftingStartExperience, craftingStartLevel, magicStartExperience, magicStartLevel;

    @Override
    public void onStart(String... args) {
        runtime = new StopWatch();
        runtime.start();

        craftingStartExperience = Skill.CRAFTING.getExperience();
        craftingStartLevel = Skill.CRAFTING.getBaseLevel();
        magicStartExperience = Skill.MAGIC.getExperience();
        magicStartLevel = Skill.MAGIC.getBaseLevel();

        getEventDispatcher().addListener(this);

        add(new HandleBank(), new SuperMake());
    }

    @Override
    public void onPaint(Graphics2D g) {
        int currentCraftingLevel = Skill.CRAFTING.getBaseLevel();
        int gainedCraftingExperience = Skill.CRAFTING.getExperience() - craftingStartExperience;
        int currentMagicLevel = Skill.MAGIC.getBaseLevel();
        int gainedMagicExperience = Skill.MAGIC.getExperience() - magicStartExperience;

        int craftingExperiencePerHour = (int) (gainedCraftingExperience * 3600000D / runtime.getRuntime());
        int magicExperiencePerHour = (int) (gainedMagicExperience * 3600000D / runtime.getRuntime());
        long craftingTimeToNextLevel = craftingExperiencePerHour != 0 ? (long) (Skill.CRAFTING.getExperienceToNextLevel() * 3600000D / craftingExperiencePerHour) : 0;
        long magicTimeToNextLevel = magicExperiencePerHour != 0 ? (long) (Skill.MAGIC.getExperienceToNextLevel() * 3600000D / magicExperiencePerHour) : 0;

        final int xOffset = 60;
        final int yOffset = 10;
        final int lineHeight = 20;

        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        g.setFont((new Font("Arial", 0, 12)));
        g.drawString("Time running: " + runtime.getRuntimeAsString(), xOffset, yOffset + lineHeight);
        g.drawString("Crafting TTL: " + CTime.formatTime(craftingTimeToNextLevel), xOffset, yOffset + lineHeight * 2);
        g.drawString("Magic TTL: " + CTime.formatTime(magicTimeToNextLevel), xOffset, yOffset + lineHeight * 3);
        g.drawString("Crafting experience: " + craftingExperiencePerHour + " xp/h (" + gainedCraftingExperience + ")", xOffset, yOffset + lineHeight * 4);
        g.drawString("Magic experience: " + magicExperiencePerHour + " xp/h (" + gainedMagicExperience + ")", xOffset, yOffset + lineHeight * 5);
        g.drawString("Crafting level: " + currentCraftingLevel + " (" + (currentCraftingLevel - craftingStartLevel) + ")", xOffset, yOffset + lineHeight * 6);
        g.drawString("Magic level: " + currentMagicLevel + " (" + (currentMagicLevel - magicStartLevel) + ")", xOffset, yOffset + lineHeight * 7);
    }
}
