package be.yurimoens.runemate.clividfarm;

import be.yurimoens.runemate.clividfarm.task.*;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.local.Varpbits;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.task.TaskScript;

import java.awt.*;

public class CLividFarm extends TaskScript implements PaintListener {

    private StopWatch runtime;
    private int startPoints;

    @Override
    public void onStart(String... args) {
        TakeLumber takeLumber;
        ConvertLumber convertLumber;

        add(
                new CurePlant(),
                new FertilisePatch(),
                new EncouragePauline(),
                new TakeProduce(),
                new DepositProduce(),
                (takeLumber = new TakeLumber()),
                (convertLumber = new ConvertLumber()),
                new FixFence(),
                new Idle(takeLumber, convertLumber)
        );

        runtime = new StopWatch();
        runtime.start();
        startPoints = Varpbits.getAt(16372).getValue();

        getEventDispatcher().addListener(this);
    }


    @Override
    public void onPaint(Graphics2D g) {
        int gainedPoints = (Varpbits.getAt(16372).getValue() - startPoints) * 10;
        int pointsPerHour = (int) (gainedPoints * 3600000D / runtime.getRuntime());
        final int xOffset = 60;
        final int yOffset = 10;
        final int lineHeight = 20;

        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        g.setFont((new Font("Arial", 0, 12)));
        g.drawString("Time running: " + runtime.getRuntimeAsString(), xOffset, yOffset + lineHeight);
        g.drawString("Points: " + pointsPerHour + " points/h (" + gainedPoints + ")", xOffset, yOffset + lineHeight * 2);
    }
}
