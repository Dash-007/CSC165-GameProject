import tage.ai.behaviortrees.*;

public class GetBig extends BTAction {
    NPC npc;

    public GetBig(NPC n) {
        super();
        npc = n;
    }

    protected BTStatus update(float elapsedTime) {
        npc.getBig();
        return BTStatus.BH_SUCCESS;
    }
}