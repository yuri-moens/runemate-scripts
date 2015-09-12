package be.yurimoens.runemate.ccitadel;

import be.yurimoens.runemate.ccitadel.task.PickupOrts;
import be.yurimoens.runemate.ccitadel.task.WeaveLoom;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.task.TaskScript;

import java.awt.*;

public class CCitadel extends TaskScript implements PaintListener {

    private StopWatch runtime;
    private int startLevel, startExperience;

    @Override
    public void onStart(String... args) {
        runtime = new StopWatch();
        runtime.start();
        startLevel = Skill.COOKING.getBaseLevel();
        startExperience = Skill.COOKING.getExperience();

        getEventDispatcher().addListener(this);
        setLoopDelay(350, 550);
        add(
                new PickupOrts(),
                new WeaveLoom()
        );
    }

    @Override
    public void onPaint(Graphics2D graphics2D) {

    }
}
