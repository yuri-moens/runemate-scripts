package be.yurimoens.runemate.cgranitecutter;

import be.yurimoens.runemate.cgranitecutter.task.CutGranite;
import be.yurimoens.runemate.cgranitecutter.task.HandleBank;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.script.framework.task.TaskScript;

import java.awt.*;

public class CGraniteCutter extends TaskScript implements PaintListener {

    @Override
    public void onStart(String... args) {
        setLoopDelay(230, 670);
        add(new CutGranite(), new HandleBank());
    }

    @Override
    public void onPaint(Graphics2D graphics2D) {

    }
}
