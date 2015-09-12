package be.yurimoens.runemate.cfisher;

import be.yurimoens.runemate.cfisher.task.*;
import be.yurimoens.runemate.util.InvestigateMeteorite;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.task.TaskScript;

import java.awt.*;

public class CFisher extends TaskScript implements PaintListener {

    // anagogic ort: 24909

    private StopWatch runtime;
    private int startLevel, startExperience;

    @Override
    public void onStart(String... args) {
        runtime = new StopWatch();
        runtime.start();
        startLevel = Skill.COOKING.getBaseLevel();
        startExperience = Skill.COOKING.getExperience();

        InvestigateMeteorite investigateMeteorite = new InvestigateMeteorite(InvestigateMeteorite.Skill.DIVINATION);

        getEventDispatcher().addListener(this);
        getEventDispatcher().addListener(investigateMeteorite);
        setLoopDelay(220, 470);
        add(investigateMeteorite, new Drop(), new CheckUrn(), new WalkToBank(), new WalkToSpot(), new HandleBank(), new Fish()); //
    }

    @Override
    public void onPaint(Graphics2D graphics2D) {

    }
}
