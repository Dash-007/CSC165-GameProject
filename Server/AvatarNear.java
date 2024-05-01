import tage.ai.behaviortrees.*;

public class AvatarNear extends BTCondition {
    NPC npc;
    NPCcontroller npcCtrl;
    GameAIServerUDP server;

    public AvatarNear(GameAIServerUDP s, NPCcontroller c, NPC n, boolean toNegate) {
        super(toNegate);
        server = s;
        npcCtrl = c;
        npc = n;
    }

    protected boolean check() {
        server.sendCheckForAvatarNear();
        return npcCtrl.getNearFlag();
    }
}
