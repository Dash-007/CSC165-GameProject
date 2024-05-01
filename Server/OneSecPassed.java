import tage.ai.behaviortrees.*;

public class OneSecPassed extends BTCondition {
    NPCcontroller npcCtrl;
    NPC npc;
    long lastUpdateTime;
    long oneSecInNano = 1000000000;

    public OneSecPassed(NPCcontroller c, NPC n, boolean toNegate) {
        super(toNegate);
        npcCtrl = c;
        npc = n;
        lastUpdateTime = System.nanoTime();
    }

    protected boolean check() {
        long currentTime = System.nanoTime();
        if (currentTime - lastUpdateTime >= oneSecInNano) {
            lastUpdateTime = currentTime;
            return true;
        }
        return false;
    }
}